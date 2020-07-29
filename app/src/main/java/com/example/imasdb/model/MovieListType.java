package com.example.imasdb.model;

import android.widget.Adapter;

import com.example.imasdb.view.MovieAdapter;

import java.util.ArrayList;

public enum MovieListType {
    LATEST(0, new MovieAdapter(new ArrayList<Movie>())), TOP_RATED(1, new MovieAdapter(new ArrayList<Movie>())), MOST_POPULAR(2, new MovieAdapter(new ArrayList<Movie>()));
    public int index;
    public MovieAdapter adapter;

    MovieListType(int i, MovieAdapter adapter) {
        this.adapter = adapter;
        this.index = i;
    }
}
