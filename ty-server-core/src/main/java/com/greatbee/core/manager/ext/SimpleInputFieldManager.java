package com.greatbee.core.manager.ext;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.manager.ext.AbstractBasicManager;
import com.greatbee.core.bean.server.InputField;
import com.greatbee.core.manager.InputFieldManager;

import java.util.List;

/**
 * Author :CarlChen
 * Date:17/7/24
 */
public class SimpleInputFieldManager extends AbstractBasicManager implements InputFieldManager {
    public SimpleInputFieldManager() {
        super(InputField.class);
    }

    @Override
    public List<InputField> getInputFields(int apiLegoId) throws DBException {
        return (List<InputField>)this.list("apiLegoId",apiLegoId);
    }

    @Override
    public List<InputField> getInputFields(String apiLegoUuid) throws DBException {
        return (List<InputField>)this.list("apiLegoUuid",apiLegoUuid);
    }
}
