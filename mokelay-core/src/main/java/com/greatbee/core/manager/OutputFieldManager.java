package com.greatbee.core.manager;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.manager.BasicManager;
import com.greatbee.api.bean.server.OutputField;

import java.util.List;

/**
 * Author :CarlChen
 * Date:17/7/24
 */
public interface OutputFieldManager extends BasicManager {
    /**
     * 通过apiLegoId获取outputfield列表
     *
     * @param apiLegoId
     * @return
     * @throws DBException
     */
    public List<OutputField> getOutputFields(int apiLegoId) throws DBException;

    /**
     * 通过apiLegoUuid获取outputfield列表
     *
     * @param apiLegoUuid
     * @return
     * @throws DBException
     */
    public List<OutputField> getOutputFields(String apiLegoUuid) throws DBException;
}
