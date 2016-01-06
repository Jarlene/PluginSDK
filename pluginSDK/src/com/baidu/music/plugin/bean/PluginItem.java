/*
 * Copyright (C) 2014 The AndroidSimples Project
 */

package com.baidu.music.plugin.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jarlene on 5/21 0021.
 */
public class PluginItem implements Parcelable {

    public static enum BUILD_TYPE {
        SHARED(1),
        SUPER(2),
        CUSTOM(3);

        private int value;

        private BUILD_TYPE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private String pluginId;

    private String pluginPath = "";

    private BUILD_TYPE buildType = BUILD_TYPE.CUSTOM;

    private int versionCode = 1;

    private String versionName;

    //    private Context context;

    private String dexPath;

    private String libPath;

    private String mainClazz;

    public PluginItem(Parcel source) {
        this.pluginId = source.readString();
        this.pluginPath = source.readString();
        this.buildType =
                source.readInt() == BUILD_TYPE.SHARED.getValue() ? BUILD_TYPE.SHARED : BUILD_TYPE
                        .CUSTOM;
        this.versionCode = source.readInt();
        this.versionName = source.readString();
        this.dexPath = source.readString();
        this.libPath = source.readString();
        this.mainClazz = source.readString();
    }

    public PluginItem(String pluginId, String pluginPath) {
        this.pluginId = pluginId;
        this.pluginPath = pluginPath;
    }

    public String getPluginId() {
        return pluginId;
    }

    public String getPluginPath() {
        return pluginPath;
    }

    public BUILD_TYPE getBuildType() {
        return buildType;
    }

    public void setBuildType(BUILD_TYPE buildType) {
        this.buildType = buildType;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    //    public Context getContext() {
    //        return context;
    //    }
    //
    //    public void setContext(Context context) {
    //        this.context = context;
    //    }

    public String getDexPath() {
        return dexPath;
    }

    public void setDexPath(String dexPath) {
        this.dexPath = dexPath;
    }

    public String getLibPath() {
        return libPath;
    }

    public void setLibPath(String libPath) {
        this.libPath = libPath;
    }

    public String getMainClazz() {
        return mainClazz;
    }

    public void setMainClazz(String mainClazz) {
        this.mainClazz = mainClazz;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pluginId);
        dest.writeString(pluginPath);
        dest.writeInt(buildType.getValue());
        dest.writeInt(versionCode);
        dest.writeString(versionName);
        dest.writeString(dexPath);
        dest.writeString(libPath);
        dest.writeString(mainClazz);
    }

    public static final Parcelable.Creator<PluginItem> CREATOR = new Creator<PluginItem>() {

        @Override
        public PluginItem createFromParcel(Parcel source) {
            return new PluginItem(source);
        }

        @Override
        public PluginItem[] newArray(int size) {
            return new PluginItem[size];
        }
    };
}
