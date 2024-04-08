package com.greatbee.core.bean.task;

import com.greatbee.base.bean.CreateDateBean;
import com.greatbee.base.bean.Identified;

import java.util.Date;

/**
 * Task Log
 * <p/>
 * Author: CarlChen
 * Date: 2017/11/30
 */
public class TaskLog implements Identified, CreateDateBean {
    private Integer id;
    private Date createDate;
    //执行任务ID
    private Integer taskId;
    //执行结果
    private String result;
    //耗时
    private Long timeConsuming;
    //开始时间
    private Date begin;
    //结束时间
    private Date finish;
    //备注
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

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

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getTimeConsuming() {
        return timeConsuming;
    }

    public void setTimeConsuming(Long timeConsuming) {
        this.timeConsuming = timeConsuming;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getFinish() {
        return finish;
    }

    public void setFinish(Date finish) {
        this.finish = finish;
    }
}
