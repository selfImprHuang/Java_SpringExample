/*
 * @(#) EncodedTestController
 * 版权声明 网宿科技, 版权所有 违者必究
 *
 * <br> Copyright:  Copyright (c) 2018
 * <br> Company:网宿科技
 * <br> @author Administrator
 * <br> @description 功能描述
 * <br> 2018-12-26 22:11:24
 */

package controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/encoded/test/")
public class EncodedController {

    /**
     * POST的方式，如果后端不设置过滤器去拦截请求并设置请求编码的话
     * 会根据Content-type的character来标识
     */

    @RequestMapping(value = "/post-default")
    public void postDefault(HttpServletRequest request){
        System.out.println(request.getCharacterEncoding());
    }

    @RequestMapping(value = "/post-utf8")
    public void postUtf8(HttpServletRequest request){
        System.out.println(request.getCharacterEncoding());
    }


    @RequestMapping(value = "/post-gbk")
    public void postGbk(HttpServletRequest request){
        System.out.println(request.getCharacterEncoding());
    }

    @RequestMapping(value = "/get")
    public void getGbk(HttpServletRequest request){
        System.out.println(request.getParameter("testName"));
    }
}
