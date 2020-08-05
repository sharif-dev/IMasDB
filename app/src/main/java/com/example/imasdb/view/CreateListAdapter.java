package com.example.imasdb.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imasdb.R;
import com.example.imasdb.model.Movie;
import com.example.imasdb.model.list_models.ListResult;
import com.squareup.picasso.Picasso;

import java.util.List;

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
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView listName;
        public TextView listDesc;

        @SuppressLint("CutPasteId")
        public ViewHolder(View itemView) {
            super(itemView);
            listName = (TextView) itemView.findViewById(R.id.listName);
            listDesc = (TextView) itemView.findViewById(R.id.listName);
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
        this.notifyDataSetChanged();
    }


}
