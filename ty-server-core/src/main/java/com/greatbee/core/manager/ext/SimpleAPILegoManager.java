package com.greatbee.core.manager.ext;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.manager.ext.AbstractBasicManager;
import com.greatbee.core.bean.server.APILego;
import com.greatbee.core.manager.APILegoManager;

import java.util.List;

/**
 * Author :CarlChen
 * Date:17/7/24
 */
public class SimpleAPILegoManager extends AbstractBasicManager implements APILegoManager {
    public SimpleAPILegoManager() {
        super(APILego.class);
    }

    /**
     * 获取API Lego 列表，并且根据index升序
     *
     * @param apiAlias
     * @return
     * @throws DBException
     */
    @Override
    public List<APILego> getAPILegoList(String apiAlias) throws DBException {
        List<APILego> als = this.list("apiAlias",apiAlias,"index",true);

        return als;
    }
}
