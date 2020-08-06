package com.example.imasdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.imasdb.model.CustomListType;
import com.example.imasdb.model.Movie;
import com.example.imasdb.model.User;
import com.example.imasdb.model.list_models.ListResult;
import com.example.imasdb.network.AuthApiEndpointInterface;
import com.example.imasdb.view.CreateListFragment;
import com.example.imasdb.view.CustomeListFragment;
import com.example.imasdb.view.OnListItemClickedListener;
import com.example.imasdb.view.OnMovieClickListener;
import com.example.imasdb.view.TrendListsFragment;
import com.example.imasdb.view.LoginActivity;
import com.example.imasdb.view.MovieFragment;
import com.example.imasdb.view.TrendListAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.imasdb.network.RetrofitBuilder.getAuthApi;


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
    private OnListItemClickedListener onListItemClickedListener;
    private MenuItem navLogin;
    private MenuItem navLogout;
    private MenuItem navList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getResources();
        setContentView(R.layout.activity_main);
        onListItemClickedListener = new OnListItemClickedListener() {
            @Override
            public void onListClick(ListResult listResult) {
                Fragment fragment = CustomeListFragment.newInstance(onMovieClickListener, CustomListType.CUSTOM, listResult);
                transitFrag(fragment, true);
            }
        };
        onMovieClickListener = new OnMovieClickListener() {
            @Override
            public void onMovieClick(Movie movie) {
                Fragment fragment = MovieFragment.newInstance(movie);
                transitFrag(fragment, true);
            }
        };
        setupDrawerMenu();
        if (!User.getUser().getLoggedIn()) {
            setSessionGuest();
        } else {
            navLogin.setVisible(false);
        }
        context = this;
        handleIntent(getIntent());
    }

    private void setSessionGuest() {
        navLogout.setVisible(false);
        navLogin.setVisible(true);
        navList.setVisible(false);
    }


    @Override
    protected void onResume() {
        if (!User.getUser().getLoggedIn()) {
            setSessionGuest();
        } else {
            navLogin.setVisible(false);
        }
        super.onResume();
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
        Menu menu = navigationView.getMenu();
        navLogin = menu.findItem(R.id.nav_login);
        navLogout = menu.findItem(R.id.nav_logout);
        navList = menu.findItem(R.id.nav_lists);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    private void handleIntent(Intent intent) {
        Fragment fragment;
        if (intent.hasExtra("searchRes")) {
            Movie movie = (Movie) getIntent().getExtras().getSerializable("searchRes");
            fragment = MovieFragment.newInstance(movie);
        } else {
            fragment = TrendListsFragment.newInstance(onMovieClickListener);
        }
        transitFrag(fragment, false);
    }


    public void logout() {
        AuthApiEndpointInterface myAuthApi = getAuthApi();
        Log.i("Session token", User.getInstance().getSessionToken().getSessionId());

        Call<Object> logout = myAuthApi.logout(res.getString(R.string.api_key), User.getInstance().getSessionToken().getSessionId());
        logout.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.code() == 200) {
                    User.getUser().setLoggedIn(false);
                    Toast.makeText(getApplicationContext(), "you logged out successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), Integer.toString(response.code()), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void launchComposeView(LoginLaunchType loginLaunchType) {
        Log.i(TAG, "launchComposeView: lunchhhhhhhhhhhhh");
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
                fragment = CreateListFragment.newInstance(onListItemClickedListener);
                break;
            case R.id.nav_logout:
                logout();
                setSessionGuest();
            default:
                fragment = TrendListsFragment.newInstance(onMovieClickListener);
        }
        transitFrag(fragment, true);
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

    @Override
    protected void onRestart() {
        if (!User.getUser().getLoggedIn()) {
            setSessionGuest();
        } else {
            navLogin.setVisible(false);
        }
        super.onRestart();
    }

    public void restart(){
        Intent intent = new Intent(MainActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}