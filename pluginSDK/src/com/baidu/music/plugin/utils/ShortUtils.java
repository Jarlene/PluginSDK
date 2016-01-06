package com.baidu.music.plugin.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import com.baidu.music.plugin.activity.ProxyActivity;
import com.baidu.music.plugin.bean.PluginItem;
import com.baidu.music.plugin.manager.PluginManager;

/**
 * 创建桌面快捷方式
 * Created by Jarlene on 5/21 0021
 */
public class ShortUtils {
    /**
     * 创建桌面快捷
     * @param context 上下文
     * @param name 显示名称
     * @param icon 显示图标
     * @param clazz 点击启动class
     */
    public static void createShortCut(Context context, int name, int icon, Class<?> clazz) {
        if (hasShortcut(context,  context.getString(name))) {
            return;
        }
        Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //不允许重复创建
        shortcutIntent.putExtra("duplicate", false);
        //需要现实的名称
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(name));
        //快捷图片
        Parcelable drawable = Intent.ShortcutIconResource.fromContext(context.getApplicationContext(), icon);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, drawable);
        //点击快捷图片，运行的程序主入口
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(context.getApplicationContext() , clazz));
        //发送广播。OK
        context.sendBroadcast(shortcutIntent);
    }

    /**
     * 创建桌面快捷
     * @param context 上下文
     * @param name 显示名称
     * @param icon 显示图标
     * @param clazz 点击启动class
     */
    public static void createShortCut(Context context, String name, int icon, Class<?> clazz) {

        if (hasShortcut(context, name)) {
            return;
        }
        Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //不允许重复创建
        shortcutIntent.putExtra("duplicate", false);
        //需要现实的名称
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        //快捷图片
        Parcelable drawable = Intent.ShortcutIconResource.fromContext(context.getApplicationContext(), icon);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, drawable);
        //点击快捷图片，运行的程序主入口
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(context.getApplicationContext() , clazz));
        //发送广播。OK
        context.sendBroadcast(shortcutIntent);
    }

    /**
     * 创建插件的桌面快捷方式
     * @param context
     * @param name
     * @param icon
     * @param pluginId
     */
    public static void createPluginShortCut(Context context, String name,
                                            int icon, String pluginId) {

        if (!PluginManager.getInstance().isInstalled(context, pluginId)) {
            return;
        }
        if (hasShortcut(context, name)) {
            return;
        }
        try {
            String pluginIdWithoutAPK = pluginId;
            if(pluginId.endsWith(".apk")) {
                pluginIdWithoutAPK = pluginId.substring(0, pluginId.indexOf("."));
            }
            Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            //不允许重复创建
            shortcutIntent.putExtra("duplicate", false);
            //需要现实的名称
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
            //快捷图片
            Parcelable drawable = Intent.ShortcutIconResource.fromContext(context.getApplicationContext(), icon);
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, drawable);
            //点击快捷图片，运行的程序主入口
            PluginItem item = PluginManager.getInstance().buildPlugin(context, pluginIdWithoutAPK);
            String mainClass = ApkUtils.getMainClassName(context, item.getPluginPath());
            item.setMainClazz(mainClass);
            Intent intent = new Intent(context.getApplicationContext(), ProxyActivity.class);
            intent.putExtra("plugin_id", pluginIdWithoutAPK);
            intent.putExtra("plugin_classname", mainClass);
//            intent.putExtra("plugin_item", item);
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
            //发送广播。OK
            context.sendBroadcast(shortcutIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除快捷方式
     * @param activity
     * @param shortcutName
     */
    public static void deleteShortCut(Context activity,String shortcutName) {
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,shortcutName);
        Intent intent = new Intent();
        intent.setClass(activity, activity.getClass());
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,intent);
        activity.sendBroadcast(shortcut);
    }


    /**
     * 检查是否有快捷方式
     * @param activity
     * @param shortcutName
     * @return
     */
    public static boolean hasShortcut(Context activity,String shortcutName) {
        String url = "";
        int sdkVersion = Integer.parseInt(android.os.Build.VERSION.SDK);
        /*大于8的时候在com.android.launcher2.settings 里查询（未测试）*/
        if(sdkVersion < 8){
            url = "content://com.android.launcher.settings/favorites?notify=true";
        }else{
            url = "content://com.android.launcher2.settings/favorites?notify=true";
        }
        ContentResolver resolver = activity.getContentResolver();
        Cursor cursor = resolver.query(Uri.parse(url), null, "title=?",new String[] {shortcutName}, null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }
}
