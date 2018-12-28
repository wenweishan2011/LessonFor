package com.jidouauto.fileexplorer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jidouauto.fileexplorer.R;
import com.jidouauto.fileexplorer.entity.VolumeInfo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VolumeListAdapter extends RecyclerView.Adapter<VolumeListAdapter.FileViewHolder>{
    private final LayoutInflater mInflater;
    private List<VolumeInfo> mVolumes;
    private OnItemClickListener mListener;

    public VolumeListAdapter(Context context) {
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
        if(mVolumes != null){
            VolumeInfo current = mVolumes.get(position);
            holder.fileItemView.setText(current.path);
        }else{
            holder.fileItemView.setText("No File");
        }
    }

    @Override
    public int getItemCount() {
        if(mVolumes != null){
            return mVolumes.size();
        }else{
            return 0;
        }
    }

    public void setVolumes(List<VolumeInfo> volumes){
        mVolumes = volumes;
        notifyDataSetChanged();
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
