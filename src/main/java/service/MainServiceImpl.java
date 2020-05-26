package service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by Administrator on 2017/7/18.
 * @author 志军
 */
@Service
@Transactional
public class MainServiceImpl implements MainService {


    @Override
    public String testAopParam(int a) {
        return String.valueOf(a);
    }

    @Override
    public void testAop() {
        System.out.println("测试Aop");
    }

}