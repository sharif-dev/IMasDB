package com.example.imasdb.network;

import com.example.imasdb.model.movie_detailes.cast.Cast;
import com.example.imasdb.model.movie_detailes.cast.CastsList;
import com.example.imasdb.model.movie_detailes.review.Reviews;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDetailsApi {
    @GET("movie/{movie_id}/credits")
    Call<CastsList> getCastsList(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<Reviews> getReviews(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @Headers("Content-Type: application/json;charset=utf-8")
    @FormUrlEncoded
    @POST("movie/{movie_id}/rating")
    Call<Object> rateMovie(@Path("movie_id") int movieId, @Query("api_key") String apiKey, @Query("session_id") String sessionId, @Field("value") int rate);

}
