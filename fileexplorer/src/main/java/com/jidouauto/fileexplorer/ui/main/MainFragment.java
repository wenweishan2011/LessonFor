package com.jidouauto.fileexplorer.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jidouauto.fileexplorer.R;
import com.jidouauto.fileexplorer.adapter.FileListAdapter;
import com.jidouauto.fileexplorer.adapter.FilePathAdapter;
import com.jidouauto.fileexplorer.entity.FileInfo;
import com.jidouauto.fileexplorer.util.FileUtil;

import java.io.File;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.jidouauto.fileexplorer.MainActivity.VOLUME_PATH;

public class MainFragment extends Fragment {

    private FileViewModel mViewModel;
    private RecyclerView mFileView;
    private RecyclerView mHeadDirNav;
    private String mVolumePath;
    private MutableLiveData<List<FileInfo>> mChildFiles;
    private MutableLiveData<List<String>> mDirNavData;


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        mFileView = view.findViewById(R.id.recyclerview);
        mHeadDirNav = view.findViewById(R.id.head_dir_nav);
        mVolumePath = getArguments().getString(VOLUME_PATH);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FileViewModel.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mHeadDirNav.setLayoutManager(layoutManager);
        mHeadDirNav.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        final FilePathAdapter headAdapter = new FilePathAdapter(getContext());
        mHeadDirNav.setAdapter(headAdapter);
        mDirNavData = mViewModel.getDirNavData(mVolumePath);
        mDirNavData.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                headAdapter.setDatas(strings);
            }
        });
        headAdapter.setItemClickListener(new FilePathAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int adapterPosition) {
                final List<String> datas = headAdapter.getDatas();
                if (datas != null) {
                    String dirpath = FileUtil.getVolumePath(mVolumePath, Objects.requireNonNull(mViewModel.getVolumes().getValue()));
                    for (int i = 0; i <= adapterPosition; i++) {
                        dirpath += File.separator;
                        dirpath += datas.get(i);
                    }
                    replaceFragment(dirpath);
                }
            }
        });

        final FileListAdapter fileAdapter = new FileListAdapter(this.getContext());
        mChildFiles = mViewModel.getChildFiles(mVolumePath);
        mChildFiles.observe(this, new Observer<List<FileInfo>>() {
            @Override
            public void onChanged(List<FileInfo> fileInfos) {
                fileAdapter.setFiles(fileInfos);
            }
        });
        mFileView.setAdapter(fileAdapter);
        mFileView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        fileAdapter.setOnItemClickListener(new FileListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int adapterPosition) {
                final FileInfo fileInfo = fileAdapter.getFiles().get(adapterPosition);
                if (fileInfo.isDir) {
                    replaceFragment(fileInfo.path);
                }
            }
        });
    }

    private void replaceFragment(String path) {
        final MainFragment fragment = MainFragment.newInstance();
        Bundle args = new Bundle();
        args.putString(VOLUME_PATH, path);
        fragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
