package com.example.kltn.service.implement;

import com.example.kltn.entity.ImageLocation;
import com.example.kltn.entity.Location;
import com.example.kltn.repository.ImageLocationRepo;
import com.example.kltn.repository.LocationRepo;
import com.example.kltn.service.CloudinaryService;
import com.example.kltn.service.ImageLocationService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageLocationServiceImpl implements ImageLocationService {
    
    private final ImageLocationRepo imageLocationRepo;
    private final LocationRepo locationRepo;
    private final CloudinaryService cloudinaryService;
    private static final Logger log = LoggerFactory.getLogger(ImageLocationServiceImpl.class);
    
    @Override
    public ImageLocation save(ImageLocation imageLocation) {
        return imageLocationRepo.save(imageLocation);
    }
    
    @Override
    public void delete(Long id) {
        imageLocationRepo.deleteById(id);
    }
    
    @Override
    public ImageLocation getById(Long id) {
        return imageLocationRepo.findById(id).orElse(null);
    }
    
    @Override
    public ImageLocation uploadImageForLocation(MultipartFile file, Long locationId) throws IOException {
        log.info("Starting upload image for location: {}", locationId);
        
        Location location = locationRepo.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        log.info("Found location: {}", location.getId());
                
        Map result = cloudinaryService.upload(file);
        log.info("Uploaded to Cloudinary: {}", result);
        
        ImageLocation imageLocation = new ImageLocation();
        imageLocation.setLocation(location);
        imageLocation.setImageLink((String) result.get("url"));
        imageLocation.setIdCloud((String) result.get("public_id"));
        
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        Instant instant = Instant.from(formatter.parse((String) result.get("created_at")));
        imageLocation.setDate(Date.from(instant));
        
        imageLocation.setType((String) result.get("format"));
        
        int bytes = (int) result.get("bytes");
        double size = (double) bytes / 1024;
        String sizeFormat = String.format("%.3f", size);
        imageLocation.setSize(sizeFormat);
        
        ImageLocation saved = imageLocationRepo.save(imageLocation);
        log.info("Saved image location: {}", saved.getId());
        
        return saved;
    }
} 