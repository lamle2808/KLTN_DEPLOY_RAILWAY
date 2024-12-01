package com.example.kltn.service.implement;

import com.example.kltn.entity.Event;
import com.example.kltn.repository.EventRepo;
import com.example.kltn.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepo eventRepo;

    @Override
    public Event saveOrUpdate(Event event) {
        try {
            validateEvent(event);
            return eventRepo.save(event);
        } catch (Exception e) {
            log.error("Lỗi khi lưu event: {}", e.getMessage());
            throw new RuntimeException("Không thể lưu event: " + e.getMessage());
        }
    }

    @Override
    public List<Event> getAll() {
        return eventRepo.findAll();
    }

    @Override
    public Event getById(Long id) {
        return eventRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy event với id: " + id));
    }

    @Override
    public void deleteEvent(Long id) {
        try {
            Event event = getById(id);
            if (!event.getOrders().isEmpty()) {
                throw new RuntimeException("Không thể xóa event đã có đơn đặt");
            }
            eventRepo.deleteById(id);
        } catch (Exception e) {
            log.error("Lỗi khi xóa event: {}", e.getMessage());
            throw new RuntimeException("Không thể xóa event: " + e.getMessage());
        }
    }

    @Override
    public Page<Event> searchEvents(
        Long categoryId, 
        Long locationId, 
        Double minPrice, 
        Double maxPrice, 
        String keyword,
        int page, 
        int size,
        String sortBy,
        String sortDir) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        
        return eventRepo.searchEvents(
            categoryId,
            locationId,
            minPrice,
            maxPrice,
            keyword,
            pageRequest
        );
    }

    @Override
    public List<Event> getRelatedEvents(Long eventId, int limit) {
        try {
            Event event = getById(eventId);
            return eventRepo.findByCategoryAndIdNot(
                event.getCategory(), 
                eventId, 
                PageRequest.of(0, limit)
            ).getContent();
        } catch (Exception e) {
            log.error("Lỗi khi lấy events liên quan: {}", e.getMessage());
            throw new RuntimeException("Không thể lấy events liên quan: " + e.getMessage());
        }
    }

    @Override
    public Page<Event> getPopularEvents(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return eventRepo.findPopularEvents(pageRequest);
    }

    @Override
    public Page<Event> getUpcomingEvents(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("enventDate").ascending());
        return eventRepo.findByEnventDateGreaterThanEqual(new Date(), pageRequest);
    }

    private void validateEvent(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event không được null");
        }
        if (!StringUtils.hasText(event.getEnventName())) {
            throw new IllegalArgumentException("Tên event không được trống");
        }
        if (event.getDepositRequired() < 0) {
            throw new IllegalArgumentException("Tiền đặt cọc không được âm");
        }
    }
} 