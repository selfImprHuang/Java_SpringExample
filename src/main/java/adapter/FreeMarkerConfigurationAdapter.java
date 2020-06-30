/*
 * @(#) FreeMarkerConfigurationAdapter
 * 版权声明 黄志军， 版权所有 违者必究
 *
 * <br> Copyright:  Copyright (c) 2018
 * <br> Company:黄志军
 * <br> @author selfImpr
 * <br> 2018-05-23 11:34:00
 * <br> @description 通过这个适配器获取到freeMaker的配置类信息，具备默认配置
 *
 *
 */

package adapter;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * author: selfImpr <br><br> 
 * description:<br>
 *                  <blockquote>
 *                  该适配器用于生成配置信息相关的类<br>
 *                  支持传入配置类或其子类<br>
 *                  该适配器希望简化配置过程，包括设置一些默认的参数，当然这些参数是可以外部设置
 *                  </blockquote><br><br><br>
 * Date: 2018/5/23/023 15:45
 */
public class FreeMarkerConfigurationAdapter {

    /**
     * 配置器的类路径
     */
    private String filePath;

    /**
     * 编码配置
     */
    private String encoding;

    /**
     * 错误解决类配置
     */
    private TemplateExceptionHandler exceptionHandler;
    /**
     * 默认错误解决类配置
     */
    private final TemplateExceptionHandler defaultExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER;

    public FreeMarkerConfigurationAdapter() {
    }

    public FreeMarkerConfigurationAdapter(String filePath, String encoding, TemplateExceptionHandler exceptionHandler) {
        this.filePath = filePath;
        this.encoding = encoding;
        this.exceptionHandler = exceptionHandler;
    }

    /**
     *
     * 我们使用反射的机制，构造有参数的构造器，作为一个适配器，接受生成继承Configuration的子类对象<br><br>
     * 这里使用了反射的机制，可以了解一下
     */
    @SuppressWarnings("unchecked")
    public <T extends Configuration> T createConfiguration(Class<T> clazz) throws IOException, IllegalAccessException,
        InstantiationException, NoSuchMethodException, InvocationTargetException {
        Constructor constructor = clazz.getDeclaredConstructor(Version.class);
        //设置语法检查？
        constructor.setAccessible(true);
        T t = (T) constructor.newInstance(Configuration.VERSION_2_3_22);
        // Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
        //默认路径配置
        String defaultPath = "tpl";
        Resource resource1 = new ClassPathResource(defaultPath);
        t.setDirectoryForTemplateLoading(resource1.getFile());
        //默认编码配置
        String defaultEncoding = "UTF-8";
        t.setDefaultEncoding(defaultEncoding);
        t.setTemplateExceptionHandler(defaultExceptionHandler);
        if (StringUtils.isNotEmpty(filePath)) {
            Resource resource = new ClassPathResource(filePath);
            t.setDirectoryForTemplateLoading(resource1.getFile());
        }
        if (StringUtils.isNotEmpty(encoding)) {
            t.setDefaultEncoding(encoding);
        }
        if (exceptionHandler != null) {
            t.setTemplateExceptionHandler(exceptionHandler);
        }

        return t;
    }
}
