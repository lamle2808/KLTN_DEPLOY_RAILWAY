package com.example.kltn.controller;

import com.example.kltn.entity.Review;
import com.example.kltn.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        Review savedReview = reviewService.saveOrUpdate(review);
        return ResponseEntity.ok(savedReview);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Review>> getReviewsByEventId(@PathVariable Long eventId) {
        List<Review> reviews = reviewService.getReviewsByEventId(eventId);
        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
} 