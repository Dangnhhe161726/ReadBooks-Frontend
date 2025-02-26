package com.example.frontend;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class FrontendAplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }
}
