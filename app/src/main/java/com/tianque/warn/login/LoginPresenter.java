package com.tianque.warn.login;

import android.text.TextUtils;

import com.tianque.warn.common.base.BasePresenter;
import com.tianque.warn.common.network.ApiException;
import com.tianque.warn.common.network.LoginResponse;
import com.tianque.warn.common.network.RetrofitManager;
import com.tianque.warn.common.rx.RxCompat;
import com.tianque.warn.common.util.ToastUtils;

import java.lang.reflect.Method;

import io.reactivex.observers.DisposableObserver;

/**
 * @author ctrun on 2018/12/25.
 */
@SuppressWarnings("WeakerAccess")
public class LoginPresenter extends BasePresenter<LoginContract.ILoginView> implements
        LoginContract.ILoginPresenter {

    private String mUserCenterSid = "";

    public LoginPresenter(LoginContract.ILoginView view) {
        super(view);
    }

    /**
     * 先获取用户中心sid，再通过用户中心sid换取雪亮token
     */
    @Override
    public void login(String userName, String password) {
        mView.showWaitingDialog();

        addDisposable(RetrofitManager.createUserCenterLoginApiService(
                sid -> mUserCenterSid = sid)
                .userCenterLogin(userName, password)
                .doOnNext(result -> {
                    if (mView != null) {
                        boolean success = Boolean.parseBoolean(result.toString());
                        if (success) {
                            return;
                        }

                        mView.hideWaitingDialog();
                        ToastUtils.showMiddleToast("登录失败，请稍候重试");
                    }
                })
                .filter(result -> {
                    boolean success = Boolean.parseBoolean(result.toString());
                    if (success) {
                        return true;
                    }

                    return false;
                })
                .flatMap(result -> RetrofitManager.getApiService().login(mUserCenterSid))
                .compose(RxCompat.applySchedulers())
                .compose(RxCompat.doOnNextOrError(() -> {
                    if (mView != null) {
                        mView.hideWaitingDialog();
                    }
                }))
                .subscribeWith(new DisposableObserver<LoginResponse>() {

                    @Override
                    public void onNext(LoginResponse response) {
                        if (mView == null) {
                            return;
                        }

                        if (response.isSuccess()) {
                            response.user.sid = response.token;
                            mView.onLoginSuccess(response.user);
                        } else {
                            String errMsg = response.getErrorMessage();
                            if (TextUtils.isEmpty(errMsg)) {
                                errMsg = "登录失败，请稍候重试";
                            }
                            ToastUtils.showMiddleToast(errMsg);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (mView != null) {
                            ToastUtils.showMiddleToast(ApiException.ERROR_MSG_CONNECT_FAIL);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                })

        );
    }

    @Override
    public void login() {
        String sid = getUserCenterSessionId();
        addDisposable(RetrofitManager.getApiService()
                .login(sid)
                .compose(RxCompat.applySchedulers())
                .subscribeWith(new DisposableObserver<LoginResponse>() {

                    @Override
                    public void onNext(LoginResponse response) {
                        if (mView == null) {
                            return;
                        }

                        if (response.isSuccess()) {
                            response.user.sid = response.token;
                            mView.onLoginSuccess(response.user);
                        } else {
                            mView.onLoginFailure();
                        }

                    }

                    @Override
                    public void onError(Throwable t) {
                        if (mView != null) {
                            mView.onLoginFailure();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    /**
     * 通过反射从平安通获取用户中心sid
     */
    public static String getUserCenterSessionId() {
        String sid = "";
        try {
            String className = "com.tianque.mobile.library.interceptor.GateWayRequestInterceptor";
            Object obj = Class.forName(className).newInstance();
            Method getSidMethod = obj.getClass().getDeclaredMethod("getSid");
            getSidMethod.setAccessible(true);
            sid = (String) getSidMethod.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sid;
    }

}
