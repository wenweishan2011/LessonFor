package com.wwsdemo.explorerdemo.weight;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wwsdemo.explorerdemo.R;
import com.wwsdemo.explorerdemo.ui.main.bean.DocumentInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.VIEW_HOLDER> {
    private final OnItemClickListener listener;
    private List<DocumentInfo> data = new ArrayList<>();

    public CustomAdapter(List<DocumentInfo> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VIEW_HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_view, null);
        return new VIEW_HOLDER(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull VIEW_HOLDER holder, int position) {
        holder.icon.setImageResource(R.mipmap.ic_launcher);
        holder.name.setText(data.get(position).display_name);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class VIEW_HOLDER extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnItemClickListener listener2;
        @BindView(R.id.name)
        public TextView name;
        @BindView(R.id.icon)
        public ImageView icon;
        @BindView(R.id.newFile)
        Button newFile;
        @BindView(R.id.newDir)
        Button newDir;
        @BindView(R.id.delete)
        Button delete;
        @BindView(R.id.rename)
        Button rename;

        public VIEW_HOLDER(@NonNull View itemView, OnItemClickListener onClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            listener2 = onClickListener;
            icon.setOnClickListener(this);
            newFile.setOnClickListener(this);
            newDir.setOnClickListener(this);
            delete.setOnClickListener(this);
            rename.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener2.onItemClick(v, getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}


