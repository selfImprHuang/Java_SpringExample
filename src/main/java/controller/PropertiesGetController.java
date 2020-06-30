package controller;

import config.propertiesScan.PropertiesScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 志军
 * @description 测试SpringCofig利用注解来实现properties配置文件的读取
 * @date  2017/11/11 21:26
 */

@Controller
public class PropertiesGetController {

    @Autowired
    private PropertiesScan propertiesConfig;

    @RequestMapping("/get/properties")
    public void getProperties() {
        System.out.println(propertiesConfig.getIsBorder());
        System.out.println(propertiesConfig.getBorderColor());
    }
}
