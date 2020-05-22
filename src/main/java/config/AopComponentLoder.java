package config;

import com.springmvc.config.aop.SensitiveAop;
import com.springmvc.config.aop.aopClass;
import com.springmvc.config.support.RequestLimitAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Created by Administrator on 2017/7/26.
 * 启动AspectJ自动代理
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan
public class AopComponentLoder {

    //在这里注册切面的bean
    @Bean
    public aopClass aopClass() {
        return new aopClass();
    }

    @Bean
    public SensitiveAop sensitiveAop() {
        return new SensitiveAop();
    }

    @Bean
    public RequestLimitAspect getRequestLimitAspect() {
        return new RequestLimitAspect();
    }

}
