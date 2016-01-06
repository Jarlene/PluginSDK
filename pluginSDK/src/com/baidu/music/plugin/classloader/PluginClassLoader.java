package com.baidu.music.plugin.classloader;

import android.app.Application;
import android.content.Context;
import com.baidu.music.plugin.bean.PluginItem;
import com.baidu.music.plugin.manager.PluginManager;
import com.baidu.music.plugin.utils.InvokeUtils;
import dalvik.system.DexClassLoader;

/**
 * Created by Jarlene on 5/21 0021.
 */
public class PluginClassLoader extends DexClassLoader {

    public PluginClassLoader(Context context, PluginItem plugin) {
        super(plugin.getPluginPath(), getDexPath(plugin.getPluginId(), context),
                getLibraryPath(plugin.getPluginId(), plugin.getPluginPath(), context),
                getParentClassLoader(context, plugin));
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    /**
     * 获得Dex路径
     *
     * @param context
     * @param pluginId
     * @return
     */
    public static String getDexPath(String pluginId, Context context) {
        return PluginManager.getInstance().getDexPath(pluginId, context);
    }

    /**
     * 获得Lib路径
     *
     * @param context
     * @param pluginId
     * @param apkPath
     * @return
     */
    public static String getLibraryPath(String pluginId, final String apkPath, Context context) {
        return PluginManager.getInstance().getLibraryPath(pluginId, apkPath, context);
    }

    /**
     * 获取父类加载器
     *
     * @param context
     * @param item
     * @return
     */
    private static ClassLoader getParentClassLoader(Context context, PluginItem item) {

        try {
            Class<?> cls = Class.forName("android.app.ActivityThread");
            Object obj = InvokeUtils.invokeMethod(cls, null, "currentActivityThread");
            Application systemApp =
                    (Application) InvokeUtils.getObjectFieldValue(cls, obj, "mInitialApplication");
            return systemApp.getClassLoader();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context.getClassLoader();
    }

}
