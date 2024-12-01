package com.example.kltn.service;

import com.example.kltn.entity.ImageEvent;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ImageEventService {
    ImageEvent save(ImageEvent imageEvent);
    void delete(Long id);
    ImageEvent getById(Long id);
    List<ImageEvent> getAllByEventId(Long eventId);
    ImageEvent uploadImageForEvent(MultipartFile file, Long eventId) throws IOException;
} 