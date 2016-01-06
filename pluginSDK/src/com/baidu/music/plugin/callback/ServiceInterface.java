package com.baidu.music.plugin.callback;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;
import com.baidu.music.plugin.context.PluginContext;

/**
 * Created by Jarlene on 5/21 0021.
 */
public interface ServiceInterface {

    public void onAttach(Service proxyActivity, PluginContext pluginContext, Messenger messenger);

    public void onCreate();

    public void onStart(Intent intent, int startId);

    public int onStartCommand(Intent intent, int flags, int startId);

    public IBinder onBind(Intent intent);

    public boolean onUnbind(Intent intent);

    public void onDestroy();
}
