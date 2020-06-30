/*
 * @(#) freemarkerController
 * 版权声明 黄志军， 版权所有 违者必究
 *
 * <br> Copyright:  Copyright (c) 2018
 * <br> Company:黄志军
 * <br> @author selfImpr
 * <br> 2018-05-21 10:04:51
 * <br> @description
 *
 *
 */

package controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import model.tpl.TplMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import service.TplService;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



@Controller
public class FreemarkerController {

    @Autowired
    private TplService tplService;

    @RequestMapping("/freemaker")
    private String freeMaker() throws IOException, TemplateException {
        TplMessage tplMessage = new TplMessage();
        tplMessage.setIdentification("[mx1234567890]");
        DateFormat dateFormat = new SimpleDateFormat("yyyy年mm月dd HH:MM:ss");
        tplMessage.setTime(dateFormat.format(new Date()));
        tplMessage.setPlatform("XXXX平台");
        tplMessage.setStatistic("123333");

        Template temp = tplService.getTemplate("alarm.tpl");
        Writer out = new StringWriter();
        temp.process(tplMessage, out);
        System.out.print(temp.toString());
        return "";
    }


    @RequestMapping("springMaker")
    public void springMarker() throws IOException, TemplateException {
        FreeMarkerConfigurationFactory freeMarkerConfigurationFactory = new FreeMarkerConfigurationFactory();
        freeMarkerConfigurationFactory.setDefaultEncoding("UTF-8");
        Configuration configuration = freeMarkerConfigurationFactory.createConfiguration();

        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setConfiguration(configuration);

    }

}


