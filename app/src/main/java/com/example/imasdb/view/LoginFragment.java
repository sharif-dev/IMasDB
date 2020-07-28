package com.example.imasdb.view;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View loginView = inflater.inflate(R.layout.fragment_login, container, false);
        Button loginButton = loginView.findViewById(R.id.login_button);
        final EditText username = loginView.findViewById(R.id.login_username);
        final EditText password = loginView.findViewById(R.id.login_password);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = username.getText().toString();
                String pass = password.getText().toString();
                login(name, pass);
            }
        });
        return loginView;
    }

    protected void login(String username, String password) {
        Resources res = getResources();
        String baseUrl = res.getString(R.string.base_url);
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
        AuthApiEndpointInterface myAuthApi = retrofit.create(AuthApiEndpointInterface.class);
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
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
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

                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }
}