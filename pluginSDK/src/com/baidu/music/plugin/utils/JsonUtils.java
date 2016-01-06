package com.baidu.music.plugin.utils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import android.text.TextUtils;

/**
 * 用于插件数据通信，主要通过String来传递值
 * Created by Jarlene on 5/21 0021.
 */
public class JsonUtils {

    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    /**
     * 将json字符串转换为数据t
     * @param json
     * @param t
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T parse(String json, T t) throws JsonSyntaxException {

        if (TextUtils.isEmpty(json)) {
            return null;
        }
        if (gson == null) {
            gson = new Gson();
        }
        return (T) gson.fromJson(json, ((Object) t).getClass());
    }

    /**
     * 将对象转换成json格式
     *
     * @param ts
     * @return
     */
    public static String objectToJson(Object ts) {
        if (ts == null) {
            return null;
        }
        String jsonStr = null;
        if (gson == null) {
            gson = new Gson();
        }
        jsonStr = gson.toJson(ts);
        return jsonStr;
    }

    /**
     * 将json格式转换成list对象
     *
     * @param jsonStr
     * @return
     */
    public static List<?> jsonToList(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        List<?> objList = null;
        if (gson == null) {
            gson = new Gson();
        }
        Type type = new TypeToken<List<?>>() {}.getType();
        objList = gson.fromJson(jsonStr, type);
        return objList;
    }

    /**
     * 将json格式转换成list对象，并准确指定类型
     *
     * @param jsonStr
     * @param type
     * @return
     */
    public static List<?> jsonToList(String jsonStr, Type type) {
        if (TextUtils.isEmpty(jsonStr) || type == null) {
            return null;
        }
        List<?> objList = null;
        if (gson == null) {
            gson = new Gson();
        }
        objList = gson.fromJson(jsonStr, type);
        return objList;
    }

    /**
     * 将json格式转换成map对象
     *
     * @param jsonStr
     * @return
     */
    public static Map<?, ?> jsonToMap(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        Map<?, ?> objMap = null;
        if (gson == null) {
            gson = new Gson();
        }
        Type type = new TypeToken<Map<?, ?>>() {}.getType();
        objMap = gson.fromJson(jsonStr, type);
        return objMap;
    }

    /**
     * 将json转换成bean对象
     *
     * @param jsonStr
     * @return
     */
    public static Object jsonToBean(String jsonStr, Class<?> clazz) {
        if (TextUtils.isEmpty(jsonStr) || clazz == null) {
            return null;
        }
        Object obj = null;
        if (gson == null) {
            gson = new Gson();
        }
        obj = gson.fromJson(jsonStr, clazz);
        return obj;
    }

    /**
     * 根据Jos中的key值获取对象
     *
     * @param jsonStr
     * @param key
     * @return
     */
    public static Object getJsonValue(String jsonStr, String key) {
        Object resultObj = null;
        Map<?, ?> resultMap = jsonToMap(jsonStr);
        if (resultMap != null && resultMap.containsKey(key)) {
            resultObj = resultMap.get(key);
        }
        return resultObj;
    }
}
