package com.dadapp.seniorproject.user;


import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.io.IOException;
import java.util.List;

@Component
public interface IUserRetrofitDAO {
//    @GET("http://localhost:8081/users/search")
//    Call<List<User>> searchUsers(@Query("query") String searchTerm) throws IOException;
}
