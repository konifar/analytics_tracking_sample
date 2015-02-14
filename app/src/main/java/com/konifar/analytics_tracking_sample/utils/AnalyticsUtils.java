package com.konifar.analytics_tracking_sample.utils;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.konifar.analytics_tracking_sample.R;

import java.util.HashMap;

public class AnalyticsUtils {

    private static AnalyticsUtils instance;
    private HashMap<TrackerName, Tracker> mTrackers = new HashMap<>();

    private AnalyticsUtils() {
    }

    public static AnalyticsUtils getInstance() {
        if (instance == null) {
            instance = new AnalyticsUtils();
        }
        return instance;
    }

    public void sendScreen(Activity activity) {
        Tracker tracker = getTracker(activity);
        tracker.setScreenName(activity.getClass().getSimpleName());
        tracker.send(new HitBuilders.AppViewBuilder().build());
    }

    private synchronized Tracker getTracker(Context context) {
        TrackerName trackerId = TrackerName.GLOBAL_TRACKER;
        if (!mTrackers.containsKey(trackerId)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
            Tracker tracker = analytics.newTracker(R.xml.global_tracker);
            mTrackers.put(trackerId, tracker);

        }
        return mTrackers.get(trackerId);
    }

    private enum TrackerName {GLOBAL_TRACKER}

}
