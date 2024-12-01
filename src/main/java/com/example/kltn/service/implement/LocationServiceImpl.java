package com.example.kltn.service.implement;

import com.example.kltn.entity.Location;
import com.example.kltn.entity.Event;
import com.example.kltn.repository.LocationRepo;
import com.example.kltn.repository.EventRepo;
import com.example.kltn.service.LocationService;
import com.example.kltn.service.EventService;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.annotation.SessionScope;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@SessionScope
public class LocationServiceImpl implements LocationService {
    
    private final LocationRepo locationRepo;
    private final EventService eventService;

    @Override
    public Location saveOrUpdate(Location location) {
        try {
            return locationRepo.save(location);
        } catch (Exception e) {
            log.error("Lỗi khi lưu hoặc cập nhật location: ", e);
            throw e;
        }
    }

    @Override
    public Location getById(Long id) {
        try {
            return locationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy location"));
        } catch (Exception e) {
            log.error("Lỗi khi tìm location với id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Location addEventToLocation(Long locationId, Event event) {
        try {
            Location location = getById(locationId);
            event.setEventLocation(location);
            location.getEvents().add(event);
            return locationRepo.save(location);
        } catch (Exception e) {
            log.error("Lỗi khi thêm sự kiện vào location: ", e);
            throw e;
        }
    }

    @Override
    public Location removeEventFromLocation(Long locationId, Long eventId) {
        try {
            Location location = getById(locationId);
            location.getEvents().removeIf(event -> event.getId().equals(eventId));
            return locationRepo.save(location);
        } catch (Exception e) {
            log.error("Lỗi khi xóa sự kiện khỏi location: ", e);
            throw e;
        }
    }

    @Override
    public List<Location> getAll() {
        try {
            return locationRepo.findAll();
        } catch (Exception e) {
            log.error("Lỗi khi lấy tất cả các location: ", e);
            throw e;
        }
    }

    @Override
    public void deleteLocation(Long id) {
        try {
            Location location = getById(id);
            locationRepo.delete(location);
        } catch (Exception e) {
            log.error("Lỗi khi xóa location với id: {}", id, e);
            throw e;
        }
    }
} 