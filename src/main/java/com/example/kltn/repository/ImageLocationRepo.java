package com.example.kltn.repository;

import com.example.kltn.entity.ImageLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageLocationRepo extends JpaRepository<ImageLocation, Long> {
    List<ImageLocation> findByLocationId(Long locationId);
    void deleteByLocationId(Long locationId);
} 