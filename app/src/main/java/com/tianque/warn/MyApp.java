package com.tianque.warn;

import android.app.ActivityManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tianque.warn.bean.UserInfo;
import com.tianque.warn.common.util.ToastUtils;
import com.tianque.warn.common.widget.CustomRefreshHeader;

import java.util.List;

/**
 * @author ctrun
 */
@SuppressWarnings("all")
public class MyApp extends MultiDexApplication {
    public static UserInfo sUserInfo;
    private static MyApp sApplication;
    public static boolean sMainCreate = false;

    public static MyApp getApplication() {
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        sUserInfo = AccountManager.loadAccount(this);

        init();
    }

    private void init() {
        ToastUtils.init(this);
        initRefreshLayout();

        if (isMainProcess()) {
        }
    }

    /**
     * 初始化下拉刷新控件
     */
    private void initRefreshLayout() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                //禁止越界回弹
                layout.setEnableOverScrollBounce(false);
                //禁止越界拖动
                layout.setEnableOverScrollDrag(false);
                //禁止上拉加载更多
                layout.setEnableLoadMore(false);
                return new CustomRefreshHeader(context);
            }
        });

    }

    public static void setMainActivityState(boolean create) {
        sMainCreate = create;
    }

    /**
     * 判断是否是主进程
     * @return
     */
    public boolean isMainProcess() {
        return this.getPackageName().equals(getProcessName(this));
    }

    private static String getProcessName(Context context) {
        ActivityManager actMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appList = actMgr.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : appList) {
            if (info.pid == android.os.Process.myPid()) {
                return info.processName;
            }
        }
        return "";
    }
}
