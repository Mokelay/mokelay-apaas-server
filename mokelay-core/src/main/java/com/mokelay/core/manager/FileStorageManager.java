package com.mokelay.core.manager;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.manager.BasicManager;
import com.mokelay.core.bean.server.FileStorage;

/**
 * FileStorage Manager
 * <p/>
 * Author :CarlChen
 * Date:17/8/1
 */
public interface FileStorageManager extends BasicManager {
    public FileStorage getFileStorage(String serializeName) throws DBException;
}
