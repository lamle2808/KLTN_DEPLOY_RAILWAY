package com.example.kltn.controller;

import com.example.kltn.entity.ServiceEvent;
import com.example.kltn.entity.ImageService;
import com.example.kltn.service.ServiceEventService;
import com.example.kltn.service.ImageServiceEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/services")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ServiceEventController {

    private final ServiceEventService serviceEventService;
    private final ImageServiceEventService imageServiceEventService;

    @GetMapping
    public ResponseEntity<List<ServiceEvent>> getAllServices() {
        List<ServiceEvent> services = serviceEventService.getAll();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceEvent> getServiceById(@PathVariable Long id) {
        ServiceEvent service = serviceEventService.getById(id);
        return ResponseEntity.ok(service);
    }

    @PostMapping
    public ResponseEntity<ServiceEvent> createService(@RequestBody ServiceEvent serviceEvent) {
        ServiceEvent createdService = serviceEventService.saveOrUpdate(serviceEvent);
        return ResponseEntity.ok(createdService);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceEvent> updateService(@PathVariable Long id, @RequestBody ServiceEvent serviceEvent) {
        serviceEvent.setId(id);
        ServiceEvent updatedService = serviceEventService.saveOrUpdate(serviceEvent);
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceEventService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/upload-images/{serviceId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadServiceImages(
            @RequestParam("files") MultipartFile[] files,
            @PathVariable Long serviceId) {
        try {
            if (files.length == 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Không có file nào được chọn"));
            }

            List<ImageService> uploadedImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    ImageService imageService = imageServiceEventService.uploadImageForService(file, serviceId);
                    uploadedImages.add(imageService);
                }
            }

            return ResponseEntity.ok(uploadedImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Lỗi khi upload ảnh: " + e.getMessage()));
        }
    }
} 