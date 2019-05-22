package com.tianque.warn.common.base;

import android.content.Context;

import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * @author ctrun on 2019/3/22.
 */
@SuppressWarnings("WeakerAccess")
public class ViewDelegateDefault extends BaseViewDelegate {
    private KProgressHUD mWaitingDialog;

    public ViewDelegateDefault(Context context) {
        super(context);
        initWaitingDialog();
    }

    private void initWaitingDialog() {
        if (!checkView()) {
            return;
        }

        mWaitingDialog = KProgressHUD.create(mContextRef.get())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setSize(60, 60);
    }

    @Override
    public void showWaitingDialog() {
        if (mWaitingDialog == null) {
            return;
        }

        mWaitingDialog.show();
    }

    @Override
    public void hideWaitingDialog() {
        if (mWaitingDialog == null) {
            return;
        }

        mWaitingDialog.dismiss();
    }

    @Override
    public void onDestroy() {
        if (mWaitingDialog != null) {
            mWaitingDialog.dismiss();
            mWaitingDialog = null;
        }

        super.onDestroy();
    }
}
