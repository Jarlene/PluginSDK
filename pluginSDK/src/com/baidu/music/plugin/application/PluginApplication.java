package com.baidu.music.plugin.application;

/**
 * 插件Application，所有插件都要继承该插件
 * <p/>
 * Created by Jarlene on 5/21 0021.
 */

public class PluginApplication extends BaseApp {

    private static PluginApplication instance = null;

    /**
     * 单例模式
     *
     * @return
     */
    public static PluginApplication getInstance() {
        if (instance == null) {
            synchronized (PluginApplication.class) {
                if (instance == null) {
                    instance = new PluginApplication();
                }
            }
        }
        return instance;
    }
}
