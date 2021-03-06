package com.konifar.analytics_tracking_sample;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.konifar.analytics_tracking_sample.events.PhotoSearchCallbackEvent;
import com.konifar.analytics_tracking_sample.models.PhotoModel;
import com.konifar.analytics_tracking_sample.models.pojo.Photo;
import com.konifar.analytics_tracking_sample.utils.AnalyticsUtils;
import com.konifar.analytics_tracking_sample.views.ListLoadingView;
import com.konifar.analytics_tracking_sample.views.adapters.PhotosArrayAdapter;
import com.konifar.analytics_tracking_sample.views.listeners.OnLoadMoreScrollListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import jp.co.recruit_mp.android.widget.HeaderFooterGridView;

public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @InjectView(R.id.grid_view)
    HeaderFooterGridView mGridView;
    @InjectView(R.id.loading)
    FrameLayout mLoading;

    private PhotosArrayAdapter adapter;
    private ListLoadingView mLoadingFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);

        setSupportActionBar(mToolbar);
        initGridView();
        initSwipeRefresh();

        showList(1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AnalyticsUtils.getInstance().sendScreen(this);
    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.theme500);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showList(1);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initGridView() {
        adapter = new PhotosArrayAdapter(this);
        mLoadingFooter = new ListLoadingView(this);
        mGridView.addFooterView(mLoadingFooter);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Photo photo = adapter.getItem(position);
                PhotoPreviewActivity.start(MainActivity.this, view, photo);
            }
        });
    }

    private void showList(final int page) {
        if (page > 1) mLoadingFooter.switchVisible(true);
        PhotoModel.getInstance(this).getCatPhotos(page);
    }

    private void initListViewScrollListener() {
        mGridView.setOnScrollListener(new OnLoadMoreScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                showList(page);
            }
        });
    }


    public void onEvent(PhotoSearchCallbackEvent event) {
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
            adapter.clear();
        }

        if (event.isSuccess()) {
            adapter.addAll(event.getPhotos());
        }

        if (mLoading.getVisibility() == View.VISIBLE) {
            initListViewScrollListener();
            mLoading.setVisibility(View.GONE);
        }

        mLoadingFooter.switchVisible(false);
    }

}
