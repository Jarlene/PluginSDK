package com.baidu.music.plugin.manager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jarlene on 5/21 0021.
 */
public abstract class BaseManager<K, T> {

    private Map<K, T> mHaspMap = Collections.synchronizedMap(new HashMap<K, T>());

    public void addItem(K key, T value) {
        if (mHaspMap == null) {
            mHaspMap = new HashMap<K, T>();
        }
        mHaspMap.put(key, value);
    }

    public void removeItem(K key) {
        if (mHaspMap != null && mHaspMap.containsKey(key)) {
            mHaspMap.remove(key);
        }
    }


    public Map<K, T> getHashmap() {
        return mHaspMap;
    }

    public T getItem(K key) {
        return mHaspMap.get(key);
    }
}
