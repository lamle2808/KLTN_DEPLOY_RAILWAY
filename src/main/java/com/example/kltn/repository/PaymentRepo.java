package com.example.kltn.repository;

import com.example.kltn.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {
    // Bạn có thể thêm các phương thức tùy chỉnh ở đây nếu cần
}
