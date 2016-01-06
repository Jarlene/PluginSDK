package com.baidu.music.plugin.application;

import android.app.Application;
import android.content.Context;
import com.baidu.music.plugin.messenger.IServiceManager;
import com.baidu.music.plugin.messenger.ServiceProvider;

/**
 * Created by Jarlene on 5/21 0021.
 */
public class BaseApp extends Application {

    private static Context mAppContext;

    public BaseApp() {
    }

    public void onCreate() {
        super.onCreate();
        mAppContext = this;
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public Object getSystemService(String name) {
        IServiceManager manager = ServiceProvider.getServiceProvider().getServiceManager();
        if (manager != null) {
            Object service = manager.getService(name);
            if (service != null) {
                return service;
            }
        }

        return super.getSystemService(name);
    }
}
