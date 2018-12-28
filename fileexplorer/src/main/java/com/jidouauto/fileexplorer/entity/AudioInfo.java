package com.jidouauto.fileexplorer.entity;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by lilin on 2015/5/21.
 */
public class AudioInfo extends FileInfo {

    public String durationStr;
    public String artist;
    public String title;
    private String mThumbnailPath;

    //TODO 音乐库刷新的问题，后续考虑
    public static AudioInfo getAudioInfo(String path, String title, String artist, long duration, String mimeType) {
        AudioInfo audioInfo = new AudioInfo();
        audioInfo.setFileInfoFields(FileInfo.getFileInfo(path, mimeType));
        audioInfo.durationStr = AudioInfo.getDurationString(duration);
        audioInfo.artist = artist;
        audioInfo.title = title;

        return audioInfo;
    }

    private void setFileInfoFields(FileInfo fileInfo) {
        this.icon = fileInfo.icon;
        this.size = fileInfo.size;
        this.fileName = fileInfo.fileName;
        this.fileTypeForTextView = fileInfo.fileTypeForTextView;
        this.mimeType = fileInfo.mimeType;
        this.path = fileInfo.path;
    }

    public String getThumbnailPath(Context context) {
        if (mThumbnailPath == null) {
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Audio.Media.ALBUM_ID},
                        MediaStore.Audio.Media.DATA + " = ?",
                        new String[]{path},
                        null
                );
                if (cursor != null && cursor.moveToFirst()) {
                    long albumId = cursor.getLong(0);
                    cursor.close();
                    cursor = context.getContentResolver().query(
                            Uri.withAppendedPath(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, albumId + ""),
                            new String[]{MediaStore.Audio.Albums.ALBUM_ART},
                            null,
                            null,
                            null
                    );
                    if (cursor != null && cursor.moveToFirst()) {
                        mThumbnailPath = cursor.getString(0);
                    }
                }
            } catch (Exception e) {

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return mThumbnailPath;
    }

    public static String getDurationString(long duration) {
        long time = (duration + 500) / 1000;
        int seconds = (int) (time % 60);
        String secondStr = (seconds >= 10) ? (seconds + "") : ("0" + seconds);
        time = time / 60;
        int minutes = (int) (time % 60);
        String minuteStr = (minutes >= 10) ? (minutes + "") : ("0" + minutes);
        time = time / 60;
        if (time == 0) {
            return minuteStr + ":" + secondStr;
        }
        return time + ":" + minuteStr + ":" + secondStr;
    }
}
