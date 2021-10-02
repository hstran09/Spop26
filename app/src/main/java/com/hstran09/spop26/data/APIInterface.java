package com.hstran09.spop26.data;

import com.hstran09.spop26.model.SearchResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("search")
    Call<SearchResponse> search(@Query("q") String q);
}
