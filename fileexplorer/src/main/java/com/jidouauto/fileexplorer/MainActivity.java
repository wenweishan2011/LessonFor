package com.jidouauto.fileexplorer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.jidouauto.fileexplorer.adapter.VolumeListAdapter;
import com.jidouauto.fileexplorer.entity.VolumeInfo;
import com.jidouauto.fileexplorer.ui.main.FileViewModel;
import com.jidouauto.fileexplorer.ui.main.MainFragment;

import java.util.ArrayList;
import java.util.List;

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
    public static final String TAB_SELECTED = "tab_selected";

    public static final int TAB_ALL = 0;
    public static final int TAB_PIC = 1;
    public static final int TAB_VIDEO = 2;

    private FileViewModel mFileViewModel;
    private RecyclerView mVolumesView;
    private int mCurrentVolumeSeleted = 0;
    private TabHost mTabHost;

    private MutableLiveData<List<VolumeInfo>> mVolumes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if(!requestActivityPremission()){
            return;
        }
        mFileViewModel = ViewModelProviders.of(this).get(FileViewModel.class);
        mVolumesView = findViewById(R.id.volumeList);
        mTabHost = findViewById(R.id.tabhost);
        mTabHost.setup();
        initTab();
        mTabHost.setCurrentTab(TAB_ALL);
        final VolumeListAdapter adapter = new VolumeListAdapter(this);
        mVolumesView.setAdapter(adapter);
        mVolumesView.setLayoutManager(new LinearLayoutManager(this));

        mVolumes = mFileViewModel.getVolumes();
        mVolumes.observe(this, new Observer<List<VolumeInfo>>() {
            @Override
            public void onChanged(List<VolumeInfo> volumeInfos) {
                if(volumeInfos.size() < mCurrentVolumeSeleted){
                    mCurrentVolumeSeleted = 0;
                }
                replaceFragment(volumeInfos.get(mCurrentVolumeSeleted).path);
                adapter.setVolumes(volumeInfos);
            }
        });
        adapter.setItemClickListener(new VolumeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int adapterPosition) {
                mCurrentVolumeSeleted = adapterPosition;
                replaceFragment(mVolumes.getValue().get(adapterPosition).path);
            }
        });
    }

    private void replaceFragment(String path) {
        final MainFragment fragment = MainFragment.newInstance();
        Bundle args = new Bundle();
        args.putString(VOLUME_PATH, path);
        args.putInt(TAB_SELECTED, mTabHost.getCurrentTab());
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow();
    }

    private void initTab() {
        // 创建第一个Tab页
        TabHost.TabSpec tab1 = mTabHost.newTabSpec("tab1")
                .setIndicator("全部") // 设置标题
                .setContent(R.id.tab01); //设置内容
        // 添加第一个标签页
        mTabHost.addTab(tab1);
        TabHost.TabSpec tab2 = mTabHost.newTabSpec("tab2")
                .setIndicator("图片")
                .setContent(R.id.tab02);
        // 添加第二个标签页
        mTabHost.addTab(tab2);
        TabHost.TabSpec tab3 = mTabHost.newTabSpec("tab3")
                .setIndicator("视频")
                .setContent(R.id.tab03);
        // 添加第三个标签页
        mTabHost.addTab(tab3);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Toast.makeText(MainActivity.this, tabId, Toast.LENGTH_SHORT).show();
                replaceFragment(mVolumes.getValue().get(mCurrentVolumeSeleted).path);
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
