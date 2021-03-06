package com.konifar.analytics_tracking_sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.konifar.analytics_tracking_sample.models.pojo.Photo;
import com.konifar.analytics_tracking_sample.utils.AnalyticsUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nineoldandroids.view.animation.AnimatorProxy;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PhotoPreviewActivity extends Activity {

    private static final String EXTRA_ORIENTATION = "extra_orientation";
    private static final String EXTRA_LEFT = "extra_left";
    private static final String EXTRA_TOP = "extra_top";
    private static final String EXTRA_WIDTH = "extra_width";
    private static final String EXTRA_HEIGHT = "extra_height";

    private static final long ANIMATION_DURATION = 300;
    private static final Interpolator decelerateInterpolator = new DecelerateInterpolator();

    @InjectView(R.id.img_preview)
    ImageView mImgPreview;

    private int mLeftDelta;
    private int mTopDelta;
    private float mWidthScale;
    private float mHeightScale;
    private int mOriginalOrientation;

    public static void start(Activity activity, View transitionView, Photo photo) {
        Intent intent = new Intent(activity, PhotoPreviewActivity.class);

        int[] screenLocation = new int[2];
        transitionView.getLocationOnScreen(screenLocation);
        int orientation = activity.getResources().getConfiguration().orientation;

        intent.putExtra(Photo.class.getSimpleName(), photo);
        intent.putExtra(EXTRA_ORIENTATION, orientation);
        intent.putExtra(EXTRA_LEFT, screenLocation[0]);
        intent.putExtra(EXTRA_TOP, screenLocation[1]);
        intent.putExtra(EXTRA_WIDTH, transitionView.getWidth());
        intent.putExtra(EXTRA_HEIGHT, transitionView.getHeight());
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        ButterKnife.inject(this);

        Bundle bundle = getIntent().getExtras();
        final Photo photo = (Photo) bundle.getSerializable(Photo.class.getSimpleName());
        final int thumbnailTop = bundle.getInt(EXTRA_TOP);
        final int thumbnailLeft = bundle.getInt(EXTRA_LEFT);
        final int thumbnailWidth = bundle.getInt(EXTRA_WIDTH);
        final int thumbnailHeight = bundle.getInt(EXTRA_HEIGHT);
        mOriginalOrientation = bundle.getInt(EXTRA_ORIENTATION);

        ImageLoader.getInstance().displayImage(photo.getImageUrl(), mImgPreview);

        if (savedInstanceState == null) {
            ViewTreeObserver observer = mImgPreview.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mImgPreview.getViewTreeObserver().removeOnPreDrawListener(this);

                    int[] screenLocation = new int[2];
                    mImgPreview.getLocationOnScreen(screenLocation);
                    mLeftDelta = thumbnailLeft - screenLocation[0];
                    mTopDelta = thumbnailTop - screenLocation[1];
                    mWidthScale = (float) thumbnailWidth / (mImgPreview.getWidth());
                    mHeightScale = (float) thumbnailHeight / mImgPreview.getHeight();

                    startEnterAnimation();

                    return true;
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        AnalyticsUtils.getInstance().sendScreen(this);
    }

    public void startEnterAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mImgPreview.setPivotX(0);
            mImgPreview.setPivotY(0);
            mImgPreview.setScaleX(mWidthScale);
            mImgPreview.setScaleY(mHeightScale);
            mImgPreview.setTranslationX(mLeftDelta);
            mImgPreview.setTranslationY(mTopDelta);
        } else {
            AnimatorProxy proxy = AnimatorProxy.wrap(mImgPreview);
            proxy.setPivotX(0);
            proxy.setPivotY(0);
            proxy.setScaleX(mWidthScale);
            proxy.setScaleY(mHeightScale);
            proxy.setTranslationX(mLeftDelta);
            proxy.setTranslationY(mTopDelta);
        }

        ViewPropertyAnimator.animate(mImgPreview)
                .setDuration(ANIMATION_DURATION)
                .scaleX(1).scaleY(1)
                .translationX(0).translationY(0)
                .setInterpolator(decelerateInterpolator);
    }

    public void startExitAnimation() {
        final boolean fadeOut;
        if (getResources().getConfiguration().orientation != mOriginalOrientation) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                mImgPreview.setPivotX(mImgPreview.getWidth() / 2);
                mImgPreview.setPivotY(mImgPreview.getHeight() / 2);
            } else {
                AnimatorProxy proxy = AnimatorProxy.wrap(mImgPreview);
                proxy.setPivotX(mImgPreview.getWidth() / 2);
                proxy.setPivotY(mImgPreview.getHeight() / 2);
            }
            mLeftDelta = 0;
            mTopDelta = 0;
            fadeOut = true;
        } else {
            fadeOut = false;
        }

        ViewPropertyAnimator.animate(mImgPreview)
                .setDuration(ANIMATION_DURATION)
                .scaleX(mWidthScale).scaleY(mHeightScale)
                .translationX(mLeftDelta).translationY(mTopDelta)
                .setInterpolator(decelerateInterpolator)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        finish();
                    }
                });

        if (fadeOut) {
            ViewPropertyAnimator.animate(mImgPreview).alpha(0);
        }
    }

    @Override
    public void onBackPressed() {
        startExitAnimation();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

}
