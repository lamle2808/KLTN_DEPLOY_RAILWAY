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
                "http://localhost:8521/KLTN-2024/api/v1/auth/verify?token=" + token);
        message.setFrom("LamLe@gmail.com");
        javaMailSender.send(message);
    }
}
