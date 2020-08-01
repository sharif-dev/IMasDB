package com.example.imasdb.view;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.imasdb.R;
import com.example.imasdb.model.Movie;
import com.example.imasdb.model.MovieListType;
import com.example.imasdb.network.MovieListBuilder;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView recent;
    RecyclerView mostPopular;
    RecyclerView topRated;
    Resources res;

    public ListsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListsFragment.
     */
    public static ListsFragment newInstance(String param1, String param2) {
        ListsFragment fragment = new ListsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View listFragment = inflater.inflate(R.layout.fragment_lists, container, false);
        recent = listFragment.findViewById(R.id.recently_played_recycler);
        mostPopular = listFragment.findViewById(R.id.most_popular_recycler);
        topRated = listFragment.findViewById(R.id.top_rated_recycler);
        res = getResources();
        ArrayList<RecyclerView> recyclerViews = setAdapters();
        MovieListBuilder movieListBuilder = new MovieListBuilder(res.getString(R.string.api_key));
        prepareLists(movieListBuilder, recyclerViews);

        return listFragment;
    }

    public void prepareLists(MovieListBuilder movieListBuilder, ArrayList<RecyclerView> recyclerViews) {
        for (int i = 0; i < MovieListType.values().length; i++) {
            RecyclerView recyclerView = recyclerViews.get(i);
            MovieListType movieListType = MovieListType.values()[i];
            movieListBuilder.getMovieList(movieListType, movieListType.adapter);
            recyclerView.setAdapter(movieListType.adapter);
            movieListType.adapter.setOnClickListener(new MovieAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Movie movie) {
                    Log.e("click", "clicked");
                    FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                    Fragment fragment = MovieFragment.newInstance(movie);
                    ft.replace(R.id.lists_fragment, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            SnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setItemAnimator(new SlideInLeftAnimator());
            Log.i("listDownloadStarted", "downloading");
        }
    };

    public ArrayList<RecyclerView> setAdapters() {
        ArrayList<RecyclerView> res = new ArrayList<>();
        res.add(recent);
        res.add(mostPopular);
        res.add(topRated);
        return res;

    }
}