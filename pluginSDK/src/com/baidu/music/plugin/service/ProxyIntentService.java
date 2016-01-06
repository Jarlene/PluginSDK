package com.baidu.music.plugin.service;

import android.app.IntentService;
import android.content.Intent;
import com.baidu.music.plugin.activity.ProxyActivity;
import com.baidu.music.plugin.activity.ProxyFragmentActivity;
import com.baidu.music.plugin.clientlog.LogUtil;
import com.baidu.music.plugin.utils.PluginUtils;

/**
 * Created by Jarlene on 5/21 0021
 */
public class ProxyIntentService extends IntentService{

	private static final String TAG = "ProxyIntentService";
	
	public ProxyIntentService() {
		super("ProxyIntentService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		if (intent == null) {
			return;
		}
		String action = intent.getAction();
		LogUtil.d(TAG, "onHandleIntent, action:" + action);
		if (PluginUtils.PLUGIN_ACTION_LAUNCH.equals(action)) {
			Intent activityIntent = new Intent(intent);
			activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			int type = intent.getIntExtra(PluginUtils.PLUGIN_PROXYCLASS_TYPE, -1);
			if (type == 0) {
				activityIntent.setClass(getApplication(),ProxyActivity.class);
			} else if (type == 1) {
				activityIntent.setClass(getApplication(),ProxyFragmentActivity.class);
			} else {
				activityIntent.setClass(getApplication(),ProxyActivity.class);
			}
			startActivity(activityIntent);
        }
	}

}
