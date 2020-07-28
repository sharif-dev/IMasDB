package com.example.imasdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.imasdb.model.User;
import com.example.imasdb.network.AuthApiEndpointInterface;
import com.example.imasdb.view.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    public enum LoginLaunchType {
        LOGIN, LOGOUT
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!getIntent().hasExtra("loginCompleted")) {
            launchComposeView(LoginLaunchType.LOGIN);
        } else {
            logout();
        }

    }

    private void logout() {
        launchComposeView(LoginLaunchType.LOGOUT);
    }

    public void launchComposeView(LoginLaunchType loginLaunchType) {
        // first parameter is the context, second is the class of the activity to launch
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra("loginType", loginLaunchType.ordinal());

        startActivity(intent); // brings up the second activity

    }

}