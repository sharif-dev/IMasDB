package com.example.imasdb.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imasdb.network.DownloadImageTask;
import com.example.imasdb.R;
import com.example.imasdb.model.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private String imageBaseUrl = "https://image.tmdb.org/t/p/w92";

    public void addAll(List<Movie> results) {
        mMovies.addAll(results);
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public ImageView movieImage;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.movieNameText);
            movieImage = (ImageView) itemView.findViewById(R.id.movieImage);
        }
    }

    private List<Movie> mMovies;

    public MovieAdapter(List<Movie> movies) {
        mMovies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.movie_item, parent, false);
        Log.e("salan", "salam");
        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        textView.setText(movie.getTitle());
        ImageView imageView = holder.movieImage;
        DownloadImageTask downloadImageTask = new DownloadImageTask(imageView);
        downloadImageTask.execute(imageBaseUrl + movie.getPosterPath());
//        imageView.setImageBitmap(movie.getImage());
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

}