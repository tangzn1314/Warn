package com.tianque.warn.common.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.tianque.warn.BuildConfig;

import java.io.File;

/**
 * @author ctrun on 2017/5/11.
 * 兼容android 7.0 工具类
 */
@SuppressWarnings("WeakerAccess")
public final class NUtils {

    private static boolean hasN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static String getFileProviderName() {
        return BuildConfig.APPLICATION_ID.concat(".fileprovider");
    }

    public static Uri getUriForFile(Context context, File file) {
        if(!hasN()) {
            return Uri.fromFile(file);
        }

        String authority = NUtils.getFileProviderName();
        return FileProvider.getUriForFile(context, authority, file);
    }

    public static void setIntentDataAndType(Context context, Intent intent, String type, File file, boolean writable) {
        if(hasN()) {
            intent.setDataAndType(NUtils.getUriForFile(context, file), type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if(writable) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type);
        }
    }
}
