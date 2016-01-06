package com.baidu.music.plugin.manager;

import android.content.Context;
import com.baidu.music.plugin.bean.PluginItem;
import com.baidu.music.plugin.classloader.PluginClassLoader;

/**
 * Created by Jarlene on 5/21 0021.
 */
public class ClassLoaderManager extends BaseManager<String, PluginClassLoader> {
    private static ClassLoaderManager _instance           = null;
    private static ClassLoader        mCurrentClassLoader = null;

    public ClassLoaderManager() {
    }

    public static ClassLoaderManager getInstance() {
        if (_instance == null) {
            _instance = new ClassLoaderManager();
        }

        return _instance;
    }

    public PluginClassLoader getClassLoader(Context context, PluginItem item) {
        PluginClassLoader classLoader = getItem(item.getPluginId());
        if (classLoader == null) {
            if (context == null) {
                return null;
            }

            classLoader = new PluginClassLoader(context, item);
            if (classLoader != null) {
                this.addItem(item.getPluginId(), classLoader);
            }
        }
        mCurrentClassLoader = classLoader;
        return classLoader;
    }

    /**
     * 得到当前正在运行的ClasLoader
     *
     * @return
     */
    public ClassLoader getCurrentClassLoader() {
        return mCurrentClassLoader;
    }


    /**
     * 获取已经加载过的ClassLoader
     *
     * @param pluginId
     * @return
     */
    public ClassLoader getClassLoader(String pluginId) {
        return getItem(pluginId);
    }

}
