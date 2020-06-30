package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @RequestMapping("/login/index")
    public String loginIndex() {
        return "login";
    }

    @RequestMapping("login/loginValidate")
    public String login(HttpServletResponse response, HttpServletRequest request) {
        /**
         * 这里有一个更佳的设置用户未操作的过期时间
         * 1.即在session创建的时候把session的创建时间放在全局里面
         * 2.过滤器过滤浏览器请求时，判断session的创建时间是不是在指定的范围内
         * 如果是的话，就更新这个时间
         * 如果不是的话就清空跳转到登陆页面
         */
        request.getSession().setAttribute("sessionTime", request.getSession().getCreationTime());
        HttpSession session = request.getSession();
        ServletContext servletContext = session.getServletContext();
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        /**
         * 如何实现一个用户登陆，然后把上一个用户踢掉
         * 1.java后端可以通过servletContext来保存用户的登陆的会话信息
         * 2.当第二个用户进行登陆的时候把第一个用户的session清空
         * 3.在前台设置一个轮询的操作，判断session是否为空。
         *
         * 这个测试要联合login.html,loginTest.html来调试，还有登陆过滤器。
         * 还是不知道怎么把已经登陆的用户挤掉???不可能在每一个页面都设置一个函数来轮询监听后台的事件
         * 除非用户刷新页面，或者发送请求。
         */
        String newSessionId = session.getId();
        //不管用户账号密码对不对都让他登陆
        session.setAttribute("name", name);
        if (servletContext.getAttribute(name) != null) {
            String oldSessionId = String.valueOf(servletContext.getAttribute(name));
            HttpSession oldSession = (HttpSession) servletContext.getAttribute(oldSessionId);
            if (!oldSessionId.equals(newSessionId)) {
                oldSession.invalidate();//清空之前的session信息
                servletContext.setAttribute(name, newSessionId);
                servletContext.setAttribute(newSessionId, session);
            }
        } else {
            servletContext.setAttribute(name, newSessionId);
            servletContext.setAttribute(newSessionId, session);
        }

        return "loginTest";

    }

    @RequestMapping("/login/test.do")
    public String loginTest() {
        return "loginTest";
    }
}
