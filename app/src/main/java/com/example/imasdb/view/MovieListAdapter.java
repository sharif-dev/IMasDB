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

import com.example.imasdb.R;
import com.example.imasdb.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private String imageBaseUrl = "https://image.tmdb.org/t/p/w342";

    private List<Movie> mMovies;

    public MovieListAdapter(List<Movie> movies) {
        mMovies = movies;
    }


    private OnItemClickListener listener;

    public void setOnClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("click", "MovieAdapter");
                    int position = getAdapterPosition(); // gets item position
                    if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                        listener.onItemClick(mMovies.get(position));
                    }
                }

            });
        }


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_movie, parent, false);
        Log.e("salan", "salam");
        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        TextView textView = holder.nameTextView;
        textView.setText(movie.getTitle());
        final ImageView imageView = holder.movieImage;
        Picasso.get().load(imageBaseUrl+movie.getPosterPath()).placeholder(R.drawable.loading_placeholder).error(R.drawable.ic_baseline_image_24).into(imageView);

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }
    public void addAll(List<Movie> results) {
        mMovies.addAll(results);
        this.notifyDataSetChanged();
    }


}
