package com.baidu.music.plugin.callback;

import android.app.Activity;
import android.os.Bundle;
import android.os.Messenger;

import com.baidu.music.plugin.context.PluginContext;

/**
 * Created by Jarlene on 5/21 0021.
 */
public interface ActivityInterface {
    public void onAttach(Activity proxyActivity, PluginContext pluginContext, Messenger messenger);

    public void onCreate(Bundle savedInstanceState);

    public void onPostCreate(Bundle savedInstanceState);

    public void onStart();

    public void onRestart();

    public void onResume();

    public void onPause();

    public void onStop();

    public void onDestroy();

}
