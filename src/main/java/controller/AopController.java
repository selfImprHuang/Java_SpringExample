package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import service.CommonService;

@Controller
public class AopController {

    @Autowired
    private CommonService commonService;

    @RequestMapping(value = "/test/aop/param", method = RequestMethod.GET)
    public void testAopParam(){
        commonService.testAopParam(1);
    }

    @RequestMapping(value = "/test/aop", method = RequestMethod.GET)
    public void testAop(){
        commonService.testAop();
    }

    @RequestMapping(value = "/test/aop/getClassMessage", method = RequestMethod.GET)
    public void getClassMessage(){
       System.out.println("测试通过Aop配合接口获取类的信息");
    }



}
