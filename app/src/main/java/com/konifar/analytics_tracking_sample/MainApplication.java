package com.konifar.analytics_tracking_sample;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.gson.GsonBuilder;
import com.konifar.analytics_tracking_sample.network.ApiModule;
import com.konifar.analytics_tracking_sample.network.FlickrApiService;
import com.konifar.analytics_tracking_sample.utils.CrashlyticsTree;
import com.konifar.analytics_tracking_sample.utils.ViewUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.parse.Parse;
import com.parse.ParseInstallation;

import dagger.ObjectGraph;
import io.fabric.sdk.android.Fabric;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import timber.log.Timber;

public class MainApplication extends Application {

    public static final FlickrApiService FLICKR_API = new RestAdapter.Builder()
            .setEndpoint(Constants.FLICKR_ENDPOINT)
            .setConverter(new GsonConverter(new GsonBuilder().setDateFormat(Constants.JSON_DATE_FORMAT).create()))
            .build()
            .create(FlickrApiService.class);

    private ObjectGraph objectGraph;

    public static MainApplication get(Context context) {
        return (MainApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        objectGraph = ObjectGraph.create(new ApiModule());

        initUniversalImageLoader();
        initTimber();

        Parse.initialize(this, Constants.PARSE_APPLICATION_ID, Constants.PARSE_CLIENT_KEY);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public void inject(Object o) {
        objectGraph.inject(o);
    }

    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashlyticsTree());
        }
    }

    private void initUniversalImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .discCache(new UnlimitedDiscCache(StorageUtils.getCacheDirectory(getApplicationContext())))
                .discCacheSize(64 * 1024 * 1024)
                .memoryCache(new LruMemoryCache(16 * 1024 * 1024))
                .memoryCacheSize(16 * 1024 * 1024)
                .defaultDisplayImageOptions(ViewUtils.getInstance().getDefaultImageOptions())
                .build();

        ImageLoader.getInstance().init(config);
    }

}
