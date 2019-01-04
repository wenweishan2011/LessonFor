package com.jidouauto.fileexplorer.ui.main;

import android.app.Application;

import com.jidouauto.fileexplorer.db.FileRespository;
import com.jidouauto.fileexplorer.entity.FileInfo;
import com.jidouauto.fileexplorer.entity.VolumeInfo;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class FileViewModel extends AndroidViewModel {

    private FileRespository mFileRespository;
    private MutableLiveData<List<FileInfo>> mChildFiles;
    private MutableLiveData<List<VolumeInfo>> mVolumes;
    private MutableLiveData<List<String>> mDirDatas;

    public FileViewModel(Application application) {
        super(application);
        mFileRespository = new FileRespository(application);
        mVolumes = mFileRespository.getCurrentVolumes();
    }

    public MutableLiveData<List<FileInfo>> getChildFiles(String parentPath, int mTabSelected){
        mChildFiles = mFileRespository.getChildFileInfos(parentPath, mTabSelected);
        return mChildFiles;
    }

    public MutableLiveData<List<VolumeInfo>> getVolumes() {
        return mVolumes;
    }

    public MutableLiveData<List<String>> getDirNavData(String volumePath) {
        mDirDatas = mFileRespository.getNavDirs(volumePath);
        return mDirDatas;
    }
}
