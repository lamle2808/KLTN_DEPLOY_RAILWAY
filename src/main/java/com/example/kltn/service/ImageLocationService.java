package com.example.kltn.service;

import com.example.kltn.entity.ImageLocation;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
public interface ImageLocationService {
    ImageLocation save(ImageLocation imageLocation);
    void delete(Long id);
    ImageLocation getById(Long id);
    ImageLocation uploadImageForLocation(MultipartFile file, Long locationId) throws IOException;
} 