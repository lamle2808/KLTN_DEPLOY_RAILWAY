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
import com.example.kltn.constant.Status;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final MomoService momoService;
    private final CustomerService customerService;
    private final OrderService orderService;
    private final VnpayService vnpayService;

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
            
            // Tạo payment URL từ MoMo
            String paymentUrl = momoService.createPayment(
                orderId,  // Sử dụng orderId mới tạo
                request.getAmount().toString(), 
                orderInfo
            );
            
            // Lưu thông tin thanh toán
            Payment payment = new Payment();
            payment.setPaymentMethod("MOMO");
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
        // Implement logic to handle VNPAY return
        return ResponseEntity.ok("VNPAY return handled");
    }
} 