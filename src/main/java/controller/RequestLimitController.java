/*
 * @(#) RequestLimitController
 * 版权声明 网宿科技, 版权所有 违者必究
 *
 * <br> Copyright:  Copyright (c) 2018
 * <br> Company:网宿科技
 * <br> @author huangzj1
 * <br> @description 功能描述
 * <br> 2018-12-16 17:27:24
 */

package controller;

import logic.requestLimit.RequestLimit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * ${DESCRIPTION}
 *
 * @author huangzj1
 * @date 2018-12-16 17:27
 */
@Controller
public class RequestLimitController {

    @RequestMapping(value = "/request/limit")
    @RequestLimit(time = 55, count = 2, waits = 100)
    @ResponseBody
    public String requestLimit(HttpServletRequest request) {
        System.out.println("我被打扰了");
        return "我被打扰了";
    }
}
