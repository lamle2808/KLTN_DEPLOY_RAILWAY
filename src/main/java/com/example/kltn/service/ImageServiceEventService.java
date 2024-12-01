package com.example.kltn.service;

import com.example.kltn.entity.ImageService;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ImageServiceEventService {
    ImageService save(ImageService imageService);
    List<ImageService> findByServiceEventId(Long serviceEventId);
    void deleteById(Long id);
    ImageService uploadImageForService(MultipartFile file, Long serviceId) throws IOException;
} 