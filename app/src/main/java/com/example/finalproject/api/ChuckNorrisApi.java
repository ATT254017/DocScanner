package com.example.finalproject.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ChuckNorrisApi {

    @GET("jokes/random")
    Call<ChuckNorrisQuote> getQuote();

}
