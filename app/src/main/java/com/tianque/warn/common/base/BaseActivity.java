package com.tianque.warn.common.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tianque.warn.R;

import org.greenrobot.eventbus.EventBus;

/**
 * @author ctrun
 */
@SuppressWarnings({"unused", "SameParameterValue"})
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements OnRefreshListener, BaseView {
    protected P mPresenter;
    private ActivityFragmentViewDelegate mViewDelegate;

    protected LayoutInflater mInflater;
    private View mContentView;
    private Toolbar mActionBarToolbar;
    protected SmartRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            handleIntent(getIntent());
        }

        mInflater = getLayoutInflater();

        mContentView = mInflater.inflate(getLayoutResId(), null);
        setContentView(mContentView);
        getActionBarToolbar();
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mViewDelegate = new ActivityFragmentViewDelegate(this, mOnClickRetry);

        mPresenter = createPresenter();
        //注意：必须在onCreate方法中调用addObserver方法
        if (mPresenter != null) {
            getLifecycle().addObserver(mPresenter);
        }

        trySetupRefreshLayout();
        setupViewsInner();

        if (isEnableEventBus()) {
            EventBus.getDefault().register(this);
        }

    }

    private void setupViewsInner() {
        beforeSetupViews();
        setupViews();
        afterSetupViews();
    }

    @Override
    protected void onDestroy() {
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

    public final void showEmptyView(int message) {
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
    public final void finishRefresh() {
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

    public final void disableRefresh() {
        mViewDelegate.disableRefresh(mRefreshLayout);
    }

    @Override
    public final void enableRefresh() {
        mViewDelegate.enableRefresh(mRefreshLayout);
    }

    protected void showDialog(String title, String msg, DialogInterface.OnClickListener clickOk) {
        mViewDelegate.showDialog(title, msg, clickOk, null, null, "确定", "取消", null);
    }

    protected void showDialog(String title, String msg,
                              DialogInterface.OnClickListener clickOk,
                              DialogInterface.OnClickListener clickCancel) {
        mViewDelegate.showDialog(title, msg, clickOk, clickCancel, null, "确定", "取消", null);
    }

    protected void showDialog(String title, String msg,
                              DialogInterface.OnClickListener clickOk,
                              DialogInterface.OnClickListener clickCancel,
                              String okButton,
                              String cancelButton) {
        mViewDelegate.showDialog(title, msg, clickOk, clickCancel, null, okButton, cancelButton, null);
    }

    protected void showDialog(String title, String msg,
                              DialogInterface.OnClickListener clickOk,
                              DialogInterface.OnClickListener clickCancel,
                              DialogInterface.OnClickListener clickNeutral,
                              String okButton,
                              String cancelButton,
                              String neutralButton) {
        mViewDelegate.showDialog(title, msg, clickOk, clickCancel, clickNeutral, okButton, cancelButton, neutralButton);
    }

    public void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    public void setActionBarTitle(int title) {
        String titleString = getString(title);
        setActionBarTitle(titleString);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar processedItems
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private View.OnClickListener mOnClickRetry = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onClickRetry();
        }
    };

    /**
     * 错误提示页面点击重试按钮该方法会被调用
     */
    protected void onClickRetry() {

    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
    }

    /**
     * 必须在initViews方法之前调用，因为initViews里可能会用到RefreshLayout控件
     */
    private void trySetupRefreshLayout() {
        mRefreshLayout = findViewById(R.id.refresh_layout);
        if (mRefreshLayout != null) {
            //mRefreshLayout.setDragRate(.3f);
            mRefreshLayout.setOnRefreshListener(this);
            disableRefresh();
        }
    }

    public Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = findViewById(R.id.toolbar_actionbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }

        }

        return mActionBarToolbar;
    }

    public boolean isEnableEventBus() {
        return false;
    }

    protected P createPresenter() {
        return null;
    }

    protected void handleIntent(Intent intent) {}
    protected abstract int getLayoutResId();
    protected abstract void setupViews();
    protected void beforeSetupViews() {}
    protected void afterSetupViews() {}
}
