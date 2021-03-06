package com.example.imasdb.view;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import com.example.imasdb.MainActivity;
import com.example.imasdb.R;
import com.example.imasdb.model.Movie;
import com.example.imasdb.model.MovieList;
import com.example.imasdb.network.MovieListsApiEndpointInterface;
import com.example.imasdb.network.RetrofitBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchableActivity extends AppCompatActivity {

    private static final String TAG = "SEARCHABLE_ACTIVITY";
    private ListView searchResultLv;
    private SearchResultAdapter adapter;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.i(TAG, "onCreate: ");
        searchResultLv = findViewById(R.id.search_result_lv);
        adapter = new SearchResultAdapter(this, new ArrayList<Movie>());
        adapter.setOnClickListener(new OnMovieClickListener() {
            @Override
            public void onMovieClick(Movie movie) {
                Log.i(TAG, "onItemClick: " + movie);
                Intent intent = new Intent(SearchableActivity.this, MainActivity.class);
                intent.putExtra("searchRes", movie);
                startActivity(intent);
            }
        });
        searchResultLv.setAdapter(adapter);
        Intent intent = getIntent();
        handleIntent(intent);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(TAG, "onNewIntent: ");
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Log.i(TAG, "handleIntent: ");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchQuery(query);
        }
    }

    private void searchQuery(String query) {
        MovieListsApiEndpointInterface movieListApi = RetrofitBuilder.getMovieApi();
        Call<MovieList> movieList = movieListApi.getSearchResults(getResources().getString(R.string.api_key), query);
        movieList.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<Movie> movies = response.body().results;
                        adapter.clear();
                        for (Movie movie : movies) {
                            adapter.add(movie);
                        }
                        adapter.notifyDataSetChanged();
                    }
                } else Log.e(TAG, "no result" + response.code());
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_view).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }
}
