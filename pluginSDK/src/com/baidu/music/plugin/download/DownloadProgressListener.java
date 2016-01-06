package com.baidu.music.plugin.download;

/**
 * 下载插件监听
 * <p/>
 * Created by Jarlene on 5/21 0021.
 */
public interface DownloadProgressListener {
    void onDownloadSize(long size, int percent);
}
