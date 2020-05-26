

package logic.requestLimit;

/**
 * ${DESCRIPTION}
 *
 * @author huangzj1
 * @date 2018-12-16 17:17
 */
public class RequestLimitException extends Exception {
    private static final long serialVersionUID = 1364225358754654702L;

    public RequestLimitException() {
        super("HTTP请求超出设定的限制");
    }

    public RequestLimitException(String message) {
        super(message);
    }

}