package com.example.kltn.service;

import com.example.kltn.entity.Event;
import java.util.List;
import org.springframework.data.domain.Page;

public interface EventService {
    Event saveOrUpdate(Event event);
    List<Event> getAll();
    Event getById(Long id);
    void deleteEvent(Long id);
    Page<Event> searchEvents(
        Long categoryId, 
        Long locationId, 
        Double minPrice, 
        Double maxPrice, 
        String keyword,
        int page, 
        int size,
        String sortBy,
        String sortDir);
    Page<Event> getUpcomingEvents(int page, int size);
    Page<Event> getPopularEvents(int page, int size);
    List<Event> getRelatedEvents(Long eventId, int limit);
} 