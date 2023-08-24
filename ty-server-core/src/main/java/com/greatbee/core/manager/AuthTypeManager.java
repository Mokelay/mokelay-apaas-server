package com.greatbee.core.manager;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.manager.BasicManager;
import com.greatbee.core.bean.auth.AuthType;

/**
 * Auth Type Manager
 * <p/>
 * Author: CarlChen
 * Date: 2017/12/14
 */
public interface AuthTypeManager extends BasicManager {
    /**
     * 根据ds的alias 查询DS详情
     *
     * @param alias
     * @return
     */
    public AuthType getAuthTypeByAlias(String alias) throws DBException;
}
