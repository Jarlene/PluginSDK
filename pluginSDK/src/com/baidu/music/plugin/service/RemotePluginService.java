package com.baidu.music.plugin.service;

import android.app.Service;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.text.TextUtils;

import com.baidu.music.plugin.clientlog.LogUtil;
import com.baidu.music.plugin.messenger.HostSupportManager;
import com.baidu.music.plugin.messenger.IServiceManager;
import com.baidu.music.plugin.messenger.ServiceProvider;
import com.baidu.music.plugin.utils.PluginUtils;

/**
 * Created by Jarlene on 5/21 0021
 */
public class RemotePluginService extends Service {

	private Messenger mMessenger = null;

	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		HandlerThread thread = new HandlerThread("host_capability");
		thread.start();
		mMessenger = new Messenger(new Handler(thread.getLooper()){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				handleMessageImpl(msg);
			}
		});
	}

	private void handleMessageImpl(Message msg) {
		Bundle bundle = msg.getData();
		if(bundle != null) {
			String supportType = bundle.getString(PluginUtils.PLUGIN_SUPPORT_TYPE);
			if(!TextUtils.isEmpty(supportType)) {
				HostSupportManager manager = (HostSupportManager)this.getSystemService(HostSupportManager.NAME);
				if(manager != null) {
					HostSupportManager.SupportAction support = manager.buildSupportAction(this, supportType);
					if (support != null) {
						String method = bundle.getString(PluginUtils.PLUGIN_METHOD);
						Parcelable params = bundle.getParcelable(PluginUtils.PLUGIN_PRARMS);
						Parcelable result = support.handleAction(method, params);
						if(msg.replyTo != null) {
							try {
								Message e = new Message();
								Bundle replyBundle = new Bundle();
								replyBundle.putString(PluginUtils.PLUGIN_METHOD, method);
								replyBundle.putParcelable(PluginUtils.PLUGIN_RESULT, result);
								e.setData(replyBundle);
								msg.replyTo.send(e);
							} catch (RemoteException e) {
								e.printStackTrace();
							}
						}
					} else {
						LogUtil.d("RemotePluginService", "the SupportAction is null");
					}

				}

			}
		}
	}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        mMessenger = null;
        super.onDestroy();
    }

	@Override
	public Object getSystemService(String name) {
		IServiceManager manager = ServiceProvider.getServiceProvider().getServiceManager();
	    if(manager != null) {
			Object service = manager.getService(name);
	        if(service != null) {
				return service;
	        }
	    }
	    return super.getSystemService(name);
	}
}
