package com.tianque.warn.common.base;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tianque.warn.R;

/**
 * @author ctrun on 2019/3/22.
 *
 */
@SuppressWarnings({"WeakerAccess", "SimplifiableIfStatement"})
public class ActivityFragmentViewDelegate extends ViewDelegateDefault {

    private View.OnClickListener mOnClickRetry;
    private View mLoadingView;
    private View mEmptyView;
    private View mErrorView;

    public ActivityFragmentViewDelegate(@NonNull FragmentActivity activity, View.OnClickListener onClickRetry) {
        super(activity);
        activity.getLifecycle().addObserver(this);
        mOnClickRetry = onClickRetry;
    }

    public ActivityFragmentViewDelegate(@NonNull Fragment fragment, View.OnClickListener onClickRetry) {
        super(fragment.getActivity());
        fragment.getLifecycle().addObserver(this);
        mOnClickRetry = onClickRetry;
    }

    /**
     * 显示loadingView，同时会把emptyView和errorView隐藏
     */
    public void showLoading(View contentView) {
        if (!checkView()) {
            return;
        }

        if (contentView == null) {
            return;
        }

        if (mLoadingView == null) {
            mLoadingView = contentView.findViewById(R.id.pb_loading);
        }

        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.VISIBLE);
        }

        hideEmptyView();
        hideErrorView();
    }

    public void showEmptyView(View contentView, int message) {
        if (!checkView()) {
            return;
        }

        if (contentView == null) {
            return;
        }

        if (mEmptyView == null) {
            final ViewStub viewStub = contentView.findViewById(R.id.vs_empty_view);
            if (viewStub != null) {
                mEmptyView = viewStub.inflate();
            }
        }

        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);

            final TextView tvMessage = mEmptyView.findViewById(R.id.tv_message);
            if (tvMessage != null) {
                tvMessage.setText(message);
            }
        }

    }

    public void showErrorView(View contentView, int message) {
        if (!checkView()) {
            return;
        }

        if (contentView == null) {
            return;
        }

        if (mErrorView == null) {
            final ViewStub viewStub = contentView.findViewById(R.id.vs_error_view);
            if (viewStub != null) {
                mErrorView = viewStub.inflate();

                final View btnRetry = mErrorView.findViewById(R.id.btn_retry);
                if (btnRetry != null) {
                    btnRetry.setOnClickListener(mOnClickRetry);
                }
            }
        }

        if (mErrorView != null) {
            mErrorView.setVisibility(View.VISIBLE);

            final TextView tvMessage = mErrorView.findViewById(R.id.tv_message);
            if (tvMessage != null) {
                tvMessage.setText(message);
            }
        }

    }

    /**
     * 将loadingView进行隐藏。
     */
    public void hideLoading() {
        if (!checkView()) {
            return;
        }

        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
    }

    /**
     * 将emptyView进行隐藏。
     */
    public void hideEmptyView() {
        if (!checkView()) {
            return;
        }

        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    /**
     * 将errorView进行隐藏。
     */
    public void hideErrorView() {
        if (!checkView()) {
            return;
        }

        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }
    }

    public void setRefreshing(SmartRefreshLayout refreshLayout, boolean refreshing) {
        if (refreshLayout != null) {
            if(refreshing) {
                refreshLayout.autoRefresh();
            } else {
                refreshLayout.finishRefresh();
            }
        }
    }

    public boolean isRefreshing(SmartRefreshLayout refreshLayout) {
        if (refreshLayout != null) {
            return refreshLayout.isRefreshing();
        }

        return false;
    }

    public boolean isEnableRefresh(SmartRefreshLayout refreshLayout) {
        if (refreshLayout != null) {
            return refreshLayout.isEnableRefresh();
        }

        return false;
    }

    public void disableRefresh(SmartRefreshLayout refreshLayout) {
        if (refreshLayout != null) {
            refreshLayout.setEnableRefresh(false);
        }
    }

    public void enableRefresh(SmartRefreshLayout refreshLayout) {
        if (refreshLayout != null) {
            refreshLayout.setEnableRefresh(true);
        }
    }

    public void showDialog(String title, String msg,
                           DialogInterface.OnClickListener clickOk,
                           DialogInterface.OnClickListener clickCancel,
                           DialogInterface.OnClickListener clickNeutral,
                           String okButton,
                           String cancelButton,
                           String neutralButton) {
        if (!checkView()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mContextRef.get());
        builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton(okButton, clickOk)
                .setNegativeButton(cancelButton, clickCancel);
        if (clickNeutral != null && !neutralButton.isEmpty()) {
            builder.setNeutralButton(neutralButton, clickNeutral);
        }
        builder.show();
    }

}
