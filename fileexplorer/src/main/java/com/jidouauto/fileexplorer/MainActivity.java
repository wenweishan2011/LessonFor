package com.jidouauto.fileexplorer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.jidouauto.fileexplorer.adapter.VolumeListAdapter;
import com.jidouauto.fileexplorer.entity.VolumeInfo;
import com.jidouauto.fileexplorer.manager.MediaStoreManager;
import com.jidouauto.fileexplorer.ui.main.FileViewModel;
import com.jidouauto.fileexplorer.ui.main.MainFragment;
import com.xuexiang.rxutil2.rxjava.RxJavaUtils;
import com.xuexiang.rxutil2.rxjava.task.RxIOTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    public static final String VOLUME_PATH = "volume_path";

    private FileViewModel mFileViewModel;
    private RecyclerView mVolumesView;
    private Set<VolumeInfo> volumeList;
    private int mCurrentVolumeSeleted = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mFileViewModel = ViewModelProviders.of(this).get(FileViewModel.class);
        mVolumesView = findViewById(R.id.volumeList);

        final VolumeListAdapter adapter = new VolumeListAdapter(this);
        mVolumesView.setAdapter(adapter);
        mVolumesView.setLayoutManager(new LinearLayoutManager(this));

        final MutableLiveData<List<VolumeInfo>> volumes = mFileViewModel.getVolumes();
        volumes.observe(this, new Observer<List<VolumeInfo>>() {
            @Override
            public void onChanged(List<VolumeInfo> volumeInfos) {
                if(volumeInfos.size() < mCurrentVolumeSeleted){
                    mCurrentVolumeSeleted = 0;
                }
                final MainFragment fragment = MainFragment.newInstance();
                Bundle args = new Bundle();
                args.putString(VOLUME_PATH, volumeInfos.get(mCurrentVolumeSeleted).path);
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .commitNow();
                adapter.setVolumes(volumeInfos);
            }
        });
        adapter.setItemClickListener(new VolumeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int adapterPosition) {
                mCurrentVolumeSeleted = adapterPosition;
                final MainFragment fragment = MainFragment.newInstance();
                Bundle args = new Bundle();
                args.putString(VOLUME_PATH, volumes.getValue().get(adapterPosition).path);
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .commitNow();
            }
        });

        if(!requestActivityPremission()){
            return;
        }

        RxJavaUtils.doInIOThread(new RxIOTask<Object>("") {
            @Override
            public Void doInIOThread(Object o) {
                MediaStoreManager.getManager(getApplication()).queryImageInfo();
                return null;
            }
        });
    }

    /**
     * 请求权限
     *
     * @return
     */
    private boolean requestActivityPremission() {
        List<String> permissions = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), 1);
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        finish();
                    }
                }
            }
        }
    }
}
