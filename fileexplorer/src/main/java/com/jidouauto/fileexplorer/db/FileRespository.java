package com.jidouauto.fileexplorer.db;

import android.app.Application;
import android.os.Environment;

import com.jidouauto.fileexplorer.ExplorerApplication;
import com.jidouauto.fileexplorer.MainActivity;
import com.jidouauto.fileexplorer.entity.FileInfo;
import com.jidouauto.fileexplorer.entity.StorageInfo;
import com.jidouauto.fileexplorer.entity.VolumeInfo;
import com.jidouauto.fileexplorer.manager.FileTaskManager;
import com.jidouauto.fileexplorer.manager.MediaStoreManager;
import com.jidouauto.fileexplorer.util.FileUtil;
import com.xuexiang.rxutil2.rxjava.RxJavaUtils;
import com.xuexiang.rxutil2.rxjava.task.RxIOTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import androidx.lifecycle.MutableLiveData;

public class FileRespository {
    private MutableLiveData<List<VolumeInfo>> mLiveDataVolumes;
    private MutableLiveData<List<FileInfo>> mChildFiles;
    private MutableLiveData<List<String>> mDirDatas;

    public FileRespository(Application application) {
        getVolumeList();
        mChildFiles = new MutableLiveData<>();
        mDirDatas = new MutableLiveData<>();
    }

    public MutableLiveData<List<FileInfo>> getChildFileInfos(String parentPath, int _type) {
        MediaStoreManager mediaManager = MediaStoreManager.getManager(ExplorerApplication.getInstance());
        switch (_type) {
            case MainActivity.TAB_ALL:
                final FileTaskManager manager = FileTaskManager.getManager(ExplorerApplication.getInstance());
                manager.listFiles(parentPath, mChildFiles);
                break;
            case MainActivity.TAB_PIC:
                final List<FileInfo> fileInfos = mediaManager.queryImageInfo(parentPath);
                mChildFiles.setValue(fileInfos);
                break;
            case MainActivity.TAB_VIDEO:
                final List<FileInfo> fileInfos2 = mediaManager.queryAudioInfo(parentPath);
                mChildFiles.setValue(fileInfos2);
                break;
            default:
        }

        return mChildFiles;
    }

    void getVolumeList() {
        if (mLiveDataVolumes == null) {
            mLiveDataVolumes = new MutableLiveData<>();
        }
        RxJavaUtils.doInIOThread(new RxIOTask<MutableLiveData<List<VolumeInfo>>>(mLiveDataVolumes) {
            @Override
            public Void doInIOThread(MutableLiveData<List<VolumeInfo>> listMutableLiveData) {
                final String[] customVolumePaths = StorageInfo.getCustomVolumePaths();
                HashSet<VolumeInfo> volumeList = new HashSet<>();
                VolumeInfo volumeInfo = null;
                for (String path : customVolumePaths) {
                    if (Environment.MEDIA_MOUNTED.equals(StorageInfo.getCustomVolumeState(path))) {
                        volumeInfo = new VolumeInfo();
                        volumeInfo.path = path;
                        volumeList.add(volumeInfo);
                    }
                }
                List<VolumeInfo> result = new ArrayList<>();
                result.addAll(volumeList);
                mLiveDataVolumes.postValue(result);
                return null;
            }
        });
    }

    public MutableLiveData<List<VolumeInfo>> getCurrentVolumes() {
        return mLiveDataVolumes;
    }

    public MutableLiveData<List<String>> getNavDirs(final String path) {
        RxJavaUtils.doInIOThread(new RxIOTask<String>(path) {
            @Override
            public Void doInIOThread(String s) {
                final String volumePath = Objects.requireNonNull(FileUtil.getVolumePath(s));
                List<String> paths = new ArrayList<>();
                if (!FileUtil.isRoot(s)) {
                    String replace = s.replace(volumePath, "");
                    if (replace.startsWith(File.separator)) {
                        replace = replace.substring(1);
                    }
                    paths = Arrays.asList(replace.split(File.separator));
                } else {
                    paths.add("/");
                }
                mDirDatas.postValue(paths);
                return null;
            }
        });
        return mDirDatas;
    }
}
