package com.example.kltn.service.implement;

import com.example.kltn.entity.ImageService;
import com.example.kltn.repository.ImageServiceRepository;
import com.example.kltn.service.ImageServiceEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceEventServiceImpl implements ImageServiceEventService {
    
    private final ImageServiceRepository imageServiceRepository;
    
    @Override
    public ImageService save(ImageService imageService) {
        return imageServiceRepository.save(imageService);
    }
    
    @Override
    public List<ImageService> findByServiceEventId(Long serviceEventId) {
        return imageServiceRepository.findByServiceEventId(serviceEventId);
    }
    
    @Override
    public void deleteById(Long id) {
        imageServiceRepository.deleteById(id);
    }
    
    @Override
    public ImageService uploadImageForService(MultipartFile file, Long serviceEventId) {
        // TODO: Implement file upload logic here
        return null;
    }
} 