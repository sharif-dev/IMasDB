package com.example.imasdb.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.imasdb.R;
import com.example.imasdb.model.CustomListType;
import com.example.imasdb.model.Movie;
import com.example.imasdb.model.MovieList;
import com.example.imasdb.model.User;
import com.example.imasdb.network.AccountApiEndpointInterface;
import com.example.imasdb.network.RetrofitBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CustomeListFragment extends Fragment {
    private OnMovieClickListener onMovieClickListener;

    private static final String ARG_PARAM1 = "type";
//    private static final String ARG_PARAM2 = "param2";
//
    private CustomListType customListType;
//    private String mParam2;
    private CustomeListAdapter adapter;
    public CustomeListFragment() {
        // Required empty public constructor
    }

    public static CustomeListFragment newInstance(OnMovieClickListener listener, CustomListType customListType) {
        CustomeListFragment fragment = new CustomeListFragment();
        Bundle args = new Bundle();
        fragment.onMovieClickListener = listener;
        args.putSerializable(ARG_PARAM1, customListType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CustomeListAdapter(new ArrayList<Movie>());
        if (getArguments() != null) {
            customListType = (CustomListType) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_custome_list, container, false);
        RecyclerView recyclerView = fragment.findViewById(R.id.my_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        getMovies();
        return fragment;
    }

    private void getMovies() {
        AccountApiEndpointInterface accountApiEndpointInterface = RetrofitBuilder.getAccountApi();
        String apiKey = getResources().getString(R.string.api_key);
        Call<MovieList> getMovies;
        switch (customListType){
            case FAVOURITE:
                getMovies = accountApiEndpointInterface.getFavouriteMovies(apiKey, User.getUser().getAccount().getId(),User.getUser().getSessionToken().getSessionId());
                break;
            case WATCH_LIST:
                getMovies = accountApiEndpointInterface.getWatchListMovies(apiKey, User.getUser().getAccount().getId(),User.getUser().getSessionToken().getSessionId());
                break;
            default:
                getMovies = accountApiEndpointInterface.getFavouriteMovies(apiKey, User.getUser().getAccount().getId(),User.getUser().getSessionToken().getSessionId());
        }
        getMovies.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if(response.isSuccessful()){
                    List<Movie> movies = response.body().results;
                    adapter.addAll(movies);
                }
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {

            }
        });
    }
}