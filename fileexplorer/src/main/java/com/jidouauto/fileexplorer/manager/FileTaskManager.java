package com.jidouauto.fileexplorer.manager;

import android.app.Application;

import com.jidouauto.fileexplorer.entity.FileInfo;
import com.jidouauto.fileexplorer.util.FileUtil;
import com.xuexiang.rxutil2.rxjava.RxJavaUtils;
import com.xuexiang.rxutil2.rxjava.task.RxAsyncTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class FileTaskManager {
    private Application mApplication;

    private static volatile FileTaskManager INSTANCE;

    private FileTaskManager(Application application) {
        this.mApplication = application;
    }

    public static FileTaskManager getManager(Application application) {
        if (INSTANCE == null) {
            synchronized (MediaStoreManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FileTaskManager(application);
                }
            }
        }
        return INSTANCE;
    }

    public void listFiles(String parentPath, MutableLiveData<List<FileInfo>> liveData) {
        RxJavaUtils.executeAsyncTask(new ListFilesTask(parentPath).setLiveData(liveData));

    }

    public void deleteFiles( List<FileInfo> dellist,InFileOperateProgressListener listener){

    }

    private interface InFileOperateProgressListener{

    }

 /*   private class DeleteFilesTask extends RxAsyncTask<List<FileInfo>, Integer>{

        @Override
        public Integer doInIOThread(List<FileInfo> list) {
            return null;
        }



        @Override
        public void doInUIThread(Integer integer) {

        }
    }*/


    private class ListFilesTask extends RxAsyncTask<String, List<FileInfo>> {
        private MutableLiveData<List<FileInfo>> listLiveData;

        public ListFilesTask(String inData) {
            super(inData);
        }

        public RxAsyncTask setLiveData(MutableLiveData<List<FileInfo>> listLiveData) {
            this.listLiveData = listLiveData;
            return this;
        }

        @Override
        public List<FileInfo> doInIOThread(String s) {
            final File[] childFiles = FileUtil.getChildFiles(s);
            List<FileInfo> result = new ArrayList<>();
            if (childFiles != null && childFiles.length > 0) {
                FileInfo fileInfo = null;
                for (File file : childFiles) {
                    fileInfo = new FileInfo();
                    fileInfo.path = file.getAbsolutePath();
                    fileInfo.fileName = file.getName();
                    fileInfo.parentPath = s;
                    fileInfo.isDir = file.isDirectory();
                    fileInfo.volumePath = FileUtil.getVolumePath(file.getAbsolutePath());
//                    fileInfo.lastModified = file.lastModified();
                    result.add(fileInfo);

                }
            }
            return result;
        }

        @Override
        public void doInUIThread(List<FileInfo> fileInfos) {
            listLiveData.setValue(fileInfos);
        }
    }

    interface OnResultBackListener<T> {
        void onResultSuccess(T result);

        void onError();
    }

}
