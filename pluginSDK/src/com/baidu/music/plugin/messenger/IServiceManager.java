package com.baidu.music.plugin.messenger;

/**
 * Created by Jarlene on 5/21 0021
 */
public interface IServiceManager {
    Object getService(String name);

    void releaseAll();
}
