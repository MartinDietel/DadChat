package com.dadapp.seniorproject.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {
    List<Review> findAll();
    List<Review> findReviewsByPlaceIdIsLikeOrderByTimestampDesc(String placeId);

}
