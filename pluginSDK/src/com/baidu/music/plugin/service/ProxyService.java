package com.baidu.music.plugin.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;
import com.baidu.music.plugin.bean.PluginItem;
import com.baidu.music.plugin.callback.ServiceInterface;
import com.baidu.music.plugin.context.PluginContext;
import com.baidu.music.plugin.manager.ContextManager;
import com.baidu.music.plugin.manager.RemotePluginServiceManager;
import com.baidu.music.plugin.utils.InvokeUtils;
import com.baidu.music.plugin.utils.PluginUtils;

/**
 * Created by Jarlene on 5/21 0021
 */
public class ProxyService extends Service {

    private ServiceInterface serviceInterface;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressWarnings("deprecation")
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        boolean ret = initPluginService(intent);
        if (ret && null != serviceInterface) {
            serviceInterface.onStart(intent, startId);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        boolean ret = initPluginService(intent);
        if (ret && null != serviceInterface) {
            serviceInterface.onStartCommand(intent, flags, startId);
        }
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        boolean res = super.onUnbind(intent);
        if (null != serviceInterface) {
            res = serviceInterface.onUnbind(intent);
        }
        return res;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != serviceInterface) {
            serviceInterface.onDestroy();
            serviceInterface = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        IBinder binder = null;
        if (null == serviceInterface) {
            initPluginService(intent);
        }
        if (null != serviceInterface) {
            binder = serviceInterface.onBind(intent);
        }
        return binder;
    }

    private boolean initPluginService(Intent intent) {
        // TODO Auto-generated method stub
        if (null == intent) {
            return false;
        }

        String serviceName = intent.getStringExtra(PluginUtils.PLUGIN_CLASS_NAME);
        PluginItem mPluginItem = intent.getParcelableExtra(PluginUtils.PLUGIN_ITEM);
        PluginContext pluginContext = ContextManager.getInstance().getContext(this, mPluginItem);
        ClassLoader classLoader = pluginContext.getClassLoader();
        Messenger messenger = RemotePluginServiceManager.getInstance().getRemotePluginMessenger();
        try {
            Class<?> pluginServiceClass = Class.forName(serviceName, true, classLoader);
            //			InvokeUtils.setObjectField(Service.class, pluginServiceClass, "this$0", this);
            serviceInterface = (ServiceInterface) InvokeUtils.newInstanceObject(pluginServiceClass);
            if (serviceInterface != null) {
                serviceInterface.onAttach(this, pluginContext, messenger);
                serviceInterface.onCreate();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
