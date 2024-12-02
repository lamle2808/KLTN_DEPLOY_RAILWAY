package com.example.kltn.service;

import com.example.kltn.entity.Review;
import java.util.List;

public interface ReviewService {
    Review saveOrUpdate(Review review);
    List<Review> getReviewsByEventId(Long eventId);
    void deleteReview(Long id);
} 