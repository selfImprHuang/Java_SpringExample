package config.refreshEvent;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:/app.properties")
public class ContextRefreshEvent implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private Environment environment;

    /**
     * 程序启动，上下文刷新或者初始化的时候会调用该方法
     * Event raised when an {@code ApplicationContext} gets initialized or refreshed.
     * 这边是选择在 onStartup方法上去配置这个监听器，但是也可以直接用@WebListener去注册这个监听器
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //因为Spring有两个上下文容器，所以这边加上一个判断，让程序运营在住容器的时候就进行定时任务的执行.
        if (event.getApplicationContext().getParent() == null) {
            try {
                //"0 0 3/1 3 * ? * ";
                String timerTime = environment.getProperty("cron");
                Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("dummyTriggerName", "group1")
                    .withSchedule(CronScheduleBuilder.cronSchedule(timerTime))
                    .build();
                JobDetail job = JobBuilder.newJob(HelloJob.class)
                    .withIdentity("dummyJobName", "group1").build();
                Scheduler scheduler = new StdSchedulerFactory().getScheduler();
                scheduler.start();
                scheduler.scheduleJob(job, trigger);
                } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        }
    }

    class HelloJob implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.print("hello quartz ");
        }
    }

}



