package com.example.kltn.service;

import com.example.kltn.entity.EventCategory;
import java.util.List;

public interface EventCategoryService {
    EventCategory saveOrUpdate(EventCategory eventCategory);
    void deleteEventCategory(Long id);
    List<EventCategory> getAll();
    EventCategory getByName(String name);
    EventCategory getById(Long id);
    List<EventCategory> searchByName(String keyword);
    boolean existsByName(String name);
} 