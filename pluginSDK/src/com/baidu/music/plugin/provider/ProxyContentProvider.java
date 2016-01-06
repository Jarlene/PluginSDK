package com.baidu.music.plugin.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.baidu.music.plugin.manager.ClassLoaderManager;
import com.baidu.music.plugin.utils.InvokeUtils;

/**
 * Created by Jarlene on 5/21 0021
 */
public class ProxyContentProvider extends ContentProvider {


    private ContentProvider mContentProvider;
    private String          mTargetProvider;
    private String          mPluginId;

    public ProxyContentProvider(String mTargetProvider, String pluginId) {
        if (TextUtils.isEmpty(mTargetProvider) || TextUtils.isEmpty(pluginId)) {
            throw new NullPointerException("BroadcastReceiver is empty");
        }
        this.mTargetProvider = mTargetProvider;
        this.mPluginId = pluginId;
    }

    protected ContentProvider getContentProvider() {
        if (mContentProvider != null) {
            return mContentProvider;
        }
        try {

            Class<?> loadClass = Class.forName(mTargetProvider, true,
                    ClassLoaderManager.getInstance().getClassLoader(mPluginId));
            if (loadClass != null) {
                this.mContentProvider = (ContentProvider) InvokeUtils.newInstanceObject(loadClass);

                InvokeUtils.setObjectField(loadClass, mContentProvider, "mContext", getContext());
                InvokeUtils.setObjectField(loadClass, mContentProvider, "mReadPermission",
                        getReadPermission());
                InvokeUtils.setObjectField(loadClass, mContentProvider, "mWritePermission",
                        getWritePermission());
                InvokeUtils.setObjectField(loadClass, mContentProvider, "mPathPermissions",
                        getPathPermissions());

                this.mContentProvider.onCreate();

                return this.mContentProvider;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public boolean onCreate() {

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        ContentProvider mContentProvider = getContentProvider();
        if (mContentProvider != null) {
            return mContentProvider.query(uri, projection, selection, selectionArgs, sortOrder);
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        ContentProvider mContentProvider = getContentProvider();
        if (mContentProvider != null) {
            return mContentProvider.getType(uri);
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        ContentProvider mContentProvider = getContentProvider();
        if (mContentProvider != null) {
            return mContentProvider.insert(uri, values);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        ContentProvider mContentProvider = getContentProvider();
        if (mContentProvider != null) {
            return mContentProvider.delete(uri, selection, selectionArgs);
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        ContentProvider mContentProvider = getContentProvider();
        if (mContentProvider != null) {
            return mContentProvider.update(uri, values, selection, selectionArgs);
        }
        return 0;
    }
}
