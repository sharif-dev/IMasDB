package com.example.imasdb.network;

import com.example.imasdb.model.Session;
import com.example.imasdb.model.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApiEndpointInterface {
    @GET("/authentication/token/new")
    Call<Token> getRequestToken(@Query("api_key") String apiKey);

    @POST
    Call<Token> getValidateToken(@Query("api_key") String apiKey,
                                 @Body String username, @Body String password, @Body String requestToken);

    @POST
    Call<Session> getUserSession(@Query("api_key") String apiKey, @Body String requestToken);
}
