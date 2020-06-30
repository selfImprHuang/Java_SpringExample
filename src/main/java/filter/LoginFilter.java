package filter;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import util.MessageUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 这里有一个更佳的设置用户未操作的过期时间
 * 1.即在session创建的时候把session的创建时间放在全局里面 (应该是登陆时间)
 * 2.过滤器过滤浏览器请求时，判断session的创建时间是不是在指定的范围内
 * 如果是的话，就更新这个时间
 * 如果不是的话就清空跳转到登陆页面
 *
 * 当这个未过期的登陆时间判断通过以后，去过滤器链发送请求会再经过这个过滤器，更新一次时间，不过这是没关系的啊。
 * 只做do请求的拦截
 *
 * 所有的测试请求都在 LoginController 中，与本工程其他无关
 */
@WebFilter(urlPatterns = "*.do", initParams = {@WebInitParam(name = "EXCLUDED_PAGES", value = "/login;/login/index")})
public class LoginFilter implements Filter {

    private Logger logger = Logger.getLogger(LoginFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println("登陆验证过滤器");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request1 = (HttpServletRequest) request;
        HttpServletResponse response1 = (HttpServletResponse) response;
        HttpSession session = request1.getSession();
        String url = request1.getRequestURI();
        String name = (String) session.getAttribute("name");
        String password = request1.getParameter("password");
        String userCode = request1.getRemoteUser();
        String userRole = (String) session.getAttribute("role");
        System.out.println(password + " " + userCode + " " + userRole );
        //登录界面展示不拦截，直接放过(其他界面需要验证登录者的信息)
        if (url.contains("/login/index") || url.contains("/login/loginValidate")) {
            chain.doFilter(request, response);
            logger.debug("登陆请求不验证拦劫 ");
            return;
        }

        String processUrl = buildWelcomeRequestUrl(request1);

        //如果用户名是空，说明没有登录信息，需要重定向到登录界面重新登录
        if (StringUtils.isBlank(name)) {
            response1.sendRedirect(processUrl + "/login/index");
            return;
        }
        long createTime = Long.parseLong(String.valueOf(session.getAttribute("sessionTime")));
        long nowTime = Long.parseLong(String.valueOf(System.currentTimeMillis()));

        /*
          判断session是否过期，这边是需要把过期的用户强制退出
          设置不操作的超时时间 --比如10秒 -- 这里设置时间久一点，是为了测试后一个用户登陆把上一个用户挤掉的例子
         */
        long overTime = 5000000;
        if (nowTime - createTime > overTime) {
            logger.debug("session过期" + (nowTime - createTime));
            session.invalidate();//清除session信息？
            response1.sendRedirect(processUrl + "/login/index");
        } else {
            session.setAttribute("sessionTime", nowTime);
            logger.debug("session没有过期");
            chain.doFilter(request, response);
        }
    }


    private String buildWelcomeRequestUrl(HttpServletRequest request) {
        String pattern = "{0}://{1}:{2}{3}";
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        String serverPort = String.valueOf(request.getServerPort());
        String contextPath = org.springframework.util.StringUtils.isEmpty(request.getContextPath()) ? "" : request.getContextPath();
        return MessageUtils.format(pattern, scheme, serverName, serverPort, contextPath);
    }

    @Override
    public void destroy() {

    }

}


