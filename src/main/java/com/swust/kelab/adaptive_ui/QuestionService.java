package com.swust.kelab.adaptive_ui;

import com.swust.kelab.httpClient.HttpClientMethods;
import com.swust.kelab.httpClient.HttpResult;
import com.swust.kelab.httpClient.Params;
import com.swust.kelab.httpClient.Result;
import com.swust.kelab.oauth.AccessToken;
import com.swust.kelab.oauth.AccessTokenService;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 获取调查表题目的控制器类
 * Created by yeta on 2017/5/14/014.
 */
@Service
public class QuestionService {

    @Value("${my_services_uri.GET_QUESTION_URI}")
    private String GET_QUESTION_URI;

    /**
     * 获取所有题目
     * @return
     */
    public Result getAll(HttpServletRequest request){
        AccessToken accessToken = (AccessToken) request.getSession().getAttribute("access_token");
        if(accessToken != null){
        	//初始化参数
            Params params = new Params();
            params.setUri(GET_QUESTION_URI);
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
}
