package config;

import Aspect.AopTestClass;
import Aspect.SensitiveAop;
import logic.requestLimit.RequestLimitAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Created by Administrator on 2017/7/26.
 * 启动AspectJ自动代理
 * @author 志军
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan
public class AopComponentLoader {

    /**
     * 通过这个类开启EnableAspectJAutoProxy注解
     * 在这边注册切面类,比较统一，不会把切面类弄得到处都是不好找
     */
    @Bean
    public AopTestClass aopClass() {
        return new AopTestClass();
    }

    /**
     * 敏感词汇限制转换
     */
    @Bean
    public SensitiveAop sensitiveAop() {
        return new SensitiveAop();
    }

    /**
     * 接口访问频率限制
     */
    @Bean
    public RequestLimitAspect getRequestLimitAspect() {
        return new RequestLimitAspect();
    }

}
