package com.baidu.music.plugin.utils;

/**
 * Created by Jarlene on 5/21 0021
 */
public class PluginUtils {

    /**
     * 插件常量，用于控制逻辑处理
     */

    public static final String PLUGIN_ID              = "plugin_id"; //插件ID
    public static final String PLUGIN_CLASS_NAME      = "plugin_classname"; //插件Activity或者Service name
    public static final String PLUGIN_ITEM            = "plugin_item"; //插件Item信息
    public static final String PLUGIN_BUNDLE          = "plugin_bundle"; //插件传递Bundle
    public static final String PLUGIN_BUILD_TYPE      = "plugin_build_type"; //插件编译方式
    public static final String PLUGIN_REQUEST_CODE    = "plugin_result_code"; //启动插件返回code
    public static final String PLUGIN_PROXYCLASS_TYPE = "plugin_proxy_class_type"; // 插件是Activity 或者是FragmentActivity


    public static final String PLUGIN_SUPPORT_TYPE = "plugin_support_type"; //与插件实时通讯的类名
    public static final String PLUGIN_METHOD       = "plugin_method";  //插件想要调用宿主的方法
    public static final String PLUGIN_PRARMS       = "plugin_params"; //插件回传给宿主的参数
    public static final String PLUGIN_RESULT       = "plugin_result"; //宿主返回给插件的结果
    public static final String PLUGIN_UUID         = "plugin_uuid";  //插件与宿主通讯的唯一标志。

    //service
    public static final String PLUGIN_ACTION_SERVICE  = "com.baidu.music.plugin.action.service";
    //activity
    public static final String PLUGIN_ACTION_LAUNCH   = "com.baidu.music.plugin.action.activity";
    //BroadcastReceiver
    public static final String PLUGIN_ACTION_RECEIVER = "com.baidu.music.plugin.action.receiver";
    //provider
    public static final String PLUGIN_ACTION_PROVIDER = "com.baidu.music.plugin.action.provider";

}
