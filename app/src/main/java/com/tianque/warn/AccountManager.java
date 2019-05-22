package com.tianque.warn;

import android.content.Context;
import android.text.TextUtils;

import com.tianque.warn.bean.UserInfo;
import com.tianque.warn.common.DataCache;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


/**
 * @author ctrun on 2018/10/19.
 *         用户账户信息管理类
 */
@SuppressWarnings("all")
public class AccountManager {
    private static final String ACCOUNT = "ACCOUNT";
    private static final String USER_RELOGIN_INFO = "USER_RELOGIN_INFO";
    /**上次成功登录时用户输入的用户名 */
    private static final String GLOBAL_LAST_LOGIN_NAME = "GLOBAL_LAST_LOGIN_NAME";

    /**某一街道的所有社区列表 */
    private static final String STREET_COMMUNITIES = "STREET_COMMUNITIES_%s";

    public static boolean isLogin() {
        if (MyApp.sUserInfo == null) {
            return false;
        }

        if (MyApp.sUserInfo.id <= 0) {
            return false;
        }

        if (TextUtils.isEmpty(MyApp.sUserInfo.sid)) {
            return false;
        }

        return true;
    }

    public static void loginOut(Context ctx) {
        removeAccount(ctx);
        MyApp.sUserInfo = new UserInfo();
    }

    public static UserInfo loadAccount(Context ctx) {
        UserInfo data = null;
        File file = new File(ctx.getFilesDir(), ACCOUNT);
        if (file.exists()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(ctx.openFileInput(ACCOUNT));
                data = (UserInfo) ois.readObject();
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (data == null) {
            data = new UserInfo();
        }

        return data;
    }

    /**
     * 保存当前登录账号信息
     */
    public static void saveAccount(Context ctx, UserInfo data) {
        File file = new File(ctx.getFilesDir(), ACCOUNT);
        if (file.exists()) {
            file.delete();
        }

        try {
            ObjectOutputStream oos = new ObjectOutputStream(ctx.openFileOutput(ACCOUNT, Context.MODE_PRIVATE));
            oos.writeObject(data);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除当前登录账号信息
     */
    public static void removeAccount(Context ctx) {
        File dir = ctx.getFilesDir();
        String[] fileNameList = dir.list();
        for (String item : fileNameList) {
            File file = new File(dir, item);
            if (file.exists() && !file.isDirectory()) {
                file.delete();
            }
        }
    }

    public static void saveReloginInfo(Context ctx, String username) {
        DataCache<String> dateCache = new DataCache<>();
        ArrayList<String> listData = dateCache.loadGlobal(ctx, USER_RELOGIN_INFO);
        for (int i = 0; i < listData.size(); ++i) {
            if (listData.get(i).equals(username)) {
                listData.remove(i);
                --i;
            }
        }

        listData.add(username);
        dateCache.saveGlobal(ctx, listData, USER_RELOGIN_INFO);
    }

    public static String loadRelogininfo(Context ctx, String key) {
        ArrayList<String> listData = new DataCache<String>().loadGlobal(ctx, USER_RELOGIN_INFO);
        for (String item : listData) {
            if (item.equals(key)) {
                return item;
            }
        }

        return "";
    }

    public static String[] loadAllRelogininfo(Context ctx) {
        ArrayList<String> listData = new DataCache<String>().loadGlobal(ctx, USER_RELOGIN_INFO);

        String[] s = new String[listData.size()];
        /*listData.toArray(s);*/

        for (int i = 0; i < listData.size(); ++i) {
            s[i] = listData.get(i);
        }

        return s;
    }

    public static void saveLastLoginName(Context context, String name) {
        new DataCache<String>().saveGlobal(context, name, GLOBAL_LAST_LOGIN_NAME);
    }

    public static String loadLastLoginName(Context context) {
        String s = new DataCache<String>().loadGlobalObject(context, GLOBAL_LAST_LOGIN_NAME);
        if (s == null) {
            return "";
        }

        return s;
    }

}
