package com.baidu.music.plugin.messenger;

import android.content.Context;
import android.os.Parcelable;
import android.text.TextUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * 主要负责插件与宿主app实时通讯
 * Created by Jarlene on 5/21 0021
 * @version 1.0
 * @createTime 2014年10月17日 上午10:10:12
 */
public abstract class HostSupportManager {

    public static final String NAME = "com.baidu.music.pluginsupport";

    private HashMap<String, Class<? extends SupportAction>> mSupportHashMap;

    public HostSupportManager() {
        initSupportActions();
    }

    /**
     * 初始化宿主支持动作
     */
    public abstract void initSupportActions();

    /**
     * 注册支持的Action
     *
     * @param action
     * @param supportActionClazz
     */
    public void addSupportAction(String action, Class<? extends SupportAction> supportActionClazz) {
        if (mSupportHashMap == null) {
            mSupportHashMap = new HashMap<String, Class<? extends SupportAction>>();
        }
        mSupportHashMap.put(action, supportActionClazz);
    }

    /**
     * 解注册Action
     *
     * @param action
     */
    public void removeSupportAction(String action) {
        if (mSupportHashMap != null && mSupportHashMap.containsKey(action)) {
            mSupportHashMap.remove(action);
        }
    }


    /**
     * 解注册所有的Action
     */
    public void removeAllSupportAction() {
        if (mSupportHashMap != null && !mSupportHashMap.isEmpty()) {
            mSupportHashMap.clear();
        }
    }


    /**
     * 获得宿主支持项
     *
     * @param context
     * @param supportType
     * @return
     */
    public SupportAction buildSupportAction(Context context, String supportType) {
        if (TextUtils.isEmpty(supportType)) {
            return null;
        }
        try {
            Class<? extends SupportAction> supportClazz = mSupportHashMap.get(supportType);
            if (supportClazz == null) {
                return null;
            }

            Constructor<? extends SupportAction> constructor =
                    supportClazz.getConstructor(Context.class);
            return constructor.newInstance(context);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 宿主支持动作
     *
     * @author yangzc
     * @version 1.0
     * @createTime 2014年10月17日 上午11:37:56
     */
    public static abstract class SupportAction {

        /**
         * 执行动作
         *
         * @param what   相当于方法
         * @param params 参数
         */
        public abstract Parcelable handleAction(String what, Parcelable params);

        protected Context mContext;

        public SupportAction(Context context) {
            this.mContext = context;
        }
    }

}
