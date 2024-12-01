package com.example.kltn.repository;

import com.example.kltn.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    // Bạn có thể thêm các phương thức tùy chỉnh ở đây nếu cần
    Optional<Employee> findByEmail(String email);
    Employee findEmployeeById(Long id);
    List<Employee> findByNameContaining(String name);
} 