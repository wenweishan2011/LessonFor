package com.jidouauto.fileexplorer.manager;

import android.app.Application;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.jidouauto.fileexplorer.BuildConfig;
import com.jidouauto.fileexplorer.entity.AudioInfo;
import com.jidouauto.fileexplorer.entity.FileInfo;
import com.jidouauto.fileexplorer.entity.VideoInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MediaStoreManager {
    private final String TAG = "explorer" + MediaStoreManager.class.getSimpleName();

    private Application mApplication;

    private static volatile MediaStoreManager INSTANCE;

    private MediaStoreManager(Application application) {
        this.mApplication = application;
    }

    public static MediaStoreManager getManager(Application application){
        if(INSTANCE == null){
            synchronized (MediaStoreManager.class){
                if(INSTANCE == null){
                    INSTANCE = new MediaStoreManager(application);
                }
            }
        }
        return INSTANCE;
    }


    public List<FileInfo> queryAudioInfo(String volumePath) {
        Cursor cursor = null;
        try {
            cursor = mApplication.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ARTIST},
                    null,
                    null,
                    null
            );
            if (cursor != null) {
                List<FileInfo> audioInfo = new ArrayList<>();
                while (cursor.moveToNext()) {
                    final String path = cursor.getString(0);
                    File file = new File(path);
                    if (file.exists() && path.startsWith(volumePath)) {
                        audioInfo.add(AudioInfo.getAudioInfo(cursor.getString(0),
                                cursor.getString(2),
                                cursor.getString(4),
                                cursor.getLong(3),
                                "audio/*"
                        ));
                    }
                }
                return audioInfo;
            }
            return null;
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.d(getTag(), "扫描失败");
                e.printStackTrace();
            }
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private String getTag() {
        return "explorer_" + MediaStoreManager.class.getSimpleName();
    }

    private List<FileInfo> queryVideoInfo(String volumePath) {
        Cursor cursor = null;
        try {
            cursor = mApplication.getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Video.Media.DATA, MediaStore.Video.Media.MIME_TYPE, MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DURATION},
                    null,
                    null,
                    null
            );
            if (cursor != null) {
                List<FileInfo> videoInfo = new ArrayList<>();
                while (cursor.moveToNext()) {
                    final String path = cursor.getString(0);
                    File file = new File(path);
                    if (file.exists() && path.startsWith(volumePath)) {
                        videoInfo.add(VideoInfo.getVideoInfo(
                                cursor.getString(0),
                                cursor.getString(2),
                                cursor.getLong(3),
                                "video/*"
                        ));
                    }
                }
                return videoInfo;
            }
            return null;
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.d(getTag(), "扫描失败");
                e.printStackTrace();
            }
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public List<FileInfo> queryImageInfo(String volumePath) {
        Log.d(TAG, "queryImageInfo: " + volumePath);
        Cursor cursor = null;
        try {
            cursor = mApplication.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.MIME_TYPE},
                    null,
                    null,
                    null
            );
            if (cursor != null) {
                List<FileInfo> fileInfos = new ArrayList<>();
                while (cursor.moveToNext()) {
                    final String path = cursor.getString(0);
                    File file = new File(path);
                    if (file.exists() && path.startsWith(volumePath)) {

                        fileInfos.add(FileInfo.getFileInfo(cursor.getString(0), "image/*"));
                    }
                }
//                new FileRespository(mApplication).insert(fileInfos);
                return fileInfos;
            }
            return null;
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
