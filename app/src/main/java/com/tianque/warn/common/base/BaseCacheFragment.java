package com.tianque.warn.common.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author ctrun on 15-7-11.
 * 封闭了页面缓存功能
 */
public abstract class BaseCacheFragment<P extends BasePresenter> extends BaseFragment<P> {

    @Override
    public View getView() {
        return mContentView;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mContentView == null) {
            super.onCreateView(inflater, container, savedInstanceState);
        }

        //缓存的contentView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个contentView已经有parent的错误。
        ViewGroup parent = (ViewGroup) mContentView.getParent();
        if (parent != null) {
            parent.removeView(mContentView);
        }

        return mContentView;
    }

}
