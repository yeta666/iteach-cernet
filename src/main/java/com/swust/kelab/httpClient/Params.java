package com.swust.kelab.httpClient;

import java.util.List;

/**
 * 用于请求的参数集中处理类
 * Created by yeta on 2017/5/14/014.
 */
public class Params {
    //uri
    private String uri;
    //参数名列表
    private List<String> paramNamesList;
    //参数值列表
    private List<String> paramValuesList;
    //头名列表
    private List<String> headerNamesList;
    //头值列表
    private List<String> headerValuesList;
    //access_token
    private String access_token;

    public Params() {
    }

    public Params(String uri, List<String> paramNamesList, List<String> paramValuesList, List<String> headerNamesList, List<String> headerValuesList, String access_token) {
        this.uri = uri;
        this.paramNamesList = paramNamesList;
        this.paramValuesList = paramValuesList;
        this.headerNamesList = headerNamesList;
        this.headerValuesList = headerValuesList;
        this.access_token = access_token;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<String> getParamNamesList() {
        return paramNamesList;
    }

    public void setParamNamesList(List<String> paramNamesList) {
        this.paramNamesList = paramNamesList;
    }

    public List<String> getParamValuesList() {
        return paramValuesList;
    }

    public void setParamValuesList(List<String> paramValuesList) {
        this.paramValuesList = paramValuesList;
    }

    public List<String> getHeaderNamesList() {
        return headerNamesList;
    }

    public void setHeaderNamesList(List<String> headerNamesList) {
        this.headerNamesList = headerNamesList;
    }

    public List<String> getHeaderValuesList() {
        return headerValuesList;
    }

    public void setHeaderValuesList(List<String> headerValuesList) {
        this.headerValuesList = headerValuesList;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

}
