package com.example.imasdb.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.imasdb.MainActivity;
import com.example.imasdb.R;
import com.example.imasdb.model.Session;
import com.example.imasdb.model.Token;
import com.example.imasdb.model.User;
import com.example.imasdb.network.AuthApiEndpointInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        res = getResources();
        int loginType = getIntent().getIntExtra("loginType", -1);
        Log.i("logintTupe", Integer.toString(loginType));
        if (loginType == MainActivity.LoginLaunchType.LOGOUT.ordinal()) {
            logout();
        }

        Button loginButton = findViewById(R.id.login_button);
        final EditText username = findViewById(R.id.login_username);
        final EditText password = findViewById(R.id.login_password);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = username.getText().toString();
                String pass = password.getText().toString();
                login(name, pass);
            }
        });

    }

    private AuthApiEndpointInterface getAuthApi() {

        Resources res = getResources();
        String baseUrl = res.getString(R.string.base_url);
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
        return retrofit.create(AuthApiEndpointInterface.class);
    }

    protected void login(String username, String password) {
        AuthApiEndpointInterface myAuthApi = getAuthApi();
        User.getInstance().setAuthDetails(username, password);
        User.getInstance().setLoginSuccess(User.LoginSuccess.IN_PROGRESS);
        getRequestToken(myAuthApi, res.getString(R.string.api_key));
    }

    protected void getRequestToken(final AuthApiEndpointInterface myAuthApi, final String apiKey) {
        Log.i("salam", "getRequestToken");
        Call<Token> call = myAuthApi.getRequestToken(apiKey);

        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    User.getInstance().setRequestToken(response.body());
                    validateRequestToken(myAuthApi, apiKey);
                } else {
                    switch (response.code()) {
                        // TODO: implement error codes handling
                    }
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void validateRequestToken(final AuthApiEndpointInterface myAuthApi, final String apiKey) {
        String requestToken = User.getInstance().getRequestToken().requestToken;
        String username = User.getInstance().getUsername();
        String password = User.getInstance().getPassword();
        Log.i("salam", "validateRequestToken");

        Call<Token> call = myAuthApi.getValidateToken(apiKey, username, password, requestToken);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    User.getInstance().setRequestToken(response.body());
                    getSessionId(myAuthApi, apiKey);
                } else {
                    Log.i("salam", "validateRequestToken");

                    switch (response.code()) {
                        // TODO: implement error codes handling
                    }
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void getSessionId(AuthApiEndpointInterface myAuthApi, String apiKey) {
        String requestToken = User.getInstance().getRequestToken().requestToken;
        Log.i("salam", requestToken);

        Call<Session> call = myAuthApi.getUserSession(apiKey, requestToken);

        call.enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                Log.i("succeed", response.message());

                if (response.isSuccessful()) {
                    User.getInstance().setSessionToken(response.body());
                    User.getInstance().setLoginSuccess(User.LoginSuccess.SUCCEED);
                    finishLogin();
                    Log.i("succeed", response.body().getSessionId());
                } else {
                    Log.i("hello", response.message());

                    switch (response.code()) {
                        // TODO: implement error codes handling
                    }
                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {
                Log.i("sssssss", t.getMessage());

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void finishLogin() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("loginCompleted", MainActivity.LoginLaunchType.LOGIN);
        startActivity(intent);
    }

    public void logout() {
        AuthApiEndpointInterface myAuthApi = getAuthApi();
        Log.i("Session token", User.getInstance().getSessionToken().getSessionId());

        Call<Object> logout = myAuthApi.logout(res.getString(R.string.api_key), User.getInstance().getSessionToken().getSessionId());
        logout.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(), "you logged out successfully", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), Integer.toString(response.code()), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}