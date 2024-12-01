package com.example.kltn.controller;

import com.example.kltn.entity.EventCategory;
import com.example.kltn.service.EventCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/event-categories")
@RequiredArgsConstructor
public class EventCategoryController {

    private final EventCategoryService eventCategoryService;

    @GetMapping
    public ResponseEntity<List<EventCategory>> getAllCategories() {
        return ResponseEntity.ok(eventCategoryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventCategory> getCategoryById(@PathVariable Long id) {
        EventCategory category = eventCategoryService.getById(id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category);
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventCategory>> searchCategories(@RequestParam String keyword) {
        return ResponseEntity.ok(eventCategoryService.searchByName(keyword));
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody EventCategory category) {
        if (eventCategoryService.existsByName(category.getCategoryName())) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Danh mục này đã tồn tại"));
        }
        return ResponseEntity.ok(eventCategoryService.saveOrUpdate(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody EventCategory category) {
        EventCategory existingCategory = eventCategoryService.getById(id);
        if (existingCategory == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Kiểm tra nếu tên mới đã tồn tại và khác với tên hiện tại
        if (!existingCategory.getCategoryName().equals(category.getCategoryName()) 
            && eventCategoryService.existsByName(category.getCategoryName())) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Tên danh mục này đã tồn tại"));
        }
        
        category.setId(id);
        return ResponseEntity.ok(eventCategoryService.saveOrUpdate(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        EventCategory category = eventCategoryService.getById(id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        
        if (!category.getEvents().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Không thể xóa danh mục đang có sự kiện"));
        }
        
        eventCategoryService.deleteEventCategory(id);
        return ResponseEntity.ok()
            .body(Map.of("message", "Đã xóa danh mục thành công"));
    }
} 