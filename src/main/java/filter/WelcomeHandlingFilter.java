/**
 *
 */

package filter;

import org.springframework.util.StringUtils;
import util.MessageUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理Welcome请求路径的过滤器：在Welcome请求路径的末尾添加/，解决Servlet 3.0+不能以编程方式来配置Welcome页面的问题。
 */
public class WelcomeHandlingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (!(servletRequest instanceof HttpServletRequest)) {
            throw new ServletException("ServletRequest is not a HttpServletRequest");
        }
        if (!(servletResponse instanceof HttpServletResponse)) {
            throw new ServletException("ServletResponse is not a HttpServletResponse");
        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String welcomeRequestUrl = buildWelcomeRequestUrl(request);
        String realRequestUrl = request.getRequestURL().toString();
        if (realRequestUrl.equals(welcomeRequestUrl + "/")) {
            // 在Welcome请求路径的末尾添加/，解决Servlet 3.0+不能以编程方式来配置Welcome页面的问题
            response.sendRedirect(realRequestUrl + "login/index");
        } else {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * 构建Welcome请求路径，示例：http://127.0.0.1:8080/sample
     *
     * @param request HttpServletRequest
     * @return Welcome请求路径
     */
    private String buildWelcomeRequestUrl(HttpServletRequest request) {
        String pattern = "{0}://{1}:{2}{3}";
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        String serverPort = String.valueOf(request.getServerPort());
        String contextPath = StringUtils.isEmpty(request.getContextPath()) ? "" : request.getContextPath();
        return MessageUtils.format(pattern, scheme, serverName, serverPort, contextPath);
    }

    @Override
    public void destroy() {

    }

}
