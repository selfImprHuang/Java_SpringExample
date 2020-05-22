/*
 * @(#) SessionListenerTask
 * 版权声明 网宿科技, 版权所有 违者必究
 *
 * <br> Copyright:  Copyright (c) 2018
 * <br> Company:网宿科技
 * <br> @author Administrator
 * <br> @description 功能描述
 * <br> 2018-11-13 20:32:51
 */

package listener;

//可以参考：https://www.cnblogs.com/wcyBlog/p/4657903.html
public class SessionListenerTask implements Runnable{

    private static boolean IS_FIRST = true;

    public static boolean isIsFirst() {
        return IS_FIRST;
    }

    public static void setIsFirst(boolean isFirst) {
        IS_FIRST = isFirst;
    }

    @Override
    public void run() {
        //模拟线程加载数据库
        try {
            Thread.sleep(10000);
            System.out.println("数据库取数据结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
