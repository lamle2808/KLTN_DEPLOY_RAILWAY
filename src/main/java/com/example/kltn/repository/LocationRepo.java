package com.example.kltn.repository;

import com.example.kltn.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepo extends JpaRepository<Location, Long> {
    // Bạn có thể thêm các phương thức tùy chỉnh ở đây nếu cần
}
