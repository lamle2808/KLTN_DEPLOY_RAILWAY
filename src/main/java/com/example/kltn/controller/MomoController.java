package com.example.kltn.controller;

import com.example.kltn.service.MomoService;
import com.example.kltn.service.PaymentService;
import com.example.kltn.service.OrderService;
import com.example.kltn.entity.Payment;
import com.example.kltn.entity.Order;
import com.example.kltn.constant.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class MomoController {
    private final MomoService momoService;
    private final PaymentService paymentService;
    private final OrderService orderService;

    @PostMapping("/create-momo")
    public ResponseEntity<?> createMomoPayment(@RequestParam String orderId, 
                                             @RequestParam String amount,
                                             @RequestParam String orderInfo) {
        try {
            String paymentUrl = momoService.createPayment(orderId, amount, orderInfo);
            
            Payment payment = new Payment();
            payment.setTransactionId(orderId);
            payment.setPaymentMethod("MOMO");
            payment.setPaymentStatus(Status.PAYMENT_PENDING.getValue());
            payment.setAmount(Double.parseDouble(amount));
            payment.setPaymentDate(new Date());
            
            paymentService.saveOrUpdate(payment);
            
            return ResponseEntity.ok(paymentUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi tạo thanh toán: " + e.getMessage());
        }
    }

    @GetMapping("/momo-return")
    public ResponseEntity<?> momoReturn(@RequestParam Map<String, String> params) {
        String orderId = params.get("orderId");
        String resultCode = params.get("resultCode");
        
        try {
            Payment payment = paymentService.getById(Long.parseLong(orderId));
            if (payment == null) {
                return ResponseEntity.badRequest().body("Không tìm thấy thông tin thanh toán");
            }

            if ("0".equals(resultCode)) {
                payment.setPaymentStatus(Status.PAYMENT_SUCCESS.getValue());
                
                // Cập nhật order nếu có
                Order order = payment.getOrder();
                if (order != null) {
                    order.setStatus(Status.ORDER_PAID.getValue());
                    orderService.saveOrUpdate(order);
                }
            } else {
                payment.setPaymentStatus(Status.PAYMENT_FAILED.getValue());
            }
            
            paymentService.saveOrUpdate(payment);
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi xử lý callback: " + e.getMessage());
        }
    }
} 