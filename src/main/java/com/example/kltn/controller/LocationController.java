package com.example.kltn.controller;

import com.example.kltn.entity.Location;
import com.example.kltn.entity.Account;
import com.example.kltn.config.JwtService;
import com.example.kltn.service.LocationService;
import com.example.kltn.repository.AccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
public class LocationController {

    private final JwtService jwtService;
    @Autowired
    private LocationService locationService;
    private final AccountRepo accountRepo;

    @GetMapping
    public ResponseEntity<?> getAllLocations() {
        try {
            return ResponseEntity.ok(locationService.getAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLocationById(@PathVariable Long id) {
        try {
            Location location = locationService.getById(id);
            return ResponseEntity.ok(location);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createLocation(
        //     @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody Location location) {
       
        
        // String jwt = token.substring(7); // Bỏ "Bearer " để lấy JWT
        // log.info("Received token: {}", jwt);
        // if (token == null || !token.startsWith("Bearer ")) {
        //     return ResponseEntity.badRequest().body("Token không được cung cấp hoặc không hợp lệ.");
        // }
        
        // try {
        //     // Trích xuất email từ token
        //     String email = jwtService.extractUsername(jwt);
            
        //     // Tìm tài khoản người dùng dựa trên email
        //     Account author = accountRepo.findByEmail(email)
        //             .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        //     // Kiểm tra tính hợp lệ của token với UserDetails
        //     UserDetails userDetails = new User(author.getEmail(), author.getPassword(), new ArrayList<>());
        //     if (!jwtService.isTokenValid(jwt, userDetails)) {
        //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ.");
        //     }
            
            // Thực hiện logic tạo Location
            Location savedLocation = locationService.saveOrUpdate(location);
            return ResponseEntity.ok(savedLocation);
            
    //     } catch (Exception e) {
    //         log.error("Lỗi khi tạo location: ", e);
    //         return ResponseEntity.badRequest().body("Lỗi khi tạo location: " + e.getMessage());
    //     }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateLocation(
            @PathVariable Long id,
            @RequestBody Location location) {
        try {
            Location existingLocation = locationService.getById(id);
            if (existingLocation == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Gi��� nguyên author từ location cũ
            location.setId(id);
            location.setAuthor(existingLocation.getAuthor());
            
            Location updatedLocation = locationService.saveOrUpdate(location);
            return ResponseEntity.ok(updatedLocation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteLocation(@PathVariable Long id) {
        try {
            locationService.deleteLocation(id);
            return ResponseEntity.ok("Xóa location thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }
} 