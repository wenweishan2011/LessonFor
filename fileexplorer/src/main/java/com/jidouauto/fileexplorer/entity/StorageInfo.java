package com.jidouauto.fileexplorer.entity;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Log;

import com.jidouauto.fileexplorer.BuildConfig;
import com.jidouauto.fileexplorer.ExplorerApplication;
import com.jidouauto.fileexplorer.R;

import java.lang.reflect.Method;

/**
 * Created by kimi on 2015/5/23.
 */
public class StorageInfo {

    private static final String TAG = "StorageInfo";
    public static final int INTERNAL_STORAGE = 1;
    public static final int TF = 2;
    public static final int USB = 3;
    private static Method getVolumePaths;
    private static Method getVolumeState;
    public static StorageManager mStorageManager = (StorageManager) ExplorerApplication.getInstance().getSystemService(Context.STORAGE_SERVICE);
    public String mStoragePath;
    public int mStorageType;
    public String mStorageName;
    public long mTotalSpace;
    public long mAvailableSpace;

    public StorageInfo(String storagePath) {
        mStoragePath = storagePath;

        if (storagePath.equalsIgnoreCase(Environment.getExternalStorageDirectory().getPath())) {
            mStorageType = INTERNAL_STORAGE;
            mStorageName = ExplorerApplication.getInstance().getString(R.string.built_in_stroage);
            return;
        }
        int position = mStoragePath.lastIndexOf("/");
        mStorageName = mStoragePath.substring(position + 1, mStoragePath.length());
        if (mStorageName.toLowerCase().contains("usb")) {
            mStorageType = USB;
        } else
            mStorageType = TF;
    }

    public void refreshSpace() {
        if (!TextUtils.isEmpty(mStoragePath)) {
            mTotalSpace = getTotalSpace(mStoragePath);
            mAvailableSpace = getAvailableSpace(mStoragePath);
        }
    }

    /**
     * 获得系统中所有磁盘路径，并不是所有获取的磁盘都处于挂载状态，需另行检测
     *
     * @return 磁盘路径，以数组形式返回
     */
    public static String[] getCustomVolumePaths() {
        try {
            if (getVolumePaths == null) {
                getVolumePaths = mStorageManager.getClass().getDeclaredMethod("getVolumePaths");
            }
            return (String[]) getVolumePaths.invoke(mStorageManager);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
                Log.d(TAG, "获取磁盘信息失败");
            }
            return null;
        }
    }

    /**
     * 获得磁盘的挂载状态
     *
     * @param volumePath 磁盘路径
     * @return 挂载状态，字符串形式
     */
    public static String getCustomVolumeState(String volumePath) {
        try {
            if (getVolumeState == null) {
                getVolumeState = mStorageManager.getClass().getDeclaredMethod("getVolumeState", String.class);
            }
            return (String) getVolumeState.invoke(mStorageManager, volumePath);
        } catch (Exception e) {

            if (BuildConfig.DEBUG) {
                e.printStackTrace();
                Log.d(TAG, "获取磁盘信息失败");
            }
            return null;
        }
    }

    /**
     * 获得目标路径所在磁盘的总空间，路径无效抛异常
     *
     * @param volumePath 目标路径
     * @return 磁盘总空间大小，以byte为单位
     */
    public static long getTotalSpace(String volumePath) {
        try {
            StatFs statFs = new StatFs(volumePath);
            if (Build.VERSION.SDK_INT >= 18) {
                return statFs.getTotalBytes();
            } else {
                return (long) statFs.getBlockCount() * statFs.getBlockSize();
            }
        } catch (IllegalArgumentException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    /**
     * 获得目标路径所在磁盘的可用空间，若路径无效抛异常
     *
     * @param volumePath 目标路径
     * @return 可用磁盘空间，以byte为单位
     */
    public static long getAvailableSpace(String volumePath) {
        try {
            StatFs statFs = new StatFs(volumePath);
            if (Build.VERSION.SDK_INT >= 18) {
                return statFs.getAvailableBytes();
            } else {
                return (long) statFs.getAvailableBlocks() * statFs.getBlockSize();
            }
        } catch (IllegalArgumentException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof StorageInfo) {
            return !TextUtils.isEmpty(((StorageInfo) o).mStoragePath) && ((StorageInfo) o).mStoragePath.equals(this.mStoragePath);
        }
        return super.equals(o);
    }
}
