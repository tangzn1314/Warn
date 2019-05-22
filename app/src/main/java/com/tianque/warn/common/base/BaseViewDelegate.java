package com.tianque.warn.common.base;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * @author ctrun on 2019/3/22.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class BaseViewDelegate implements LifecycleObserver {
    private static final String TAG = "BaseViewDelegate";

    /**
     * 显示等待对话框
     */
    abstract void showWaitingDialog();

    /**
     * 隐藏等待对话框
     */
    abstract void hideWaitingDialog();

    /**
     * 当 mvp的view 销毁时最好清空掉context
     */
    protected WeakReference<Context> mContextRef;

    public BaseViewDelegate(Context context) {
        mContextRef = new WeakReference<>(context);
    }

    @Nullable
    public Context context() {
        return null == mContextRef ? null : mContextRef.get();
    }

    /**
     * 检测context是否为null
     * 因为在View销毁时会调用onDestroy,会置空context,所以如果context不是null,则说明View还存在
     */
    @SuppressWarnings("RedundantIfStatement")
    protected boolean checkView() {
        Context context;
        if (null == mContextRef || null == (context = mContextRef.get())) {
            return false;
        }
        if (context instanceof Activity && isActivityFinishingOrDestroy((Activity) context)) {
            return false;
        }
        return true;
    }

    protected boolean isActivityFinishingOrDestroy(Activity activity) {
        if (Build.VERSION.SDK_INT >= 17) {
            return activity.isFinishing() || activity.isDestroyed();
        } else {
            return activity.isFinishing();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if(null != mContextRef){
            if(null != mContextRef.get()){
                mContextRef.clear();
            }
            mContextRef = null;
        }
    }
}
