/*
 * @(#) JobScheduler
 * 版权声明 黄志军， 版权所有 违者必究
 *
 * <br> Copyright:  Copyright (c) 2018
 * <br> Company:黄志军
 * <br> @author selfImpr
 * <br> 2018-05-25 11:36:07
 * <br> @description 调度job
 *
 *
 */

package scheduler;

import scheduler.adapter.JobAdapter;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Trigger;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 这个调度器也应该是继承调度接口，把job放进来以后，我会生成为一个调度的任务Bean
 */
public class JobScheduler extends SchedulerFactoryBean {

    private List<Trigger> triggers = new ArrayList<>();

    private List<JobAdapter> jobAdapters = new ArrayList<>();

    public void addJob(JobAdapter job) throws ParseException {
        if (StringUtils.isNotEmpty(job.getCronExpression())) {
            jobAdapters.add(job);
            createAndAddTrigger();
        } else {
            logger.info("因为你添加的这个job的cron表单式为：" + job.getCronExpression() + "所以这个任务不添加到调用队列");
        }

    }

    private void createAndAddTrigger() {
        for (JobAdapter jobAdapter : jobAdapters) {
            JobDetailFactoryBean jobDetail = new JobDetailFactoryBean();
            jobDetail.setJobClass(jobAdapter.getClass());
            jobDetail.setName(jobAdapter.getName());
            jobDetail.setGroup(jobAdapter.getGroup());
            jobDetail.afterPropertiesSet();
            CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
            // 这个设置名称的这个是不是需要被指定？
            cronTriggerFactoryBean.setName(jobAdapter.getTriggerName() == null ? jobAdapter.getName() + UUID.randomUUID().toString()
                .replace("-", "") : jobAdapter.getTriggerName());
            cronTriggerFactoryBean.setJobDetail(jobDetail.getObject());
            cronTriggerFactoryBean.setCronExpression(jobAdapter.getCronExpression());
            try {
                cronTriggerFactoryBean.afterPropertiesSet();
            } catch (ParseException e) {
                logger.error("trigger初始化错误了", e);
            }
            triggers.add(cronTriggerFactoryBean.getObject());
        }
    }


    public void afterProperties() {
        Trigger[] t1 = new Trigger[]{};
        setTriggers(triggers.toArray(t1));
    }
}
