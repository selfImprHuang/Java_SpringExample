

package annotation;

import java.lang.annotation.*;

/**
 * 在方法上标识是否进行敏感词处理。
 * 这边这个注解主要是配合Aop去拦截
 * 这边拦截之后只会处理方法的出参数，这边敏感词约定就是处理出参
 * @author 志军
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
@Documented
@Inherited  //可以继承
public @interface Sensitive {
    /**
     * 是否进行参数的敏感处理
     */
   boolean process() default true;
}
