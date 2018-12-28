package com.jidouauto.fileexplorer;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class ExplorerApplication extends Application {
    private static Application mApplication;

    public static Application getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        Stetho.initializeWithDefaults(this);
    }
}
