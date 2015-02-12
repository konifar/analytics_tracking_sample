package com.konifar.analytics_tracking_sample.network;

import com.konifar.analytics_tracking_sample.Constants;
import com.konifar.analytics_tracking_sample.models.pojo.PhotoSource;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface FlickrApiService {

    /**
     * https://www.flickr.com/services/api/flickr.photos.search.html
     */
    @GET("/services/rest?method=flickr.photos.search&sort=interestingness-desc&api_key=" + Constants.FLICKR_API_KEY + "&format=json&nojsoncallback=1")
    public void photosSearch(@Query("text") String text,
                             @Query("page") Integer page,
                             @Query("perpage") Integer perpage,
                             Callback<PhotoSource> cb);

}
