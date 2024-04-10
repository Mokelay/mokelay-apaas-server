package com.mokelay.core.manager.ext;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.manager.ext.AbstractBasicManager;
import com.mokelay.core.bean.server.FileStorage;
import com.mokelay.core.manager.FileStorageManager;

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
