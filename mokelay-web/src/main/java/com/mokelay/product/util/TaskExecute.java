package com.mokelay.product.util;

import com.greatbee.base.bean.DBException;
import com.greatbee.core.bean.task.TaskLog;
import com.greatbee.core.manager.TYDriver;
import com.mokelay.product.MokelayUtil;
import com.mokelay.product.Response;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * TaskExecute 必须是一个公共类
 *
 * @author xiaobc
 * @date 17/12/18
 */
public class TaskExecute implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String apiAlias = context.getMergedJobDataMap().getString("apiAlias");
        Integer taskId = context.getMergedJobDataMap().getInt("taskId");
        TYDriver tyDriver = (TYDriver) context.getMergedJobDataMap().get("tyDriver");

        TaskLog taskLog = new TaskLog();
        long begin = System.currentTimeMillis();
        taskLog.setCreateDate(new Date(begin));
        taskLog.setBegin(new Date(begin));

        Response response = MokelayUtil.executeAPIAlias(apiAlias, tyDriver, null, null,true);

        long end = System.currentTimeMillis();
        taskLog.setFinish(new Date(end));

        taskLog.setTaskId(taskId);
        taskLog.setTimeConsuming(end - begin);

        if (response.isOk()) {
            taskLog.setResult("SUCCESS");
        } else {
            taskLog.setResult("FAIL");
            StringBuilder remark = new StringBuilder();
            remark.append("Code: ").append(response.getCode()).append(",Message:").append(response.getMessage());
            taskLog.setRemark(remark.toString());
        }

        //TODO Task Log的记录需要可配置，方案待定 2024-04-10
//        try {
//            tyDriver.getTaskLogManager().add(taskLog);
//        } catch (DBException e) {
//            e.printStackTrace();
//        }
    }
}
