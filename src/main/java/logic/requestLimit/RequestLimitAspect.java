
package logic.requestLimit;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import util.DateTimeUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ${DESCRIPTION}
 *
 * @author huangzj1
 * @date 2018-12-16 17:10
 */
@Aspect
public class RequestLimitAspect {
    private static final Logger logger = LoggerFactory.getLogger("RequestLimitLogger");


    private static final Map<String, RequestLimitBody> URL_MAP = new ConcurrentHashMap<>();


    @Before(value = "@annotation(limit) &&args(request)")
    public void requestLimit(JoinPoint joinPoint, HttpServletRequest request, RequestLimit limit) throws RequestLimitException {
        try {
            if (request == null) {
                throw new RequestLimitException("方法中缺失HttpServletRequest参数");
            }

            if(limit.waits()<limit.time()){
                throw new RequestLimitException("程序错误，延时时间不能小于规定时间");
            }

            //其实这边还应该加上一个客户端的ip或者登录用户来做标识，但是我这边没做
            String key = "req_limit_".concat(joinPoint.getSignature().getName());
            RequestLimitBody body = URL_MAP.get(key);
            if (body == null) {
                body = new RequestLimitBody();
                body.setClickCount(1);
                body.setLastClick(DateTimeUtil.getNowSeconds());
                URL_MAP.put(key, body);
                return;
            }

            //如果点击超过次数，延迟点击,并且还没到达时间。
            if (body.getDelayClickUtilTIme() != null && DateTimeUtil.getNowSeconds() - body.getDelayClickUtilTIme() <= limit.waits()) {
                System.out.println("超过点击次数，还需要等待" + (limit.waits() - (DateTimeUtil.getNowSeconds() - body.getDelayClickUtilTIme())) + "秒");
                throw new RequestLimitException("超过点击次数，还需要等待" + (limit.waits() - (DateTimeUtil.getNowSeconds() - body.getDelayClickUtilTIme())) + "秒");
            }

            //如果最后一次点击在规定时间之内并且点击次数超过规定点击次数.进入延迟点击
            if (DateTimeUtil.getNowSeconds() - body.getLastClick() < limit.time() && body.getClickCount() >= limit.count()) {
                body.setDelayClickUtilTIme(DateTimeUtil.getNowSeconds());
                System.out.println("暂时不可点击，需等待" + limit.waits() + "秒");
                throw new RequestLimitException("暂时不可点击，需等待" + limit.waits() + "秒");
            }

            //如果第一次点击到最后一次点击超过了规定时间重置（前提是前面的都要不满足）
            if (DateTimeUtil.getNowSeconds() - body.getLastClick() > limit.time()) {
                body.setClickCount(1);
                body.setLastClick(DateTimeUtil.getNowSeconds());
                URL_MAP.put(key, body);
                return;
            }

            //没有超过时间要加1
            body.setClickCount(body.getClickCount() + 1);
        } catch (RequestLimitException e) {
            throw e;
        } catch (Exception e) {
            logger.error("发生异常: ", e);
        }
    }
}

