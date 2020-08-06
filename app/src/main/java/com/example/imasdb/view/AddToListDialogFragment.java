package com.example.imasdb.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imasdb.R;
import com.example.imasdb.model.User;
import com.example.imasdb.model.list_models.List;
import com.example.imasdb.model.list_models.ListResult;
import com.example.imasdb.network.RetrofitBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddToListDialogFragment extends DialogFragment {
    private RecyclerView recyclerView;

    public AddToListDialogFragment() {
    }

    public static AddToListDialogFragment newInstance(String title) {
        AddToListDialogFragment frag = new AddToListDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_add_to_lists, container);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view);
        AddToListAdapter addToListAdapter = new AddToListAdapter(new ArrayList<ListResult>());
        OnListItemClickedListener onListItemClickedListener = new OnListItemClickedListener() {
            @Override
            public void onListClick(ListResult listResult) {
                Toast.makeText(getContext(),"Added",Toast.LENGTH_LONG).show();
//                addToList(listResult);
            }
        };
        addToListAdapter.setOnListItemClickedListener(onListItemClickedListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(addToListAdapter);
        String title = getArguments().getString("title", "Choose List");
        getDialog().setTitle(title);
        getLists();
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
                    ((AddToListAdapter) recyclerView.getAdapter()).addAll(response.body());
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
