package com.greatbee.core.bean.client;

import com.greatbee.base.bean.IND;
import com.greatbee.base.bean.ext.SimpleIND;

/**
 * Author: CarlChen
 * Date: 2017/8/14
 */
public class Interactive extends SimpleIND implements IND {
    //所属PageBB
    private Integer pbbId;
    //事件名
    private String triggerEventName;

    //执行类型
    private String executeType;
    //触发的方法
    private Integer executePbbId;
    private String executeBBMethodName;

    //存储的触发脚本
    private String executeScript;

    public String getExecuteType() {
        return executeType;
    }

    public void setExecuteType(String executeType) {
        this.executeType = executeType;
    }

    public Integer getPbbId() {
        return pbbId;
    }

    public void setPbbId(Integer pbbId) {
        this.pbbId = pbbId;
    }

    public String getTriggerEventName() {
        return triggerEventName;
    }

    public void setTriggerEventName(String triggerEventName) {
        this.triggerEventName = triggerEventName;
    }

    public Integer getExecutePbbId() {
        return executePbbId;
    }

    public void setExecutePbbId(Integer executePbbId) {
        this.executePbbId = executePbbId;
    }

    public String getExecuteBBMethodName() {
        return executeBBMethodName;
    }

    public void setExecuteBBMethodName(String executeBBMethodName) {
        this.executeBBMethodName = executeBBMethodName;
    }

    public String getExecuteScript() {
        return executeScript;
    }

    public void setExecuteScript(String executeScript) {
        this.executeScript = executeScript;
    }
}

enum ExecuteType {
    //触发方法
    TriggerMethod("trigger_method"),
    //自定义脚本
    CustomScript("custom_script");
    private String type;

    ExecuteType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
