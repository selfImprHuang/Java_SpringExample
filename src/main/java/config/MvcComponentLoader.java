package config;

import adapter.FreeMarkerConfigurationAdapter;
import extention.AlarmTplConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import scheduler.JobScheduler;
import scheduler.adapter.BaseJob;
import scheduler.adapter.JobAdapter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.Properties;

/**
 * Created by com on 2017/7/25.
 * 配置自动加载注解类
 *
 * 没有代码配置出现的情况
 *
 * 没有配置视图解析器。如果这样的话， Spring默认会使
 用BeanNameView-Resolver，这个视图解析器会查找ID与视
 图名称匹配的bean，并且查找的bean要实现View接口，它以这样
 的方式来解析视图。
 没有启用组件扫描。这样的结果就是， Spring只能找到显式声明
 在配置类中的控制器。
 这样配置的话， DispatcherServlet会映射为应用的默认
 Servlet，所以它会处理所有的请求，包括对静态资源的请求，如
 图片和样式表（在大多数情况下，这可能并不是你想要的效
 果）。
 *
 *
 * spring Mvc配置(相当于spring-servlet.xml)
 *      1.Json返回格式化转换
 *      2.设置全局日期参数的字符串表示格式
 *      3.激活@Controller模式
 *      4.配置拦截器
 *      5.类名到视图名的自动映射
 *      6.对模型视图名称的解析
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan({"config"})//启动自动扫描注解
public class MvcComponentLoader extends WebMvcConfigurerAdapter {


    /**
     * 这里是配置转换器的方法，我们使用@ControllerAdvice需要这个配置，添加我们的转换器
     */
    // @Bean
    // public HttpMessageConverterBuilder httpMessageConverterBuilder() {
    //     return new HttpMessageConverterBuilder();
    // }
    //
    // @Override
    // public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    //     converters.addAll(httpMessageConverterBuilder().buildHttpMessageConverters());
    // }

    /**
     * 把异常处理器作为一个bean返回，会把spring默认的异常处理器覆盖，这样的做法是在说你的容器只想托管给一个异常处理器来处理
     * @return
     */
    // @Bean
    // public MyExceptionHandler getExceptionHandler() {
    //     return new MyExceptionHandler();
    // }

    /**
     * 如果我们需要配置多个异常处理器的话， 我们可以在这里面进行异常处理器的注册和他的顺序的处理
     * @param exceptionResolvers
     */
    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        // exceptionResolvers.add(new MyExceptionHandler());
        // ...
    }

    /**
     * 这边应该是配置的才是正确的适配器
     */
    @Bean
    public JobScheduler getJobScheduler(WebApplicationContext context) throws ParseException {
        JobScheduler jobScheduler = new JobScheduler();
        JobAdapter jobAdapter = new JobAdapter((BaseJob) context.getBean("myJob"), "myJob", "cron");
        jobScheduler.addJob(jobAdapter);
        //在调用这个方法之前，所有的配置都没有被加入到定时器中，也就是说一切还没尘埃落定
        jobScheduler.afterProperties();
        return jobScheduler;
    }

    /**
     * 错误的适配器事例
     */
    // @Bean(name = "triggerAdapter")
    // public SchedulerCronTriggerAdapter getSchedulerCronTriggerAdapter(WebApplicationContext webApplicationContext) {
    //     SchedulerCronTriggerAdapter schedulerCronTriggerAdapter = new SchedulerCronTriggerAdapter(webApplicationContext);
    //     //作为适配器，我应该自己去判断他的类型
    //     MyJobDetail alarmSendJob = new MyJobDetail();
    //     schedulerCronTriggerAdapter.setQuartzJobBeans(alarmSendJob);
    //     schedulerCronTriggerAdapter.createTriggers();
    //     // webApplicationContext.getEnvironment().getProperty("cronRunTime");
    //     return schedulerCronTriggerAdapter;
    // }
    //
    // @Bean
    // public SchedulerFactoryBean getSchedulerFactoryBean(WebApplicationContext webApplicationContext) {
    //     SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
    //     //设置application和他的key，这个地方是为了这继承的那个方法里面得到scheduler，进而得到applicationContext
    //     schedulerFactoryBean.setApplicationContextSchedulerContextKey("application");
    //     schedulerFactoryBean.setApplicationContext(webApplicationContext);
    //     List<Trigger> triggers = ((SchedulerCronTriggerAdapter) webApplicationContext.getBean("triggerAdapter")).getTriggers();
    //     //因为传进去的是--可变参数--，他是可以支持原生数组的
    //     Trigger[] triggers1 = new Trigger[]{};
    //     schedulerFactoryBean.setTriggers(triggers.toArray(triggers1));
    //     return schedulerFactoryBean;
    // }


    // /**
    //  * 配置一个trigger作为bean，当然这个trigger还可以是别的类型的
    //  * @return
    //  */
    // @Bean(name = "cronTriggerFactoryBean")
    // public CronTriggerFactoryBean getCronTriggerFactoryBean() {
    //     CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
    //     JobDetail jobDetail = newJob(MyJobDetail.class).build();
    //     cronTriggerFactoryBean.setJobDetail(jobDetail);
    //     cronTriggerFactoryBean.setCronExpression("0/5 * * * * ? ");
    //     return cronTriggerFactoryBean;
    // }
    //
    // /**
    //  * 使用这个trigger的scheduler的配置，也就是定时器的配置，spring原生配置
    //  * @param webApplicationContext
    //  * @return
    //  */
    // @Bean
    // public SchedulerFactoryBean getSchedulerFactoryBean(WebApplicationContext webApplicationContext) {
    //     SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
    //     Trigger ctfb = (Trigger) webApplicationContext.getBean("cronTriggerFactoryBean");
    //     schedulerFactoryBean.setTriggers((Trigger) ctfb);
    //     return schedulerFactoryBean;
    // }

    /**
     * author: selfImpr <br><br> 
     * description: <br><br>如何去使用一个配置型的工具（来自尚峰的建议）：<br>
     *              1.查阅官网，得到大概的用法<br>
     *              2.下载jar包研究源码，第一步，对所有的类进行归类，比如一个配置型的工具类包，需要配置信息和调用信息<br>
     *              3.分类完成之后，弄清楚分级的类之间的关系，主要理出一条线，比如我专注用Temple这个类，我就找到这个类相关的继承关系等<br>
     *              4.查看大概源代码。因为是配置信息，所以要注意得到temple对象的方法中如何使用这些配置信息<br><br>
     *              freemarker相关问题<br>
     *                  1.为什么不使用配置的引用，而是新建一份配置，增加内存风险？答：配置信息如果被修改等，可能造成多线程并发问题。新建一份配置信息进行
     *                  temple的生成可能造成内存风险，但是这里运用了cache的方式，减轻了这个风险
     * Date: 2018/5/21/021 9:52
     * @return 返回类型描述
     * @throws Exception 异常说明
     */
    @Bean
    public AlarmTplConfiguration AlarmTplConfiguration() throws IOException, InvocationTargetException,
        NoSuchMethodException, InstantiationException, IllegalAccessException {
        FreeMarkerConfigurationAdapter freeMarkerConfigurationAdapter = new FreeMarkerConfigurationAdapter();
        /**
         * 这里的思路是:<br>
         *            <b>1.类的有一些参数是可以使用默认设置的，我们这边给他一个使用默认设置的机会</b>
         *            <b>2.类的有一些参数也可以是用户来配置的，所以说这里我们提供了一个设置参数的机会</b>
         */
        return freeMarkerConfigurationAdapter.createConfiguration(AlarmTplConfiguration.class);
    }


    /**
     * 配置编码过滤器
     */
    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        return characterEncodingFilter;
    }

    /**
     * 这是要配置页面过滤，如40X的页面...
     * 参考地址：http://blog.csdn.net/projectarchitect/article/details/42463471
     */
     @Bean
     public SimpleMappingExceptionResolver simpleMappingExceptionResolver(){
         SimpleMappingExceptionResolver simpleMappingExceptionResolver = new SimpleMappingExceptionResolver();
         Properties properties = new Properties();
         properties.setProperty("org.apache.shiro.authz.UnauthenticatedException","success");

         simpleMappingExceptionResolver.setExceptionMappings(properties);
         return simpleMappingExceptionResolver;
     }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/success");
    }

    /**
     *
     * 如果没有配置 这个request就需要我自己去解析，不然的话，spring会帮助我解析完成
     */
     @Bean
     public MultipartResolver multipartResolver() throws IOException {
         CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
         multipartResolver.setUploadTempDir(new FileSystemResource("/cache/files"));
         //设置文件大小
          multipartResolver.setMaxInMemorySize(1110);
          multipartResolver.setMaxInMemorySize(2220);
         return multipartResolver;
     }

    /**配置视图解析器*/
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolve = new InternalResourceViewResolver();
        resolve.setPrefix("/html/");
        resolve.setSuffix(".html");
        resolve.setOrder(0);
        resolve.setExposeContextBeansAsAttributes(true);

        return resolve;
    }

    /**
     * @author : selfimpr
     * @description: 好像同一种类型的视图解析器不能配置多个，但是可以配置多个不同类型的视图解析器
     * 参考地址：http://blog.csdn.net/xlxxcc/article/details/51148927
     * @date: 2017/11/17 15:43
     */
    // @Bean
    // public InternalResourceViewResolver internalResourceViewResolver(){
    //     InternalResourceViewRes  olver resolve = new InternalResourceViewResolver();
    //     resolve.setPrefix("/html/");
    //     resolve.setSuffix(".jsp");
    //     resolve.setOrder(1);
    //     resolve.setExposeContextBeansAsAttributes(true);
    //
    //     return resolve;
    // }

    /**配置静态资源的处理*/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * by 黄志军
         * 2017.08.08
         * */
        /**前面的是页面引用的地址,后面的函数是请求的本地资源地址
         * 如果两个地址是不一样,是为了实现安全性,黑客访问页面请求地址时,想访问本地的资源地址,
         * 却找不到真是的地址*/

        /**.setCachePeriod()
         * 该方法实现对静态资源的缓存,保证访问的速度和资源的应用率
         * */
        registry.addResourceHandler("/react/**.js").addResourceLocations("/react/");
        /**可以进行多个资源的配置--添加的参数是数组--*/
        registry.addResourceHandler("/css/**.css").addResourceLocations("/css/");
    }


    /**解释
     *       新的WebConfig类还扩展了WebMvcConfigurerAdapter
     并重写了其configureDefaultServletHandling()方法。通过
     调用DefaultServlet-HandlerConfigurer的enable()方法，
     我们要求DispatcherServlet将对静态资源的请求转发到Servlet容
     器中默认的Servlet上，而不是使用DispatcherServlet本身来处理
     此类请求。
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
