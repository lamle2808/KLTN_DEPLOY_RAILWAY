package com.example.kltn.service.implement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.example.kltn.config.JwtService;
import com.example.kltn.dataBean.AccountChange;
import com.example.kltn.dataBean.CustomerDataBean;
import com.example.kltn.entity.Account;
import com.example.kltn.entity.Customer;
import com.example.kltn.entity.Role;
import com.example.kltn.entity.VerificationToken;
import com.example.kltn.repository.AccountRepo;
import com.example.kltn.repository.VerificationTokenRepo;
import com.example.kltn.service.AccountService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@SessionScope
public class AccountServiceimpl implements AccountService {
    private final AccountRepo accountRepo;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final VerificationTokenRepo tokenRepository;

    @Override
    public Optional<Account> getByEmail(String email) {
        return accountRepo.findByEmail(email);
    }

    @Override
    public Optional<Account> getByPhoneNumber(String phoneNumber) {
        return accountRepo.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Account saveOrUpdate(Account account) {
        return accountRepo.save(account);
    }

    @Override
    public Account getById(Long id) {
        return accountRepo.findAccountById(id);
    }

    @Override
    public Account getByEmail2(String email) {
        return accountRepo.findAccountByEmail(email);
    }

    @Override
    public Account register(Account account) {
        String password = account.getPassWordAccount();
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        String encodedPassword = passwordEncoder.encode(password);
        account.setPassWordAccount(encodedPassword);
        return accountRepo.save(account);
    }

    @Override
    public List<Account> getAll() {
        return accountRepo.findAll();
    }

    @Override
    public String login(Account account) {
        String token = "";
        try {
            if (account.getEmail() != null || !"".equals(account.getEmail())) {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(account.getEmail(), account.getPassword()));
                var user = accountRepo.findByEmail(account.getEmail()).orElseThrow();
                return jwtService.generateToken(user);
            }
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(account.getPhoneNumber(), account.getPassword()));
            var user = accountRepo.findByPhoneNumber(account.getPhoneNumber()).orElseThrow();
            return jwtService.generateToken(user);
        } catch (Exception exception) {
            return token;
        }
    }

    @Override
    public int changePassword(AccountChange account) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }

    @Override
    public String forgotPassword(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'forgotPassword'");
    }

    @Override
    public CustomerDataBean customerLogin(String token, Customer customer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'customerLogin'");
    }

    @Override
    public Account removeRoleFromAccount(Account account, Role role) {
        account.getRoles().remove(role);
        return accountRepo.save(account);
    }

    @Override
    public Account addRoleToAccount(Account account, Role role) {
        account.getRoles().add(role);
        return accountRepo.save(account);
    }

    @Override
    public VerificationToken createVerificationToken(Account account) {
         // Kiểm tra xem người dùng đã có mã xác minh chưa
         Optional<VerificationToken> existingToken = tokenRepository.findByAccount(account);
         if (existingToken.isPresent()) {
             // Nếu mã xác minh đã có, và mã xác minh cũ đã hết hạn, xóa mã cũ
             if (existingToken.get().getExpiryDate().isBefore(LocalDateTime.now())) {
                 tokenRepository.delete(existingToken.get());
             } else {
                 // Nếu mã còn hiệu lực, không cần tạo mã mới và trả về null
                 return null;
             }
         }
         
         // Tạo mã xác minh mới
         String token = UUID.randomUUID().toString();
         VerificationToken verificationToken = new VerificationToken();
         verificationToken.setToken(token);
         verificationToken.setAccount(account);
         verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // Mã xác minh có hiệu lực trong 24 giờ
         tokenRepository.save(verificationToken);
         return verificationToken;
    }

    @Override
    public boolean verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = tokenRepository.findByToken(token);
        if (verificationToken.isPresent() && verificationToken.get().getExpiryDate().isAfter(LocalDateTime.now())) {
            Account account = verificationToken.get().getAccount();
            account.setEnable(1); // Kích hoạt tài khoản
            account.setIsVerified(1);
            accountRepo.save(account);
            return true;
        }
        return false;
    }

    

}
