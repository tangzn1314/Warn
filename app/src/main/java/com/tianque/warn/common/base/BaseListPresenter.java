package com.tianque.warn.common.base;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tianque.warn.bean.PageEntity;
import com.tianque.warn.common.network.BaseObserver;
import com.tianque.warn.common.network.BaseResponse;
import com.tianque.warn.common.rx.RxCompat;
import com.tianque.warn.common.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author ctrun 2017/11/15.
 */
@SuppressWarnings("WeakerAccess")
public abstract class BaseListPresenter<T, V extends BaseListView> extends BasePresenter<V> {

    private int mOldPage = 1;
    /**
     * 当前页码
     */
    protected int mPage = 1;
    /**
     * 默认每页加载10条数据
     */
    protected int mPageSize = 10;

    public BaseListPresenter(V view) {
        super(view);
    }

    @SuppressWarnings("unchecked")
    public void loadNextPageData(Object... params) {
        addDisposable(
                initPageObservable(params)
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(RxCompat.doOnNextOrError(() -> {
                            if (mView != null) {
                                mView.finishRefresh();
                                mView.hideLoading();
                            }
                        }))
                        .subscribeWith(new BaseObserver<PageEntity<T>>() {

                                           @Override
                                           protected void onSuccess(PageEntity<T> pageEntity) {
                                               if (mView == null) {
                                                   return;
                                               }

                                               mView.enableRefresh();

                                               List<T> data = pageEntity.result == null ? new ArrayList<T>() : pageEntity.result;

                                               BaseQuickAdapter adapter = mView.getListAdapter();
                                               if (adapter != null) {
                                                   boolean hasMore = data.size() >= mPageSize;
                                                   boolean updateAll = mPage == 1;

                                                   if (updateAll) {
                                                       adapter.setNewData(data);
                                                   } else {
                                                       adapter.addData(data);
                                                   }

                                                   if (hasMore) {
                                                       adapter.loadMoreComplete();
                                                   } else {
                                                       adapter.loadMoreEnd(true);
                                                   }

                                                   boolean needShowEmptyView = mView.isEnableEmptyView() && adapter.getData().isEmpty();
                                                   if (needShowEmptyView) {
                                                       mView.showEmptyView();
                                                   }

                                               }

                                               mPage++;
                                               mOldPage = mPage;
                                           }

                                           @Override
                                           protected void onFailure(String errMsg) {
                                               if (mView != null) {
                                                   if (!TextUtils.isEmpty(errMsg)) {
                                                       ToastUtils.showBottomToast(errMsg);
                                                   }

                                                   BaseQuickAdapter adapter = mView.getListAdapter();
                                                   if (adapter != null) {

                                                       if (!adapter.getData().isEmpty()) {
                                                           adapter.loadMoreFail();
                                                       }

                                                       boolean needShowErrorView = mView.isEnableEmptyView() && adapter.getData().isEmpty();
                                                       if (needShowErrorView) {
                                                           mView.showErrorView();
                                                       }

                                                   }
                                               }

                                               mPage = mOldPage;
                                           }
                                       }
                        )
        );
    }

    public void initPage() {
        mOldPage = mPage;
        mPage = 1;
    }

    protected abstract Observable<BaseResponse<PageEntity<T>>> initPageObservable(Object... params);
}
