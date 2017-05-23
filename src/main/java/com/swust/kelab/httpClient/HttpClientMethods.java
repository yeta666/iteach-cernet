package com.swust.kelab.httpClient;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于请求资源服务器的http类
 * Created by yeta on 2017/5/14/014.
 */

public class HttpClientMethods {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientMethods.class);

    /**
     * post方法，带参数的请求都用post方法
     * @param params
     * @return
     */
    public HttpResult post(Params params) {
        //初始化返回结果
        HttpResult resultUtils = new HttpResult();
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost(params.getUri());
        // 创建参数
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        if(params.getParamNamesList() != null){
            for(int i = 0; i < params.getParamNamesList().size(); i++){
                formparams.add(new BasicNameValuePair((String) params.getParamNamesList().get(i), (String) params.getParamValuesList().get(i)));
            }
        }
        // 设置头
        if(params.getHeaderNamesList() != null){
            for(int i = 0; i < params.getHeaderNamesList().size(); i++){
                httppost.setHeader(params.getHeaderNamesList().get(i), params.getHeaderValuesList().get(i));
            }
        }
        if(params.getAccess_token() != null && !params.getAccess_token().equals("")){
            formparams.add(new BasicNameValuePair("access_token", params.getAccess_token()));
        }
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                int status_code = response.getStatusLine().getStatusCode();
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String responseContent = EntityUtils.toString(entity, "UTF-8");
                    logger.info("--------------------------------------------------------");
                    logger.info("Executing request = " + httppost.getURI());
                    logger.info("Status code = " + status_code);
                    logger.info("Response content = " + responseContent);
                    logger.info("--------------------------------------------------------");
                    resultUtils.setSuccess(true);
                    resultUtils.setStatusCode(status_code);
                    resultUtils.setResponseContent(responseContent);
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            resultUtils.setSuccess(false);
            resultUtils.setResponseContent(e.getMessage());
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            resultUtils.setSuccess(false);
            resultUtils.setResponseContent(e1.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            resultUtils.setSuccess(false);
            resultUtils.setResponseContent(e.getMessage());
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultUtils;
    }

    /**
     * get方法，不带参数的请求都用get方法
     */
    public HttpResult get(Params params) {
        // 初始化返回结果
        HttpResult resultUtils = new HttpResult();
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            // 创建httpget.
            HttpGet httpget = new HttpGet(params.getUri() + "?access_token=" + params.getAccess_token());
            System.out.println("executing request " + httpget.getURI());
            // 执行get请求.
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                int status_code = response.getStatusLine().getStatusCode();
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                // 打印响应状态
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    String responseContent = EntityUtils.toString(entity, "UTF-8");
                    logger.info("--------------------------------------------------------");
                    logger.info("Executing request = " + httpget.getURI());
                    logger.info("Status code = " + status_code);
                    logger.info("Response content = " + responseContent);
                    logger.info("--------------------------------------------------------");
                    resultUtils.setSuccess(true);
                    resultUtils.setStatusCode(status_code);
                    resultUtils.setResponseContent(responseContent);
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            resultUtils.setSuccess(false);
            resultUtils.setResponseContent(e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
            resultUtils.setSuccess(false);
            resultUtils.setResponseContent(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            resultUtils.setSuccess(false);
            resultUtils.setResponseContent(e.getMessage());
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultUtils;
    }
}
