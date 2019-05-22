package com.tianque.warn.common.base;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tianque.warn.R;

import org.greenrobot.eventbus.EventBus;

/**
 * @author ctrun
 */
@SuppressWarnings({"unused", "SimplifiableIfStatement"})
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements OnRefreshListener, BaseView {
    protected P mPresenter;
    private ActivityFragmentViewDelegate mViewDelegate;

    protected LayoutInflater mInflater;
    protected View mContentView;
    protected SmartRefreshLayout mRefreshLayout;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        if(getArguments() != null) {
            handleArguments(getArguments());
        }
        super.onCreate(saveInstanceState);

        mInflater = LayoutInflater.from(getActivity());

        mViewDelegate = new ActivityFragmentViewDelegate(this, mOnClickRetry);

        mPresenter = createPresenter();
        if (mPresenter != null) {
            getLifecycle().addObserver(mPresenter);
        }

        if (isEnableEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        if (isEnableEventBus()) {
            EventBus.getDefault().unregister(this);
        }

        super.onDestroy();
    }

    @Nullable
    @Override
    public Context context() {
        return mViewDelegate.context();
    }

    @Override
    public void showLoading() {
        mViewDelegate.showLoading(mContentView);
    }

    @Override
    public void showEmptyView() {
        showEmptyView(R.string.common_no_data);
    }

    public void showEmptyView(int message) {
        mViewDelegate.showEmptyView(mContentView, message);
    }

    @Override
    public void showErrorView() {
        showErrorView(R.string.common_load_failure);
    }

    public void showErrorView(int message) {
        mViewDelegate.showErrorView(mContentView, message);
    }

    @Override
    public void hideLoading() {
        mViewDelegate.hideLoading();
    }

    @Override
    public void hideEmptyView() {
        mViewDelegate.hideEmptyView();
    }

    @Override
    public void hideErrorView() {
        mViewDelegate.hideErrorView();
    }

    @Override
    public void showWaitingDialog() {
        mViewDelegate.showWaitingDialog();
    }

    @Override
    public void hideWaitingDialog() {
        mViewDelegate.hideWaitingDialog();
    }

    @Override
    public void finishRefresh() {
        mViewDelegate.setRefreshing(mRefreshLayout, false);
    }

    public final void setRefreshing(boolean refreshing) {
        mViewDelegate.setRefreshing(mRefreshLayout, refreshing);
    }

    protected final boolean isRefreshing() {
        return mViewDelegate.isRefreshing(mRefreshLayout);
    }

    protected final boolean isEnableRefresh() {
        return mViewDelegate.isEnableRefresh(mRefreshLayout);
    }

    public void disableRefresh() {
        mViewDelegate.disableRefresh(mRefreshLayout);
    }

    @Override
    public void enableRefresh() {
        mViewDelegate.enableRefresh(mRefreshLayout);
    }

    protected void showDialog(String title, String msg, DialogInterface.OnClickListener clickOk) {
        mViewDelegate.showDialog(title, msg, clickOk, null, null, "确定", "取消", null);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(getLayoutResId(), container, false);

        trySetupRefreshLayout();
        setupViewsInner();

        return mContentView;
    }

    private void setupViewsInner() {
        beforeSetupViews();
        setupViews();
        afterSetupViews();
    }

    private View.OnClickListener mOnClickRetry = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onClickRetry();
        }
    };

    protected void onClickRetry() {

    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) { }

    /**
     * 必须在setupViews方法之前调用，因为setupViews里可能会调用swipeRefreshLayout控件
     */
    public void trySetupRefreshLayout() {
        mRefreshLayout = findViewById(R.id.refresh_layout);
        if (mRefreshLayout != null) {
            //mRefreshLayout.setDragRate(.3f);
            mRefreshLayout.setOnRefreshListener(this);
            disableRefresh();
        }
    }

    public <T extends View> T findViewById(@IdRes int id) {
        if(mContentView == null) {
            return null;
        }

        return mContentView.findViewById(id);
    }

    public boolean isEnableEventBus() {
        return false;
    }

    protected P createPresenter() {
        return null;
    }


    protected void handleArguments(Bundle args) {}
    protected abstract int getLayoutResId();
    protected abstract void setupViews();
    protected void beforeSetupViews() {}
    protected void afterSetupViews() {}
}
