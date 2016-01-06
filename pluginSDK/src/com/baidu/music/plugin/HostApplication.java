package com.baidu.music.plugin;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;


import com.baidu.music.plugin.clientlog.LogUtil;
import com.baidu.music.plugin.manager.ApplicationManager;
import com.baidu.music.plugin.manager.RemotePluginServiceManager;
import com.baidu.music.plugin.application.BaseApp;

/**
 * Created by Jarlene on 5/21 0021
 */
public class HostApplication extends BaseApp {


    private static HostApplication instance = null;

    /**
     * 单例模式
     *
     * @return
     */
    public static HostApplication getInstance() {
        if (instance == null) {
            synchronized (HostApplication.class) {
                if (instance == null) {
                    instance = new HostApplication();
                }
            }
        }
        return instance;
    }

    private void initPluginSystem() {
        String processName = getProccessName(android.os.Process.myPid());
        LogUtil.v("PluginSDKApplication", processName);
        RemotePluginServiceManager.getInstance().initRemotePluginService(this);
    }

    private void releasePluginSystem() {
        String processName = getProccessName(Process.myPid());
        LogUtil.v("PluginSDKApplication", processName);
        RemotePluginServiceManager.getInstance().releaseRemotePluginService(this);
    }


    private String getProccessName(int pid) {
        try {
            ActivityManager mActivityManager =
                    (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                    .getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getProcessId(String processName) {
        if (TextUtils.isEmpty(processName)) {
            return 0;
        }
        try {
            ActivityManager mActivityManager =
                    (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                    .getRunningAppProcesses()) {
                if (appProcess.processName.equalsIgnoreCase(processName)) {
                    return appProcess.pid;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 退出App时候调用
     */
    public void exitApp() {
        try {
            releasePluginSystem();
            int processId = getProcessId(getProccessName(Process.myPid()) + ":plugin");
            android.os.Process.killProcess(processId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationManager.getInstance().dispatchAppOnCreate();
        initPluginSystem();
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        ApplicationManager.getInstance().dispatchAppOnLowMemory();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ApplicationManager.getInstance().diapatchAppOnConfigurationChanged(newConfig);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        ApplicationManager.getInstance().diapatchAppOnTerminate();
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        ApplicationManager.getInstance().dispatchAppOnTrimMemory(level);
    }
}
