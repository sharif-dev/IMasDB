package com.example.imasdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;

import com.example.imasdb.model.Movie;
import com.example.imasdb.view.ListsFragment;
import com.example.imasdb.view.LoginActivity;
import com.example.imasdb.view.MovieFragment;
import com.example.imasdb.view.MovieListAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_ACTIVITY";

    public enum LoginLaunchType {
        LOGIN, LOGOUT
    }

    private Resources res;
    List<Movie> m = new ArrayList<Movie>();

    private Activity context;

    //        RecyclerView recent;
//    RecyclerView recent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getResources();
        setContentView(R.layout.activity_main);
        context = this;
        Log.i(TAG, "onCreate: ");
        handleIntent(getIntent());
    }
    private void handleIntent(Intent intent){
        //        if (!intent.hasExtra("loginCompleted")) {
//            launchComposeView(LoginLaunchType.LOGIN);
//        }
        if(intent.hasExtra("searchRes")){
            Movie movie = (Movie) getIntent().getExtras().getSerializable("searchRes");
            Log.i(TAG, "handleIntent: hasExtra"+movie);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = MovieFragment.newInstance(movie);
            ft.replace(R.id.fl_main_fragment, fragment);
            ft.commit();
        }
        else{
            Log.i(TAG, "handleIntent: donthaveExtra");
            ListsFragment listsFragment = new ListsFragment();
            listsFragment.setOnClickListener(new MovieListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Movie movie) {
                    Fragment fragment = MovieFragment.newInstance(movie);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fl_main_fragment, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fl_main_fragment, listsFragment);
            ft.commit();
        }
    }

    private void logout() {
        launchComposeView(LoginLaunchType.LOGOUT);
    }

    public void launchComposeView(LoginLaunchType loginLaunchType) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra("loginType", loginLaunchType.ordinal());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().popBackStack();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        Log.i(TAG, "onCreateOptionsMenu: ");
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search_view).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

}