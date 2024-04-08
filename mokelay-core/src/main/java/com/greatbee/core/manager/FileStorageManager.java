package com.greatbee.core.manager;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.manager.BasicManager;
import com.greatbee.core.bean.server.FileStorage;

/**
 * FileStorage Manager
 * <p/>
 * Author :CarlChen
 * Date:17/8/1
 */
public interface FileStorageManager extends BasicManager {
    public FileStorage getFileStorage(String serializeName) throws DBException;
}
