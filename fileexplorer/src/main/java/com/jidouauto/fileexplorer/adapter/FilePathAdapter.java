package com.jidouauto.fileexplorer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jidouauto.fileexplorer.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FilePathAdapter extends RecyclerView.Adapter<FilePathAdapter.FileViewHolder>{
    private final LayoutInflater mInflater;
    private List<String> mPaths;
    private OnItemClickListener mListener;

    public FilePathAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_dirpath_item, parent, false);
        return new FileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        if(mPaths != null){
            String current = mPaths.get(position);
            holder.fileItemView.setText(current);
        }else{
            holder.fileItemView.setText("No File");
        }
    }

    @Override
    public int getItemCount() {
        if(mPaths != null){
            return mPaths.size();
        }else{
            return 0;
        }
    }

    public void setDatas(List<String> paths){
        mPaths = paths;
        notifyDataSetChanged();
    }

    public List<String> getDatas(){
        return mPaths;
    }

    public void setItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView fileItemView;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            fileItemView = itemView.findViewById(R.id.textView);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onItemClick(v,getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int adapterPosition);
    }

}
