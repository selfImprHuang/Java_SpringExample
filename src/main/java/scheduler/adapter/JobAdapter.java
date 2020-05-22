/*
 * @(#) JobAdapter
 * 版权声明 黄志军， 版权所有 违者必究
 *
 * <br> Copyright:  Copyright (c) 2018
 * <br> Company:黄志军
 * <br> @author selfImpr
 * <br> 2018-05-25 11:33:37
 * <br> @description
 *
 *
 */

package scheduler.adapter;


import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * jobAdapter 适配器可能是一个需要实现的类也可能是一个制造这个使用类的制造类
 * 明显的，这里就是一个需要实现的类。
 */
public class JobAdapter extends QuartzJobBean {

    private Logger logger = Logger.getLogger(JobAdapter.class);

    /**
     *     需要的job 类的接口<br><br>
     *     适配器的目的是跳过new的过程，调用接口去找到他的具体实现类，调用实现类的方法
     */
    private BaseJob baseJob;

    /**
     * 跟调度相关的调度任务名称
     */
    private String name;

    /**
     * 跟调度相关的调度组的名称
     */
    private String group;


    /**
     * propertis里面的name
     */
    private String propertyKey;

    /**
     *  propertis里面的value
     */
    private String propertyValue;

    /**
     * job生成的trigger的名称
     */
    private String triggerName;

    /**
     * job生成的tigger的group
     */
    private String triggerGroup;

    /**
     * 表达式
     */
    private String cronExpression;

    public JobAdapter() {
    }

    /**
     * 三个可能必须参数
     * @param baseJob job实例
     * @param name 名称
     * @param propertyKey 配置表达式的key
     */
    public JobAdapter(BaseJob baseJob, String name, String propertyKey) {
        this.baseJob = baseJob;
        this.name = name;
        this.propertyKey = propertyKey;
        //这边要怎么去获取，有多找那个方式，但是用spring的方式还是用java的方式？
        if (propertyKey != null) {
            this.cronExpression = "0 0 3/1 3 * ? * ";
        }
    }

    /**
     * 三个必须参数
     * @param name
     * @param propertyValue
     * @param baseJob
     */
    public JobAdapter(String name, String propertyValue, BaseJob baseJob) {
        this.baseJob = baseJob;
        this.name = name;
        this.propertyValue = propertyValue;
        this.cronExpression = propertyValue;
    }


    @Override
    protected void executeInternal(JobExecutionContext context) {
        try {
            baseJob.doScheduleTask();
        } catch (Exception e) {
            logger.error(baseJob.getClass().getName() + "该任务不存在", e);
        }
    }

    public BaseJob getBaseJob() {
        return baseJob;
    }

    public void setBaseJob(BaseJob baseJob) {
        this.baseJob = baseJob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getCronExpression() {
        return cronExpression;
    }
}
