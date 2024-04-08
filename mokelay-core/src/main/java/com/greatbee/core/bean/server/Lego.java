package com.greatbee.core.bean.server;

import com.greatbee.base.bean.AliasBean;
import com.greatbee.base.bean.IND;
import com.greatbee.base.bean.ext.SimpleIND;

/**
 * Lego
 * <p/>
 * Author :CarlChen
 * Date:17/7/7
 */
public class Lego extends SimpleIND implements IND, AliasBean {

    public Lego() {
    }

    private String alias;

    private String supportDST;

    private String icon;

    private Boolean generateSupport;//是否支持一键生成

    private Integer useNum;//使用数量

    @Override
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSupportDST() {
        return supportDST;
    }

    public void setSupportDST(String supportDST) {
        this.supportDST = supportDST;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean isGenerateSupport() {
        return generateSupport;
    }

    public void setGenerateSupport(Boolean generateSupport) {
        this.generateSupport = generateSupport;
    }

    public Integer getUseNum() {
        return useNum;
    }

    public void setUseNum(Integer useNum) {
        this.useNum = useNum;
    }
}
