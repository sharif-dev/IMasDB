package com.example.imasdb.network;

import android.graphics.Bitmap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface ImageApiEndpointInterface {
    @GET("/t/p/w500/")
    @Streaming
    Call<Bitmap> getImage(@Query("api_key") String apiKey);
}
