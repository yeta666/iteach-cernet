package com.swust.kelab.httpClient;

/**
 * http请求的返回结果
 * Created by yeta on 2017/5/14/014.
 */
public class HttpResult {
    //success
    private boolean success;
    //statusCode
    private int statusCode;
    //responseContent
    private String responseContent;

    public HttpResult() {
    }

    public HttpResult(boolean success, int statusCode, String responseContent) {
        this.success = success;
        this.statusCode = statusCode;
        this.responseContent = responseContent;
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

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

}
