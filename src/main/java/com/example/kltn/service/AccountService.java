package com.example.kltn.service;

import java.util.List;
import java.util.Optional;

import com.example.kltn.entity.Account;
import com.example.kltn.entity.Role;
import com.example.kltn.entity.VerificationToken;

public interface AccountService {
    List<Account> getAll();

    Account register(Account account);

    String login(Account account);

    Optional<Account> getByEmail(String email);

    Optional<Account> getByPhoneNumber(String phoneNumber);

    Account getById(Long id);

    boolean addRoleToAccount(Account account, Role role);

    boolean removeRoleFromAccount(Account account, Role role);

    VerificationToken createVerificationToken(Account account);

    boolean verifyAccount(String token);

    Account saveOrUpdate(Account account);

    boolean forgotPassword(String email);
}
