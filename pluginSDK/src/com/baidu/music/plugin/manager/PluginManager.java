package com.baidu.music.plugin.manager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import com.baidu.music.plugin.activity.PluginActivity;
import com.baidu.music.plugin.activity.PluginFragmentActivity;
import com.baidu.music.plugin.bean.PluginItem;
import com.baidu.music.plugin.context.PluginContext;
import com.baidu.music.plugin.service.ProxyIntentService;
import com.baidu.music.plugin.utils.ApkUtils;
import com.baidu.music.plugin.utils.FileUtils;
import com.baidu.music.plugin.utils.PluginUtils;
import com.baidu.music.plugin.utils.ZipUtils;

import java.io.File;
import java.io.IOException;


/**
 * Created by Jarlene on 5/21 0021.
 */
public class PluginManager {


    private static PluginManager instance = null;

    /**
     * 单例模式
     *
     * @return
     */
    public static PluginManager getInstance() {
        if (instance == null) {
            synchronized (PluginManager.class) {
                if (instance == null) {
                    instance = new PluginManager();
                }
            }
        }
        return instance;
    }

    /**
     * 打开插件并传入数据
     *
     * @param context
     * @param intent
     * @param apkPath
     */
    public void startPluginActivity(Context context, Intent intent, String apkPath) {
        // TODO Auto-generated method stub
        if (!isValidate(context, apkPath)) {
            return;
        }
        String pluginId = FileUtils.getFileName(apkPath);
        File targetFile = copyPlugin(context, pluginId, apkPath);
        PluginItem item = buildPlugin(context, pluginId);
        if (targetFile != null && targetFile.exists()) {
            openPlugin(context, intent, item, 0);
        }

    }

    /**
     * 打开插件，并启动相关activity，传入参数
     * @param context
     * @param intent
     * @param apkPath
     * @param activityName
     */
    public void startPluginActivity(Context context, Intent intent, String apkPath,
            String activityName) {
        // TODO Auto-generated method stub
        if (!isValidate(context, apkPath)) {
            return;
        }
        String pluginId = FileUtils.getFileName(apkPath);
        File targetFile = copyPlugin(context, pluginId, apkPath);
        PluginItem item = buildPlugin(context, pluginId);
        item.setMainClazz(activityName);
        if (targetFile != null && targetFile.exists()) {
            openPlugin(context, intent, item, 0);
        }

    }


    /**
     * 传教PluginItem
     * @param context
     * @param pluginId
     * @return
     */
    public PluginItem buildPlugin(Context context, String pluginId) {
        File pluginFile;
        if (pluginId.endsWith(".apk")) {
            pluginFile = new File(getPluginDir(context), pluginId);
        } else {
            pluginFile = new File(getPluginDir(context), pluginId + ".apk");
        }
        PluginItem item = new PluginItem(pluginId, pluginFile.getAbsolutePath());
        PackageInfo packageInfo = ApkUtils.getPackageInfo(context, pluginFile.getAbsolutePath());
        if (packageInfo != null) {
            item.setVersionCode(packageInfo.versionCode);
            item.setVersionName(packageInfo.versionName);
        }
        item.setMainClazz(ApkUtils.getMainClassName(context, item.getPluginPath()));
        item.setDexPath(getDexPath(pluginId, context));
        item.setLibPath(getLibraryPath(pluginId, item.getPluginPath(), context));
        return item;
    }


    /**
     * 所有插件的路径
     *
     * @param context
     * @return
     */
    public String getPluginDir(Context context) {
        return context.getDir("plugin", Context.MODE_PRIVATE).getAbsolutePath();
    }

    /**
     * 获得Dex路径
     *
     * @param context
     * @param pluginId
     * @return
     */
    public String getDexPath(String pluginId, Context context) {
        String pluginIdWithoutAPK = pluginId;
        if (pluginId.endsWith(".apk")) {
            pluginIdWithoutAPK = pluginId.substring(0, pluginId.indexOf("."));
        }
        File file = new File(getPluginDir(context), pluginIdWithoutAPK + "/dex");
        try {
            FileUtils.deletelDir(file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        file.mkdirs();
        String path = file.getAbsolutePath();
        return path;
    }

    /**
     * 获得Lib路径
     *
     * @param context
     * @param pluginId
     * @param apkPath
     * @return
     */
    public String getLibraryPath(String pluginId, final String apkPath, Context context) {
        String pluginIdWithoutAPK = pluginId;
        if (pluginId.endsWith(".apk")) {
            pluginIdWithoutAPK = pluginId.substring(0, pluginId.indexOf("."));
        }
        File file = new File(getPluginDir(context), pluginIdWithoutAPK + "/lib");
        try {
            FileUtils.deletelDir(file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        file.mkdirs();
        final String path = file.getAbsolutePath();
        new Thread() {
            @Override
            public void run() {
                ZipUtils.extractLibraries(apkPath, path);
            }
        }.start();
        return path;
    }

    /**
     * 拷贝插件
     *
     * @param context  上下文
     * @param pluginId 插件ID
     * @param rawPath  插件原始路径
     * @return 拷贝后的文件
     */
    public File copyPlugin(Context context, String pluginId, String rawPath) {
        try {
            if (TextUtils.isEmpty(rawPath)) {
                return null;
            }

            String pluginIdWithoutAPK = pluginId;
            if (pluginId.endsWith(".apk")) {
                pluginIdWithoutAPK = pluginId.substring(0, pluginId.indexOf("."));
            }

            File targetFile = new File(getPluginDir(context), pluginIdWithoutAPK + ".apk");
            String apkPath = rawPath;
            if (rawPath.startsWith("file:///android_asset/")) {
                apkPath = rawPath.replace("file:///android_asset/", "");
                if (!targetFile.isDirectory()) {
                    targetFile.delete();
                }
                FileUtils.deletelDir(
                        new File(getPluginDir(context), pluginIdWithoutAPK).getAbsolutePath());
                return FileUtils.copyAssets(context, apkPath, targetFile.getAbsolutePath());
            }

            File sourceFile = new File(apkPath);

            //源文件不存在
            if (!sourceFile.exists()) {
                if (targetFile.exists()) {
                    return targetFile;
                } else {
                    return null;
                }
            }

            PackageInfo oldPkgInfo = ApkUtils.getPackageInfo(context, targetFile.getAbsolutePath());

            //第一次安装
            if (!targetFile.exists() || oldPkgInfo == null) {
                return FileUtils.copyFile(apkPath, targetFile.getAbsolutePath());
            }

            //升级插件

            PackageInfo newPkgInfo = ApkUtils.getPackageInfo(context, apkPath);
            if (newPkgInfo != null) {
                if (newPkgInfo.versionCode > oldPkgInfo.versionCode) {
                    if (!targetFile.isDirectory()) {
                        targetFile.delete();
                    }
                    FileUtils.deletelDir(
                            new File(getPluginDir(context), pluginIdWithoutAPK).getAbsolutePath());
                    return FileUtils.copyFile(apkPath, targetFile.getAbsolutePath());
                }
            }
            return targetFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 打开插件
     *
     * @param context
     * @param intentSource
     * @param item
     * @param requestCode
     */
    public void openPlugin(Context context, Intent intentSource, PluginItem item, int requestCode) {
        String mainClazzName = item.getMainClazz();
        PluginContext pluginContext = ContextManager.getInstance().getContext(context, item);
        int type = getProxyActivityClass(pluginContext, mainClazzName);

        Intent service = new Intent(context, ProxyIntentService.class);
        service.setAction(PluginUtils.PLUGIN_ACTION_LAUNCH);
        service.putExtra(PluginUtils.PLUGIN_PROXYCLASS_TYPE, type);
        service.putExtra(PluginUtils.PLUGIN_ID, item.getPluginId());
        service.putExtra(PluginUtils.PLUGIN_CLASS_NAME, mainClazzName);
        service.putExtra(PluginUtils.PLUGIN_ITEM, item);
        if (intentSource != null) {
            service.putExtra(PluginUtils.PLUGIN_BUNDLE, intentSource.getExtras());
        }
        service.putExtra(PluginUtils.PLUGIN_REQUEST_CODE, requestCode);
        context.startService(service);

    }

    /**
     * 获取activity 类型（Activity或者FragmentActivity）
     * @param context
     * @param mainClass
     * @return
     */
    private int getProxyActivityClass(PluginContext context, String mainClass) {
        int type = -1;
        try {
            Class<?> activityClass = Class.forName(mainClass, true, context.getClassLoader());
            if (PluginActivity.class.isAssignableFrom(activityClass)) {
                type = 0;
            } else if (PluginFragmentActivity.class.isAssignableFrom(activityClass)) {
                type = 1;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return type;
    }


    /**
     * 删除插件
     *
     * @param context  上下文
     * @param pluginId 插件ID
     * @param rawPath  插件在SD卡中的路径
     */
    public void deletePlugin(Context context, String pluginId, String rawPath) {
        String pluginIdWithoutAPK = pluginId;
        if (pluginId.endsWith(".apk")) {
            pluginIdWithoutAPK = pluginId.substring(0, pluginId.indexOf("."));
        }
        File file = new File(rawPath);
        if (file.exists()) {
            file.delete();
        }
        File destFile = new File(getPluginDir(context), pluginIdWithoutAPK + ".apk");
        try {
            if (!destFile.isDirectory()) {
                destFile.delete();
            }
            FileUtils.deletelDir(
                    new File(getPluginDir(context), pluginIdWithoutAPK).getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查插件是否安装
     *
     * @param context  上下文
     * @param pluginId 插件ID
     * @return
     */
    public boolean isInstalled(Context context, String pluginId) {
        String pluginIdWithoutAPK = pluginId;
        if (pluginId.endsWith(".apk")) {
            pluginIdWithoutAPK = pluginId.substring(0, pluginId.indexOf("."));
        }
        File destFile = new File(getPluginDir(context), pluginIdWithoutAPK + ".apk");
        if (destFile.exists()) {
            return isValidate(context, destFile.getAbsolutePath());
        }
        return false;
    }

    /**
     * 是否是合法的安装包
     *
     * @param context 上下文
     * @param apkPath 插件的路径
     * @return
     */
    public boolean isValidate(Context context, String apkPath) {
        return ApkUtils.isValidate(context, apkPath);
    }

    /**
     * 是否需要更新
     *
     * @param context     上下文
     * @param pluginId    插件ID
     * @param versionCode 版本号
     * @return
     */
    public boolean isNeedUpdate(Context context, String pluginId, int versionCode) {
        String pluginIdWithoutAPK = pluginId;
        if (pluginId.endsWith(".apk")) {
            pluginIdWithoutAPK = pluginId.substring(0, pluginId.indexOf("."));
        }
        File targetFile = new File(getPluginDir(context), pluginIdWithoutAPK + ".apk");
        PackageInfo pkgInfo = ApkUtils.getPackageInfo(context, targetFile.getAbsolutePath());
        if (pkgInfo != null) {
            return versionCode > pkgInfo.versionCode;
        }
        return true;
    }

    /**
     * 获得插件版本号
     *
     * @param context  上下文
     * @param pluginId 插件ID
     * @return
     */
    public int getVersion(Context context, String pluginId) {
        String pluginIdWithoutAPK = pluginId;
        if (pluginId.endsWith(".apk")) {
            pluginIdWithoutAPK = pluginId.substring(0, pluginId.indexOf("."));
        }
        File targetFile = new File(getPluginDir(context), pluginIdWithoutAPK + ".apk");
        PackageInfo pkgInfo = ApkUtils.getPackageInfo(context, targetFile.getAbsolutePath());
        if (pkgInfo != null) {
            return pkgInfo.versionCode;
        }
        return -1;
    }


}
