package com.example.imasdb.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.imasdb.R;
import com.example.imasdb.model.RequestBodies.CreateListBody;
import com.example.imasdb.model.User;
import com.example.imasdb.model.list_models.List;
import com.example.imasdb.model.list_models.ListResult;
import com.example.imasdb.network.RetrofitBuilder;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.imasdb.model.User.getApiKey;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateListFragment extends Fragment {


    RecyclerView recyclerView;

    private OnListItemClickedListener onListItemClickedListener;

    public CreateListFragment() {
    }

    public static CreateListFragment newInstance(OnListItemClickedListener onListItemClickedListener) {
        CreateListFragment fragment = new CreateListFragment();
        Bundle args = new Bundle();
        fragment.onListItemClickedListener = onListItemClickedListener;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_list, container, false);
        recyclerView = view.findViewById(R.id.createListRecycler);
        Button button = view.findViewById(R.id.createListButton);
        final EditText editText = view.findViewById(R.id.listName);
        final EditText desc = view.findViewById(R.id.description);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewList(editText.getText().toString(), desc.getText().toString());
            }
        });
        CreateListAdapter createListAdapter = new CreateListAdapter(new ArrayList<ListResult>());
        createListAdapter.setOnListItemClickedListener(onListItemClickedListener);
        createListAdapter.setOnListItemDeleted(new CreateListAdapter.OnListItemDeleted() {
            @Override
            public void onListDeleted(String listId) {
                Call<Object> del = RetrofitBuilder.getCreateListApi().deleteList(listId, getApiKey(), User.getUser().getSessionToken().getSessionId());
                del.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.i("DELLEEET", "onResponse: "+response.code());
                        if(response.code() == 500 || response.code() == 200 || response.code() == 201){
                            getLists();
                        }
                    }
                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(createListAdapter);
        getLists();
        return view;
    }

    private void createNewList(String name, String desc) {
        Log.e("ses", User.getInstance().getSessionToken().getSessionId());
        Call<Object> create = RetrofitBuilder.getCreateListApi().createList(getResources().getString(R.string.api_key), User.getUser().getSessionToken().getSessionId(), new CreateListBody(name, desc, "en"));
        create.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("create", response.code() + " " + response.message() + " " + response.errorBody());
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(),"list created",Toast.LENGTH_SHORT).show();
                    getLists();
                } else {
                    Toast.makeText(getContext(), "try again! internet not connected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getContext(), "try again! internet not connected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getLists() {
        Call<List> myList = RetrofitBuilder.getCreateListApi()
                .getLists(User.getUser().getAccount().getId(), getResources().getString(R.string.api_key), User.getUser().getSessionToken().getSessionId(), 1);
        myList.enqueue(new Callback<List>() {
            @Override
            public void onResponse(Call<List> call, Response<List> response) {
                Log.e("list error", response.code() + "");

                if (response.isSuccessful()) {
                    Log.e("list comp", response.code() + "");
                    ((CreateListAdapter) recyclerView.getAdapter()).clear();
                    ((CreateListAdapter) recyclerView.getAdapter()).addAll(response.body());
                } else {
                    Log.e("list error", response.code() + "");

                }
            }

            @Override
            public void onFailure(Call<List> call, Throwable t) {
                Log.e("list error", t.getMessage());
                Toast.makeText(getContext(), "could not fetch your list. try again later", Toast.LENGTH_LONG);
            }
        });
    }
}