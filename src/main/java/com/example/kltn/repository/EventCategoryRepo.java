package com.example.kltn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.example.kltn.entity.EventCategory;

@Repository
public interface EventCategoryRepo extends JpaRepository<EventCategory, Long> {
    EventCategory findByCategoryName(String categoryName);
    List<EventCategory> findByCategoryNameContaining(String keyword);
    boolean existsByCategoryName(String categoryName);
}
