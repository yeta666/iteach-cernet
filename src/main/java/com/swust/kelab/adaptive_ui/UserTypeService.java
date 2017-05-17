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
 * 与用户类型有关的逻辑处理类
 * Created by yeta on 2017/5/15/015.
 */
@Service
public class UserTypeService {

    @Value("${my_services_uri.GET_USER_TYPE_URI}")
    private String GET_USER_TYPE_URI;

    public Result getUserType(String userId, String answers, HttpServletRequest request) {
    	AccessToken accessToken = (AccessToken) request.getSession().getAttribute("access_token");
        if(accessToken != null){
            //初始化参数
            Params params = new Params();
            params.setUri(GET_USER_TYPE_URI);
            List<String> paramNamesList = new ArrayList<String>();
            paramNamesList.add("userId");
            paramNamesList.add("answers");
            List<String> paramValuesList = new ArrayList<String>();
            paramValuesList.add(userId);
            paramValuesList.add(answers);
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
