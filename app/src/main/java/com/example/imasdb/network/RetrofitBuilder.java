package com.example.imasdb.network;

import android.app.Application;
import android.content.res.Resources;

import com.example.imasdb.MainActivity;
import com.example.imasdb.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    private static String baseUrl = "https://api.themoviedb.org/3/";

    static Gson gson = new GsonBuilder().setLenient().create();
    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson)).build();


    public static AuthApiEndpointInterface getAuthApi() {
        return retrofit.create(AuthApiEndpointInterface.class);
    }

    public static MovieListsApiEndpointInterface getMovieApi() {
        return retrofit.create(MovieListsApiEndpointInterface.class);
    }

    public static MovieDetailsApi getMovieDetailApi() {
        return retrofit.create(MovieDetailsApi.class);
    }
}

