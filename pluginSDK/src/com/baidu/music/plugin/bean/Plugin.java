/*
 * Copyright (C) 2014 The AndroidSimples Project
 */

package com.baidu.music.plugin.bean;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Jarlene on 5/21 0021.
 */
public class Plugin implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int STATE_DISCOVERY   = 0;/* 0:发现,未下载 */
    public static final int STATE_DOWNLOADED  = 1;/* 1 : 下载完成 */
    public static final int STATE_INSTALLED   = 2;/*2 : 完装完成*/
    public static final int STATE_ACTIVE      = 3;/*3 : 已激活*/
    public static final int STATE_NEED_UPDATE = 4;/* 4 : 需要更新最新版本*/

    private String title;

    private String desc;

    private String date;

    private String imageUrl;

    private String pkgName;

    private String activity;

    private String downloadUrl;

    private String file;

    private String size;

    private int versionCode;

    private int pluginStatus = STATE_DISCOVERY;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public void setPluginStatus(int status) {
        this.pluginStatus = status;
    }

    public int getPlguinStatus() {
        return pluginStatus;
    }

    public String localPath(String appPath) {
        String path = appPath + File.separatorChar + file;
        if (!path.endsWith(".apk")) {
            path = path.concat(".apk");
        }
        return path;
    }
}
