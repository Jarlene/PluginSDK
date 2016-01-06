package com.baidu.music.plugin.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;

import com.baidu.music.plugin.clientlog.LogUtil;
import com.baidu.music.plugin.service.RemotePluginService;

/**
 * 对RemotePluginService相关的进行操作，包括注册service和解注册service
 * Created by Jarlene on 5/21 0021.
 */
public class RemotePluginServiceManager {

    private static RemotePluginServiceManager mInstance;
    private        BusServiceConnection       mBusServiceConnection;
    private Messenger mBusMessenger = null;

    private RemotePluginServiceManager() {
    }

    public static RemotePluginServiceManager getInstance() {
        if (mInstance == null) {
            synchronized (RemotePluginServiceManager.class) {
                if (mInstance == null) {
                    mInstance = new RemotePluginServiceManager();
                }
            }
        }
        return mInstance;
    }

    public void initRemotePluginService(Context context) {
        LogUtil.v("<===========RemotePluginService initRemotePluginService========>");
        Intent busService = new Intent(context, RemotePluginService.class);
        context.startService(busService);
        if (this.mBusServiceConnection == null) {
            this.mBusServiceConnection = new RemotePluginServiceManager.BusServiceConnection();
        }

        context.bindService(busService, this.mBusServiceConnection, 0);
    }

    public void releaseRemotePluginService(Context context) {
        LogUtil.v("<===========RemotePluginService releaseRemotePluginService========>");
        Intent busService = new Intent(context, RemotePluginService.class);
        if (mBusServiceConnection != null) {
            context.unbindService(this.mBusServiceConnection);
            this.mBusServiceConnection = null;
        }
        this.mBusMessenger = null;
        context.stopService(busService);
    }

    public Messenger getRemotePluginMessenger() {
        return this.mBusMessenger;
    }

    private class BusServiceConnection implements ServiceConnection {
        private BusServiceConnection() {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            RemotePluginServiceManager.this.mBusMessenger = new Messenger(service);
            LogUtil.v("<===========RemotePluginService onServiceConnected========>");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.v("<===========RemotePluginService onServiceDisconnected========>");
        }
    }
}
