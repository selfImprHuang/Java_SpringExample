

package listener;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import listener.task.FileTimerTask;

/**
 * 通过ServletContextListener监听器在Servlet容器初始化和销毁的时候进行处理
 *
 * 　当Servlet 容器启动或终止Web 应用时，会触发ServletContextEvent 事件，该事件由ServletContextListener 来处理。在 ServletContextListener
 *   接口中定义了处理ServletContextEvent 事件的两个方法。
 * @author 志军
 */
@WebListener
public class SchemeTaskListener implements ServletContextListener {

    /**
     * 当Servlet 容器启动Web 应用时调用该方法。在调用完该方法之后，容器再对Filter 初始化，
     * 并且对那些在Web 应用启动时就需要被初始化的Servlet 进行初始化。
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //这边模仿处理一个文件定时删除的任务，当一个Servlet容器创建的时候会进行删除操作，就好像之前有接触过的数据库表配置初始化,大概类似这种的用法.
        //ScheduledExecutorService
        Timer timer = new Timer();
        FileTimerTask fileTimerTask = new FileTimerTask(sce.getServletContext().getRealPath("/"));
        //设置开始执行的时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        //Timer有四个schedule方法和两个scheduleAtFixedRate方法，好像前者的执行更实时，schedule方法分别对应单次执行和延迟多久后执行
        timer.schedule(fileTimerTask, date);

        //-----------------------------------这边是alibaba的校验规则检测出来的，用ScheduledExecutorService的方式来代替Timer处理会更好一点

        //多线程并行处理定时任务时，Timer运行多个TimeTask时，只要其中之一没有捕获抛出的异常，其它任务便会自动终止运行，使用ScheduledExecutorService则没有这个问题。
        //org.apache.commons.lang3.concurrent.BasicThreadFactory
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());
        executorService.scheduleAtFixedRate(fileTimerTask, 1000, 60 * 10, TimeUnit.HOURS);
    }

    /**
     * 当Servlet 容器终止Web 应用时调用该方法。在调用该方法之前，容器会先销毁所有的Servlet 和Filter 过滤器。
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
