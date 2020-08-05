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


public class CustomeListFragment extends Fragment {
    private OnMovieClickListener onMovieClickListener;

    private static final String ARG_PARAM1 = "type";
//    private static final String ARG_PARAM2 = "param2";
//
    private CustomListType customListType;
//    private String mParam2;

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
        getMovies();
        return fragment;
    }

    private void getMovies() {
        switch (customListType){
            case FAVOURITE:
                break;
        }
    }
}