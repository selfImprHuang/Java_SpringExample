

package annotation;

import java.lang.annotation.*;

/**
 * 测试通过切片的方式获取到类的属性方法等信息
 * @author 志军
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD,ElementType.FIELD})
@Documented
@Inherited  //可以继承
public @interface AopGetClassMessage {
    String use() default "没写";
}
