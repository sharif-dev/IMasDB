package com.example.imasdb.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters

    private String mParam1;
    private String mParam2;
    private CreateListAdapter createListAdapter;
    RecyclerView recyclerView;
    private OnListItemClickedListener onListItemClickedListener = new OnListItemClickedListener() {
        @Override
        public void onListClick(ListResult listResult) {

        }
    };

    public CreateListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateListFragment newInstance(String param1, String param2) {
        CreateListFragment fragment = new CreateListFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_list, container, false);
        recyclerView = view.findViewById(R.id.createListRecycler);
        Button button = view.findViewById(R.id.createListButton);
        createListAdapter = new CreateListAdapter(new ArrayList<ListResult>());
        getLists();
        final EditText editText = view.findViewById(R.id.listName);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewList(editText.getText().toString());
            }
        });

        recyclerView.setAdapter(new CreateListAdapter(new ArrayList<ListResult>()));
        return view;
    }

    private void createNewList(String name) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("name", name);
        Call<Object> create = RetrofitBuilder.getCreateListApi().createList(getResources().getString(R.string.api_key), User.getUser().getSessionToken().getSessionId(),hashMap);
        create.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("create", response.code() + " " + response.message() + " " + response.errorBody());
                if (response.isSuccessful()) {
                    getLists();
                } else {
                    Toast.makeText(getContext(), "try again! internet not connected", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("create", t.getMessage() + "");

            }
        });
    }

    public void getLists() {
        Call<List> myList = RetrofitBuilder.getCreateListApi()
                .getLists(User.getUser().getAccount().getId(), getResources().getString(R.string.api_key), User.getUser().getSessionToken().getSessionId(), 1);
        myList.enqueue(new Callback<List>() {
            @Override
            public void onResponse(Call<List> call, Response<List> response) {
                if (response.isSuccessful()) {
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