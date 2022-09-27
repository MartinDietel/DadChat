package com.dadapp.seniorproject.review;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class ReviewController {

    private final static Logger log = LoggerFactory.getLogger(ReviewController.class);
    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/reviewlist")
    public String reviewList(Model model) {
        model.addAttribute("listReviews", reviewService.getReviews());
        model.addAttribute("text", "Review List");
        return "reviewlist";
    }

    @RequestMapping(value = "/checkReviews", method = RequestMethod.POST)
    @ResponseBody
    public List<Review> checkReviews(@RequestParam String placeId)
    {
        return reviewService.searchReviews(placeId);
    }

    @RequestMapping(value = "/addReview", method = RequestMethod.POST)
    @ResponseBody
    public Review addReview(@ModelAttribute Review review, Model model, Principal principal)
    {
        review.addTimestamp();
        review.setUsername(principal.getName());
        reviewService.addReview(review);
        return review;
    }
}
