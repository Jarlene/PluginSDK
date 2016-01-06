package com.baidu.music.plugin.manager;

import android.content.Context;
import com.baidu.music.plugin.bean.PluginItem;
import com.baidu.music.plugin.context.PluginContext;
import com.baidu.music.plugin.context.PluginResource;

/**
 * Created by Jarlene on 5/21 0021.
 */
public class ResourceManager extends BaseManager<String, PluginResource> {

    private static ResourceManager instance = null;

    private ResourceManager() {
    }

    /**
     * 获得插件上下文管理实例
     *
     * @return 管理器实例
     */
    public static ResourceManager getInstance() {
        if (instance == null) {
            synchronized (ResourceManager.class) {
                if (instance == null) {
                    instance = new ResourceManager();
                }
            }
        }
        return instance;
    }


    public PluginResource getReflectResource(Context context, PluginItem item) {
        PluginResource pluginResource = getItem(item.getPluginId());
        if (pluginResource == null) {
            if (context == null) {
                return null;
            }
            PluginContext pluginContext = ContextManager.getInstance().getContext(context, item);
            if (pluginContext != null) {
                pluginResource = new PluginResource(pluginContext, item);
            }
            if (pluginResource != null) {
                addItem(item.getPluginId(), pluginResource);
            }
        }
        return pluginResource;
    }
}
