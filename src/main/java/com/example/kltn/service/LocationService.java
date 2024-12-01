package com.example.kltn.service;

import com.example.kltn.entity.Location;
import com.example.kltn.entity.Event;
import java.util.List;

public interface LocationService {
    List<Location> getAll();
    Location saveOrUpdate(Location location);
    Location getById(Long id);
    Location addEventToLocation(Long locationId, Event event);
    Location removeEventFromLocation(Long locationId, Long eventId);
    void deleteLocation(Long id);
    // Thêm các phương thức khác nếu cần
}