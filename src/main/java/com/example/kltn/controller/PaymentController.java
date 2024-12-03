package com.example.kltn.controller;

import com.example.kltn.entity.Payment;
import com.example.kltn.service.PaymentService;
import com.example.kltn.service.MomoService;
import com.example.kltn.dto.PaymentRequest;
import com.example.kltn.entity.Customer;
import com.example.kltn.service.CustomerService;
import com.example.kltn.service.OrderService;
import com.example.kltn.service.VnpayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.kltn.config.VnpayConfig;
import com.example.kltn.constant.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final MomoService momoService;
    private final CustomerService customerService;
    private final OrderService orderService;
    private final VnpayService vnpayService;
    private final VnpayConfig vnpayConfig;

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAll();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.getById(id);
        return ResponseEntity.ok(payment);
    }

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest request) {
        try {
            // Tạo orderId unique
            String orderId = "TEST_ORDER_" + UUID.randomUUID().toString().substring(0, 8);
            
            String orderInfo;
            if (request.getOrderInfo() == null || request.getOrderInfo().trim().isEmpty()) {
                orderInfo = "Thanh toán đơn hàng " + orderId;
            } else {
                orderInfo = request.getOrderInfo();
            }
            
            // Tạo payment URL từ VNPay
            String paymentUrl = vnpayService.createPayment(
                orderId,  // Sử dụng orderId mới tạo
                request.getAmount().toString(), 
                orderInfo
            );
            
            // Lưu thông tin thanh toán
            Payment payment = new Payment();
            payment.setPaymentMethod("VNPAY");
            payment.setPaymentStatus(Status.PAYMENT_PENDING.getValue());
            payment.setAmount(request.getAmount());
            payment.setPaymentDate(new Date());
            
            // Liên kết với customer nếu có
            if (request.getCustomerId() != null) {
                Customer customer = customerService.getById(request.getCustomerId());
                payment.setCustomer(customer);
            }
            
            paymentService.saveOrUpdate(payment);
            
            // Trả về payment URL và orderId mới
            return ResponseEntity.ok(Map.of(
                "paymentUrl", paymentUrl,
                "orderId", orderId
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Lỗi tạo thanh toán: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @RequestBody Payment payment) {
        payment.setId(id);
        Payment updatedPayment = paymentService.saveOrUpdate(payment);
        return ResponseEntity.ok(updatedPayment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/momo/callback")
    public ResponseEntity<?> momoCallback(@RequestParam Map<String, String> params) {
        try {
            String orderId = params.get("orderId");
            String resultCode = params.get("resultCode");
            
            if ("0".equals(resultCode)) {
                Payment payment = paymentService.getById(Long.parseLong(orderId));
                if (payment != null) {
                    payment.setPaymentStatus(Status.PAYMENT_SUCCESS.getValue());
                    paymentService.saveOrUpdate(payment);
                    
                    // Cập nhật order status nếu có
                    if (payment.getOrder() != null) {
                        payment.getOrder().setStatus(Status.ORDER_PAID.getValue());
                        orderService.saveOrUpdate(payment.getOrder());
                    }
                }
                return ResponseEntity.ok(Map.of("message", "Thanh toán thành công"));
            }
            return ResponseEntity.badRequest().body(Map.of("message", "Thanh toán thất bại"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/momo-notify")
    public ResponseEntity<?> momoNotify(@RequestBody Map<String, String> notifyParams) {
        try {
            if (momoService.processPaymentNotify(notifyParams)) {
                return ResponseEntity.ok(Map.of("message", "Notify processed successfully"));
            }
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to process notify"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/create-vnpay")
    public ResponseEntity<?> createVnpayPayment(@RequestParam String orderId, 
                                                @RequestParam String amount,
                                                @RequestParam String orderInfo) {
        try {
            String paymentUrl = vnpayService.createPayment(orderId, amount, orderInfo);
            return ResponseEntity.ok(paymentUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi tạo thanh toán VNPAY: " + e.getMessage());
        }
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<?> vnpayReturn(@RequestParam Map<String, String> params) {
        try {
            // Lấy các tham số cần thiết từ VNPay
            String vnp_TxnRef = params.get("vnp_TxnRef");
            String vnp_ResponseCode = params.get("vnp_ResponseCode");
            String vnp_SecureHash = params.get("vnp_SecureHash");

            // Xác thực mã băm (checksum)
            String calculatedHash = vnpayService.hmacSHA512(vnpayConfig.getHashSecret(), params);
            if (!calculatedHash.equals(vnp_SecureHash)) {
                return ResponseEntity.badRequest().body("Invalid checksum");
            }

            // Kiểm tra mã phản hồi từ VNPay
            if ("00".equals(vnp_ResponseCode)) {
                // Giao dịch thành công, cập nhật trạng thái đơn hàng
                paymentService.updatePaymentStatus(vnp_TxnRef, "PAID");
                return ResponseEntity.ok("Payment successful");
            } else {
                // Giao dịch thất bại
                return ResponseEntity.badRequest().body("Payment failed");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing payment: " + e.getMessage());
        }
    }

    @PutMapping("/update-status/{orderId}")
    public ResponseEntity<?> updatePaymentStatus(@PathVariable String orderId, @RequestParam String status) {
        try {
            paymentService.updatePaymentStatus(orderId, status);
            return ResponseEntity.ok("Trạng thái thanh toán đã ược cập nhật thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi cập nhật trạng thái thanh toán: " + e.getMessage());
        }
    }
} 