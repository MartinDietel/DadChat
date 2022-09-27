package com.dadapp.seniorproject.review;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;


@Entity
@Table(name = "review")
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
@Getter
@Setter
public class Review {

    private final static Logger log = LoggerFactory.getLogger(Review.class);

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int reviewId;

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    private String placeId;

    private String username;

    private int reviewStars;

    private String reviewText;

    private String timestamp;

    public void addTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.timestamp = sdf2.format(timestamp);
    }

    public Review(String username, String placeId, int reviewStars, String reviewText, String timestamp) {
        this.username = username;
        this.placeId = placeId;
        this.reviewStars = reviewStars;
        this.reviewText = reviewText;
        this.timestamp = timestamp;
    }

    public Review(String username, String placeId, int reviewStars, String reviewText) {
        this.username = username;
        this.placeId = placeId;
        this.reviewStars = reviewStars;
        this.reviewText = reviewText;
        addTimestamp();
    }
}
