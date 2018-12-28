package com.wwsdemo.explorerdemo.ui.main;

import android.content.ContentResolver;
import android.net.Uri;

import com.wwsdemo.explorerdemo.repository.DocumentInfoRepository;
import com.wwsdemo.explorerdemo.ui.main.bean.DocumentInfo;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private String mModelId;
    private DocumentInfoRepository documentInfoRepository;
    private ContentResolver contentResolver;
    private MutableLiveData<DocumentInfo> data;



    public void init(String mid, ContentResolver contentResolver) {
        this.mModelId = mid;
        data = new MutableLiveData<>();
        this.contentResolver = contentResolver;
        documentInfoRepository = new DocumentInfoRepository();
    }

    public LiveData<List<DocumentInfo>> getData(Uri uri){
        final LiveData<List<DocumentInfo>> documents = documentInfoRepository.getDocuments(contentResolver, uri);
        return documents;
    }
}
