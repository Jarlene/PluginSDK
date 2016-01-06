package com.baidu.music.plugin.context;

import android.app.Application;
import android.content.*;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import com.baidu.music.plugin.bean.PluginItem;
import com.baidu.music.plugin.manager.ApplicationManager;
import com.baidu.music.plugin.utils.InvokeUtils;

import java.io.File;

/**
 * Created by Jarlene on 5/21 0021.
 */
public class PluginContext extends ContextThemeWrapper {

    private static final String TAG = "PluginContext";

    private String         mPluginId;
    private String         mPluginPath;
    private AssetManager   mAssetManager;
    private Resources      mResources;
    private ClassLoader    mClassLoader;
    private LayoutInflater mInflater;
    private Resources      mProxyResource;
    private Context        mContext;


    public PluginContext(Context base, PluginItem item, ClassLoader classLoader) {
        super(base, 0);
        this.mContext = base;
        this.mPluginId = item.getPluginId();
        this.mPluginPath = item.getPluginPath();
        this.mProxyResource = base.getResources();
        this.mClassLoader = classLoader;
    }

    @Override
    public Resources getResources() {
        if (mResources == null) {
            mResources = new Resources(getAssets(), mProxyResource.getDisplayMetrics(),
                    mProxyResource.getConfiguration());
        }
        return mResources;
    }

    @Override
    public AssetManager getAssets() {
        Log.v(TAG, "getAssets");
        if (mAssetManager == null) {
            mAssetManager = (AssetManager) InvokeUtils.newInstanceObject(AssetManager.class);
            InvokeUtils.invokeMethod(mAssetManager, "addAssetPath", new Class[] { String.class },
                    new Object[] { mPluginPath });
        }
        return mAssetManager;
    }

    @Override
    public Theme getTheme() {
        return getPluginTheme(getResources());
    }


    @Override
    public ClassLoader getClassLoader() {
        Log.v(TAG, "getClassLoader");
        return mClassLoader;
    }

    @Override
    public Object getSystemService(String name) {
        Log.v(TAG, "getSystemService");
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (mInflater == null) {
                mInflater = LayoutInflater.from(mContext).cloneInContext(this);
            }
            return mInflater;
        }
        return super.getSystemService(name);
    }

    @Override
    public Context getApplicationContext() {
        Context context = getApplication();
        if (context == null) {
            context = super.getApplicationContext();
        }
        return context;
    }

    //
    //    @Override
    //    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
    //        Log.v(TAG, "registerReceiver");
    //        return super.registerReceiver(receiver, filter);
    //    }
    //
    //    @Override
    //    public void unregisterReceiver(BroadcastReceiver receiver) {
    //        Log.v(TAG, "unregisterReceiver");
    //        super.unregisterReceiver(receiver);
    //    }

    @Override
    public File getCacheDir() {
        return super.getCacheDir();
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return super.getSharedPreferences(name, mode);
    }

    @Override
    public File getDatabasePath(String name) {
        return super.getDatabasePath(name);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
            SQLiteDatabase.CursorFactory factory) {
        return super.openOrCreateDatabase(name, mode, factory);
    }

    /**
     * 获得Application
     *
     * @return
     */
    public Application getApplication() {
        return ApplicationManager.getInstance().getApplication(mPluginId);

    }

    private Theme getPluginTheme(Resources selfResources) {
        Theme theme = selfResources.newTheme();
        int themeResId = getInnerRIdValue("com.android.internal.R.style.Theme");
        theme.applyStyle(themeResId, true);
        return theme;
    }


    private int getInnerRIdValue(String rIdStrnig) {
        int value = -1;
        try {
            int rindex = rIdStrnig.indexOf(".R.");
            String Rpath = rIdStrnig.substring(0, rindex + 2);
            int fieldIndex = rIdStrnig.lastIndexOf(".");
            String fieldName = rIdStrnig.substring(fieldIndex + 1, rIdStrnig.length());
            rIdStrnig = rIdStrnig.substring(0, fieldIndex);
            String type = rIdStrnig.substring(rIdStrnig.lastIndexOf(".") + 1, rIdStrnig.length());
            String className = Rpath + "$" + type;

            Class<?> cls = Class.forName(className);
            value = cls.getDeclaredField(fieldName).getInt(null);

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return value;
    }
}
