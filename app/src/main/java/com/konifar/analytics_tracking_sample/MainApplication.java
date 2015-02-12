package com.konifar.analytics_tracking_sample;

import android.app.Application;

import com.google.gson.GsonBuilder;
import com.konifar.analytics_tracking_sample.network.FlickrApiService;
import com.konifar.analytics_tracking_sample.utils.ViewUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class MainApplication extends Application {

    public static final FlickrApiService FLICKR_API = new RestAdapter.Builder()
            .setEndpoint(Constants.FLICKR_ENDPOINT)
            .setConverter(new GsonConverter(new GsonBuilder().setDateFormat(Constants.JSON_DATE_FORMAT).create()))
            .build()
            .create(FlickrApiService.class);

    @Override
    public void onCreate() {
        super.onCreate();

        initUniversalImageLoader();
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
