package com.greatbee.core.manager;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.manager.BasicManager;
import com.greatbee.core.bean.server.API;

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
