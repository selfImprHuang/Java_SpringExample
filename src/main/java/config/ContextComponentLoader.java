package config;


import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.WebApplicationContext;
import redis.clients.jedis.JedisPoolConfig;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;


/**
 *       @author 志军
 *       用@Configuration注解该类，等价 与XML中配置beans；用@Bean标注方法等价于XML中配置bean.
 *       相当于applicationContext.xml文件
 *
 *       1.自动扫描web包 ,将带有注解的类 纳入spring容器管理
 *       2.引入jdbc配置文件
 *       3.dataSource 配置
 *       4.mybatis文件配置，扫描所有mapper文件
 *       5.spring与mybatis整合配置，扫描所有dao
 *       6.对dataSource 数据源进行事务管理
 *       7.配置使Spring采用CGLIB代理
 *       8.启用对事务注解的支持
 */
 @Configuration
 @EnableTransactionManagement
 @ComponentScan
public class ContextComponentLoader {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 配置数据源
     */
    @Bean
    public DataSource dataSource() {
        //这边数据库的配置需要根据对应的地址进行修改
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        driverManagerDataSource.setUrl("jdbc:oracle:thin:@127.0.0.1:3306:ORCL");
        driverManagerDataSource.setUsername("admin");
        driverManagerDataSource.setPassword("admin");
        //可以配置数据池的其他东西
        return driverManagerDataSource;
    }

    @Bean
    public SqlSessionFactoryBean getSqlSessionFactoryBean(DataSource x) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        //設置dataSource
        sqlSessionFactoryBean.setDataSource(x);
        Properties properties = new Properties();
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = null;
        try {
            resources = pathMatchingResourcePatternResolver.getResources("dao/*.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqlSessionFactoryBean.setMapperLocations(resources);
         sqlSessionFactoryBean.setConfigurationProperties(properties);
        //全局设置返回的结果map不把null的参数去掉。这个好像局部设置会比较好一点
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCallSettersOnNulls(true);
        sqlSessionFactoryBean.setConfiguration(configuration);
        try {
            sqlSessionFactoryBean.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlSessionFactoryBean;
    }

    /**
     * 配置mapper的掃描類
     */
    @Bean
    public MapperScannerConfigurer getMapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        Properties pro = new Properties();
        /**這裡不是用Properties类来接收属性???*/
//        pro.setProperty("basePackage","com.springmvc.test.mapper");
//        pro.setProperty("sqlSessionFactoryBeanName","sqlSessionFactory");
        /**mapper和dao放在一个包下面*/
        /**setBasePackage的方法只能设置一个地址,
         * 扫描mapper时找不到对应的Resposity时会报错,
         * 但是扫描到Resposity没扫描到mapper不会报错*/
        mapperScannerConfigurer.setBasePackage("dao");
//        mapperScannerConfigurer.setBasePackage("com.springmvc.test.dao");
//        mapperScannerConfigurer.setSqlSessionFactory(sqlSessionFactory);
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("getSqlSessionFactoryBean");


        try {
            return mapperScannerConfigurer;
        } catch (Exception ex) {
            System.out.println("发生错误");
            ex.printStackTrace();
        }

        return mapperScannerConfigurer;
    }

    /**
     * 配置mybatis的事务管理
     */
    @Bean
    public DataSourceTransactionManager getDataSourceTransactionManager(DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }


    /**
     * PersistenceExceptionTranslationPostProcessor是一个
     bean 后置处理器（bean post-processor），它会在所有拥
     有@Repository注解的类上添加一个通知器（advisor），这样就会
     捕获任何平台相关的异常并以Spring非检查型数据访问异常的形式重
     新抛出。
     * */
    @Bean
    public BeanPostProcessor persistenceTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }


    /**
     * 配置redis 的数据源、数据连接池、事务
     */
    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig createJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // #最大空闲数，数据库连接的最大空闲时间。超过空闲时间，数据库连接将被标记为不可用，然后被释放。设为0表示无限制。
        jedisPoolConfig.setMaxIdle(300);
        // #连接池的最大数据库连接数。设为0表示无限制
        jedisPoolConfig.setMaxTotal(600);
        // #最大建立连接等待时间。如果超过此时间将接到异常。设为-1表示无限制。
        jedisPoolConfig.setMaxWaitMillis(5000);
        // #在borrow一个jedis实例时，是否提前进行alidate操作；如果为true，则得到的jedis实例均是可用的；
        jedisPoolConfig.setTestOnBorrow(true);
        return jedisPoolConfig;
    }

    @Bean(name = "jedisConnectionFactory")
    public JedisConnectionFactory createJedisConnectionFactory(WebApplicationContext context) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName("127.0.0.1");
        jedisConnectionFactory.setPort(6379);

        //redis好像是只有一个用户，默认是没有密码的，在控制台设置密码好像是暂时的，第二次重启的时候这个密码就会失效
        //如果redis没有密码，就不要给这个参数进行设值操作，不然会报错，而且redis桌面应用中的密码和链接redis客户端的密码好像也会冲突
        jedisConnectionFactory.setPassword("123456");
        //连接池配置
        jedisConnectionFactory.setPoolConfig((JedisPoolConfig) context.getBean("jedisPoolConfig"));
        return jedisConnectionFactory;
    }


    /**
     * redis操作模版,使用该对象可以操作redis
     */
    @Bean(name = "redisTemplate")
    public RedisTemplate createRedisTemplate(WebApplicationContext context) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory((RedisConnectionFactory) context.getBean("jedisConnectionFactory"));
        // 如果不配置Serializer，那么存储的时候缺省使用String，如果用User类型存储，那么会提示错误User can't cast to String！！
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        //开启事务
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }


}

