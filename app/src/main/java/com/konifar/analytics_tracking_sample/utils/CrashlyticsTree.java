package com.konifar.analytics_tracking_sample.utils;

import com.crashlytics.android.Crashlytics;

import timber.log.Timber;

public class CrashlyticsTree extends Timber.HollowTree {

    @Override
    public void i(String message, Object... args) {
        Crashlytics.log(String.format(message, args));
    }

    @Override
    public void i(Throwable t, String message, Object... args) {
        i(message, args);
    }

    @Override
    public void e(String message, Object... args) {
        i("ERROR: " + message, args);
    }

    @Override
    public void e(Throwable t, String message, Object... args) {
        e(message, args);
        Crashlytics.logException(t);
    }

}