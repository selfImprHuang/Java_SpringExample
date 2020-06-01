package config.init;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author  志军
 * 测试一下<code>@PostConstruct</code>注解
 */
@Component
public class InitPost {

    /**
     * 该注解相当于servlet的init方法，且只会执行一次
     * 不知道这个的执行顺序，后面可以再了解一下
     */
    @PostConstruct
    public void initPost(){
        System.out.println("我被初始化了");
    }
}
