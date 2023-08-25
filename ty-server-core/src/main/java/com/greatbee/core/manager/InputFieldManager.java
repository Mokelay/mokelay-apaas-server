package com.greatbee.core.manager;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.manager.BasicManager;
import com.greatbee.core.bean.server.InputField;

import java.util.List;

/**
 * Author :CarlChen
 * Date:17/7/24
 */
public interface InputFieldManager extends BasicManager {
    /**
     * 通过APILego ID获取InoutField列表
     *
     * @param apiLegoId
     * @return
     * @throws DBException
     */
    public List<InputField> getInputFields(int apiLegoId) throws DBException;

    /**
     * 通过APILego UUid获取InoutField列表
     *
     * @param apiLegoUuid
     * @return
     * @throws DBException
     */
    public List<InputField> getInputFields(String apiLegoUuid) throws DBException;
}
