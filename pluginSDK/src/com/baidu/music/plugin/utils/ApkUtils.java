package com.baidu.music.plugin.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import com.baidu.music.plugin.clientlog.LogUtil;


/**
 * apk相关执行动作
 * <p/>
 * Created by Jarlene on 5/21 0021
 */
public class ApkUtils {

    private static final String TAG = "ApkUtils";

    /**
     * 获取主Activity
     *
     * @param context
     * @param apkPath
     * @return
     */
    public static String getMainClassName(Context context, String apkPath) {
        PackageInfo packageInfo = getPackageInfo(context, apkPath);
        if (packageInfo == null) {
            return null;
        }
        if (packageInfo.activities != null && packageInfo.activities.length > 0) {
            String activity = packageInfo.activities[0].name;
            for (int i = 0; i < packageInfo.activities.length; i++) {
                LogUtil.v(TAG, packageInfo.activities[i].name);
            }
            return activity;
        }
        return null;
    }

    /**
     * 获取Application
     *
     * @param context
     * @param apkPath
     * @return
     */
    public static String getApplicationName(Context context, String apkPath) {
        PackageInfo packageInfo = getPackageInfo(context, apkPath);
        if (packageInfo == null) {
            return null;
        }
        return packageInfo.applicationInfo.className;
    }

    /**
     * 获取PackageInfo
     *
     * @param context
     * @param apkPath
     * @return
     */
    public static PackageInfo getPackageInfo(Context context, String apkPath) {
        PackageInfo packageInfo = context.getPackageManager().getPackageArchiveInfo(apkPath, 1);
        return packageInfo;
    }

    /**
     * 获取apk图标
     *
     * @param context
     * @param apkFilepath
     * @return
     */
    public static Drawable getAppIcon(Context context, String apkFilepath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = getPackageInfo(context, apkFilepath);
        if (pkgInfo == null) {
            return null;
        }

        ApplicationInfo appInfo = pkgInfo.applicationInfo;
        if (Build.VERSION.SDK_INT >= 8) {
            appInfo.sourceDir = apkFilepath;
            appInfo.publicSourceDir = apkFilepath;
        }

        return pm.getApplicationIcon(appInfo);
    }

    /**
     * 检验插件是否完整
     *
     * @param context
     * @param apkPath
     * @return
     */
    public static boolean isValidate(Context context, String apkPath) {
        PackageInfo info = getPackageInfo(context, apkPath);
        return info != null;
    }



}
