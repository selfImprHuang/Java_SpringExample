/*
 * @(#) ShareResponseObject
 * 版权声明 网宿科技, 版权所有 违者必究
 *
 * <br> Copyright:  Copyright (c) 2018
 * <br> Company:网宿科技
 * <br> @author Administrator
 * <br> @description 功能描述
 * <br> 2018-11-13 20:34:52
 */

/**
 *
 */

package config.response;

/**
 * 响应对象。
 *
 */
public class ResponseObject<T> {

    /**
     * 状态码
     */
    private String status;

    /**
     * 描述信息
     */
    private String message;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 数据对象
     */
    private T dataView;

    public ResponseObject(String code, String message, T value) {
        this.status = code;
        this.message = message;
        this.dataView = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getDataView() {
        return dataView;
    }

    public void setDataView(T dataView) {
        this.dataView = dataView;
    }
}
