package com.tianque.warn;

import com.tianque.warn.common.base.BaseActivity;
import com.tianque.warn.login.LoginActivity;

/**
 * @author ctrun  on 2018/10/19.
 *         入口页面
 */
public class EntranceActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.app_activity_entrance;
    }

    @Override
    protected void setupViews() {

        findViewById(R.id.iv_image).postDelayed(() -> login(), 300);

    }

    private void login() {
        WarnActivity.start(this);
        /*if (AccountManager.isLogin()) {
            WarnActivity.start(this);
        } else {
            LoginActivity.start(this, true);
        }*/

        finish();
    }

}

