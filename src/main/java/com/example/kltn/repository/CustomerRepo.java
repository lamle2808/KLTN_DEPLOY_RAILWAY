package com.example.kltn.repository;

import com.example.kltn.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {
    // Bạn có thể thêm các phương thức tùy chỉnh ở đây nếu cần
    Optional<Customer> findByEmail(String email);
    Customer findCustomerById(Long id);
    List<Customer> findByNameContaining(String name);
} 