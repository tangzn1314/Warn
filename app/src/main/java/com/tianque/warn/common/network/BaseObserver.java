package com.tianque.warn.common.network;

import android.util.Log;

import com.tianque.warn.AccountManager;
import com.tianque.warn.MyApp;
import com.tianque.warn.common.util.ToastUtils;
import com.tianque.warn.login.LoginActivity;

import io.reactivex.observers.DisposableObserver;


/**
 * @author ctrun 2018/8/9.
 */
@SuppressWarnings("WeakerAccess")
public abstract class BaseObserver<T> extends DisposableObserver<BaseResponse<T>> {
    private static final String TAG = "BaseObserver";

    public BaseObserver() {

    }

    /**
     * 请求成功回调方法
     * @param t 成功返回的实体对象
     */
    protected abstract void onSuccess(T t);

    protected void onFailure(String errMsg) {
    }

    @Override
    public void onNext(BaseResponse<T> response) {
        //todo 后期可能优化(通过自定义GsonResponseBodyConverter进行帐号失效验证)
        if (response.isSessionExpired()) {
            Log.i(TAG, "账号失效或在别处登陆");

            ToastUtils.showMiddleToast("账号失效，请重新登录");
            AccountManager.loginOut(MyApp.getApplication());
            LoginActivity.start(MyApp.getApplication());
            return;
        }

        if (response.isSuccess()) {
            onSuccess(response.data);
        } else {
            onFailure(response.getErrorMessage());
        }
    }

    @Override
    public void onError(Throwable t) {
        Log.e(TAG, "", t);

        onFailure(ApiException.ERROR_MSG_CONNECT_FAIL);
    }

    @Override
    public void onComplete() {
    }

}
