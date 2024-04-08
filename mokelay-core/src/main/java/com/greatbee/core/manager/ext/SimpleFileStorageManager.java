package com.greatbee.core.manager.ext;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.manager.ext.AbstractBasicManager;
import com.greatbee.core.bean.server.FileStorage;
import com.greatbee.core.manager.FileStorageManager;

import java.util.List;

/**
 * Author :CarlChen
 * Date:17/8/1
 */
public class SimpleFileStorageManager extends AbstractBasicManager implements FileStorageManager {
    public SimpleFileStorageManager() {
        super(FileStorage.class);
    }

    @Override
    public FileStorage getFileStorage(String serializeName) throws DBException {
        List<FileStorage> fs = this.list("serializeName",serializeName);

        return fs.size()>0?fs.get(0):null;
    }
}
