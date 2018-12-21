package com.wwsdemo.explorerdemo.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private String mModelId;
    private MutableLiveData<String> data;

    public void init(String mid) {
        this.mModelId = mid;
        data = new MutableLiveData<>();
    }

    public MutableLiveData<String> getData(){
        return data;
    }
}
