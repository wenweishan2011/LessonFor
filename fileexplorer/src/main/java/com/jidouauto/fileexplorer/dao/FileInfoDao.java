package com.jidouauto.fileexplorer.dao;

import com.jidouauto.fileexplorer.entity.FileInfo;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface FileInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FileInfo fileInfo);

    @Query("SELECT * FROM file_info_table ORDER BY size DESC")
    LiveData<List<FileInfo>> getChildFileInfo();

}
