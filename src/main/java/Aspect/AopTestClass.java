package Aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * @author : selfimpr
 * @description :整理文件添加注解，通过aspectj进行切面注入
 * @date : 2017/11/11 21:28
 */
@Aspect
public class AopTestClass {
    @Before("execution(* service.MainServiceImpl.*(..))")
    public void begin(){
        System.out.println("表演开始");
    }
    @After("execution(* service.MainServiceImpl.*(..))")
    public void end(){
        System.out.println("表演结束");
    }

    /**
     * 通过这个声明来实现环绕通知，所谓的环绕通知就是说在在一个方法中规定了类运行几个状态直接的通知事件
     * 可以这样声明来实现不用写一大串的代码
     */
    @Pointcut("execution(* service.MainServiceImpl.*(..))")
    public void perform(){}

    @Around("perform()")
    public void  AroundAop(ProceedingJoinPoint proceedingJoinPoint){
        try{
            System.out.println("我在程序运行之前进行了通知");
            //这个方法代表被切接口进行执行，上面的方法表示之前执行，下面的方法表示之后执行
            proceedingJoinPoint.proceed();
            System.out.println("我在程序运行之后进行了通知");
        }catch(Throwable throwable){//抛出错误的时候进行aop通知
            throwable.printStackTrace();
            System.out.println("运行错误产生的aop通知");
        }
    }

    /**
     * 这个是测试有传参的Aop接口环绕
     * @param testNum 入参，根据方法决定入参的数量与类型
     */
    @Pointcut("execution(* service.MainServiceImpl.testAopParam(int)) && args(testNum)")
    public void testAopParams(int testNum){
    }

    @After("testAopParams(testNum)")
    public void testHasParam(int testNum){
        System.out.println(testNum);
    }

}
