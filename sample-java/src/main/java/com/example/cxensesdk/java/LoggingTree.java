package com.example.cxensesdk.java;

import android.util.Log;

import org.jetbrains.annotations.Nullable;

import timber.log.Timber;

public class LoggingTree extends Timber.DebugTree {
    @Override
    protected boolean isLoggable(@Nullable String tag, int priority) {
        return BuildConfig.DEBUG || priority >= Log.ERROR;
    }
}
