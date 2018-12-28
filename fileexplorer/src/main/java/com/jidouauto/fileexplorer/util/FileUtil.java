package com.jidouauto.fileexplorer.util;

import com.jidouauto.fileexplorer.entity.StorageInfo;
import com.jidouauto.fileexplorer.entity.VolumeInfo;

import java.io.File;
import java.util.List;

public class FileUtil {
    public static File[] getChildFiles(String parentPath){
        if(parentPath.isEmpty()){
            return null;
        }
        File parentFile = new File(parentPath);
        if(parentFile.exists() && parentFile.isDirectory()){
            return parentFile.listFiles();

        }
        return null;
    }

    /**
     * 检测文件所在磁盘的路径
     *
     * @param path 文件路径
     * @return 磁盘路径，若未查找到返回null
     */
    public static String getVolumePath(String path) {
        String[] volumePaths = StorageInfo.getCustomVolumePaths();
        for (String volumePath : volumePaths) {
            if (path.startsWith(volumePath)) {
                return volumePath;
            }
        }
        return null;
    }
    /**
     * 检测文件所在磁盘的路径
     *
     * @param path 文件路径
     * @param volumes 所有盘符
     * @return 磁盘路径，若未查找到返回null
     */
    public static String getVolumePath(String path, List<VolumeInfo> volumes) {
        for (VolumeInfo volumePath : volumes) {
            if (path.startsWith(volumePath.path)) {
                return volumePath.path;
            }
        }
        return null;
    }

    /**
     * 是否是根目录
     * @return
     */
    public static boolean isRoot(String path){
        final String volumePath = getVolumePath(path);
        return volumePath.equals(path);
    }
}
