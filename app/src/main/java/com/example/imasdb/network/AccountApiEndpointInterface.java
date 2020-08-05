package com.example.imasdb.network;

import com.example.imasdb.model.Account;
import com.example.imasdb.model.MovieList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccountApiEndpointInterface {

    @GET("account/{account_id}/favorite/movies")
    Call<MovieList> getFavouriteMovies( @Path("account_id") int accountId,@Query("api_key") String apikey,@Query("session_id") String sessionId);
    @GET("account")
    Call<Account> getAccountDetail(@Query("api_key") String apikey,@Query("session_id") String sessionId);

    @GET("account/{account_id}/watchlist/movies")
    Call<MovieList> getWatchListMovies(@Path("account_id") int accountId,@Query("api_key") String apikey ,@Query("session_id") String sessionId);

}
