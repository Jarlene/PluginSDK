package com.baidu.music.plugin.messenger;

import android.os.*;
import com.baidu.music.plugin.callback.HostCallBack;
import com.baidu.music.plugin.utils.PluginUtils;

/**
 * Created by Jarlene on 5/21 0021.
 */
public class CapabilityController {

    private Messenger mMessenger;

    public CapabilityController(Messenger messenger) {
        this.mMessenger = messenger;
    }

    public void invokeHostMethod(String supportType, String method, Parcelable params,
            final HostCallBack callBack) throws RemoteException {
        invokeHostMethod(supportType, method, params, callBack, Looper.myLooper());
    }

    public void invokeHostMethod(String supportType, String method, Parcelable params,
            final HostCallBack callBack, Looper callBackLooper) throws RemoteException {
        if (mMessenger != null) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString(PluginUtils.PLUGIN_SUPPORT_TYPE, supportType);
            bundle.putString(PluginUtils.PLUGIN_METHOD, method);
            bundle.putParcelable(PluginUtils.PLUGIN_PRARMS, params);
            msg.setData(bundle);
            if (callBack != null && callBackLooper != null) {
                msg.replyTo = new Messenger(new Handler(callBackLooper) {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (callBack != null) {
                            String method = msg.getData().getString(PluginUtils.PLUGIN_METHOD);
                            Parcelable result =
                                    msg.getData().getParcelable(PluginUtils.PLUGIN_RESULT);
                            callBack.onHostCallBack(method, result);
                        }
                    }
                });
            }
            mMessenger.send(msg);
        }
    }
}
