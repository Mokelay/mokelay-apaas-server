package com.mokelay.core.manager;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.manager.BasicManager;
import com.mokelay.core.bean.server.API;

/**
 * Author :CarlChen
 * Date:17/7/24
 */
public interface APIManager extends BasicManager {
    /**
     * 通过ALias获取API
     *
     * @param alias
     * @return
     * @throws DBException
     */
    public API getAPIByAlias(String alias) throws DBException;
}
