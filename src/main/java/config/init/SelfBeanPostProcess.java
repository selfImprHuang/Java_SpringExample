/*
 * @(#) MyBeanPostProcess
 * 版权声明 黄志军， 版权所有 违者必究
 *
 * <br> Copyright:  Copyright (c) 2018
 * <br> Company:黄志军
 * <br> @author selfImpr
 * <br> 2018-05-28 22:19:40
 * <br> @description
 *
 *
 */

package config.init;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author  志军
 * 自定义Bean初始化前后处理的方法
 * 这边Spring使用的也是模板模式
 */
@Component
public class SelfBeanPostProcess implements BeanPostProcessor {

    /**
     * 在Bean初始化之前进行处理
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
        throws BeansException {
        System.out.println("beanName-----------" + beanName);
        return bean;
    }

    /**
     * 在Bean初始化之后进行处理
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
        throws BeansException {
        return bean;
    }
}
