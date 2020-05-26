package Aspect;


import annotation.AopGetClassMessage;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Arrays;


/**
 * 测试通过切片的方式获取到类的属性方法等信息
 * @author 志军
 */
@Aspect
public class GetClassMessageAop {

    /**
     * 参考地址 https://www.cnblogs.com/qiumingcheng/p/5923928.html
     */
    @Around("execution(* controller.*.*(..)) && @annotation(aopAnnotation)")
    public Object getClassMessage(ProceedingJoinPoint pjp, AopGetClassMessage aopAnnotation) throws Throwable {
        System.out.println("拦截到了" + pjp.getSignature().getName() + "方法...");

        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        Class clazz = targetMethod.getClass();
        System.out.printf("类的名称：%s,方法名称:%s,注解的说明：%s\n", pjp.getSignature().getName(), targetMethod.getName(), aopAnnotation.use());
        System.out.println("类的信息:"+clazz.getName()+"所有方法:"+ Arrays.toString(clazz.getMethods()));
        return pjp.proceed();
    }
}
