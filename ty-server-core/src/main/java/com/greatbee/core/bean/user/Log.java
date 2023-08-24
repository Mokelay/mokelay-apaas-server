package com.greatbee.core.bean.user;

import com.greatbee.base.bean.CreateDateBean;
import com.greatbee.base.bean.Identified;

import java.util.Date;

/**
 * Log
 *
 * @author xiaobc
 * @date 17/11/15
 */
public class Log implements Identified, CreateDateBean {

    private Integer id;
    private Date createDate;
    private String operateUserAlias;//操作用户别名
    private String operateUserName;//操作用户名称
    private String operateApi;//操作的API
    private String operateLego;//操作Lego
    private String input;//输入参数  json格式
    private String output;//输出参数  json格式
    private String content;//操作内容  比如更新a字段值变成了b字段值
    private String operateOi;//操作oi

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getOperateUserAlias() {
        return operateUserAlias;
    }

    public void setOperateUserAlias(String operateUserAlias) {
        this.operateUserAlias = operateUserAlias;
    }

    public String getOperateUserName() {
        return operateUserName;
    }

    public void setOperateUserName(String operateUserName) {
        this.operateUserName = operateUserName;
    }

    public String getOperateApi() {
        return operateApi;
    }

    public void setOperateApi(String operateApi) {
        this.operateApi = operateApi;
    }

    public String getOperateLego() {
        return operateLego;
    }

    public void setOperateLego(String operateLego) {
        this.operateLego = operateLego;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOperateOi() {
        return operateOi;
    }

    public void setOperateOi(String operateOi) {
        this.operateOi = operateOi;
    }
}
