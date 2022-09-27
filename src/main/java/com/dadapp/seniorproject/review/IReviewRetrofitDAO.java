package com.dadapp.seniorproject.review;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.io.IOException;
import java.util.List;

public interface IReviewRetrofitDAO {

    @GET("http://localhost:8081/reviews/search")
    Call<List<Review>> searchReviews(@Query("query") String placeId) throws IOException;
}
