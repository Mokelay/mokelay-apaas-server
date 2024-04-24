package com.mokelay.core.manager;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.manager.BasicManager;
import com.mokelay.core.bean.auth.AuthType;

import java.util.List;

/**
 * Auth Type Manager
 * <p/>
 * Author: CarlChen
 * Date: 2017/12/14
 */
public interface AuthTypeManager extends BasicManager {
    /**
     * 根据alias 查询AuthType详情
     *
     * @param alias
     * @return
     */
    public AuthType getAuthTypeByAlias(String alias) throws DBException;
}
