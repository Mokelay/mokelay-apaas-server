package com.greatbee.core.manager;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.manager.BasicManager;
import com.greatbee.core.bean.buzz.Code;

/**
 * Author :CarlChen
 * Date:17/7/24
 */
public interface CodeManager extends BasicManager {
    /**
     * 通过ALias获取Code
     *
     * @param alias
     * @return
     * @throws DBException
     */
    public Code getCodeByAlias(String alias) throws DBException;
}
