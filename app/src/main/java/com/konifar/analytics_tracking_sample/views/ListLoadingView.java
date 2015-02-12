package com.konifar.analytics_tracking_sample.views;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.konifar.analytics_tracking_sample.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ListLoadingView extends LinearLayout {

    @InjectView(R.id.list_loading)
    FrameLayout mListLoading;

    public ListLoadingView(Context context) {
        super(context);
        inflate(context, R.layout.ui_list_loading, this);
        ButterKnife.inject(this);
    }

    public void switchVisible(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        mListLoading.setVisibility(visibility);
    }

}
