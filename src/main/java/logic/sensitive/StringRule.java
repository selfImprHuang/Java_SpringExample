package logic.sensitive;

import org.apache.commons.lang3.StringUtils;

/**
 * 这边是一个通用的String处理规则，也可以分来对不同的String进行处理，比如说电话怎么处理，邮箱怎么处理
 */
public class StringRule implements SensitiveRule<String>{

    @Override
    public String process(String s) {
        if(StringUtils.isNotBlank(s)){
           s = s.charAt(0) + "XXX" + s.charAt(s.length()-1);
        }
        return s;
    }
}
