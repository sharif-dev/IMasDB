package com.example.imasdb.network;

import com.example.imasdb.model.Movie;
import com.example.imasdb.model.MovieList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface MovieListsApiEndpointInterface {

    @GET("movie/now_playing")
    Call<MovieList> getLatestMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MovieList> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieList> getTopRated(@Query("api_key") String apiKey);

    @GET("search/movie")
    Call<MovieList> getSearchResults(@Query("api_key") String apikey,@Query("query") String query);



}
