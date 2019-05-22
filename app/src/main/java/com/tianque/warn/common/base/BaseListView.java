package com.tianque.warn.common.base;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * @author ctrun on 2018/09/15
 */
public interface BaseListView extends BaseView {

    BaseQuickAdapter getListAdapter();

    /**
     * 是否支持空布局
     */
    boolean isEnableEmptyView();

}
