package com.baidu.music.plugin.messenger;

/**
 * Created by Jarlene on 5/21 0021
 */
public class ServiceProvider {

    private static ServiceProvider sInstance       = null;
    private        IServiceManager mServiceManager = null;

    private ServiceProvider() {
    }

    /**
     * 单例
     * @return
     */
    public static ServiceProvider getServiceProvider() {
        if (sInstance == null) {
            synchronized (ServiceProvider.class) {
                if (sInstance == null) {
                    sInstance = new ServiceProvider();
                }
            }
        }

        return sInstance;
    }

    /**
     * 注册信息
     * @param sensor
     */
    public void registServiceManager(IServiceManager sensor) {
        this.mServiceManager = sensor;
    }

    public IServiceManager getServiceManager() {
        return this.mServiceManager;
    }
}
