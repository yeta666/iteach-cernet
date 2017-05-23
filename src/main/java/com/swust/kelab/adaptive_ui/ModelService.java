package com.swust.kelab.adaptive_ui;

import com.swust.kelab.httpClient.HttpClientMethods;
import com.swust.kelab.httpClient.HttpResult;
import com.swust.kelab.httpClient.Params;
import com.swust.kelab.httpClient.Result;
import com.swust.kelab.oauth.AccessToken;
import com.swust.kelab.oauth.AccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * 与模型有关的逻辑处理类
 * Created by yeta on 2017/5/15/015.
 */
@Service
public class ModelService {

    @Value("${my_services_uri.GET_MODEL_URI}")
    private String GET_MODEL_URI;
    
    @Value("${my_services_uri.DELETE_MODEL_URI}")
    private String DELETE_MODEL_URI;
    
    /**
     * 获取所有模型
     * @param request
     * @return
     */
    public Result getAll(HttpServletRequest request){
        AccessToken accessToken = (AccessToken) request.getSession().getAttribute("access_token");
        if(accessToken != null){
        	//初始化参数
            Params params = new Params();
            params.setUri(GET_MODEL_URI);
            
            params.setAccess_token(accessToken.getAccess_token());
            //请求
            HttpClientMethods httpClientMethods = new HttpClientMethods();
            HttpResult questionResult = httpClientMethods.get(params);
            //验证请求结果
            if (!questionResult.isSuccess()) {
                return new Result(false, -1, questionResult.getResponseContent(), null);
            } else {
                //获取question
                return new Result(true, questionResult.getStatusCode(), questionResult.getResponseContent(), null);
            }
        }else{
        	return new Result(false, -1, "session中没有access_token!", null);
        }  
    }
    
    /**
     * 删除一个或多个模型
     * @param userId
     * @param answers
     * @param request
     * @return
     */
    public Result delete(String ids, HttpServletRequest request) {
    	AccessToken accessToken = (AccessToken) request.getSession().getAttribute("access_token");
        if(accessToken != null){
            //初始化参数
            Params params = new Params();
            params.setUri(DELETE_MODEL_URI);
            List<String> paramNamesList = new ArrayList<String>();
            paramNamesList.add("ids");
            List<String> paramValuesList = new ArrayList<String>();
            paramValuesList.add(ids);
            params.setParamNamesList(paramNamesList);
            params.setParamValuesList(paramValuesList);
            params.setAccess_token(accessToken.getAccess_token());
            //请求
            HttpClientMethods httpClientMethods = new HttpClientMethods();
            HttpResult userTypeResult = httpClientMethods.post(params);
            //验证请求结果
            if (!userTypeResult.isSuccess()) {
                return new Result(false, -1, userTypeResult.getResponseContent(), null);
            } else {
                //获取userType
                return new Result(true, userTypeResult.getStatusCode(), userTypeResult.getResponseContent(), null);
            }
        }else{
        	return new Result(false, -1, "session中没有access_token!", null);
        }  
    }
}
