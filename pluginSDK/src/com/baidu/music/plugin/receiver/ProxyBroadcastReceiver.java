package com.baidu.music.plugin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.baidu.music.plugin.manager.ClassLoaderManager;
import com.baidu.music.plugin.utils.InvokeUtils;

/**
 * Created by Jarlene on 5/21 0021
 */
public class ProxyBroadcastReceiver extends BroadcastReceiver {

    private BroadcastReceiver mBroadcastReceiver;
    private String            mTargetReceiver;
    private String            mPluginId;

    public ProxyBroadcastReceiver(String receiver, String pluginId) {
        if (TextUtils.isEmpty(receiver) || TextUtils.isEmpty(pluginId)) {
            throw new NullPointerException("BroadcastReceiver is empty");
        }
        this.mTargetReceiver = receiver;
        this.mPluginId = pluginId;
    }

    private BroadcastReceiver getBroadcastReceiver() {
        if (mBroadcastReceiver != null) {
            return mBroadcastReceiver;
        }
        try {
            Class<?> loadClass = Class.forName(mTargetReceiver, true,
                    ClassLoaderManager.getInstance().getClassLoader(mPluginId));
            if (loadClass != null) {
                this.mBroadcastReceiver =
                        (BroadcastReceiver) InvokeUtils.newInstanceObject(loadClass);
                attachPendingResult(mBroadcastReceiver);
            }
            return this.mBroadcastReceiver;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void attachPendingResult(Object obj) {
        try {
            Class<?> clazz = Class.forName(BroadcastReceiver.class.getName(), true,
                    ClassLoaderManager.getInstance().getCurrentClassLoader());
            Object mPendingResult = InvokeUtils.getObjectFieldValue(clazz, this, "mPendingResult");
            InvokeUtils.setObjectField(clazz, obj, "mPendingResult", mPendingResult);
            Object mDebugUnregister =
                    InvokeUtils.getObjectFieldValue(clazz, this, "mDebugUnregister");
            InvokeUtils.setObjectField(clazz, obj, "mDebugUnregister", mDebugUnregister);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (getBroadcastReceiver() != null) {
            mBroadcastReceiver.onReceive(context, intent);
        }
    }
}
