

package Aspect;

import annotation.Sensitive;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * 这个aop是为了处理敏感词汇注解自动解析处理的
 * @author 志军
 */
@Aspect
public class SensitiveAop {


    /**
     * spring可以直接对加了注解的方法进行处理
     */
    @Before(value = "@annotation(annotation.SensitiveParam)")
    public void begin() {
        System.out.println("表演开始");
    }

    /**
     * 在Controller层进行注解的拦截(敏感词汇处理)
     */
   @Around("execution(* controller.*.*(..)) && @annotation(sensitive)")
    public Object getClassMessage(ProceedingJoinPoint pjp, Sensitive sensitive) {
       //todo 这边需要怎么去处理，我还没想清楚
       return null;
    }
}
