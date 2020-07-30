package com.example.imasdb.view;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.imasdb.R;
import com.example.imasdb.model.Movie;
import com.example.imasdb.model.movie_detailes.cast.Cast;
import com.example.imasdb.model.movie_detailes.cast.CastsList;
import com.example.imasdb.model.movie_detailes.review.Review;
import com.example.imasdb.model.movie_detailes.review.Reviews;
import com.example.imasdb.network.DownloadImageTask;
import com.example.imasdb.network.MovieDetailsApi;
import com.example.imasdb.network.RetrofitBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFragment extends Fragment {

    private Movie movie;
    public Resources res;
    private String imageBaseUrl = "https://image.tmdb.org/t/p/w342";

    public MovieFragment() {
        // Required empty public constructor
    }

    public static MovieFragment newInstance(Movie movie) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putSerializable("movie", movie);
        fragment.setArguments(args);
        return fragment;
    }

    TextView casts;
    TextView reviews;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getParentFragment().getChildFragmentManager().popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        if (getArguments() != null) {
            movie = (Movie) getArguments().getSerializable("movie");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        res = getResources();
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        TextView pop = view.findViewById(R.id.popularityText);
        pop.setText(movie.getPopularity().intValue() + "");
        TextView year = view.findViewById(R.id.production_year);
        year.setText("( " + movie.getReleaseDate() + " )");
        TextView name = view.findViewById(R.id.movieNameText);
        name.setText(movie.getTitle());
        TextView summary = view.findViewById(R.id.summaryText);
        summary.setText(movie.getOverview());
        TextView ratings = view.findViewById(R.id.ratingNumber);
        ratings.setText(String.valueOf(movie.getVoteAverage()));
        ImageView imageView = view.findViewById(R.id.movieImage);
        DownloadImageTask downloadImageTask = new DownloadImageTask(imageView);
        downloadImageTask.execute(imageBaseUrl + movie.getPosterPath());
        casts = view.findViewById(R.id.actors);
        reviews = view.findViewById(R.id.reviewText);
        setMovieCastsAndReviews();
//        getFragmentManager().popBackStack();
        return view;

    }

    private void setMovieCastsAndReviews() {
        MovieDetailsApi movieDetailsApi = RetrofitBuilder.getMovieDetailApi();
        Call<CastsList> castsListCall = movieDetailsApi.getCastsList(movie.getId(), res.getString(R.string.api_key));
        Call<Reviews> reviewsCall = movieDetailsApi.getReviews(movie.getId(), res.getString(R.string.api_key));
        castsListCall.enqueue(new Callback<CastsList>() {
            @Override
            public void onResponse(Call<CastsList> call, Response<CastsList> response) {
                if (response.isSuccessful()) {
                    CastsList castsList = response.body();
                    if (castsList != null) {
                        setCasts(castsList.getCast().subList(0, 7));
                        Log.e("casts, get", String.valueOf(response.message()));
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<CastsList> call, Throwable t) {

            }
        });
        reviewsCall.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                Reviews reviews = response.body();
                if (reviews != null) {
                    setReviews(reviews.getReviews());
                }
            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {

            }
        });
    }

    private void setCasts(List<Cast> casts) {
        StringBuilder builder = new StringBuilder();
        for (Cast cast : casts) {
            builder = builder.append(cast.getName()).append(" as : ").append(cast.getCharacter()).append("\n\n");
        }
        this.casts.setText(builder.toString());
        Log.e("setCasts", builder.toString());

    }

    private void setReviews(List<Review> reviews) {
        StringBuilder builder = new StringBuilder();
        for (Review review : reviews) {
            builder = builder.append(review.getAuthor()).append(" : ").append(review.getContent()).append("\n\n");
        }
        this.reviews.setText(builder.toString());
    }

}