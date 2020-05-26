package logic.sensitive;

import org.apache.commons.lang3.StringUtils;

/**
 * 这边是一个关于Test类的处理规则，最好是对所有的类型进行细分
 * @author 志军
 */
public class ObjectRule implements SensitiveRule<Test>{

    @Override
    public Test process(Test test) {
       if (test !=null){
           if(StringUtils.isNotBlank(test.getPhone())){
               test.setPhone(test.getPhone().charAt(0) + "XXX" + test.getPhone().charAt(test.getPhone().length()-1));
           }

           if(StringUtils.isNotBlank(test.getEmail())){
               test.setEmail(test.getEmail().charAt(0) + "XXX" + test.getEmail().charAt(test.getEmail().length()-1));
           }
       }
        return null;
    }
}
