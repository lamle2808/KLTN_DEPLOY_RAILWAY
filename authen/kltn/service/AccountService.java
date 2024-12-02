package com.example.kltn.service;

import java.util.List;
import java.util.Optional;

import com.example.kltn.dataBean.AccountChange;
import com.example.kltn.dataBean.CustomerDataBean;
import com.example.kltn.entity.Account;
import com.example.kltn.entity.Customer;
import com.example.kltn.entity.Role;
import com.example.kltn.entity.VerificationToken;

public interface AccountService {
    Optional<Account> getByEmail(String email);

    Optional<Account> getByPhoneNumber(String phoneNumber);

    Account saveOrUpdate(Account account);

    Account getById(Long id);

    Account getByEmail2(String email);

    Account register(Account account);

    List<Account> getAll();

    String login(Account account);

    int changePassword(AccountChange account);

    String forgotPassword(String email);

    CustomerDataBean customerLogin(String token, Customer customer);

    Account removeRoleFromAccount(Account account, Role role);

    Account addRoleToAccount(Account account, Role role);

    public VerificationToken createVerificationToken(Account account);

    public boolean verifyAccount(String token);
}
