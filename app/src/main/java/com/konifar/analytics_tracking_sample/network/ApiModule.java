package com.konifar.analytics_tracking_sample.network;

import com.konifar.analytics_tracking_sample.MainApplication;
import com.konifar.analytics_tracking_sample.models.PhotoModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = {PhotoModel.class})
public class ApiModule {
    @Provides
    @Singleton
    FlickrApiService provideFlickrApiService() {
        return MainApplication.FLICKR_API;
    }
}
