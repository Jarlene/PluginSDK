package com.baidu.music.plugin.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Messenger;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.baidu.music.plugin.callback.ActivityInterface;
import com.baidu.music.plugin.clientlog.LogUtil;
import com.baidu.music.plugin.context.PluginContext;
import com.baidu.music.plugin.messenger.CapabilityController;

/**
 * Created by Jarlene on 5/21 0021.
 */
public class PluginFragmentActivity extends FragmentActivity implements ActivityInterface {

    private static final String           TAG            =
            PluginFragmentActivity.class.getSimpleName();
    private              FragmentActivity mProxyActivity = null;
    private PluginContext        mPluginContext;
    private CapabilityController mCapabilityController;
    private View                 mContentView;

    @Override
    public void onAttach(Activity proxyActivity, PluginContext pluginContext, Messenger messenger) {
        mProxyActivity = (FragmentActivity) proxyActivity;
        mCapabilityController = new CapabilityController(messenger);
        mPluginContext = pluginContext;
        attachBaseContext(mPluginContext);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogUtil.d(TAG, "onCreate");
        if (mProxyActivity == null) {
            super.onCreate(savedInstanceState);
        }

    }


    public CapabilityController getCapabilityController() {
        return mCapabilityController;
    }

    public Activity getProxyActivity() {
        return mProxyActivity;
    }

    public Context getPluginContext() {
        return mPluginContext;
    }

    @Override
    public void setContentView(int layoutResID) {
        LogUtil.d(TAG, "setContentView");
        if (mProxyActivity != null) {
            mContentView = LayoutInflater.from(mPluginContext).inflate(layoutResID, null);
            mProxyActivity.setContentView(mContentView);
        } else {
            super.setContentView(layoutResID);
        }
    }

    @Override
    public void setContentView(View view) {
        LogUtil.d(TAG, "setContentView");
        if (mProxyActivity != null) {
            mContentView = view;
            mProxyActivity.setContentView(mContentView);
        } else {
            super.setContentView(view);
        }
    }

    @Override
    public View findViewById(int id) {
        LogUtil.d(TAG, "findViewById");
        if (mProxyActivity != null && mContentView != null && mContentView.findViewById(
                id) != null) {
            return mContentView.findViewById(id);
        } else {
            return super.findViewById(id);
        }
    }


    @Override
    public Intent getIntent() {
        if (mProxyActivity != null) {
            return mProxyActivity.getIntent();
        }
        return super.getIntent();
    }

    @Override
    public LayoutInflater getLayoutInflater() {
        LogUtil.d(TAG, "getLayoutInflater");
        if (mProxyActivity != null) {
            return mProxyActivity.getLayoutInflater();
        } else {
            return super.getLayoutInflater();
        }
    }

    @Override
    public MenuInflater getMenuInflater() {
        if (mProxyActivity == null) {
            return super.getMenuInflater();
        } else {
            return mProxyActivity.getMenuInflater();
        }
    }

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        if (mProxyActivity != null) {
            mProxyActivity.overridePendingTransition(enterAnim, exitAnim);
        } else {
            super.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    @Override
    public Object getSystemService(String name) {
        if (mProxyActivity != null) {
            return mProxyActivity.getSystemService(name);
        } else {
            return super.getSystemService(name);
        }
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        if (mProxyActivity == null) {
            return super.getSharedPreferences(name, mode);
        } else {
            return mProxyActivity.getSharedPreferences(name, mode);
        }
    }

    @Override
    public Context getApplicationContext() {
        if (mProxyActivity == null) {
            return super.getApplicationContext();
        } else {
            return mProxyActivity.getApplicationContext();
        }
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        LogUtil.d(TAG, "onPostCreate");
        if (mProxyActivity == null) {
            super.onPostCreate(savedInstanceState);
        }
    }

    @Override
    public void onStart() {
        LogUtil.d(TAG, "onStart");
        if (mProxyActivity == null) {
            super.onStart();
        }
    }

    @Override
    public void onRestart() {
        LogUtil.d(TAG, "onRestart");
        if (mProxyActivity == null) {
            super.onRestart();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.d(TAG, "onActivityResult");
        if (mProxyActivity == null) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResume() {
        LogUtil.d(TAG, "onResume");
        if (mProxyActivity == null) {
            super.onResume();
        }
    }

    @Override
    public void onPause() {
        LogUtil.d(TAG, "onPause");
        if (mProxyActivity == null) {
            super.onPause();
        }
    }

    @Override
    public void onStop() {
        LogUtil.d(TAG, "onStop");
        if (mProxyActivity == null) {
            super.onStop();
        }
    }

    @Override
    public void onDestroy() {
        LogUtil.d(TAG, "onDestroy");
        if (mProxyActivity == null) {
            super.onDestroy();
        }
    }

    @Override
    public void finish() {
        LogUtil.d(TAG, "finish");
        if (mProxyActivity != null) {
            mProxyActivity.finish();
        } else {
            super.finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mProxyActivity == null) {
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (mProxyActivity == null) {
            super.onNewIntent(intent);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (mProxyActivity == null) {
            super.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mProxyActivity == null) {
            return super.onTouchEvent(event);
        } else {
            return mProxyActivity.onTouchEvent(event);
        }

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mProxyActivity == null) {
            return super.onKeyUp(keyCode, event);
        } else {
            return mProxyActivity.onKeyUp(keyCode, event);
        }
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        if (mProxyActivity == null) {
            super.onWindowAttributesChanged(params);
        } else {
            mProxyActivity.onWindowAttributesChanged(params);
        }
    }

    @Override
    public WindowManager getWindowManager() {
        if (mProxyActivity != null) {
            return mProxyActivity.getWindowManager();
        } else {
            return super.getWindowManager();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (mProxyActivity == null) {
            super.onWindowFocusChanged(hasFocus);
        } else {
            mProxyActivity.onWindowFocusChanged(hasFocus);
        }
    }

    @Override
    public int getChangingConfigurations() {
        if (mProxyActivity != null) {
            return mProxyActivity.getChangingConfigurations();
        } else {
            return super.getChangingConfigurations();
        }
    }

    @Override
    public Window getWindow() {
        if (mProxyActivity != null) {
            return mProxyActivity.getWindow();
        } else {
            return super.getWindow();
        }
    }

    @Override
    public void setTheme(int resid) {
        if (mProxyActivity != null) {
            mProxyActivity.setTheme(resid);
        } else {
            super.setTheme(resid);
        }
    }

    @Override
    public String getPackageName() {
        if (mProxyActivity != null) {
            return mProxyActivity.getPackageName();
        } else {
            return super.getPackageName();
        }
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        if (mProxyActivity != null) {
            return mProxyActivity.getApplicationInfo();
        } else {
            return super.getApplicationInfo();
        }
    }

    @Override
    public void onBackPressed() {
        if (mProxyActivity == null) {
            super.onBackPressed();
        } else {
            mProxyActivity.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mProxyActivity == null) {
            return super.onCreateOptionsMenu(menu);
        } else {
            return mProxyActivity.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mProxyActivity == null) {
            return super.onOptionsItemSelected(item);
        } else {
            return mProxyActivity.onOptionsItemSelected(item);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (mProxyActivity != null) {
            mProxyActivity.startActivityForResult(intent, requestCode);
        } else {
            super.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public ComponentName startService(Intent service) {
        if (mProxyActivity != null) {
            return mProxyActivity.startService(service);
        } else {
            return super.startService(service);
        }
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        if (mProxyActivity != null) {
            return mProxyActivity.bindService(service, conn, flags);
        } else {
            return super.bindService(service, conn, flags);
        }
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        if (mProxyActivity != null) {
            mProxyActivity.unbindService(conn);
        } else {
            super.unbindService(conn);
        }
    }


    ///////////////////For Fragments//////////////////////
    @Override
    public FragmentManager getSupportFragmentManager() {
        if (mProxyActivity == null) {
            return super.getSupportFragmentManager();
        }
        return mProxyActivity.getSupportFragmentManager();
    }

    @Override
    public LoaderManager getSupportLoaderManager() {
        if (mProxyActivity == null) {
            return super.getSupportLoaderManager();
        }
        return mProxyActivity.getSupportLoaderManager();
    }

}
