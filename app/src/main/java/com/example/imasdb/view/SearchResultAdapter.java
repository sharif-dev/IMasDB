package com.example.imasdb.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.imasdb.R;
import com.example.imasdb.model.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchResultAdapter extends ArrayAdapter<Movie> {
    private String imageBaseUrl = "https://image.tmdb.org/t/p/w92";

    private SearchResultAdapter.OnItemClickListener listener;

    public void setOnClickListener(SearchResultAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    private static class ViewHolder {
        public ImageView ivCover;
        public TextView tvTitle;
        public TextView tvReleaseDate;
    }

    public SearchResultAdapter(@NonNull Context context, ArrayList<Movie> arrMovie) {
        super(context, 0, arrMovie);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Movie movie = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.search_res_item, parent, false);
            viewHolder.ivCover = (ImageView) convertView.findViewById(R.id.ivMovieCover);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvReleaseDate = (TextView) convertView.findViewById(R.id.tvReleaseDate);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTitle.setText(movie.getTitle());
        viewHolder.tvReleaseDate.setText(movie.getReleaseDate());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(movie);
            }
        });
        Picasso.get().load(imageBaseUrl+movie.getPosterPath()).placeholder(R.drawable.loading_placeholder).error(R.drawable.ic_baseline_image_24).into(viewHolder.ivCover);
        return convertView;
        //TODO:add no image default cover
        //TODO:add onCLickListeners
    }
}
