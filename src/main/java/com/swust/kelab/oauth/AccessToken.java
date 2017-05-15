package com.swust.kelab.oauth;

/**
 * access_token的实体类
 * Created by yeta on 2017/5/14/014.
 */
public class AccessToken {
    //access_token
    private String access_token;
    //token_tpe
    private String token_type;
    //expires_in
    private String expires_in;
    //scope
    private String scope;

    public AccessToken() {
    }

    public AccessToken(String access_token, String token_type, String expires_in, String scope) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
        this.scope = scope;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
