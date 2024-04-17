package com.mokelay.product;

import com.mokelay.base.bean.DBException;
import com.mokelay.base.util.BooleanUtil;
import com.mokelay.base.util.CollectionUtil;
import com.mokelay.core.bean.task.Task;
import com.mokelay.core.lego.system.TYPPC;
import com.mokelay.core.manager.TYDriver;
import com.mokelay.product.util.TaskExecute;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * TY Task 入口
 * <p/>
 * 从数据库中读取Task列表，并且加入进quartz中
 * <p/>
 * Author: CarlChen
 * Date: 2017/11/29
 */
@Service("tyTask")
public class MokelayTask {
    private static final Logger logger = Logger.getLogger(MokelayTask.class);

    @Autowired
    private TYDriver tyDriver;

    @PostConstruct
    public void buildTask() {
        if (BooleanUtil.toBool(TYPPC.getTYProp("ty.job.enable"))) {
            System.out.println("Begin to build task....");

            List<Task> tasks = tyDriver.getTyCacheService().listTasks();

            if (CollectionUtil.isValid(tasks)) {
                SchedulerFactory schedulerfactory = new StdSchedulerFactory();//通过schedulerFactory获取一个调度器
                try {
                    Scheduler scheduler = schedulerfactory.getScheduler();//通过schedulerFactory获取一个调度器

                    for (Task task : tasks) {
                        JobDataMap jobDataMap = new JobDataMap();
                        jobDataMap.put("taskId", task.getId());
                        jobDataMap.put("apiAlias", task.getApiAlias());
                        jobDataMap.put("tyDriver", tyDriver);
                        JobDetail job = JobBuilder.newJob(TaskExecute.class)
                                .setJobData(jobDataMap)
                                .withIdentity(task.getAlias(), task.getGroup()).build();

                        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(task.getAlias(), task.getGroup())
                                .withSchedule(CronScheduleBuilder.cronSchedule(task.getCron()))
                                .startNow().build();

                        scheduler.scheduleJob(job, trigger);//把作业和触发器注册到任务调度中
                    }

                    scheduler.start();//启动调度
                } catch (SchedulerException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
