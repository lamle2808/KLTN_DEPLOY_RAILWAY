package com.example.kltn.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.kltn.dataBean.LoginResponse;
import com.example.kltn.entity.Account;
import com.example.kltn.entity.VerificationToken;
import com.example.kltn.service.AccountService;
import com.example.kltn.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
@Slf4j
public class AuthController {
    private final AccountService accountService;
    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Account account) {
        Account accountSaved = accountService.register(account);
        VerificationToken token = accountService.createVerificationToken(accountSaved);
        if (token != null) {
            emailService.sendVerificationEmail(account.getEmail(), token.getToken());
            return ResponseEntity.ok("Đăng ký thành công. Kiểm tra email để xác minh tài khoản.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email đã đăng ký trước đó và mã xác minh còn hiệu lực.");
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam String token) {
        if (accountService.verifyAccount(token)) {
            return ResponseEntity.ok("Tài khoản của bạn đã được xác minh thành công.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã xác minh không hợp lệ hoặc đã hết hạn.");
        }
    }

    @PostMapping("/resend-verification-email")
    public ResponseEntity<String> resendVerificationEmail(@RequestParam String email) {
        Optional<Account> account = accountService.getByEmail(email);

        if (account.isPresent() && !account.get().isEnabled()) {
            VerificationToken token = accountService.createVerificationToken(account.get());
            if (token != null) {
                emailService.sendVerificationEmail(account.get().getEmail(), token.getToken());
                return ResponseEntity.ok(buildResponsePage("Đã gửi lại email xác minh.", true));
            } else {
                return ResponseEntity.ok(buildResponsePage("Email này đã có mã xác minh hợp lệ.", false));
            }
        } else {
            return ResponseEntity.ok(buildResponsePage("Email không tồn tại hoặc tài khoản đã được xác minh.", false));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Account account) {
        try {
            log.info("Đang xử lý đăng nhập cho email: {}", account.getEmail());
            
            String token = accountService.login(account);
            if (token.equals("")) {
                log.warn("Đăng nhập thất bại - email hoặc mật khẩu không chính xác: {}", account.getEmail());
                return ResponseEntity.badRequest().body("Email hoặc mật khẩu không chính xác");
            }
            
            Account accountLogin = accountService.getByEmail(account.getEmail()).orElseThrow();
            if (accountLogin.getEnable() == 0 || accountLogin.getIsVerified() == 0) {
                log.warn("Đăng nhập thất bại - tài khoản chưa được kích hoạt: {}", account.getEmail());
                return ResponseEntity.badRequest().body("Tài khoản chưa được kích hoạt");
            }
            
            log.info("Đăng nhập thành công cho email: {}", account.getEmail());
            LoginResponse loginResponse = LoginResponse.fromAccount(token, accountLogin);
            return ResponseEntity.ok().body(token);
            
        } catch (Exception exception) {
            log.error("Lỗi đăng nhập cho email: {}", account.getEmail(), exception);
            return ResponseEntity.badRequest()
                .body("Có lỗi xảy ra trong quá trình đăng nhập: " + exception.getMessage());
        }
    }

    private String buildResponsePage(String message, boolean success) {
        return "<!DOCTYPE html>" +
                "<html lang=\"vi\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Thông báo</title>" +
                "    <style>" +
                "        body {" +
                "            font-family: Arial, sans-serif;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "            display: flex;" +
                "            justify-content: center;" +
                "            align-items: center;" +
                "            height: 100vh;" +
                "            background-color: #f4f4f9;" +
                "        }" +
                "        .container {" +
                "            text-align: center;" +
                "            background-color: #fff;" +
                "            padding: 20px;" +
                "            border-radius: 8px;" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);" +
                "            width: 100%;" +
                "            max-width: 400px;" +
                "        }" +
                "        h1 {" +
                "            margin-bottom: 20px;" +
                "        }" +
                "        .message {" +
                "            font-size: 18px;" +
                "            color: #333;" +
                "        }" +
                "        .success {" +
                "            color: green;" +
                "        }" +
                "        .error {" +
                "            color: red;" +
                "        }" +
                "        @media (max-width: 600px) {" +
                "            .container {" +
                "                width: 90%;" +
                "                max-width: 350px;" +
                "            }" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <h1>Thông báo</h1>" +
                "        <p class=\"message\" style=\"color: " + (success ? "green" : "red") + ";\">" + message + "</p>"
                +
                "    </div>" +
                "</body>" +
                "</html>";
    }
}
