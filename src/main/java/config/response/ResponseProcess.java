/*
 * @(#) ResponseConfiguration
 * 版权声明 黄志军， 版权所有 违者必究
 *
 * <br> Copyright:  Copyright (c) 2018
 * <br> Company:黄志军
 * <br> @author selfImpr
 * <br> 2018-05-31 14:59:23
 * <br> @description
 *
 *
 */

package config.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

/**
 * 这里通过一个注解和继承的方式，对所有出控制器（被某一个注解标注的类） - 这里自定了一个注解为TestResponse
 * 对这种类进行参数的处理
 * 好像这个注解还有叠加的作用....
 * 这个注解也可以作为异常处理的操作。即在每个方法上面添加处理的class。还想这样很麻烦啊，还不如用异常处理器
 * @author 志军
 */
@ControllerAdvice
public class ResponseProcess extends AbstractMappingJacksonResponseBodyAdvice {

    /**
     * 这个注解好像是必须捕获ResponseBody这个解决才会进行执行，@ResponseBody表明这个返回的东西是在报文里面，
     * 且如果不配置转换器，spring会自动提供7个，但是如果我们想规定顺序就要自己配置
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        System.out.println( returnType.getMethod().getName());
        return returnType.hasMethodAnnotation(ResponseBody.class);
    }

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType, MethodParameter returnType,
                                           ServerHttpRequest request, ServerHttpResponse response) {
        Object value = bodyContainer.getValue();
        ResponseObject<Object> object = new ResponseObject<>("200", "调度成功", value);
        bodyContainer.setValue(object);
    }
}
