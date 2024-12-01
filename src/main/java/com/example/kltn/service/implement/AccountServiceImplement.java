package com.example.kltn.service.implement;
import com.example.kltn.entity.Account;
import com.example.kltn.entity.Role;
import com.example.kltn.entity.VerificationToken;

// Repositories
import com.example.kltn.repository.AccountRepo;
import com.example.kltn.repository.RoleRepo;
import com.example.kltn.repository.VerificationTokenRepo;

// Services
import com.example.kltn.service.AccountService;
import com.example.kltn.service.EmailService;
import com.example.kltn.config.JwtService;

// Spring Security
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

// Spring Framework
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Lombok
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Java Utils
import java.util.HashSet;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountServiceImplement implements AccountService, UserDetailsService {
    private final AccountRepo accountRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final VerificationTokenRepo verificationTokenRepo;
    private final EmailService emailService;
    private final RoleRepo roleRepo;

    @Override
    public Account register(Account account) {
        try {
            log.info("Đang xử lý đăng ký cho email: {}", account.getEmail());
            
            if (accountRepo.findByEmail(account.getEmail()).isPresent()) {
                log.warn("Email đã tồn tại: {}", account.getEmail());
                throw new RuntimeException("Email đã tồn tại");
            }

            account.setPassWordAccount(passwordEncoder.encode(account.getPassWordAccount()));
            account.setEnable(1);
            account.setIsVerified(0);
            
            Role userRole = roleRepo.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role không tồn tại"));
            account.setRoles(new HashSet<>(Collections.singletonList(userRole)));
            
            Account savedAccount = accountRepo.save(account);
            log.info("Đăng ký thành công cho email: {}", account.getEmail());
            
            return savedAccount;
        } catch (Exception e) {
            log.error("Lỗi trong quá trình đăng ký cho email: {}", account.getEmail(), e);
            throw e;
        }
    }

    @Override
    public String login(Account account) {
        try {
            log.info("Đang xử lý đăng nhập cho email: {}", account.getEmail());
            
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    account.getEmail(),
                    account.getPassWordAccount()
                )
            );

            if (authentication.isAuthenticated()) {
                Account authenticatedAccount = (Account) authentication.getPrincipal();
                String token = jwtService.generateToken(authenticatedAccount);
                log.info("Đăng nhập thành công cho email: {}", account.getEmail());
                return token;
            }

            log.warn("Đăng nhập thất bại cho email: {}", account.getEmail());
            return "";
            
        } catch (Exception e) {
            log.error("Lỗi trong quá trình đăng nhập cho email: {}", account.getEmail(), e);
            return "";
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            return accountRepo.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Không tìm thấy tài khoản với email: {}", email);
                    return new UsernameNotFoundException("Không tìm thấy tài khoản với email: " + email);
                });
        } catch (Exception e) {
            log.error("Lỗi khi tải thông tin user với email: {}", email, e);
            throw e;
        }
    }

    @Override
    public List<Account> getAll() {
        try {
            log.info("Đang lấy danh sách tất cả tài khoản");
            return accountRepo.findAll();
        } catch (Exception e) {
            log.error("Lỗi khi lấy danh sách tài khoản", e);
            throw e;
        }
    }

    @Override
    public Account getById(Long id) {
        try {
            log.info("Đang tìm tài khoản với id: {}", id);
            return accountRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với id: " + id));
        } catch (Exception e) {
            log.error("Lỗi khi tìm tài khoản với id: {}", id, e);
            throw e;
        }
    }

    @Override
    public boolean removeRoleFromAccount(Account account, Role role) {
        try {
            log.info("Đang xóa role {} khỏi tài khoản {}", role.getName(), account.getUsername());
            
            if (account.getRoles().contains(role)) {
                account.getRoles().remove(role);
                accountRepo.save(account);
                log.info("Đã xóa role thành công");
                return true;
            } else {
                log.warn("Tài khoản không có role này");
                return false;
            }
        } catch (Exception e) {
            log.error("Lỗi khi xóa role khỏi tài khoản", e);
            return false;
        }
    }

    @Override
    public Account saveOrUpdate(Account account) {
        try {
            log.info("Đang lưu/cập nhật tài khoản: {}", account.getEmail());
            return accountRepo.save(account);
        } catch (Exception e) {
            log.error("Lỗi khi lưu/cập nhật tài khoản", e);
            throw e;
        }
    }

    @Override
    public VerificationToken createVerificationToken(Account account) {
        try {
            log.info("Đang tạo token xác thực cho tài khoản: {}", account.getEmail());
            String token = UUID.randomUUID().toString();
            VerificationToken verificationToken = new VerificationToken(token, account);
            verificationToken = verificationTokenRepo.save(verificationToken);
            emailService.sendVerificationEmail(account.getEmail(), token);
            log.info("Đã tạo và gửi token xác thực thành công");
            return verificationToken;
        } catch (Exception e) {
            log.error("Lỗi khi tạo token xác thực", e);
            throw e;
        }
    }

    @Override
    public Optional<Account> getByEmail(String email) {
        try {
            log.info("Đang tìm tài khoản với email: {}", email);
            return accountRepo.findByEmail(email);
        } catch (Exception e) {
            log.error("Lỗi khi tìm tài khoản với email: {}", email, e);
            throw e;
        }
    }

    @Override
    public boolean addRoleToAccount(Account account, Role role) {
        try {
            log.info("Đang thêm role {} cho tài khoản {}", role.getName(), account.getEmail());
            account.getRoles().add(role);
            accountRepo.save(account);
            log.info("Đã thêm role thành công");
            return true;
        } catch (Exception e) {
            log.error("Lỗi khi thêm role cho tài khoản", e);
            return false;
        }
    }

    @Override
    public boolean forgotPassword(String email) {
        try {
            log.info("Đang xử lý yêu cầu quên mật khẩu cho email: {}", email);
            Account account = getByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với email: " + email));
            String token = UUID.randomUUID().toString();
            
            // Lưu token vào account
            account.setResetPassWordToken(token);
            account.setResetPassWordTokenExpiry(LocalDateTime.now().plusHours(24)); // Token hết hạn sau 24h
            accountRepo.save(account);
            
            emailService.sendPasswordResetEmail(email, token);
            log.info("Đã gửi email đặt lại mật khẩu thành công");
            return true;
        } catch (Exception e) {
            log.error("Lỗi khi xử lý yêu cầu quên mật khẩu cho email: {}", email, e);
            return false;
        }
    }

    @Override
    public Optional<Account> getByPhoneNumber(String phoneNumber) {
        try {
            log.info("Đang tìm tài khoản với số điện thoại: {}", phoneNumber);
            return accountRepo.findByPhoneNumber(phoneNumber);
        } catch (Exception e) {
            log.error("Lỗi khi tìm tài khoản theo số điện thoại", e);
            throw e;
        }
    }

    @Override
    public boolean verifyAccount(String token) {
        try {
            log.info("Đang xác thực tài khoản với token: {}", token);
            VerificationToken verificationToken = verificationTokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token không hợp lệ"));
            
            Account account = verificationToken.getAccount();
            account.setIsVerified(1);
            accountRepo.save(account);
            return true;
        } catch (Exception e) {
            log.error("Lỗi khi xác thực tài khoản", e);
            return false;
        }
    }
} 