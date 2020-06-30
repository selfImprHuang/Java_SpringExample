/*
 * @(#) FullFillTimeValueController
 * 版权声明 网宿科技, 版权所有 违者必究
 *
 * <br> Copyright:  Copyright (c) 2018
 * <br> Company:网宿科技
 * <br> @author Administrator
 * <br> @description 功能描述
 * <br> 2018-11-24 01:14:40
 */

package controller;

import com.google.common.collect.Lists;
import model.fullFillTime.OriginTimeValue;
import model.fullFillTime.TargetTimeValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import util.time.FillFullTimeValueUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

/**
 * @author 志军
 * 测试补时间点
 */
@Controller
public class FullFillTimeValueController {


    @RequestMapping(value = "/time-test")
    public void fullFillTimeValue() {
        List<OriginTimeValue> originTimeValueList = Lists.newArrayList();
        originTimeValueList.add(new OriginTimeValue(1542995383, 109991));

        List<TargetTimeValue> targetTimeValues1 = FillFullTimeValueUtil.MINUTE.fullFillTimeValue(LocalDateTime.now(), LocalDateTime.now()
                .plusDays(1), 1, originTimeValueList,
            TimeZone.getDefault());


        List<TargetTimeValue> targetTimeValues2 = FillFullTimeValueUtil.HOUR.fullFillTimeValue(LocalDateTime.now(), LocalDateTime.now()
                .plusDays(1), 2, originTimeValueList,
            TimeZone.getDefault());

        List<TargetTimeValue> targetTimeValues3 = FillFullTimeValueUtil.DAY.fullFillTimeValue(LocalDateTime.now(), LocalDateTime.now()
                .plusDays(100), 10, originTimeValueList,
            TimeZone.getDefault());

        List<TargetTimeValue> targetTimeValues4 = FillFullTimeValueUtil.MINUTE.fullFillTimeValue(LocalDateTime.now(), LocalDateTime.now()
                .plusDays(1), 1, null,
            TimeZone.getDefault());


        targetTimeValues1.forEach(targetTimeValue -> {
            System.out.println(targetTimeValue.getTime() + " , " + targetTimeValue.getValue());
        });

        System.out.println("---------------------------------------------------------------------");

        targetTimeValues2.forEach(targetTimeValue -> {
            System.out.println(targetTimeValue.getTime() + " , " + targetTimeValue.getValue());
        });

        System.out.println("---------------------------------------------------------------------");

        targetTimeValues3.forEach(targetTimeValue -> {
            System.out.println(targetTimeValue.getTime() + " , " + targetTimeValue.getValue());
        });

    }
}
