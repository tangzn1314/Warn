package com.tianque.warn.common.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author ctrun 2017/11/15.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class BasePresenter<V extends BaseView> implements LifecycleObserver {
    private static final String TAG = "BasePresenter";

    protected V mView;
    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public BasePresenter(V view) {
        mView = view;
    }

    protected void addDisposable(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    protected void removeDisposable(Disposable disposable) {
        mCompositeDisposable.remove(disposable);
    }

    protected void removeAllDisposables() {
        mCompositeDisposable.dispose();
        mCompositeDisposable.clear();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        removeAllDisposables();
        mView = null;
    }

}
