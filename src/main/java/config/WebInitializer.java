package config;

import filter.FilterDelegator;
import filter.WelcomeHandlingFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.EventListener;
import java.util.List;

/**
 * Created by com on 2017/7/25.
 * <p>
 * 相当于web.xml的加载配置
 */

/***
 *    FilterRegistration.Dynamic welcomePagFilter = container.addFilter("WelcomePagFilter", new WelcomePagFilter());
 *    welcomePagFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
 *    上述是配置过滤器作用的请求.
 *    这里已经包装起来了
 */

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {


    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        /**配置和监听器*/
        registerServletFilters(servletContext);
        registerListeners(servletContext);
        // QuartzListener quartzListener = new QuartzListener();
        // servletContext.addListener(quartzListener.getClass());
    }


    /**
     * 配置ContextComponentLoader
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        // return new Class<?>[]{ContextComponentLoder.class};
        return new Class<?>[]{};
    }


    /**
     * 配置DispatcherServlet
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{MvcComponentLoader.class};
    }

    /**将DispatcherServlet映射到指定路径
     配置ServletMappings*/
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }


    /**
     * 在ServletContext中添加过滤器，并把过滤器应用于DispatcherServlet。
     *
     * @param servletContext Servlet上下文
     */
    protected void registerServletFilters(ServletContext servletContext) {
        List<FilterDelegator> list = new ArrayList<>();
        /**添加基本的过滤器配置,包括欢迎页和编码格式*/
        configServletFilters(list);
        for (FilterDelegator filterDelegator : list) {
            if (StringUtils.hasText(filterDelegator.getFilterName())) {
                registerServletFilter(servletContext, filterDelegator.getFilter(), filterDelegator.getFilterName());
            } else {
                registerServletFilter(servletContext, filterDelegator.getFilter());
            }
        }

    }

    /**
     * 配置DispatcherServlet的过滤器。
     * 添加基本的过滤器配置
     *
     * @param servletFilters DispatcherServlet的过滤器
     */
    protected void configServletFilters(List<FilterDelegator> servletFilters) {
        /**
         * 配置编码格式和欢迎页面
         */
        // servletFilters.add(new filter(new CharacterEncodingFilter("UTF-8")));
        servletFilters.add(new FilterDelegator(new WelcomeHandlingFilter()));
    }

    /**
     * 在ServletContext中添加过滤器，并把过滤器应用于DispatcherServlet。
     *
     * @param servletContext Servlet上下文
     * @param filter 过滤器
     * @param filterName 过滤器名称
     */
    protected final void registerServletFilter(ServletContext servletContext, Filter filter, String filterName) {
        FilterRegistration.Dynamic registration = servletContext.addFilter(filterName, filter);
        if (registration == null) {
            throw new IllegalStateException("Duplicate Filter registration for '" + filterName + "'. Check to ensure the Filter is only "
                + "configured once.");
        }
        registration.setAsyncSupported(isAsyncSupported());
        registration.addMappingForServletNames(getDispatcherTypes(), false, getServletName());
    }

    /**
     * 获取过滤器的分发器类型。<br>
     * 1、支持异步操作时，默认为：DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE、DispatcherType.ASYNC；<br>
     * 2、不支持异步操作时，默认为：DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE。
     * <p>
     *
     * 参考：<br>
     * AbstractDispatcherServletInitializer.getDispatcherTypes()
     *
     * @return DispatcherType集合
     */
    protected final EnumSet<DispatcherType> getDispatcherTypes() {
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE);
        if (isAsyncSupported()) {
            dispatcherTypes.add(DispatcherType.ASYNC);
        }
        configDispatcherTypes(dispatcherTypes);
        return dispatcherTypes;
    }

    /**
     * 配置过滤器的分发器类型。
     *
     * @param dispatcherTypes 过滤器的分发器类型
     */
    protected void configDispatcherTypes(EnumSet<DispatcherType> dispatcherTypes) {

    }

    /**
     * 在ServletContext中添加监听器。
     *
     * @param servletContext Servlet上下文
     */
    protected final void registerListeners(ServletContext servletContext) {
        List<Class<? extends EventListener>> listenerClasses = new ArrayList();

        // ExcelTempFileListener excelTempFileListener = new ExcelTempFileListener();
        // TestHttpSessionListener testHttpSessionListener = new TestHttpSessionListener();

        configListenerClasses(listenerClasses);
        for (Class<? extends EventListener> listenerClass : listenerClasses) {
            servletContext.addListener(listenerClass);
        }
        /**添加监听器???? --- 2017.08.23----*/
        // servletContext.addListener(excelTempFileListener);
        // servletContext.addListener(testHttpSessionListener);
    }

    /**
     * 配置监听器。
     *
     * @param listenerClasses 监听器类
     */
    protected void configListenerClasses(List<Class<? extends EventListener>> listenerClasses) {


    }


}
