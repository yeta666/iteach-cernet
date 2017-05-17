package com.swust.kelab.httpClient;

/**
 * 自定义返回结果格式
 * Created by yeta on 2017/5/14/014.
 */
public class Result {
    //success
    private boolean success;
    //statusCode
    private int statusCode;
    //失败原因或提示信息
    private String message;
    //数据
    private Object data;

    public Result() {
    }

    public Result(boolean success, int statusCode, String message, Object data) {
        this.success = success;
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
