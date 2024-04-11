package com.mokelay.db.manager;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.manager.BasicManager;
import com.mokelay.db.bean.oi.OI;

/**
 * Created by CarlChen on 16/10/11.
 */
public interface OIManager extends BasicManager {
    /**
     * 根据Alias获取OI
     *
     * @param alias
     * @return
     * @throws DBException
     */
    public OI getOIByAlias(String alias) throws DBException;

    /**
     * 通过resource获取OI
     *
     * @param resource
     * @return
     * @throws DBException
     */
    public OI getOIByResource(String dsAlias  , String resource) throws  DBException;
}
