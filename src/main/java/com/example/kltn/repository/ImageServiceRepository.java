package com.example.kltn.repository;

import com.example.kltn.entity.ImageService;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImageServiceRepository extends JpaRepository<ImageService, Long> {
    List<ImageService> findByServiceEventId(Long serviceEventId);
} 