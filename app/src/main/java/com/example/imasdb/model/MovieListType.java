package com.example.imasdb.model;

import com.example.imasdb.view.MovieListAdapter;

import java.util.ArrayList;

public enum MovieListType {
    LATEST(0, new MovieListAdapter(new ArrayList<Movie>())), TOP_RATED(1, new MovieListAdapter(new ArrayList<Movie>())), MOST_POPULAR(2, new MovieListAdapter(new ArrayList<Movie>()));
    public int index;
    public MovieListAdapter adapter;

    MovieListType(int i, MovieListAdapter adapter) {
        this.adapter = adapter;
        this.index = i;

    }
}
