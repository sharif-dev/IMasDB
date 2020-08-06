package com.example.imasdb.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.imasdb.MainActivity;
import com.example.imasdb.R;
import com.example.imasdb.model.Account;
import com.example.imasdb.model.Session;
import com.example.imasdb.model.Token;
import com.example.imasdb.model.User;
import com.example.imasdb.network.AccountApiEndpointInterface;
import com.example.imasdb.network.AuthApiEndpointInterface;
import com.example.imasdb.network.RetrofitBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.imasdb.network.RetrofitBuilder.getAuthApi;

public class LoginActivity extends AppCompatActivity {
    Resources res;
    Button signUpBtn;
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
        signUpBtn = findViewById(R.id.signup_btn);
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
        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loadSignUpPage();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadSignUpPage() {
        String url = "https://www.themoviedb.org/signup";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
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
                    Toast.makeText(getApplicationContext(),"failed to connect!"+response.code(),Toast.LENGTH_SHORT);
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
                    Toast.makeText(getApplicationContext(),"failed to connect!"+response.code(),Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.i("salam", "validateRequestTok " + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void getSessionId(AuthApiEndpointInterface myAuthApi, final String apiKey) {
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
                    getAccountDetails(apiKey);
                } else {
                    Toast.makeText(getApplicationContext(),"failed to connect!"+response.code(),Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getAccountDetails(String apiKey) {
        AccountApiEndpointInterface accountApiEndpointInterface = RetrofitBuilder.getAccountApi();
        Call<Account> getAccount = accountApiEndpointInterface.getAccountDetail(apiKey,User.getUser().getSessionToken().getSessionId());
        getAccount.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if(response.isSuccessful()){
                    User.getUser().setAccount(response.body());
                    finishLogin();
                }
                else {
                    Log.i("getAccountDetail", "onResponse: "+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {

            }
        });
    }

    private void finishLogin() {
        User.getUser().setLoggedIn(true);
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
                    User.getUser().setLoggedIn(false);
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