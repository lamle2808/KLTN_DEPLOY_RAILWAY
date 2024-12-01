package com.example.kltn.controller;

import com.example.kltn.entity.ImageEvent;
import com.example.kltn.entity.ServiceEvent;
import com.example.kltn.entity.ImageService;
import com.example.kltn.entity.Event;
import com.example.kltn.service.CloudinaryService;
import com.example.kltn.service.ImageEventService;
import com.example.kltn.service.ServiceEventService;
import com.example.kltn.service.ImageServiceEventService;
import com.example.kltn.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class EventController {

    private final CloudinaryService cloudinaryService;
    private final ImageEventService imageEventService;
    private final ServiceEventService serviceEventService;
    private final ImageServiceEventService imageServiceEventService;
    private final EventService eventService;
    
    @PostMapping(value = "/upload-event-image/{eventId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadEventImage(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long eventId) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "File không được để trống"));
            }

            Map result = cloudinaryService.upload(file);
            ImageEvent imageEvent = new ImageEvent();
            imageEvent.setImageLink((String) result.get("url"));
            imageEvent.setIdCloud((String) result.get("public_id"));
            
            Event event = eventService.getById(eventId);
            if (event == null) {
                return ResponseEntity.badRequest().body("Event không tồn tại");
            }
            imageEvent.setEvent(event);
            
            DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
            Instant instant = Instant.from(formatter.parse((String) result.get("created_at")));
            imageEvent.setDate(Date.from(instant));
            imageEvent.setType((String) result.get("format"));
            
            int bytes = (int) result.get("bytes");
            double size = (double) bytes / 1024;
            String sizeFormat = String.format("%.3f", size);
            imageEvent.setSize(sizeFormat);
            
            return ResponseEntity.ok().body(imageEventService.save(imageEvent));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping(value = "/upload-service-image/{serviceId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadServiceImage(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long serviceId) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "File không được để trống"));
            }

            Map result = cloudinaryService.upload(file);
            ImageService imageService = new ImageService();
            imageService.setImageLink((String) result.get("url"));
            imageService.setIdCloud((String) result.get("public_id"));
            
            ServiceEvent service = serviceEventService.getById(serviceId);
            if (service == null) {
                return ResponseEntity.badRequest().body("Service không tồn tại");
            }
            imageService.setServiceEvent(service);
            
            DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
            Instant instant = Instant.from(formatter.parse((String) result.get("created_at")));
            imageService.setDate(Date.from(instant));
            imageService.setType((String) result.get("format"));
            
            int bytes = (int) result.get("bytes");
            double size = (double) bytes / 1024;
            String sizeFormat = String.format("%.3f", size);
            imageService.setSize(sizeFormat);
            
            return ResponseEntity.ok().body(imageServiceEventService.save(imageService));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Event>> searchEvents(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "eventDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        
        try {
            Page<Event> events = eventService.searchEvents(
                categoryId, locationId, minPrice, maxPrice, keyword, 
                page, size, sortBy, sortDir);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new PageImpl<>(Collections.emptyList()));
        }
    }

    @GetMapping("/popular")
    public ResponseEntity<Page<Event>> getPopularEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Event> events = eventService.getPopularEvents(page, size);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new PageImpl<>(Collections.emptyList()));
        }
    }

    @GetMapping("/upcoming")
    public ResponseEntity<Page<Event>> getUpcomingEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Event> events = eventService.getUpcomingEvents(page, size);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new PageImpl<>(Collections.emptyList()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody Event event) {
        try {
            return ResponseEntity.ok(eventService.saveOrUpdate(event));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        try {
            event.setId(id);
            return ResponseEntity.ok(eventService.saveOrUpdate(event));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
            return ResponseEntity.ok()
                .body(Map.of("message", "Đã xóa sự kiện thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable Long id) {
        try {
            Event event = eventService.getById(id);
            if (event == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(event);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
} 