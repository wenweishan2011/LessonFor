package com.jidouauto.fileexplorer.entity;

/**
 * Created by Administrator on 2015/5/22.
 */
public class VideoInfo extends FileInfo {
    public String durationStr;
    public String title;

    //TODO 库刷新的问题，后续考虑
    public static VideoInfo getVideoInfo(String path, String title, long duration, String mimeType) {
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setFileInfoFields(FileInfo.getFileInfo(path, mimeType));
        videoInfo.durationStr = VideoInfo.getDurationString(duration);
        videoInfo.title = title;

        return videoInfo;
    }

    private void setFileInfoFields(FileInfo fileInfo) {
        this.icon = fileInfo.icon;
        this.size = fileInfo.size;
        this.fileName = fileInfo.fileName;
        this.fileTypeForTextView = fileInfo.fileTypeForTextView;
        this.mimeType = fileInfo.mimeType;
        this.path = fileInfo.path;
    }

    public static String getDurationString (long duration) {
        long time = (duration + 500) / 1000;
        int seconds = (int)(time % 60);
        String secondStr = (seconds >= 10) ? (seconds + "") : ("0" + seconds);
        time = time / 60;
        int minutes = (int)(time % 60);
        String minuteStr = (minutes >= 10) ? (minutes + "") : ("0" + minutes);
        time = time / 60;
        if (time == 0) {
            return minuteStr + ":" + secondStr;
        }
        return time + ":" + minuteStr + ":" + secondStr;
    }
}
