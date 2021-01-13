package com.example.cxensesdk.java;

import android.app.Application;

import timber.log.Timber;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new LoggingTree());
    }
}
