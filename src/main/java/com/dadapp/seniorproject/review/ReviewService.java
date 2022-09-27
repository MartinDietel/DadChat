package com.dadapp.seniorproject.review;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public class ReviewService implements IReviewService {

    Logger log = LoggerFactory.getLogger(ReviewService.class);
    private final ReviewRepo reviewRepo;

    public ReviewService(ReviewRepo reviewRepo) {
        this.reviewRepo = reviewRepo;
    }


    @Override
    public List<Review> getReviews() {
        return reviewRepo.findAll();
    }

    public List<Review> searchReviews(String placeId) {
        return reviewRepo.findReviewsByPlaceIdIsLikeOrderByTimestampDesc(placeId);
    }

    public void addReview(Review review) {
        reviewRepo.save(review);

   }

}
