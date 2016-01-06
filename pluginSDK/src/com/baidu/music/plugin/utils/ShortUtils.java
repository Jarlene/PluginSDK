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
 * ���������ݷ�ʽ
 * Created by Jarlene on 5/21 0021
 */
public class ShortUtils {
    /**
     * ����������
     * @param context ������
     * @param name ��ʾ����
     * @param icon ��ʾͼ��
     * @param clazz �������class
     */
    public static void createShortCut(Context context, int name, int icon, Class<?> clazz) {
        if (hasShortcut(context,  context.getString(name))) {
            return;
        }
        Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //�������ظ�����
        shortcutIntent.putExtra("duplicate", false);
        //��Ҫ��ʵ������
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(name));
        //���ͼƬ
        Parcelable drawable = Intent.ShortcutIconResource.fromContext(context.getApplicationContext(), icon);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, drawable);
        //������ͼƬ�����еĳ��������
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(context.getApplicationContext() , clazz));
        //���͹㲥��OK
        context.sendBroadcast(shortcutIntent);
    }

    /**
     * ����������
     * @param context ������
     * @param name ��ʾ����
     * @param icon ��ʾͼ��
     * @param clazz �������class
     */
    public static void createShortCut(Context context, String name, int icon, Class<?> clazz) {

        if (hasShortcut(context, name)) {
            return;
        }
        Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //�������ظ�����
        shortcutIntent.putExtra("duplicate", false);
        //��Ҫ��ʵ������
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        //���ͼƬ
        Parcelable drawable = Intent.ShortcutIconResource.fromContext(context.getApplicationContext(), icon);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, drawable);
        //������ͼƬ�����еĳ��������
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(context.getApplicationContext() , clazz));
        //���͹㲥��OK
        context.sendBroadcast(shortcutIntent);
    }

    /**
     * ��������������ݷ�ʽ
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
            //�������ظ�����
            shortcutIntent.putExtra("duplicate", false);
            //��Ҫ��ʵ������
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
            //���ͼƬ
            Parcelable drawable = Intent.ShortcutIconResource.fromContext(context.getApplicationContext(), icon);
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, drawable);
            //������ͼƬ�����еĳ��������
            PluginItem item = PluginManager.getInstance().buildPlugin(context, pluginIdWithoutAPK);
            String mainClass = ApkUtils.getMainClassName(context, item.getPluginPath());
            item.setMainClazz(mainClass);
            Intent intent = new Intent(context.getApplicationContext(), ProxyActivity.class);
            intent.putExtra("plugin_id", pluginIdWithoutAPK);
            intent.putExtra("plugin_classname", mainClass);
//            intent.putExtra("plugin_item", item);
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
            //���͹㲥��OK
            context.sendBroadcast(shortcutIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * ɾ����ݷ�ʽ
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
     * ����Ƿ��п�ݷ�ʽ
     * @param activity
     * @param shortcutName
     * @return
     */
    public static boolean hasShortcut(Context activity,String shortcutName) {
        String url = "";
        int sdkVersion = Integer.parseInt(android.os.Build.VERSION.SDK);
        /*����8��ʱ����com.android.launcher2.settings ���ѯ��δ���ԣ�*/
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
