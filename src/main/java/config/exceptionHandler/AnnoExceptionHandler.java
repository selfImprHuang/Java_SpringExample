package config.exceptionHandler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class AnnoExceptionHandler {

    /**
     * @a
     * @param exception
     * @return
     */
    @ExceptionHandler(CustomGenericException.class)//可以直接写@ExceptionHandler,不指明异常类，会自动映射
    public ModelAndView customGenericExceptionHnadler(CustomGenericException exception){ //还可以声明接收其他任意参数
        ModelAndView modelAndView = new ModelAndView("generic_error");
        modelAndView.addObject("errCode",exception.getErrCode());
        modelAndView.addObject("errMsg",exception.getErrMsg());
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)//可以直接写@EceptionHandler，IOExeption继承于Exception
    public ModelAndView allExceptionHandler(Exception exception){
        ModelAndView modelAndView = new ModelAndView("generic_error");
        modelAndView.addObject("errMsg", "this is Exception.class");
        return modelAndView;
    }


    public class CustomGenericException extends RuntimeException{
        private static final long serialVersionUID = 1L;

        private String errCode;
        private String errMsg;

        public String getErrCode() {
            return errCode;
        }


        public String getErrMsg() {
            return errMsg;
        }

        public CustomGenericException(String errCode, String errMsg) {
            this.errCode = errCode;
            this.errMsg = errMsg;
        }
    }

}
