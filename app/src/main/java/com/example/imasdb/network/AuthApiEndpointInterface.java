package com.example.imasdb.network;

import com.example.imasdb.model.Session;
import com.example.imasdb.model.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApiEndpointInterface {
    @GET("/3/authentication/token/new")
    Call<Token> getRequestToken(@Query("api_key") String apiKey);

    @FormUrlEncoded
    @POST("/3/authentication/token/validate_with_login")
    Call<Token> getValidateToken(@Query("api_key") String apiKey,
                                 @Field("username") String username, @Field("password") String password, @Field("request_token") String requestToken);

    @FormUrlEncoded
    @POST("/3/authentication/session/new")
    Call<Session> getUserSession(@Query("api_key") String apiKey, @Field("request_token") String requestToken);
}
