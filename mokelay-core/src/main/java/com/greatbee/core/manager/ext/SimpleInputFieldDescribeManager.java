package com.greatbee.core.manager.ext;

import com.greatbee.base.manager.ext.AbstractBasicManager;
import com.greatbee.core.bean.server.InputFieldDescribe;
import com.greatbee.core.manager.InputFieldDescribeManager;

/**
 * Author :CarlChen
 * Date:17/7/24
 */
public class SimpleInputFieldDescribeManager extends AbstractBasicManager implements InputFieldDescribeManager {
    public SimpleInputFieldDescribeManager() {
        super(InputFieldDescribe.class);
    }
}
