package com.example.kltn.service.implement;

import com.example.kltn.entity.Review;
import com.example.kltn.repository.ReviewRepo;
import com.example.kltn.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepo reviewRepo;

    @Override
    public Review saveOrUpdate(Review review) {
        return reviewRepo.save(review);
    }

    @Override
    public List<Review> getReviewsByEventId(Long eventId) {
        return reviewRepo.findByEventId(eventId);
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepo.deleteById(id);
    }
} 