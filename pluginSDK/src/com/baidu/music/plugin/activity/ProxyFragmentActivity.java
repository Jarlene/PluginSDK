package com.baidu.music.plugin.activity;

import android.annotation.TargetApi;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Messenger;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.*;

import com.baidu.music.plugin.bean.PluginItem;
import com.baidu.music.plugin.callback.ActivityInterface;
import com.baidu.music.plugin.clientlog.LogUtil;
import com.baidu.music.plugin.context.PluginContext;
import com.baidu.music.plugin.manager.ContextManager;
import com.baidu.music.plugin.manager.PluginManager;
import com.baidu.music.plugin.manager.RemotePluginServiceManager;
import com.baidu.music.plugin.service.ProxyService;
import com.baidu.music.plugin.utils.InvokeUtils;
import com.baidu.music.plugin.utils.PluginUtils;

/**
 * Created by Jarlene on 5/21 0021.
 */
public class ProxyFragmentActivity extends FragmentActivity {

    private static final String TAG = ProxyFragmentActivity.class.getSimpleName();

    private ActivityInterface activityInterface;
    private PluginItem        mPluginItem;
    private String mPluginId   = "";
    private Bundle bundleParam = null;
    private PluginContext mPluginContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            try {
                super.onSaveInstanceState(savedInstanceState);
            } catch (Exception e) {
                e.printStackTrace();
            }
            savedInstanceState = null;
        }

        LogUtil.d(TAG, "onCreate()");
        if (getIntent() == null) {
            finish();
            return;
        }
        try {
            mPluginId = getIntent().getStringExtra(PluginUtils.PLUGIN_ID);
            String className = getIntent().getStringExtra(PluginUtils.PLUGIN_CLASS_NAME);
            mPluginItem = getIntent().getParcelableExtra(PluginUtils.PLUGIN_ITEM);
            bundleParam = getIntent().getBundleExtra(PluginUtils.PLUGIN_BUNDLE);
            if (mPluginItem == null) {
                mPluginItem = PluginManager.getInstance().buildPlugin(this, mPluginId);
            }
            if (TextUtils.isEmpty(className) || mPluginItem == null) {
                finish();
                return;
            }
            initPlugin(className, savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initPlugin(String className, Bundle savedInstanceState) {
        mPluginContext = ContextManager.getInstance().getContext(this, mPluginItem);
        Messenger messenger = RemotePluginServiceManager.getInstance().getRemotePluginMessenger();
        try {
            Class<?> mainClazz = Class.forName(className, true, mPluginContext.getClassLoader());
            LogUtil.d(TAG, "the launch class is " + mainClazz);
            activityInterface = (ActivityInterface) InvokeUtils.newInstanceObject(mainClazz);
            if (activityInterface != null) {
                activityInterface.onAttach(this, mPluginContext, messenger);
                activityInterface.onCreate(savedInstanceState);
                LogUtil.d(TAG, "the pluginFragmentActivity onAttach and onCreate");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AssetManager getAssets() {
        if (mPluginContext != null) {
            return mPluginContext.getAssets();
        }
        return super.getAssets();
    }

    @Override
    public Resources getResources() {
        if (mPluginContext != null) {
            return mPluginContext.getResources();
        }
        return super.getResources();
    }

    @Override
    public Resources.Theme getTheme() {
        if (mPluginContext != null) {
            return mPluginContext.getTheme();
        }
        return super.getTheme();

    }

    @Override
    public ClassLoader getClassLoader() {
        if (mPluginContext != null) {
            return mPluginContext.getClassLoader();
        }
        return super.getClassLoader();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        LogUtil.d(TAG, "onPostCreate()");
        if (activityInterface != null) {
            activityInterface.onPostCreate(savedInstanceState);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d(TAG, "onResume()");
        if (activityInterface != null) {
            activityInterface.onResume();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d(TAG, "onStart()");
        if (activityInterface != null) {
            activityInterface.onStart();
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.d(TAG, "onRestart()");
        if (activityInterface != null) {
            activityInterface.onRestart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d(TAG, "onStop()");
        if (activityInterface != null) {
            activityInterface.onStop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d(TAG, "onPause()");
        if (activityInterface != null) {
            activityInterface.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, "onDestroy()");
        if (activityInterface != null) {
            activityInterface.onDestroy();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);

    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        super.onWindowAttributesChanged(params);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (Build.VERSION.SDK_INT < 16) {
            if (intent.getComponent() != null && !TextUtils.isEmpty(
                    intent.getComponent().getClassName())) {
                String className = intent.getComponent().getClassName();
                if (ProxyActivity.class.getName().equals(className)) {
                    super.startActivityForResult(intent, requestCode);
                    return;
                }

                Intent proxyIntent = new Intent(intent);
                proxyIntent.putExtra(PluginUtils.PLUGIN_ID, mPluginId);
                proxyIntent.putExtra(PluginUtils.PLUGIN_CLASS_NAME, className);
                proxyIntent.putExtra(PluginUtils.PLUGIN_ITEM, mPluginItem);
                if (bundleParam != null) {
                    proxyIntent.putExtra(PluginUtils.PLUGIN_BUNDLE, bundleParam);
                }
                proxyIntent.setClass(this, ProxyFragmentActivity.class);
                super.startActivityForResult(proxyIntent, requestCode);
            } else {
                super.startActivityForResult(intent, requestCode);
            }
        } else {
            super.startActivityForResult(intent, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        if (intent.getComponent() != null && !TextUtils.isEmpty(
                intent.getComponent().getClassName())) {
            String className = intent.getComponent().getClassName();
            if (ProxyActivity.class.getName().equals(className)) {
                super.startActivityForResult(intent, requestCode, options);
                return;
            }

            Intent proxyIntent = new Intent(intent);
            proxyIntent.putExtra(PluginUtils.PLUGIN_ID, mPluginId);
            proxyIntent.putExtra(PluginUtils.PLUGIN_CLASS_NAME, className);
            proxyIntent.putExtra(PluginUtils.PLUGIN_ITEM, mPluginItem);
            if (bundleParam != null) {
                proxyIntent.putExtra(PluginUtils.PLUGIN_BUNDLE, bundleParam);
            }
            proxyIntent.setClass(this, ProxyFragmentActivity.class);
            super.startActivityForResult(proxyIntent, requestCode, options);
        } else {
            super.startActivityForResult(intent, requestCode, options);
        }
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        if (service.getComponent() != null) {
            ComponentName componentName = service.getComponent();
            String serviceName = componentName.getClassName();
            Intent intent = new Intent(this, ProxyService.class);
            intent.putExtra(PluginUtils.PLUGIN_CLASS_NAME, serviceName);
            intent.putExtra(PluginUtils.PLUGIN_ITEM, mPluginItem);
            return super.bindService(intent, conn, flags);
        } else {
            return super.bindService(service, conn, flags);
        }
    }


}
