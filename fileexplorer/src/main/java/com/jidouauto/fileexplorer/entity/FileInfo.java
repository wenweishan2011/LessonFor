package com.jidouauto.fileexplorer.entity;

import android.webkit.MimeTypeMap;

import com.jidouauto.fileexplorer.ExplorerApplication;
import com.jidouauto.fileexplorer.R;

import java.io.File;
public class FileInfo {
    public String path;
    public String parentPath;
    public String fileName;
    public String mimeType;
    public int icon;
    public String lastModified;
    public String size;
    public String fileTypeForTextView;
    public boolean isDir = false;
    public String volumePath;

    public static FileInfo getFileInfo(String filePath, String mimeType) {
        FileInfo fileInfo = new FileInfo();
        if (mimeType == null) {
            fileInfo.mimeType = null;
        } else {
            fileInfo.mimeType = mimeType.toLowerCase();
        }
        fileInfo.path = filePath;
//        fileInfo.isCheck = false;
        File file = new File(filePath);
        if (fileInfo.mimeType != null) {//若为已知文件
            if (fileInfo.mimeType.equals("dir")) {//若为文件夹
                fileInfo.icon = R.drawable.icon_folder;
                fileInfo.fileTypeForTextView = ExplorerApplication.getInstance().getString(R.string.folder);
            } else {
                if (fileInfo.mimeType.startsWith("audio") || fileInfo.mimeType.startsWith("application/ogg")
                        || fileInfo.mimeType.startsWith("application/x-flac") || fileInfo.mimeType.startsWith("aac")
                        || mimeType.startsWith("flac")) {//若为音频文件
                    fileInfo.icon = -1;
                    fileInfo.fileTypeForTextView =
                            ExplorerApplication.getInstance().getString(R.string.audio);
                } else if (fileInfo.mimeType.startsWith("application/pdf")) {//若为pdf文件
                    fileInfo.icon = R.drawable.icon_pdf;
                    fileInfo.fileTypeForTextView = "PDF";
                } else if (fileInfo.mimeType.startsWith("application/rar") || fileInfo.mimeType.startsWith("application/zip")
                        || fileInfo.mimeType.startsWith("application/x-gtar") || fileInfo.mimeType.startsWith("application/x-tar")) {//若为压缩文件
                    fileInfo.icon = R.drawable.icon_zip;
                    fileInfo.fileTypeForTextView = ExplorerApplication.getInstance().getString(R.string.zip_file);
                } else if (fileInfo.mimeType.startsWith("application/vnd.android.package-archive")) {//若文件为apk
                    fileInfo.icon = R.drawable.icon_apk;
                    fileInfo.fileTypeForTextView = ExplorerApplication.getInstance().getString(R.string.apk);
                } else if (fileInfo.mimeType.startsWith("application/msword")
                        || fileInfo.mimeType.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml")) {//若为word文档
                    fileInfo.icon = R.drawable.icon_word;
                    fileInfo.fileTypeForTextView = ExplorerApplication.getInstance().getString(R.string.word);
                } else if (fileInfo.mimeType.startsWith("application/vnd.ms-excel")
                        || fileInfo.mimeType.startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml")) {//若为excel文件
                    fileInfo.icon = R.drawable.icon_excel;
                    fileInfo.fileTypeForTextView = ExplorerApplication.getInstance().getString(R.string.excel);
                } else if (fileInfo.mimeType.startsWith("application/vnd.ms-powerpoint")
                        || fileInfo.mimeType.startsWith("application/vnd.openxmlformats-officedocument.presentationml")) {//若文件为ppt
                    fileInfo.icon = R.drawable.icon_ppt;
                    fileInfo.fileTypeForTextView = ExplorerApplication.getInstance().getString(R.string.ppt);
                } else if (fileInfo.mimeType.startsWith("application/vnd.visio")) {//若为visio文件
                    fileInfo.icon = R.drawable.icon_visio;
                    fileInfo.fileTypeForTextView = ExplorerApplication.getInstance().getString(R.string.visio);
                } else if (fileInfo.mimeType.startsWith("application/x-bittorrent")) {//若为torrent文件
                    fileInfo.icon = R.drawable.icon_torrent;
                    fileInfo.fileTypeForTextView = ExplorerApplication.getInstance().getString(R.string.bt);
                } else if (fileInfo.mimeType.startsWith("application/x-iso9660-image")) {//若为iso镜像文件
                    fileInfo.icon = R.drawable.icon_iso;
                    fileInfo.fileTypeForTextView = ExplorerApplication.getInstance().getString(R.string.iso);
                } else if (fileInfo.mimeType.startsWith("application/x-msi")) {//若为msi文件
                    fileInfo.icon = R.drawable.icon_msi;
                    fileInfo.fileTypeForTextView = ExplorerApplication.getInstance().getString(R.string.msi);
                } else if (fileInfo.mimeType.startsWith("application/x-shockwave-flash")) {//若为swf文件
                    fileInfo.icon = R.drawable.icon_flash;
                    fileInfo.fileTypeForTextView = ExplorerApplication.getInstance().getString(R.string.flash);
                } else if (fileInfo.mimeType.startsWith("application/xhtml+xml") || fileInfo.mimeType.startsWith("text/html")) {//若为网页文件
                    fileInfo.icon = R.drawable.icon_html;
                    fileInfo.fileTypeForTextView = ExplorerApplication.getInstance().getString(R.string.html);
                } else if (fileInfo.mimeType.startsWith("image")) {//若为图片文件
                    fileInfo.icon = -1;
                    fileInfo.fileTypeForTextView =
                            ExplorerApplication.getInstance().getString(R.string.picture);
                } else if (fileInfo.mimeType.startsWith("text/comma-separated-values")) {//若为csv文件
                    fileInfo.icon = R.drawable.icon_csv;
                    fileInfo.fileTypeForTextView = ExplorerApplication.getInstance().getString(R.string.csv);
                } else if (fileInfo.mimeType.startsWith("text/comma-separated-values")) {//若为css文件
                    fileInfo.icon = R.drawable.icon_css;
                    fileInfo.fileTypeForTextView = ExplorerApplication.getInstance().getString(R.string.css);
                } else if (fileInfo.mimeType.startsWith("text/plain")) {//若为文本文件
                    fileInfo.icon = R.drawable.icon_txt;
                    fileInfo.fileTypeForTextView = ExplorerApplication.getInstance().getString(R.string.txt);
                } else if (fileInfo.mimeType.startsWith("text/xml")) {//若为xml
                    fileInfo.icon = R.drawable.icon_xml;
                    fileInfo.fileTypeForTextView = ExplorerApplication.getInstance().getString(R.string.xml);
                } else if (fileInfo.mimeType.startsWith("text/log")) {//若为log文件
                    fileInfo.icon = R.drawable.icon_txt;
                    fileInfo.fileTypeForTextView = ExplorerApplication.getInstance().getString(R.string.log);
                } else if (fileInfo.mimeType.startsWith("text/x-vcalendar")) {//若为vcs备忘录文件
                    fileInfo.icon = R.drawable.icon_vcs;
                    fileInfo.fileTypeForTextView = ExplorerApplication.getInstance().getString(R.string.vcs);
                } else if (fileInfo.mimeType.startsWith("text/x-vcard")) {//若为名片文件
                    fileInfo.icon = R.drawable.icon_vcf;
                    fileInfo.fileTypeForTextView = ExplorerApplication.getInstance().getString(R.string.vcf);
                } else if (fileInfo.mimeType.startsWith("res/*")) {
                    fileInfo.icon = R.drawable.icon_res;
                    fileInfo.fileTypeForTextView = ExplorerApplication.getInstance().getString(R.string.res);
                } else {
                    if (fileInfo.mimeType.startsWith("video")) {//若为视频文件
                        fileInfo.icon = -1;
                        fileInfo.fileTypeForTextView =
                                ExplorerApplication.getInstance().getString(R.string.video);
                    } else {//若为无法识别文件
                        fileInfo.icon = R.drawable.icon_others;
                        fileInfo.fileTypeForTextView = ExplorerApplication.getInstance().getString(R.string.unknow);
                    }
                }
            }
        } else {//若为未知文件
            fileInfo.icon = R.drawable.icon_others;
            fileInfo.fileTypeForTextView = ExplorerApplication.getInstance().getString(R.string.unknow);
        }
        fileInfo.fileName = file.getName();
        fileInfo.size = getFormattedSize(file);
        return fileInfo;
    }

    /**
     * 通过扩展名获取文件的mimeType
     *
     * @param file 目标文件
     * @return mimeType， 文件夹返回“dir”，未知返回null
     */
    public static String getMimeType(File file) {
        if (!file.exists()) {
            return null;
        }
        if (file.isDirectory()) {
            return "dir";
        } else {
            String fileName = file.getName();
            int position = fileName.lastIndexOf('.');
            if (position == -1 || (position+1)> fileName.length()) {
                return null;
            }
            String extendName = fileName.substring(position + 1);
            extendName = extendName.toLowerCase();
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extendName);
//            Log.d("FileInfo", "jeffyu " + file.getName() + mimeType);
            switch (extendName) {
                case "flac":
                    mimeType = "audio/*";
                    break;
                case "aac":
                    mimeType = "audio/*";
                    break;
                case "ape":
                    mimeType = "audio/*";
                    break;
                case "log":
                    mimeType = "text/log";
                    break;
                case "res":
                    mimeType = "res/*";
                    break;
                case "rm":
                case "rmvb":
                    mimeType = "video/*";
                    break;
                case "mkv":
                    mimeType = "video/*";
                    break;
                case "flv":
                    mimeType = "video/*";
                    break;
                case "vob":
                    mimeType = "video/*";
                    break;
                case "lrc":
                    mimeType = "text/lrc";
                    break;
            }
            return mimeType;
        }
    }

    /**
     * 获得文件的大小，不足1k的用b表示，不足1m的用k表示，不足1g的用m表示，
     * 保留2位小数，文件夹的大小不能显示
     *
     * @param file 目标文件的abstract path
     * @return 使用正确单位表示的文件大小，文件夹返回null
     */
    public static String getFormattedSize(File file) {
        if (file.isDirectory()) {//若为文件夹
            return null;
        } else {//不为文件夹
            double sizeFormatted = file.length();
            if (sizeFormatted < 1024) {//大小不满1k
                return sizeFormatted + "B";
            }
            sizeFormatted = sizeFormatted / 1024d;
            sizeFormatted = (long) (sizeFormatted * 100d + 0.5d);//保留2位小数
            sizeFormatted = sizeFormatted / 100d;
            if (sizeFormatted < 1024) {//大小不满1M
                return sizeFormatted + "K";
            }
            sizeFormatted = sizeFormatted / 1024d;
            sizeFormatted = (long) (sizeFormatted * 100d + 0.5d);//保留2位小数
            sizeFormatted = sizeFormatted / 100d;
            if (sizeFormatted < 1024) {//大小不满1G
                return sizeFormatted + "M";
            }
            sizeFormatted = sizeFormatted / 1024d;
            sizeFormatted = (long) (sizeFormatted * 100d + 0.5d);//保留2位小数
            sizeFormatted = sizeFormatted / 100d;
            return sizeFormatted + "G";//大小超过1G
        }
    }
}
