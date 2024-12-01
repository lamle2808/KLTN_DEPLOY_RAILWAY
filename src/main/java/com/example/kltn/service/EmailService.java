package com.example.kltn.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@SessionScope
public class EmailService {
    private final JavaMailSender javaMailSender;

    public void sendVerificationEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Xác minh tài khoản");
        message.setText("Vui lòng nhấp vào liên kết sau để xác minh tài khoản của bạn: " +
                "http://localhost:8521/KLTN-SERVER-2024/api/v1/auth/verify?token=" + token);
        message.setFrom("lamle28082001@gmail.com");
        javaMailSender.send(message);
    }

    public void sendPaymentSuccessEmail(String to, String transactionId, double amount) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Thanh toán thành công");
            message.setText(String.format(
                "Thanh toán của bạn đã được xử lý thành công.\n" +
                "Mã giao dịch: %s\n" +
                "Số tiền: %,.0f VNĐ\n" +
                "Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!",
                transactionId, amount
            ));
            javaMailSender.send(message);
            log.info("Đã gửi email thông báo thanh toán thành công tới: {}", to);
        } catch (Exception e) {
            log.error("Lỗi gửi email: " + e.getMessage(), e);
        }
    }

    public void sendPasswordResetEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Đặt lại mật khẩu");
        message.setText("Vui lòng nhấp vào liên kết sau để đặt lại mật khẩu của bạn: " +
                "http://localhost:8521/KLTN-2024/api/v1/auth/reset-password?token=" + token);
        message.setFrom("LamLe@gmail.com");
        javaMailSender.send(message);
        log.info("Đã gửi email đặt lại mật khẩu tới: {}", email);
    }
}
