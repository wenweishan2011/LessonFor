package com.jidouauto.fileexplorer.db;

import android.content.Context;
import android.util.Log;

import com.jidouauto.fileexplorer.dao.FileInfoDao;
import com.jidouauto.fileexplorer.entity.FileInfo;
import com.xuexiang.rxutil2.rxjava.RxJavaUtils;
import com.xuexiang.rxutil2.rxjava.task.RxIOTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {FileInfo.class}, version = 1)
public abstract class FileRoomDatabase extends RoomDatabase {
    private static String TAG = "explorer_" + FileRoomDatabase.class.getSimpleName();

    private static volatile FileRoomDatabase INSTANCE;

    public abstract FileInfoDao fileInfoDao();

    static FileRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FileRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FileRoomDatabase.class,
                            "file_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    RxJavaUtils.doInIOThread(new RxIOTask<FileRoomDatabase>(INSTANCE) {

                        @Override
                        public Void doInIOThread(FileRoomDatabase fileRoomDatabase) {
                            Log.d(TAG, "doInIOThread: ");
                            final FileInfoDao fileInfoDao = fileRoomDatabase.fileInfoDao();
                            FileInfo fileInfo = new FileInfo();
                            fileInfo.setPath("test");
                            fileInfo.setFileName("test");
                            fileInfoDao.insert(fileInfo);
                            FileInfo fileInfo2 = new FileInfo();
                            fileInfo2.setPath("test");
                            fileInfo2.setFileName("test2");
                            fileInfoDao.insert(fileInfo2);
                            return null;
                        }
                    });
                }
            };
}
