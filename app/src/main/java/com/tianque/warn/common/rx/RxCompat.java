package com.tianque.warn.common.rx;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ctrun on 2019/3/21.
 */

public final class RxCompat {

    private RxCompat() {
    }

    /**
     * 统一线程转换处理
     */
    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return new ObservableTransformer<T, T>() {

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io());
            }
        };
    }

    public static <T> ObservableTransformer<T, T> doOnCompletedOrError(final RxFunc0 doOnCompletedOrErrorFunc) {
        return new ObservableTransformer<T, T>() {

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        doOnCompletedOrErrorFunc.call();
                    }
                }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        doOnCompletedOrErrorFunc.call();
                    }
                });
            }
        };
    }


    public static <T> ObservableTransformer<T, T> doOnNextOrError(final RxFunc0 doOnNextOrErrorFunc) {
        return new ObservableTransformer<T, T>() {

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.doOnNext(new Consumer<T>() {

                    @Override
                    public void accept(T t) throws Exception {
                        doOnNextOrErrorFunc.call();
                    }
                })
                        .doOnError(new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                doOnNextOrErrorFunc.call();
                            }
                        });
            }
        };

    }

    public static <T> SingleTransformer<T, T> doOnSuccessOrError(final RxFunc0 doOnSuccessOrErrorFunc) {
        return new SingleTransformer<T, T>() {
            @Override
            public SingleSource<T> apply(Single<T> upstream) {
                return upstream.doOnSuccess(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {
                        doOnSuccessOrErrorFunc.call();
                    }
                })
                        .doOnError(new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                doOnSuccessOrErrorFunc.call();
                            }
                        });
            }
        };
    }

}
