package com.tianque.warn.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * @author ctrun on 2017/10/10.
 */
public class ObservableScrollView extends ScrollView {

    public Callbacks callbacks;

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (callbacks != null) {
            callbacks.onScrollChanged(t);
        }
    }

    public static interface Callbacks {
        void onScrollChanged(int t);
    }
}
