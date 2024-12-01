package com.example.kltn.service.implement;

import com.example.kltn.entity.EventCategory;
import com.example.kltn.repository.EventCategoryRepo;
import com.example.kltn.service.EventCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventCategoryServiceImpl implements EventCategoryService {
    
    private final EventCategoryRepo eventCategoryRepo;
    
    @Override
    public EventCategory saveOrUpdate(EventCategory eventCategory) {
        return eventCategoryRepo.save(eventCategory);
    }
    
    @Override
    public void deleteEventCategory(Long id) {
        eventCategoryRepo.deleteById(id);
    }
    
    @Override
    public List<EventCategory> getAll() {
        return eventCategoryRepo.findAll();
    }
    
    @Override
    public EventCategory getByName(String name) {
        return eventCategoryRepo.findByCategoryName(name);
    }
    
    @Override
    public EventCategory getById(Long id) {
        return eventCategoryRepo.findById(id).orElse(null);
    }
    
    @Override
    public List<EventCategory> searchByName(String keyword) {
        return eventCategoryRepo.findByCategoryNameContaining(keyword);
    }
    
    @Override
    public boolean existsByName(String name) {
        return eventCategoryRepo.existsByCategoryName(name);
    }
} 