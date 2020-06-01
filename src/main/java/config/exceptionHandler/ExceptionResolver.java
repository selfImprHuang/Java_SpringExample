package config.exceptionHandler;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 志军
 * 继承spring的问题处理类， spring有默认的异常处理体系，默认的异常处理体系是先进行处理的。
 * 这里继承Order接口可以把我们的处理器作为位置0的处理器。但是默认的异常处理器的位置也是0，所以我们无法控制他们谁先处理
 *
 * 可以参考MvcComponentLoader的描述
 *
 * 通过继承接口进行处理是第一种方式
 */
@Order(value = 0)
public class ExceptionResolver implements HandlerExceptionResolver {


    /**
     * 厉害了。这边也可测试一下如果是不给这个异常放在第一位的话，会出现什么情况，
     * 其他的异常处理体系是不是会把异常进行处理
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        System.out.printf("发现了异常，进入%s类", ExceptionResolver.class.getName());
        System.out.printf("错误信息：%s", ex.getMessage());
        //这里是匹配错误类型，把错误信息加上的地方  -  你可能会匹配很多的错误，这里的做法应该是对错误进行分类。
        ExceptionResponse exceptionResponse = null;
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) ex;
            //这里面包含了错误的信息
            System.out.println(methodArgumentNotValidException.toString());
            exceptionResponse =  new ExceptionResponse("500","报错");
        }
        //最后就是异常处理返回
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setExtractValueFromSingleKeyModel(true);
        return new ModelAndView(view, "response",exceptionResponse);
    }

}
    class  ExceptionResponse{
        private String code;
        private String message;

        ExceptionResponse(String code, String message) {
            this.code = code;
            this.message = message;
        }
    }
