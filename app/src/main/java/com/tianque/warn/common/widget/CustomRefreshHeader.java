package com.tianque.warn.common.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.tianque.warn.R;

/**
 * @author ctrun on 2018/10/13.
 */
public class CustomRefreshHeader extends FrameLayout implements RefreshHeader {

    private Animation mRotateAnimLoadingOnce;
    private Animation mRotateAnimLoading;

    protected RefreshKernel refreshKernel;
    protected SpinnerStyle spinnerStyle = SpinnerStyle.Translate;
    private ImageView ivLoading;

    protected int backgroundColor;
    protected int finishDuration = 100;

    public CustomRefreshHeader(Context context) {
        this(context, null);
    }

    public CustomRefreshHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public CustomRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.common_custom_refresh_header, this);
        ivLoading = findViewById(R.id.iv_loading);

        mRotateAnimLoadingOnce = AnimationUtils.loadAnimation(getContext(), R.anim.common_rotate_anim_loading_once);
        mRotateAnimLoading = AnimationUtils.loadAnimation(getContext(), R.anim.common_rotate_anim_loading);
    }

    @Override
    public void onPulling(float percent, int offset, int height, int extendHeight) {

    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
                stopAnimator();
                break;
            case PullDownToRefresh:
                stopAnimator();
                break;
            case ReleaseToRefresh:
                startAnimator();
                break;
            default:
                break;
        }
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout layout, int height, int extendHeight) {
        ivLoading.startAnimation(mRotateAnimLoading);
    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        ivLoading.clearAnimation();

        return finishDuration;
    }

    private void startAnimator() {
        ivLoading.startAnimation(mRotateAnimLoadingOnce);
    }

    private void stopAnimator() {
        ivLoading.clearAnimation();
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public void onReleasing(float percent, int offset, int headerHeight, int extendHeight) {

    }

    @Override
    public void onReleased(RefreshLayout refreshLayout, int height, int extendHeight) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void setPrimaryColors(int... colors) {
        if (colors.length > 0) {
            if (!(getBackground() instanceof BitmapDrawable)) {
                setPrimaryColor(colors[0]);
            }
        }
    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int extendHeight) {
        refreshKernel = kernel;
        refreshKernel.requestDrawBackgroundForHeader(backgroundColor);
    }

    @SuppressWarnings("UnusedReturnValue")
    public CustomRefreshHeader setPrimaryColor(@ColorInt int primaryColor) {
        setBackgroundColor(backgroundColor = primaryColor);
        if (refreshKernel != null) {
            refreshKernel.requestDrawBackgroundForHeader(backgroundColor);
        }
        return this;
    }

    @SuppressWarnings("unused")
    public CustomRefreshHeader setSpinnerStyle(SpinnerStyle style) {
        this.spinnerStyle = style;
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return spinnerStyle;
    }

    @NonNull @Override
    public View getView() {
        return this;
    }
}
