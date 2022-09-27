package com.dadapp.seniorproject.event;

import com.dadapp.seniorproject.review.Review;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.io.IOException;
import java.util.List;

public interface IEventRetrofitDAO {

   // @GET("http://localhost:8081/events/search")
   // Call<List<Events>> searchEvents(@Query("query") String lat, String lon) throws IOException;
}
