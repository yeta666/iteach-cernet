package com.swust.kelab.web.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.swust.kelab.service.LoginService;

public class OnlineUserListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        // 取得登录的用户id
        if(session.getAttribute("userId")!=null){
        	int userId = (Integer) session.getAttribute("userId");
            if (userId > 0) {
                ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(session
                        .getServletContext());
                LoginService loginService = (LoginService) ctx.getBean("loginService"); // 填写要注入的类,注意第一个字母小写
                loginService.loginOff(userId);//退出
                // 清理工作
                session.invalidate();
            }
        }
    }
}
