package com.wwsdemo.explorerdemo.repository;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract.Document;

import com.wwsdemo.explorerdemo.ui.main.bean.DocumentInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class DocumentInfoRepository {
//    private ContentResolver mContentResolver;

    public LiveData<List<DocumentInfo>> getDocuments(ContentResolver contentResolver, Uri uri){
        final MutableLiveData<List<DocumentInfo>> data = new MutableLiveData<>();
        List<DocumentInfo> list = new ArrayList<>();
        DocumentInfo documentInfo;
        final Cursor result = contentResolver.query(uri, null, null, null, null);
        while(result !=null && result.moveToNext()){
            documentInfo = new DocumentInfo();
            documentInfo.display_name = result.getString(result.getColumnIndex(Document.COLUMN_DISPLAY_NAME));
            documentInfo.document_id = result.getString(result.getColumnIndex(Document.COLUMN_DOCUMENT_ID));
            documentInfo.flags = result.getString(result.getColumnIndex(Document.COLUMN_FLAGS));
            documentInfo.icon = result.getString(result.getColumnIndex(Document.COLUMN_ICON));
            documentInfo.lastModified = result.getString(result.getColumnIndex(Document.COLUMN_LAST_MODIFIED));
            documentInfo.mime_type = result.getString(result.getColumnIndex(Document.COLUMN_MIME_TYPE));
            documentInfo.size = result.getString(result.getColumnIndex(Document.COLUMN_SIZE));
            list.add(documentInfo);
        }
        data.setValue(list);
        return data;
    }

}
