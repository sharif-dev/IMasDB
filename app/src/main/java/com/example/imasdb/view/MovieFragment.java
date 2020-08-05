package com.example.imasdb.view;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imasdb.R;
import com.example.imasdb.model.Movie;
import com.example.imasdb.model.RequestBodies.FavouriteMovie;
import com.example.imasdb.model.RequestBodies.Rate;
import com.example.imasdb.model.RequestBodies.WatchlistMovie;
import com.example.imasdb.model.User;
import com.example.imasdb.model.movie_detailes.cast.Cast;
import com.example.imasdb.model.movie_detailes.cast.CastsList;
import com.example.imasdb.model.movie_detailes.review.Review;
import com.example.imasdb.model.movie_detailes.review.Reviews;
import com.example.imasdb.network.AccountApiEndpointInterface;
import com.example.imasdb.network.MovieDetailsApi;
import com.example.imasdb.network.RetrofitBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFragment extends Fragment {

    private Movie movie;
    public Resources res;
    private String imageBaseUrl = "https://image.tmdb.org/t/p/w342";

    public MovieFragment() {

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
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        setupRatingBar(ratingBar);

        ImageView imageView = view.findViewById(R.id.movieImage);
        Picasso.get().load(imageBaseUrl + movie.getPosterPath()).placeholder(R.drawable.loading_placeholder).error(R.drawable.ic_baseline_image_24).into(imageView);
        casts = view.findViewById(R.id.actors);
        reviews = view.findViewById(R.id.reviewText);
        setMovieCastsAndReviews();
        Log.e("hello", "s");
        ImageButton favImgBtn = view.findViewById(R.id.img_btn_fav);
        ImageButton watchListImgBtn = view.findViewById(R.id.img_btn_watch_list);
        setUpFavBtn(favImgBtn);
        setupWatchListBtn(watchListImgBtn);
        return view;
    }
    //todo: clean this section
    private void setupWatchListBtn(final ImageButton watchListImgBtn) {
        watchListImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountApiEndpointInterface api = RetrofitBuilder.getAccountApi();
                Call<Object> addToFav = api.addToWatchList(User.getUser().getAccount().getId(), res.getString(R.string.api_key), User.getUser().getSessionToken().getSessionId(), new WatchlistMovie("movie", movie.getId(), true));
                addToFav.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (!response.isSuccessful()) {
                            Log.e("failed rating", response.message() + response.code() + response.body());
                            Toast.makeText(getContext(), "failed! try again later", Toast.LENGTH_LONG).show();
                        }
                        watchListImgBtn.setImageDrawable(res.getDrawable(R.drawable.ic_baseline_favorite_24));
                        Toast.makeText(getContext(), "added to watch list!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Log.e("failed rating", Objects.requireNonNull(t.getMessage()));
                        Toast.makeText(getContext(), "failed! try again later", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void setUpFavBtn(final ImageButton favImgBtn) {
        favImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountApiEndpointInterface api = RetrofitBuilder.getAccountApi();
                Call<Object> addToFav = api.addToFav(User.getUser().getAccount().getId(), res.getString(R.string.api_key), User.getUser().getSessionToken().getSessionId(), new FavouriteMovie("movie", movie.getId(), true));
                addToFav.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (!response.isSuccessful()) {
                            Log.e("failed rating", response.message() + response.code() + response.body());
                            Toast.makeText(getContext(), "failed! try again later", Toast.LENGTH_LONG).show();
                        }
                        favImgBtn.setImageDrawable(res.getDrawable(R.drawable.ic_baseline_favorite_24));
                        Toast.makeText(getContext(), "added to favourites!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Log.e("failed rating", Objects.requireNonNull(t.getMessage()));
                        Toast.makeText(getContext(), "failed! try again later", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void setupRatingBar(RatingBar ratingBar) {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                MovieDetailsApi api = RetrofitBuilder.getMovieDetailApi();
                Call<Object> rate = api.rateMovie(movie.getId(), res.getString(R.string.api_key), User.getUser().getSessionToken().getSessionId(), new Rate((int) v * 2));
                rate.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (!response.isSuccessful()) {
                            Log.e("failed rating", response.message() + response.code() + response.body());
                            Toast.makeText(getContext(), "rating failed! try again later", Toast.LENGTH_LONG).show();
                        }
                        Toast.makeText(getContext(), "rated", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Log.e("failed rating", Objects.requireNonNull(t.getMessage()));
                        Toast.makeText(getContext(), "rating failed! try again later", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
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
                        setCasts(castsList.getCast().
                                subList(0, castsList.getCast().size() < 8 ? castsList.getCast().size() : 7));
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