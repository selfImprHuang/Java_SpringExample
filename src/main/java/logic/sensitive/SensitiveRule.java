package logic.sensitive;

/**
 * @author 志军
 * @description 敏感词汇处理规则
 * @param <T> 处理的类型
 */
public interface SensitiveRule<T> {

    /**
     * 自定义类型处理规则
     */
    T process(T t);
}
