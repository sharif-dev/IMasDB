package com.example.imasdb.network;

import com.example.imasdb.model.movie_detailes.cast.CastsList;
import com.example.imasdb.model.movie_detailes.review.Reviews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDetailsApi {
    @GET("/movie/{movie_id}/credits")
    Call<CastsList> getCastsList(@Path("movie_id") String movieId, @Query("api_key") String apiKey);

    @GET("/movie/{movie_id}/reviews")
    Call<Reviews> getReviews(@Path("movie_id") String movieId, @Query("api_key") String apiKey);
}
