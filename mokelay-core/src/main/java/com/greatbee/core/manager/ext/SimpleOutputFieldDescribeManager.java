package com.greatbee.core.manager.ext;

import com.greatbee.base.manager.ext.AbstractBasicManager;
import com.greatbee.core.bean.server.OutputFieldDescribe;
import com.greatbee.core.manager.OutputFieldDescribeManager;

/**
 * Author :CarlChen
 * Date:17/7/24
 */
public class SimpleOutputFieldDescribeManager extends AbstractBasicManager implements OutputFieldDescribeManager {
    public SimpleOutputFieldDescribeManager() {
        super(OutputFieldDescribe.class);
    }
}
