package config.propertiesScan;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * @author  selfimpr
 * @description 通过该配置来实现对properties的属性进行读取
 * @date  2017/11/11 21:25
 *
 * <code>@PropertySource</code>注解 解决中文乱码问题 好像还是需要设置IDEA的编码
 * //参考地址：http://blog.csdn.net/j3oker/article/details/53839210
 */
@Configuration
@PropertySource(value = "classpath:appilcation.properties",encoding = "UTF-8")
public class PropertiesScan {

     @Value("${area_id}")
     public  String isBorder;

     @Value("${local_request_id}")
     public  String borderColor;

    public String getIsBorder() {
        return isBorder;
    }

    public String getBorderColor() {
        return borderColor;
    }

    /**
     * 要想使用@Value 用${}占位符注入属性，这个bean是必须的，这个就是占位bean,另一种方式是不用value直接用Envirment变量直接getProperty('key')
     */
    @Bean
     public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
     return new PropertySourcesPlaceholderConfigurer();
     }
}