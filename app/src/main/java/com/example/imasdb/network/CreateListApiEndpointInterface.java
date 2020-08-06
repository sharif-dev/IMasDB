package com.example.imasdb.network;

import com.example.imasdb.model.RequestBodies.AddToListBody;
import com.example.imasdb.model.RequestBodies.CreateListBody;
import com.example.imasdb.model.list_models.List;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CreateListApiEndpointInterface {
    @GET("account/{account_id}/lists")
    Call<List> getLists(@Path("account_id") int accountId, @Query("api_key") String apiKey, @Query("session_id") String sessionId, @Query("page") int page);

    @POST("list")
    @Headers("Content-Type: application/json;charset=utf-8")
    Call<Object> createList(@Query("api_key") String apiKey, @Query("session_id") String sessionId, @Body CreateListBody name);

    @DELETE("list/{list_id}")
    Call<Object> deleteList(@Path("list_id") String listId, @Query("api_key") String apiKey, @Query("session_id") String sessionId);

    @POST("list/{list_id}/add_item")
    @Headers("Content-Type: application/json;charset=utf-8")
    Call<Object> addMovieToList(@Path("list_id") String listId, @Query("api_key") String apiKey, @Query("session_id") String sessionId, @Body AddToListBody body);
}
