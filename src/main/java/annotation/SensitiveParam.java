

package annotation;

import logic.sensitive.SensitiveRule;
import logic.sensitive.StringRule;

import java.lang.annotation.*;

/**
 * 是否对出参的词汇进行敏感词处理
 * @author 志军
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
@Documented
@Inherited  //可以继承
public @interface SensitiveParam {

    /**
     * 是否处理
     */
    boolean process() default true;

    /**
     * 处理规则
     */
    Class<? extends SensitiveRule> rule() default StringRule.class;
}
