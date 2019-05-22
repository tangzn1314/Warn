package com.tianque.warn.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.tianque.warn.R;

/**
 * @author ctrun
 * loadingView 控件
 */
public class LoadingView extends FrameLayout {

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.common_loading_view, this);

        initAnimator();
    }

    private void initAnimator() {

    }

}
