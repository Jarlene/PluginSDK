package com.baidu.music.plugin.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.IBinder;
import android.os.Messenger;
import com.baidu.music.plugin.callback.ServiceInterface;
import com.baidu.music.plugin.context.PluginContext;
/**
 * Created by Jarlene on 5/21 0021
 */
public class PluginService extends Service implements ServiceInterface {

    private Service mProxyService;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onAttach(Service proxyActivity, PluginContext pluginContext, Messenger messenger) {
        // TODO Auto-generated method stub
        mProxyService = proxyActivity;
        attachBaseContext(pluginContext);
    }


    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {

        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Object getSystemService(String name) {
        if (mProxyService != null) {
            return mProxyService.getSystemService(name);
        } else {
            return super.getSystemService(name);
        }
    }

    @Override
    public String getPackageName() {
        if (mProxyService != null) {
            return mProxyService.getPackageName();
        } else {
            return super.getPackageName();
        }
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        if (mProxyService != null) {
            return mProxyService.getApplicationInfo();
        } else {
            return super.getApplicationInfo();
        }
    }
}
