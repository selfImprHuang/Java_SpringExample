package controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import service.CommonService;

/**
 * @author selfimpr
 * @description  测试cache缓存是否设置成功，可以在配置文件中指定位置看到cache,这边的配置是放在
 * @see config.CacheSetFromProperty
 * @date : 2017/11/11 21:46
 */
@Controller
public class CacheController {

    @Autowired
    private CommonService commonService;

    @RequestMapping(value = "/cache/cache_test", method = RequestMethod.GET)
    public String cacheTest() {
        String cache = commonService.cacheTest();
        System.out.println(cache);
        return "success";
    }

}
