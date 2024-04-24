package com.mokelay.core.manager;

/**
 * TY Driver
 * <p>
 * Created by CarlChen on 16/10/11.
 */
public interface TYDriver {

    /**
     * FileStorageManager Manager
     *
     * @return
     */
    public FileStorageManager getFileStorageManager();

    /**
     * TY Cache Service
     * @return
     */
    TYCacheService getTyCacheService();
}
