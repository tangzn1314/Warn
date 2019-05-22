package com.tianque.warn.common.util;

import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tianque.warn.R;

/**
 * @author ctrun on 15/8/13.
 */
@SuppressWarnings("all")
public class ToastUtils {

    private static Context sContext;
    private static Toast sToast;
    private static int sBottomOffset = 0;

    private ToastUtils() {
    }

    /**
     * 请在Application的onCreate()方法中调用该方法进行初始化工作
     */
    public static void init(Application application) {
        sContext = application;
        sBottomOffset = sContext.getResources().getDimensionPixelOffset(R.dimen.common_toast_y);
        sToast = new Toast(sContext);
    }

    public static void showBottomToast(String msg) {
        makeText(msg);

        sToast.setGravity(Gravity.BOTTOM, 0, sBottomOffset);
        sToast.show();
    }

    public static void showBottomToast(int messageId) {
        makeText(messageId);

        sToast.setGravity(Gravity.BOTTOM, 0, sBottomOffset);
        sToast.show();
    }

    public static void showMiddleToast(int resId) {
        makeText(resId);

        sToast.setGravity(Gravity.CENTER, 0, 0);
        sToast.show();
    }

    public static void showMiddleToast(String msg) {
        makeText(msg);

        sToast.setGravity(Gravity.CENTER, 0, 0);
        sToast.show();
    }

    private static void makeText(CharSequence text) {
        LayoutInflater inflate = LayoutInflater.from(sContext);
        View v = inflate.inflate(R.layout.common_toast_view, null);
        TextView tv = v.findViewById(R.id.tv_message);
        tv.setText(text);

        sToast.setView(v);
    }

    private static void makeText(int resId) {
        CharSequence text = sContext.getResources().getText(resId);
        makeText(text);
    }

}
