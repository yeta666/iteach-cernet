package com.swust.kelab.oauth;

import com.alibaba.fastjson.JSONObject;
import com.swust.kelab.httpClient.HttpClientMethods;
import com.swust.kelab.httpClient.HttpResult;
import com.swust.kelab.httpClient.Params;
import com.swust.kelab.httpClient.Result;
import com.swust.kelab.utils.Base64Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * 和access_token有关的逻辑处理类
 * Created by yeta on 2017/5/14/014.
 */
@Service

public class AccessTokenService {
	
    @Value("${my_client_infos.TOKEN_URI}")
    private String TOKEN_URI;

    @Value("${my_client_infos.CLIENT_ID}")
    private String CLIENT_ID;

    @Value("${my_client_infos.CLIENT_SECRET}")
    private String CLIENT_SECRET;

    @Value("${my_client_infos.GRANT_TYPE}")
    private String GRANT_TYPE;

    @Value("${my_client_infos.SCOPE}")
    private String SCOPE;

    public Result getAccessTokenService(){
        //初始化参数
        Params p_params = new Params();
        p_params.setUri(TOKEN_URI);
        List<String> p_paramNamesList = new ArrayList<String>();
        p_paramNamesList.add("scope");
        p_paramNamesList.add("grant_type");
        p_params.setParamNamesList(p_paramNamesList);
        List<String> p_paramValuesList = new ArrayList<String>();
        p_paramValuesList.add(SCOPE);
        p_paramValuesList.add(GRANT_TYPE);
        p_params.setParamValuesList(p_paramValuesList);
        List<String> h_headerNamesList = new ArrayList<String>();
        h_headerNamesList.add("Authorization");
        p_params.setHeaderNamesList(h_headerNamesList);
        List<String> h_headervaluesList = new ArrayList<String>();
        //Base64Utils base64Utils = new Base64Utils();
        //h_headervaluesList.add("Basic " + base64Utils.getBase64(CLIENT_ID + ":" + CLIENT_SECRET));
        Base64Util base64util = new Base64Util();
        h_headervaluesList.add("Basic " + base64util.encodeStr(CLIENT_ID + ":" + CLIENT_SECRET));
        p_params.setHeaderValuesList(h_headervaluesList);
        //请求
        HttpClientMethods httpClientMethods = new HttpClientMethods();
        HttpResult accessTokenResult = httpClientMethods.post(p_params);
        //验证请求结果
        if(!accessTokenResult.isSuccess()){
            //连接不上授权服务器
            return new Result(false, -1,"无法连接授权服务器！", new AccessToken());
        }else {
            if (accessTokenResult.getStatusCode() != 200) {
                //参数错误或验证错误
                return new Result(false, accessTokenResult.getStatusCode(), accessTokenResult.getResponseContent(), new AccessToken());
            } else {
                //获取成功
                //获取access_token
                AccessToken accessToken = new AccessToken();
                String responseContent = accessTokenResult.getResponseContent();
                JSONObject jsonObject = JSONObject.parseObject(responseContent);
                accessToken.setAccess_token(jsonObject.getString("access_token"));
                accessToken.setExpires_in(jsonObject.getString("expires_in"));
                accessToken.setScope(jsonObject.getString("scope"));
                accessToken.setToken_type(jsonObject.getString("token_type"));
                //返回结果
                return new Result(true, accessTokenResult.getStatusCode(), "", accessToken);
            }
        }
    }
}
