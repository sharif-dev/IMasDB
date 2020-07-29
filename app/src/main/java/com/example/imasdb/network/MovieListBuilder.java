package com.example.imasdb.network;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imasdb.MainActivity;
import com.example.imasdb.R;
import com.example.imasdb.model.Movie;
import com.example.imasdb.model.MovieList;
import com.example.imasdb.model.MovieListType;
import com.example.imasdb.view.MovieAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.imasdb.model.MovieListType.LATEST;
import static com.example.imasdb.model.MovieListType.MOST_POPULAR;
import static com.example.imasdb.model.MovieListType.TOP_RATED;

public class MovieListBuilder {
    private String apiKey;
    //    private static MovieListBuilder movieListBuilder = null;
    public MovieList latest = new MovieList();
    public MovieList mostPop = new MovieList();
    public MovieList topRated = new MovieList();

    public List<Movie> lat = new ArrayList<>();
    public List<Movie> mostP = new ArrayList<>();
    public List<Movie> topR = new ArrayList<>();

    private RecyclerView.Adapter topRatedAdapter, popAdapter, latestAdapter;

    public MovieListBuilder(String apiKey) {
        this.apiKey = apiKey;
    }

    public void newGetMovieList() {

    }

    public void getMovieList(MovieListType movieListType, MovieAdapter adapter) {

        MovieListsApiEndpointInterface movieListsApi = RetrofitBuilder.getMovieApi();
        switch (movieListType) {
            case LATEST:
                Call<MovieList> latestMovies = movieListsApi.getLatestMovies(apiKey);
                setEnqueue(latestMovies, LATEST, adapter);
                break;
            case TOP_RATED:
                Call<MovieList> topRatedMovies = movieListsApi.getPopularMovies(apiKey);
                setEnqueue(topRatedMovies, TOP_RATED, adapter);
                break;
            case MOST_POPULAR:
                Call<MovieList> mostPopular = movieListsApi.getTopRated(apiKey);
                setEnqueue(mostPopular, MOST_POPULAR, adapter);
                break;
        }

    }

    private void setEnqueue(Call<MovieList> enqueueList, final MovieListType listType, final MovieAdapter adapter) {
        enqueueList.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        adapter.addAll(response.body().results);
                    }
//                    switch (listType) {
//                        case MOST_POPULAR:
//                            mostPop = response.body();
//                            if (mostPop != null) {
//
//                            }
//                            break;
//                        case TOP_RATED:
//                            topRated = response.body();
//                            if (topRated != null) {
//                                movieListBuilder.topR.addAll(topRated.results);
//                            }
//                            break;
//                        case LATEST:
//                            latest = response.body();
//                            if (latest != null) {
//                                lat.addAll(latest.results);
//                                latestAdapter.notifyDataSetChanged();
//                                Log.e("number", String.valueOf(lat.size()));
//                            }
//                            break;
//                    }
                } else Log.e("NOt", "" + response.code());
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Log.i("failes", t.getMessage());
            }
        });
    }

    //    public void setAdapters(MovieAdapter recent,MovieAdapter topRated, MovieAdapter mostPopular ) {
    public void setAdapters(RecyclerView.Adapter recent) {
        latestAdapter = recent;

    }
}
