package com.example.kltn.repository;

import com.example.kltn.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {
    Payment findByOrderId(Long orderId);
    List<Payment> findByCustomer_NameContaining(String name);
}
