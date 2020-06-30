package service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


/**
 * Created by Administrator on 2017/7/18.
 * @author 志军
 */
@Service
public class CommonServiceImpl implements CommonService {


    @Override
    public String testAopParam(int a) {
        return String.valueOf(a);
    }

    @Override
    public void testAop() {
        System.out.println("测试Aop");
    }


    @Override
    @Cacheable(cacheNames = "bm")
    public String cacheTest() {
        System.out.println("我是cache，我执行了");
        return "123444";
    }
}