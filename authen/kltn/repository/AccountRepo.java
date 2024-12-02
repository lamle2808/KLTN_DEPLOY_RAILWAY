package com.example.kltn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kltn.entity.Account;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    Optional<Account> findByPhoneNumber(String phoneNumber);
    Account findAccountByEmail(String email);

    Account findAccountByPhoneNumber(String phoneNumber);

    Account findAccountById(Long id);

}
