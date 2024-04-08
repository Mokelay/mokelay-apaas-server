package com.greatbee.core.manager.ext;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.manager.ext.AbstractBasicManager;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.core.bean.buzz.Code;
import com.greatbee.core.manager.CodeManager;

import java.util.List;

/**
 * Author :CarlChen
 * Date:17/7/24
 */
public class SimpleCodeManager extends AbstractBasicManager implements CodeManager {
    public SimpleCodeManager() {
        super(Code.class);
    }

    @Override
    public Code getCodeByAlias(String alias) throws DBException {
        List<Code> codes = this.list("alias",alias);

        if(CollectionUtil.isValid(codes)){
            return codes.get(0);
        }
        return null;
    }
}
