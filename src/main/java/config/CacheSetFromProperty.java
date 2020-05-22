package config;

import net.sf.ehcache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * @author : selfimpr
 * @description: 配置cache，通过xml文件规定cache的规则
 *               然后声明为一个bean，通过注解来使用cache
 * @date : 2017/11/3 10:52
 */
@Configuration
@EnableCaching
public class CacheSetFromProperty {

    @Bean
    public EhCacheCacheManager cacheCacheManager(CacheManager ehCacheManagerFactoryBean){
        return new EhCacheCacheManager(ehCacheManagerFactoryBean);
    }


    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean(){
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        //通过xml配置cache的命名空间和规则
        ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("cache/ehcache.xml"));
        //设置缓存工厂为单例，否则会报错。
        ehCacheManagerFactoryBean.setShared(true);
        return ehCacheManagerFactoryBean;
    }

}
