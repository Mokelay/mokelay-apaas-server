package com.greatbee.core.bean.auth;

import com.greatbee.base.bean.AliasBean;
import com.greatbee.base.bean.IND;
import com.greatbee.base.bean.ext.SimpleIND;

/**
 * 授权类型
 * <p/>
 * Author: CarlChen
 * Date: 2017/12/14
 */
public class AuthType extends SimpleIND implements IND, AliasBean {
    //别名
    private String alias;
    //判断执行的API
    private String judgeAPIAlias;
    //没有权限的错误代码
    private Long noAuthErrorCode;

    @Override
    public String getAlias() {
        return alias;
    }

    public String getJudgeAPIAlias() {
        return judgeAPIAlias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setJudgeAPIAlias(String judgeAPIAlias) {
        this.judgeAPIAlias = judgeAPIAlias;
    }

    public Long getNoAuthErrorCode() {
        return noAuthErrorCode;
    }

    public void setNoAuthErrorCode(Long noAuthErrorCode) {
        this.noAuthErrorCode = noAuthErrorCode;
    }
}
