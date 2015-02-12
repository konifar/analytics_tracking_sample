package com.konifar.analytics_tracking_sample.views.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.konifar.analytics_tracking_sample.R;
import com.konifar.analytics_tracking_sample.models.pojo.Photo;
import com.konifar.analytics_tracking_sample.views.AspectRatioImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PhotosArrayAdapter extends ArrayAdapter<Photo> {

    public PhotosArrayAdapter(Context context) {
        super(context, R.layout.item_photo, new ArrayList<Photo>());
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null || view.getTag() == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
            holder = new ViewHolder(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final Photo photo = getItem(pos);
        String imageUrl = photo.getImageUrl();

        if (holder.mImgPreview.getTag() == null || !holder.mImgPreview.getTag().equals(imageUrl)) {
            ImageLoader.getInstance().displayImage(imageUrl, holder.mImgPreview);
            holder.mImgPreview.setTag(imageUrl);
        }

        view.setTag(holder);

        return view;
    }

    public void addAll(List<Photo> photos) {
        if (photos == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            super.addAll(photos);
        } else {
            for (Photo item : photos) {
                super.add(item);
            }
        }
    }

    static class ViewHolder {
        @InjectView(R.id.img_preview)
        AspectRatioImageView mImgPreview;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
