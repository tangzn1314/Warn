package com.tianque.warn.common.base;

import android.content.Context;
import android.support.annotation.Nullable;

/**
 * @author ctrun
 */

@SuppressWarnings("unused")
public interface BaseView {

    /**
     * 获取当前Viewer的Context对象,一般为Activity的Context对象
     */
    @Nullable
    Context context();

    void showLoading();

    void hideLoading();

    void showEmptyView();

    void showErrorView();

    void hideEmptyView();

    void hideErrorView();

    void showWaitingDialog();

    void hideWaitingDialog();

    void finishRefresh();

    void enableRefresh();

}
