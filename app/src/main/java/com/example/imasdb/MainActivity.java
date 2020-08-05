package com.example.imasdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.imasdb.model.CustomListType;
import com.example.imasdb.model.Movie;
import com.example.imasdb.model.User;
import com.example.imasdb.view.CreateListFragment;
import com.example.imasdb.view.CustomeListFragment;
import com.example.imasdb.view.OnMovieClickListener;
import com.example.imasdb.view.TrendListsFragment;
import com.example.imasdb.view.LoginActivity;
import com.example.imasdb.view.MovieFragment;
import com.example.imasdb.view.TrendListAdapter;
import com.google.android.material.navigation.NavigationView;

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
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private OnMovieClickListener onMovieClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getResources();
        setContentView(R.layout.activity_main);
        onMovieClickListener = new OnMovieClickListener() {
            @Override
            public void onMovieClick(Movie movie) {
                Fragment fragment = MovieFragment.newInstance(movie);
                transitFrag(fragment, true);
            }
        };
        setupDrawerMenu();
        context = this;
        handleIntent(getIntent());
    }

    private void setupDrawerMenu() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
        mDrawer.addDrawerListener(drawerToggle);
        navigationView = (NavigationView) findViewById(R.id.nvView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    private void handleIntent(Intent intent) {
        if (!User.getUser().getLoggedIn()) {
            launchComposeView(LoginLaunchType.LOGIN);
        }
        Fragment fragment;
        if (intent.hasExtra("searchRes")) {
            Movie movie = (Movie) getIntent().getExtras().getSerializable("searchRes");
            fragment = MovieFragment.newInstance(movie);
        } else {
            fragment = TrendListsFragment.newInstance(onMovieClickListener);
        }
        transitFrag(fragment, false);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_view).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void transitFrag(Fragment fragment, Boolean addToStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (addToStack) {
            ft.replace(R.id.fl_main_fragment, fragment).addToBackStack(null).commit();
        } else {
            ft.replace(R.id.fl_main_fragment, fragment).commit();
        }
    }

    private void selectDrawerItem(MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_login:
                launchComposeView(LoginLaunchType.LOGIN);
                return;
            case R.id.nav_trends:
                fragment = TrendListsFragment.newInstance(onMovieClickListener);
                break;
            case R.id.nav_favourite:
                fragment = CustomeListFragment.newInstance(onMovieClickListener, CustomListType.FAVOURITE);
                break;
            case R.id.nav_watchlist:
                fragment = CustomeListFragment.newInstance(onMovieClickListener, CustomListType.WATCH_LIST);
                break;
            case R.id.nav_create_list:
                fragment = CreateListFragment.newInstance("", "");
                break;
            default:
                fragment = TrendListsFragment.newInstance(onMovieClickListener);
        }
        transitFrag(fragment, false);
        setTitle(item.getTitle());
        mDrawer.closeDrawers();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}