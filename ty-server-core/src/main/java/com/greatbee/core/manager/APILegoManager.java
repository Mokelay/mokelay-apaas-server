package com.greatbee.core.manager;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.manager.BasicManager;
import com.greatbee.api.bean.server.APILego;

import java.util.List;

/**
 * API Lego Manager
 * <p/>
 * Author :CarlChen
 * Date:17/7/24
 */
public interface APILegoManager extends BasicManager {
    /**
     * 获取API Lego 列表，并且根据index升序
     *
     * @param apiAlias
     * @return
     * @throws DBException
     */
    public List<APILego> getAPILegoList(String apiAlias) throws DBException;
}
