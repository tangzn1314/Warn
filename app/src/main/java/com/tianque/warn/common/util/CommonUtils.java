package com.tianque.warn.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ctrun
 */
@SuppressWarnings("all")
public class CommonUtils {
    private static final String TAG = "CommonUtils";

    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }

        return false;
    }

    public static String m(double f) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(f);
    }

    public static boolean isPhone(String s) {
        if (s == null || "".equals(s)) {
            return false;
        }

        String regExp = "^(1)\\d{10}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(s);
        return m.find();
    }

    public static boolean isEmail(String s) {
        String regExp = "^.+@.+$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(s);
        return m.find();
    }

    public static String countToString(long count) {
        if (count == 0) {
            return "";
        } else if (count > 99) {
            return "99+";
        } else {
            return String.valueOf(count);
        }
    }

    public static String encodeUtf8(String s) {
        try {
            return URLEncoder.encode(s, "utf-8");
        } catch (Exception e) {
            return "";
        }
    }

    public static String decodeUtf8(String s) {
        try {
            return URLDecoder.decode(s, "utf-8");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 跳转至拨号界面
     */
    public static void callDial(Context context, String phoneNumber) {
        try {
            context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
        } catch (Exception e) {
            Log.e(TAG, "调用拨号功能异常", e);
        }
    }

    public static void showAppDetailInfo(Activity activity, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        activity.startActivityForResult(intent, requestCode);
    }

}
