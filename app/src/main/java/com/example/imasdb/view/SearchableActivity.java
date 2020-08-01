package com.example.imasdb.view;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Log.i(TAG, "onCreate: ");
        searchResultLv = findViewById(R.id.search_result_lv);
        adapter = new SearchResultAdapter(this,new ArrayList<Movie>());
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
        Call<MovieList> movieList = movieListApi.getSearchResults(getResources().getString(R.string.api_key),query);
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

}
