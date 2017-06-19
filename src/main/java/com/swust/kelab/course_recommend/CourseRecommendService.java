package com.swust.kelab.course_recommend;

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
 * 与课程推荐有关的逻辑处理类
 * Created by yeta on 2017/5/15/015.
 */
@Service
public class CourseRecommendService {

    @Value("${my_services_uri.GET_COURSE_RECOMMEND_URI}")
    private String GET_COURSE_RECOMMEND_URI;
    
    @Autowired
    private AccessTokenService accessTokenService;

    public Result getCourseRecommend(String userId, HttpServletRequest request) {
    	//获取access_token
    	AccessToken accessToken = new AccessToken();
        Result getAccessTokenResult = accessTokenService.getAccessTokenService();
        if (!getAccessTokenResult.isSuccess()) {
        	request.getSession().setAttribute("access_token", null);
        } else {
        	accessToken = (AccessToken) getAccessTokenResult.getData();
        	request.getSession().setAttribute("access_token", accessToken);
        }
        if(accessToken != null){
            //初始化参数
            Params params = new Params();
            params.setUri(GET_COURSE_RECOMMEND_URI);
            List<String> paramNamesList = new ArrayList<String>();
            paramNamesList.add("userId");
            List<String> paramValuesList = new ArrayList<String>();
            paramValuesList.add(userId);
            params.setParamNamesList(paramNamesList);
            params.setParamValuesList(paramValuesList);
            params.setAccess_token(accessToken.getAccess_token());
            //请求
            HttpClientMethods httpClientMethods = new HttpClientMethods();
            HttpResult courseRecommendResult = httpClientMethods.post(params);
            //验证请求结果
            if (!courseRecommendResult.isSuccess()) {
                return new Result(false, -2, courseRecommendResult.getResponseContent(), null);
            } else {
                //获取userType
                return new Result(true, courseRecommendResult.getStatusCode(), courseRecommendResult.getResponseContent(), null);
            }
        }else{
        	return new Result(false, -1, "session中没有access_token!", null);
        }  
    }
}
