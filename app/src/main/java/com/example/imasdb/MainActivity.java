package com.example.imasdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.imasdb.model.Movie;
import com.example.imasdb.model.MovieListType;
import com.example.imasdb.model.User;
import com.example.imasdb.network.AuthApiEndpointInterface;
import com.example.imasdb.network.MovieListBuilder;
import com.example.imasdb.network.MovieListsApiEndpointInterface;
import com.example.imasdb.network.RetrofitBuilder;
import com.example.imasdb.view.ListsFragment;
import com.example.imasdb.view.LoginActivity;
import com.example.imasdb.view.MovieAdapter;
import com.example.imasdb.view.MovieFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.ScaleInAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_ACTIVITY";

    public enum LoginLaunchType {
        LOGIN, LOGOUT
    }

    private Resources res;
    List<Movie> m = new ArrayList<Movie>();

    private Activity context;

    //        RecyclerView recent;
//    RecyclerView recent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getResources();
        setContentView(R.layout.activity_main);
//        if (!getIntent().hasExtra("loginCompleted")) {
//            launchComposeView(LoginLaunchType.LOGIN);
//        }
        context = this;
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.main_activity, new ListsFragment());
//        ft.commit();

    }


    private void logout() {
        launchComposeView(LoginLaunchType.LOGOUT);
    }

    public void launchComposeView(LoginLaunchType loginLaunchType) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra("loginType", loginLaunchType.ordinal());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().popBackStack();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        Log.i(TAG, "onCreateOptionsMenu: ");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle().equals("search")) {
            Log.i(TAG, "onOptionsItemSelected: "+item.getTitle());
            return onSearchRequested();
        } else {
            return false;
        }
    }
}