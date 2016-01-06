package com.baidu.music.plugin.manager;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.res.Configuration;
import android.os.Build;
import android.text.TextUtils;
import com.baidu.music.plugin.HostApplication;

import java.util.Iterator;

/**
 * Created by Jarlene on 5/21 0021.
 */
public class ApplicationManager extends BaseManager<String, Application> {


    private static ApplicationManager instance = null;

    /**
     * 单例模式
     *
     * @return
     */
    public static ApplicationManager getInstance() {
        if (instance == null) {
            synchronized (ApplicationManager.class) {
                if (instance == null) {
                    instance = new ApplicationManager();
                }
            }
        }
        return instance;
    }

    public void addApplication(String appName, Application app) {
        if (app == null || TextUtils.isEmpty(appName)) {
            return;
        }
        addItem(appName, app);
        app.onCreate();
    }

    public Application getApplication(String appName) {

        Application app = getItem(appName);
        if (app == null) {
            app = HostApplication.getInstance();
        }
        return app;
    }

    public void removeApplication(String appName) {
        removeItem(appName);
    }

    public void dispatchAppOnCreate() {
        if (getHashmap() == null || getHashmap().size() == 0) {
            return;
        }
        Iterator<String> keyIterator = getHashmap().keySet().iterator();
        while (keyIterator.hasNext()) {
            String appName = keyIterator.next();
            Application app = getItem(appName);
            app.onCreate();
        }
    }

    public void dispatchAppOnLowMemory() {
        if (getHashmap() == null || getHashmap().size() == 0) {
            return;
        }
        Iterator<String> keyIterator = getHashmap().keySet().iterator();
        while (keyIterator.hasNext()) {
            String appName = keyIterator.next();
            Application app = getItem(appName);
            app.onLowMemory();
        }
    }

    public void diapatchAppOnConfigurationChanged(Configuration newConfig) {
        if (getHashmap() == null || getHashmap().size() == 0) {
            return;
        }
        Iterator<String> keyIterator = getHashmap().keySet().iterator();
        while (keyIterator.hasNext()) {
            String appName = keyIterator.next();
            Application app = getItem(appName);
            app.onConfigurationChanged(newConfig);
        }
    }

    public void diapatchAppOnTerminate() {
        if (getHashmap() == null || getHashmap().size() == 0) {
            return;
        }
        Iterator<String> keyIterator = getHashmap().keySet().iterator();
        while (keyIterator.hasNext()) {
            String appName = keyIterator.next();
            Application app = getItem(appName);
            app.onTerminate();
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void dispatchAppOnTrimMemory(int level) {
        if (getHashmap() == null || getHashmap().size() == 0) {
            return;
        }
        Iterator<String> keyIterator = getHashmap().keySet().iterator();
        while (keyIterator.hasNext()) {
            String appName = keyIterator.next();
            Application app = getItem(appName);
            app.onTrimMemory(level);
        }
    }


}
