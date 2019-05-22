package com.tianque.warn.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tianque.warn.AccountManager;
import com.tianque.warn.MyApp;
import com.tianque.warn.R;
import com.tianque.warn.bean.UserInfo;
import com.tianque.warn.common.SimpleTextWatcher;
import com.tianque.warn.common.base.BaseActivity;
import com.tianque.warn.common.util.ToastUtils;
import com.tianque.warn.common.util.UIUtils;
import com.tianque.warn.common.widget.LoginAutoCompleteEdit;


/**
 * @author ctrun
 * 登录界面
 */
@SuppressWarnings("all")
public class LoginActivity extends BaseActivity<LoginPresenter> implements View.OnClickListener, LoginContract.ILoginView {

    /**
     * @param context
     * @param clearTask true 先关闭当前所有Activity
     */
    public static void start(Context context, boolean clearTask) {
        Intent intent = new Intent(context, LoginActivity.class);
        if (!clearTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void start(Context context) {
        LoginActivity.start(context, false);
    }

    private LoginAutoCompleteEdit mEtAccount;
    private EditText mEtPassword;
    private Button mBtnLogin;

    TextWatcher mTextWatcher = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            updateLoginButton();
        }
    };

    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.login_activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            boolean needHideSoftKeyboard;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        needHideSoftKeyboard = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(needHideSoftKeyboard) {
                            needHideSoftKeyboard = false;
                            UIUtils.popSoftkeyboard(getApplicationContext(), mEtAccount, false);
                        }
                        break;
                    default:
                        break;
                }

                return false;
            }
        });

    }

    @Override
    protected void setupViews() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mEtAccount = findViewById(R.id.et_account);
        mEtPassword = findViewById(R.id.et_password);
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);

        mEtAccount.addTextChangedListener(mTextWatcher);
        mEtPassword.addTextChangedListener(mTextWatcher);
        updateLoginButton();

        String lastLoginName = AccountManager.loadLastLoginName(getApplicationContext());
        if (!lastLoginName.isEmpty()) {
            mEtAccount.setText(lastLoginName);
            mEtAccount.setDisableAuto(false);
            mEtPassword.requestFocus();
        }

        mEtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean actionLogin = actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
                if (actionLogin) {
                    onClick(mBtnLogin);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String account = mEtAccount.getText().toString().trim();
                String password = mEtPassword.getText().toString().trim();

                if (TextUtils.isEmpty(account)) {
                    ToastUtils.showMiddleToast(R.string.login_toast_empty_account);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    ToastUtils.showMiddleToast(R.string.login_toast_empty_password);
                    return;
                }

                if (password.length() < 5) {
                    ToastUtils.showMiddleToast(R.string.login_toast_password_length_not_enough);
                    return;
                }

                mPresenter.login(account, password);
                break;
            default:
                break;
        }

    }

    private void updateLoginButton() {
        if (mEtAccount.getText().length() == 0) {
            mBtnLogin.setEnabled(false);
            return;
        }

        if (mEtPassword.getText().length() == 0) {
            mBtnLogin.setEnabled(false);
            return;
        }

        mBtnLogin.setEnabled(true);
    }

    @Override
    public void onLoginSuccess(UserInfo userInfo) {
        AccountManager.saveAccount(this, userInfo);
        MyApp.sUserInfo = userInfo;

        String account = mEtAccount.getText().toString().trim();
        AccountManager.saveReloginInfo(getApplicationContext(), account);
        AccountManager.saveLastLoginName(getApplicationContext(), account);

        //WarnActivity.start(this);
        finish();
    }

    @Override
    public void onLoginFailure() {

    }

}
