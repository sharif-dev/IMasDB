package com.example.imasdb.network;

import com.example.imasdb.model.RequestBodies.CreateListBody;
import com.example.imasdb.model.list_models.List;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
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

}
