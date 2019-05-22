package com.tianque.warn.common.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tianque.warn.R;

/**
 * @author ctrun on 2018/10/9.
 * 系统动态权限申请检查工具类
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class PermissionUtils {
    private static final String TAG = "PermissionUtils";
    public static final int SETTINGS_REQ_CODE = 17071;

    /**
     * 检查手机状态权限
     */
    public static boolean checkPhoneState(FragmentActivity activity) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        return rxPermissions.isGranted(Manifest.permission.READ_PHONE_STATE);
    }

    /**
     * 检查手机存储权限
     */
    public static boolean checkExternalStorage(FragmentActivity activity) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        return rxPermissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE) && rxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 检查相机权限
     */
    public static boolean checkCamera(FragmentActivity activity) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        return rxPermissions.isGranted(Manifest.permission.CAMERA);
    }

    /**
     * 检查定位权限
     */
    public static boolean checkLocation(FragmentActivity activity) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        return rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION) && rxPermissions.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION);

    }

    /**
     * 检查录音权限
     */
    public static boolean checkRecordAudio(FragmentActivity activity) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        return rxPermissions.isGranted(Manifest.permission.RECORD_AUDIO);
    }

    /**
     * 检查VoIP视频通话权限
     */
    public static boolean checkVoIPCall(FragmentActivity activity) {
        return checkRecordAudio(activity) && checkCamera(activity) && checkPhoneState(activity);

    }

    /**
     * 检查选择图片权限
     */
    public static boolean checkPhotoPick(FragmentActivity activity) {
        return checkExternalStorage(activity) && checkCamera(activity);

    }

    /**
     * 申请一些常用权限
     */
    public static void requestCommon(FragmentActivity activity, OnRequestPermissionListener listener) {
        final String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
        };

        requestPermissions(activity, permissions, listener);
    }

    /**
     * 申请VoIP视频通话权限
     */
    public static void requestVoIPCall(FragmentActivity activity, OnRequestPermissionListener listener) {
        final String[] permissions = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE
        };

        requestPermissions(activity, permissions, listener);
    }

    private static void requestPermissions(Fragment fragment, String[] permissions, OnRequestPermissionListener listener) {
        requestPermissions(fragment.getActivity(), permissions, listener);
    }

    @SuppressLint("CheckResult")
    private static void requestPermissions(FragmentActivity activity, String[] permissions, final OnRequestPermissionListener listener) {
        if (activity == null) {
            return;
        }

        if (permissions == null || permissions.length == 0) {
            return;
        }

        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.setLogging(true);
        rxPermissions
                .requestEachCombined(permissions)
                .subscribe(permission -> {
                    Log.i(TAG, "Permission result " + permission);

                    //全部同意后调用
                    if (permission.granted) {
                        if (listener != null) {
                            listener.onGranted();
                        }
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        //只要有一个选择:拒绝,但没有选择"以后不再询问",以后申请权限,会继续弹出提示
                        if (listener != null) {
                            listener.onShowRational();
                        }
                    } else {
                        //只要有一个选择:拒绝,但选择"以后不再询问",以后申请权限,不会继续弹出提示
                        if (listener != null) {
                            listener.onDenied();
                        }
                    }
                });
    }

    public interface OnRequestPermissionListener {
        /**
         * 当全部权限的申请被用户允许之后,该方法会被调用
         */
        void onGranted();

        /**
         * 禁止，但没有选择“不再询问”，以后申请权限，不会继续弹出提示,
         * 该方法将会被调用.
         */
        void onShowRational();

        /**
         * 禁止，但选择“不再询问”，以后申请权限，不会继续弹出提示,
         * 该方法将会被调用
         */
        void onDenied();
    }

    public static void showAppSettingsDialog(Activity activity, int resId) {
        String message = activity.getString(resId);
        showAppSettingsDialog(activity, message);
    }

    public static void showAppSettingsDialog(Activity activity, String message) {
        showAppSettingsDialog(activity, message, null, null);
    }

    @SuppressWarnings("SameParameterValue")
    public static void showAppSettingsDialog(Activity activity, int resId, DialogInterface.OnClickListener clickSetting, DialogInterface.OnClickListener clickCancel) {
        String message = activity.getString(resId);
        showAppSettingsDialog(activity, message, clickSetting, clickCancel);
    }

    public static void showAppSettingsDialog(final Activity activity, String message, final DialogInterface.OnClickListener clickSetting, final DialogInterface.OnClickListener clickCancel) {
        String tempMessage = activity.getString(R.string.common_permission_no_granted, message);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setMessage(tempMessage)
                .setPositiveButton("去设置", (dialog, which) -> {
                    dialog.dismiss();
                    CommonUtils.showAppDetailInfo(activity, SETTINGS_REQ_CODE);

                    if(clickSetting != null) {
                        clickSetting.onClick(dialog, which);
                    }
                })
                .setNegativeButton("暂不", (dialog, which) -> {
                    dialog.dismiss();

                    if(clickCancel != null) {
                        clickCancel.onClick(dialog, which);
                    }
                });
        builder.show();
    }

}
