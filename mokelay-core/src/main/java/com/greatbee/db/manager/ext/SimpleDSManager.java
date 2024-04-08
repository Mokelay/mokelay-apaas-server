package com.greatbee.db.manager.ext;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.manager.ext.AbstractBasicManager;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.db.bean.oi.DS;
import com.greatbee.db.manager.DSManager;

import java.util.List;

/**
 * Simple DS Manager
 * <p/>
 * Created by CarlChen on 2017/5/24.
 */
public class SimpleDSManager extends AbstractBasicManager implements DSManager {
    public SimpleDSManager() {
        super(DS.class);
    }

    @Override
    public DS getDSByAlias(String alias) throws DBException {
        List<DS> dsList = this.list("alias", alias);
        if (CollectionUtil.isValid(dsList)) {
            return dsList.get(0);
        } else {
            return null;
        }
    }
}
