package com.example.imasdb.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imasdb.R;
import com.example.imasdb.model.Movie;
import com.example.imasdb.model.User;
import com.example.imasdb.model.list_models.ListResult;
import com.example.imasdb.network.RetrofitBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.imasdb.model.User.getApiKey;

public class CreateListAdapter extends RecyclerView.Adapter<CreateListAdapter.ViewHolder> {
    private List<ListResult> lists;
    private OnListItemClickedListener onListItemClickedListener;


    public CreateListAdapter(List<ListResult> lists) {

        this.lists = lists;

    }

    public void setOnListItemClickedListener(OnListItemClickedListener onListItemClickedListener) {
        this.onListItemClickedListener = onListItemClickedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.list_presenter, parent, false);
        return new CreateListAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListResult listResult = lists.get(position);
        TextView nameView = holder.listName;
        nameView.setText(listResult.getName());
        TextView descView = holder.listDesc;
        descView.setText(listResult.getDescription());
        Log.e("helloooo", "   dssd");
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView listName;
        public TextView listDesc;
        private Button del;

        public ViewHolder(View itemView) {
            super(itemView);
            listName = (TextView) itemView.findViewById(R.id.listNameSet);
            listDesc = (TextView) itemView.findViewById(R.id.listDesc);
            del = (Button) itemView.findViewById(R.id.delete_list);
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    Call<Object> del = RetrofitBuilder.getCreateListApi().deleteList(lists.get(position).getId().toString(), getApiKey(), User.getUser().getSessionToken().getSessionId());
                    del.enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            if (response.code() == 200) {
                                lists.remove(position);
                                notifyItemRemoved(position);
                            }
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {

                        }
                    });
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("click", "MovieAdapter");
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onListItemClickedListener.onListClick(lists.get(position));
                    }
                }

            });
        }


    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void addAll(com.example.imasdb.model.list_models.List results) {
        lists.clear();
        lists.addAll(results.getListResults());
        Log.e("size", results.getListResults().size() + "");
        this.notifyDataSetChanged();
    }


}
