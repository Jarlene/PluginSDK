package com.baidu.music.plugin.manager;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import com.baidu.music.plugin.HostApplication;
import com.baidu.music.plugin.bean.PluginItem;
import com.baidu.music.plugin.context.PluginContext;
import com.baidu.music.plugin.utils.ApkUtils;
import com.baidu.music.plugin.utils.InvokeUtils;
import dalvik.system.DexClassLoader;

/**
 * Created by Jarlene on 5/21 0021.
 */
public class ContextManager extends BaseManager<String, PluginContext> {

    private static ContextManager instance = null;


    private ContextManager() {
    }

    /**
     * 获得插件上下文管理实例
     *
     * @return 管理器实例
     */
    public static ContextManager getInstance() {
        if (instance == null) {
            synchronized (ContextManager.class) {
                if (instance == null) {
                    instance = new ContextManager();
                }
            }
        }
        return instance;
    }


    public PluginContext getContext(Context base, PluginItem item) {
        PluginContext context = getItem(item.getPluginId());
        if (context == null) {
            if (base == null) {
                return null;
            }
            context = createContext(base, item);
            if (context != null) {
                addItem(item.getPluginId(), context);
            }
        }

        return context;
    }

    private PluginContext createContext(Context base, PluginItem item) {
        //        DexClassLoader classLoader = new DexClassLoader(item.getPluginPath(),
        //                PluginManager.getInstance().getDexPath(item.getPluginId(), base),
        //                PluginManager.getInstance().getLibraryPath(item.getPluginId(), item
        // .getPluginPath(), base),
        //                base.getClassLoader());
        DexClassLoader classLoader = ClassLoaderManager.getInstance().getClassLoader(base, item);
        //构造上下文
        PluginContext pluginContext = new PluginContext(base, item, classLoader);

        Application app = buildApplication(base, item.getPluginPath(), pluginContext, classLoader);
        if (app != null) {
            ApplicationManager.getInstance().addApplication(item.getPluginId(), app);
        }

        return pluginContext;
    }

    /**
     * 构造Application
     *
     * @param context
     * @param apkPath
     * @param pluginContext
     * @param classLoader
     * @return
     */
    private Application buildApplication(Context context, String apkPath, Context pluginContext,
            ClassLoader classLoader) {
        String appClazzName = ApkUtils.getApplicationName(context, apkPath);
        try {
            Class<?> appClazz = Class.forName(appClazzName, true, classLoader);
            Application app = (Application) InvokeUtils.newInstanceObject(appClazz);
            attachApplication(app, pluginContext);
            Object value = InvokeUtils.getObjectFieldValue(Application.class,
                    HostApplication.getAppContext(), "mLoadedApk");
            InvokeUtils.setObjectField(Application.class, app, "mLoadedApk", value);
            return app;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void attachApplication(Application app, Context context) {
        Context baseContext = app.getBaseContext();
        if (baseContext == null) {
            InvokeUtils.invokeMethod(ContextWrapper.class, app, "attachBaseContext",
                    new Class[] { Context.class }, new Object[] { context });
        }
    }

}
