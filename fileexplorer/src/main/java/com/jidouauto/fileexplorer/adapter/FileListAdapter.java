package com.jidouauto.fileexplorer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jidouauto.fileexplorer.R;
import com.jidouauto.fileexplorer.entity.FileInfo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileViewHolder> {
    private final LayoutInflater mInflater;
    private List<FileInfo> mFiles;
    private OnItemClickListener mListener;

    public FileListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new FileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        if(mFiles != null){
            FileInfo current = mFiles.get(position);
            holder.fileItemView.setText(current.fileName);
        }else{
            holder.fileItemView.setText("No File");
        }
    }

    @Override
    public int getItemCount() {
        if(mFiles != null){
            return mFiles.size();
        }else{
            return 0;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mListener = onItemClickListener;
    }

    public void setFiles(List<FileInfo> files){
        mFiles = files;
        notifyDataSetChanged();
    }

    public List<FileInfo> getFiles(){
        return mFiles;
    }

    class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private final TextView fileItemView;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            fileItemView = itemView.findViewById(R.id.textView);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onItemClick(v, getAdapterPosition());
            }
        }


        @Override
        public boolean onLongClick(View v) {
            if(mListener != null){
                mListener.onItemLongClick(v, getAdapterPosition());
            }
            return true;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int adapterPosition);
        void onItemLongClick(View v, int adapterPosition);
    }

}
