package com.konifar.analytics_tracking_sample.models;

import com.konifar.analytics_tracking_sample.MainApplication;
import com.konifar.analytics_tracking_sample.events.PhotoSearchCallbackEvent;
import com.konifar.analytics_tracking_sample.models.pojo.PhotoSource;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

public class PhotoModel {

    private static final String CAT_SEARCH_TEXT = "cat";
    private static final int PER_PAGE = 36;
    private static PhotoModel instance;

    private PhotoModel() {
    }

    public static PhotoModel getInstance() {
        if (instance == null) {
            instance = new PhotoModel();
        }
        return instance;
    }

    public void getCatPhotos(int page) {
        MainApplication.FLICKR_API.photosSearch(CAT_SEARCH_TEXT, page, PER_PAGE, new Callback<PhotoSource>() {
            @Override
            public void success(PhotoSource photoSource, Response response) {
                EventBus.getDefault().post(new PhotoSearchCallbackEvent(true, photoSource.getPhotos()));
            }

            @Override
            public void failure(RetrofitError error) {
                Timber.e(error.getMessage());
                EventBus.getDefault().post(new PhotoSearchCallbackEvent(false));
            }
        });
    }

}
