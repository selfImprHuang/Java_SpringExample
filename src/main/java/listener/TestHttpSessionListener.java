package listener;

import org.apache.log4j.Logger;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 处理Session创建和消亡的类，这边通过<code>@WebListener</code>注解直接声明为监听器
 * @author 志军
 */
@WebListener
public class TestHttpSessionListener implements HttpSessionListener {

    private Logger logger = Logger.getLogger(HttpSessionListener.class);
    @Override
    public void sessionCreated(HttpSessionEvent se) {

        SessionListenerTask sessionListenerTask = new SessionListenerTask();
        HttpSession session =  se.getSession();
        // 设置session过期时间 --这种方式并不科学
         se.getSession().setMaxInactiveInterval(20);
        logger.debug("session创建时间："+session.getCreationTime());

        //为了让页面加载看起来更流畅，这里模拟了两个情况，一个是用线程实现不等待,一个是加载数据库等待
        if(SessionListenerTask.isIsFirst()){
            sessionListenerTask.run();
            SessionListenerTask.setIsFirst(false);
        }else{
            //这里是同步情况
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
               throw new RuntimeException(e);
            }
        }
        System.out.println("session 加载开始，时间为：" + System.currentTimeMillis());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        se.getSession().getCreationTime();
        System.out.println("session过期了");
    }
}
