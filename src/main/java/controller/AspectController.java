package controller;

import annotation.Sensitive;
import logic.sensitive.SensitiveObject;
import logic.sensitive.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 切面测试类
 * @author 志军
 */
@Controller
public class AspectController {


    @RequestMapping(value = "/aop/sensitive", method = RequestMethod.GET)
    @Sensitive
    public SensitiveObject testSensitive(){
        SensitiveObject sensitiveObject = new SensitiveObject();
        sensitiveObject.setPhone("1820000000000");
        sensitiveObject.setNoPhone("1820000000000");
        Test test = new Test();
        test.setPhone("1820000000000");
        test.setNum(1);
        test.setEmail("8515@qq.com");
        sensitiveObject.setTest(test);
        return sensitiveObject;
    }
}
