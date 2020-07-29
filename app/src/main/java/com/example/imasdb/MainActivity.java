package com.example.imasdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.imasdb.model.Movie;
import com.example.imasdb.model.MovieListType;
import com.example.imasdb.model.User;
import com.example.imasdb.network.AuthApiEndpointInterface;
import com.example.imasdb.network.MovieListBuilder;
import com.example.imasdb.network.MovieListsApiEndpointInterface;
import com.example.imasdb.network.RetrofitBuilder;
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

    public enum LoginLaunchType {
        LOGIN, LOGOUT
    }

    private Resources res;
    List<Movie> m = new ArrayList<Movie>();

    RecyclerView recent;
    RecyclerView mostPopular;
    RecyclerView topRated;

    //    RecyclerView recent;
//    RecyclerView recent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getResources();
        setContentView(R.layout.activity_main);
//        if (!getIntent().hasExtra("loginCompleted")) {
//            launchComposeView(LoginLaunchType.LOGIN);
//        }

        MovieListBuilder movieListBuilder = new MovieListBuilder(res.getString(R.string.api_key));
        ArrayList<RecyclerView> recyclerViews = setAdapters();
        prepareLists(movieListBuilder, recyclerViews);


    }

    public void prepareLists(MovieListBuilder movieListBuilder, ArrayList<RecyclerView> recyclerViews) {
        for (int i = 0; i < MovieListType.values().length; i++) {
            RecyclerView recyclerView = recyclerViews.get(i);
            MovieListType movieListType = MovieListType.values()[i];
            movieListBuilder.getMovieList(movieListType, movieListType.adapter);
            recyclerView.setAdapter(movieListType.adapter);
            movieListType.adapter.setOnClickListener(new MovieAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Movie movie) {
                    Log.e("click", "clicked");
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Fragment fragment = MovieFragment.newInstance(movie);
                    ft.replace(R.id.main_activity, fragment);
                    ft.addToBackStack(null);
                    ft.show(fragment);
                }
            });
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            SnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setItemAnimator(new SlideInLeftAnimator());
            Log.i("listDownloadStarted", "downloading");
        }
    }

    public ArrayList<RecyclerView> setAdapters() {
        ArrayList<RecyclerView> res = new ArrayList<>();
        recent = findViewById(R.id.recently_played_recycler);
        mostPopular = findViewById(R.id.most_popular_recycler);
        topRated = findViewById(R.id.top_rated_recycler);
        res.add(recent);
        res.add(mostPopular);
        res.add(topRated);
        return res;
        //        List<Movie> mm = movieListBuilder.lat;
//        recent.setAdapter(new MovieAdapter(movieListBuilder.lat));
//        movieListBuilder.setAdapters(recent.getAdapter());
//        recent.getAdapter().notifyDataSetChanged();
//        recent.setLayoutManager(layoutManager);
////
//        RecyclerView pop = findViewById(R.id.most_popular_recycler);
//        pop.setAdapter(new MovieAdapter(movieListBuilder.mostPop.results));
//        pop.setLayoutManager(layoutManager);
//
//        RecyclerView topRated = findViewById(R.id.top_rated_recycler);
//        topRated.setAdapter(new MovieAdapter(movieListBuilder.topRated.results));
//        topRated.setLayoutManager(layoutManager);

    }

    private void logout() {
        launchComposeView(LoginLaunchType.LOGOUT);
    }

    public void launchComposeView(LoginLaunchType loginLaunchType) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra("loginType", loginLaunchType.ordinal());
        startActivity(intent);
    }


}