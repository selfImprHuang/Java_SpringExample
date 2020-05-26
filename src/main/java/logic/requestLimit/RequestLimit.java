

package logic.requestLimit;

import java.lang.annotation.*;

/**
 * 限制60秒内访问10次，之后等待300秒（时间可以自定义）
 *
 * @author huangzj1
 * @date 2018-12-16 17:07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
@Documented
@Inherited  //可以继承
public @interface RequestLimit {

    /**
     * 多少时间内
     */
    int time() default 60;

    /**
     * 超过多少次数
     */
    int count() default 10;

    /**
     * 超过之后等待
     */
    int waits() default 300;
}
