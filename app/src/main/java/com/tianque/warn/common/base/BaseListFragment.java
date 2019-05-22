package com.tianque.warn.common.base;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tianque.warn.R;

/**
 * @author ctrun on 2018/8/10.
 */

public abstract class BaseListFragment<P extends BaseListPresenter> extends BaseFragment<P> implements
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener, BaseListView {
    protected RecyclerView mRecyclerView;
    protected BaseQuickAdapter mAdapter;

    @Override
    public BaseQuickAdapter getListAdapter() {
        return mAdapter;
    }

    @Override
    protected void beforeSetupViews() {
        mRecyclerView = findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = initAdapter();
        if (mAdapter != null) {
            mAdapter.setOnItemClickListener(this);
            mAdapter.setOnItemChildClickListener(this);
            mAdapter.bindToRecyclerView(mRecyclerView);
            mAdapter.setLoadMoreView(new BaseListActivity.SimpleLoadMoreView());
            mAdapter.setEnableLoadMore(isEnableLoadMore());
            if (isEnableLoadMore()) {
                mAdapter.setOnLoadMoreListener(() -> {
                    if(isRefreshing()) {
                        mAdapter.loadMoreComplete();
                        return;
                    }

                    loadMore();
                }, mRecyclerView);
            }
        }
    }

    @Override
    protected void onClickRetry() {
        showLoading();
        onRefresh(mRefreshLayout);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        mPresenter.initPage();
        loadMore();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    /**
     * 是否支持加载更多
     * 默认支持
     */
    protected boolean isEnableLoadMore() {
        return true;
    }

    /**
     * 是否支持空布局
     * 默认支持
     */
    @Override
    public boolean isEnableEmptyView() {
        return true;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.common_list_view;
    }

    protected abstract BaseQuickAdapter initAdapter();
    protected abstract void loadMore();
}
