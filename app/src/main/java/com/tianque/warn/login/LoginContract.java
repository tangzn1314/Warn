package com.tianque.warn.login;

import com.tianque.warn.bean.UserInfo;
import com.tianque.warn.common.base.BaseView;

/**
 * @author ctrun on 2019/3/24.
 */

public interface LoginContract {

    interface ILoginView extends BaseView {

        void onLoginSuccess(UserInfo userInfo);

        void onLoginFailure();

    }

    interface ILoginPresenter {

        void login(String userName, String password);

        void login();

    }

}
