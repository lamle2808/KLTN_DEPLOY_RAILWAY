package com.example.kltn.service.implement;

import com.example.kltn.entity.ImageEvent;
import com.example.kltn.repository.ImageEventRepo;
import com.example.kltn.service.ImageEventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageEventServiceImpl implements ImageEventService {
    
    private final ImageEventRepo imageEventRepo;
    
    @Override
    public ImageEvent save(ImageEvent imageEvent) {
        return imageEventRepo.save(imageEvent);
    }
    
    @Override
    public void delete(Long id) {
        imageEventRepo.deleteById(id);
    }
    
    @Override
    public ImageEvent getById(Long id) {
        return imageEventRepo.findById(id).orElse(null);
    }

    @Override
    public List<ImageEvent> getAllByEventId(Long eventId) {
        return imageEventRepo.findByEventId(eventId);
    }

    @Override
    public ImageEvent uploadImageForEvent(MultipartFile file, Long eventId) {
        // TODO: Implement image upload logic
        return null;
    }
} 