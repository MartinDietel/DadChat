package com.dadapp.seniorproject.search;

import javassist.bytecode.stackmap.TypeData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public interface ISearchRetrofitDAO {


    @GET("http://localhost:8081/searches/search")
    Call<List<Search>> searchSearches(@Query("query") String searchTerm) throws IOException;

}
