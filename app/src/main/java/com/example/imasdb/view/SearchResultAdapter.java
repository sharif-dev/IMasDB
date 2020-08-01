package com.example.imasdb.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.imasdb.R;
import com.example.imasdb.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchResultAdapter extends ArrayAdapter<Movie> {
    private String imageBaseUrl = "https://image.tmdb.org/t/p/w92";

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
        ViewHolder viewHolder;
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
        Picasso.with(getContext()).load(imageBaseUrl+movie.getPosterPath()).into(viewHolder.ivCover);
        return convertView;
        //TODO:add no image default cover
        //TODO:add onCLickListeners
    }
}
