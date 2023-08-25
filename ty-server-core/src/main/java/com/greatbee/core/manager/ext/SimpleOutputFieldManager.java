package com.greatbee.core.manager.ext;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.manager.ext.AbstractBasicManager;
import com.greatbee.core.bean.server.OutputField;
import com.greatbee.core.manager.OutputFieldManager;

import java.util.List;

/**
 * Author :CarlChen
 * Date:17/7/24
 */
public class SimpleOutputFieldManager extends AbstractBasicManager implements OutputFieldManager {
    public SimpleOutputFieldManager() {
        super(OutputField.class);
    }

    @Override
    public List<OutputField> getOutputFields(int apiLegoId) throws DBException {
        return (List<OutputField>)this.list("apiLegoId",apiLegoId);
    }

    @Override
    public List<OutputField> getOutputFields(String apiLegoUuid) throws DBException {
        return (List<OutputField>)this.list("apiLegoUuid",apiLegoUuid);
    }
}
