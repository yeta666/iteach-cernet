package com.swust.kelab.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.swust.kelab.utils.CookieUtil;
import com.swust.kelab.web.json.JsonAndView;

@Controller
@RequestMapping("/home")
public class HomeController {
	
	@Resource
    HttpServletRequest request;
	/**
	 * 获取导航菜单信息
	 * @return
	 */
	@RequestMapping(value="/navibar",method = RequestMethod.GET)
	public JsonAndView navibar(HttpServletResponse respons){
		JsonAndView jv = new JsonAndView();
		HttpSession session = request.getSession();
		Cookie[] cookies = request.getCookies();
		String cookieValue = CookieUtil.findCookieValue(cookies, "user");
		if (cookieValue != null && !"".equals(cookieValue) && session.getAttribute("userId")!=null) {
			//校验数据是否完整
			String[] objArry = CookieUtil.legality(cookieValue, request);
				//校验结果
				if(objArry!=null){
					jv.setRet(true);
					jv.addData("msg", "数据安全！");
					jv.addData("userId", objArry[0]);//登录id[0]
					jv.addData("userLoginname", objArry[1]);//登录名字 [1]
					jv.addData("userDepaId", objArry[2]);//用户部门[2]
					jv.addData("userDepaType", objArry[3]);//用户部门类型[3]
					jv.addData("depaName", objArry[4]);//用户部门名称[4]
					jv.addData("userType", objArry[5]);//用户角色[5]
					jv.addData("userName", objArry[6]);//真实姓名[6]
				}else{
					CookieUtil.deleteCookie(cookies, "user", respons);
					jv.addData("msg", "数据不安全！");
					jv.addData("userId", 0);//登录id[0]
					jv.addData("userLoginname", "");//登录名字 [1]
					jv.addData("userDepaId", 0);//用户部门[2]
					jv.addData("userDepaType", 0);//用户部门类型[3]
					jv.addData("depaName", "");//用户部门名称[4]
					jv.addData("userType", 0);//用户角色[4]
					jv.addData("userName", "未登录");//真实姓名[5]
				}
		} else {
			CookieUtil.deleteCookie(cookies, "user", respons);
			jv.addData("msg", "空数据！");
			jv.addData("userId", 0);//登录id[0]
			jv.addData("userLoginname", "");//登录名字 [1]
			jv.addData("userDepaId", 0);//用户部门[2]
			jv.addData("userType", 0);//用户角色[3]
			jv.addData("depaName", "");//用户部门名称[4]
			jv.addData("userName", "未登录");//真实姓名[5]
		}
		return jv;
	}
}
