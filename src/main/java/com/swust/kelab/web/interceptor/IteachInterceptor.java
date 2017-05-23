package com.swust.kelab.web.interceptor;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.swust.kelab.utils.CommonUtil;
import com.swust.kelab.utils.CookieUtil;

public class IteachInterceptor implements HandlerInterceptor {

	private static final String LOGIN_URL = "/iteach/static/html/home.html";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 查看视频验证码 放行
		if (request.getRequestURI().endsWith("getVideoConfirmCode.do"))
			return true;
		// 文档、视频资料下载Action方法 放行
		if (request.getRequestURI().contains("downloadDocumentResource.do"))
			return true;
		// 防视频超时，伪请求
		if (request.getRequestURI().contains("keepAliveWhenWatchingVideo.do"))
			return true;
		if (request.getRequestURI().endsWith("recom.do")) {
			return true;
		}
		// 获取所有调查表题目
		if (request.getRequestURI().endsWith("question/getAll")) {
			return true;
		}
		// 获取用户类型
		if (request.getRequestURI().endsWith("userType/getUserType")) {
			return true;
		}
		// 获取所有模型
		if (request.getRequestURI().endsWith("model/getAll")) {
			return true;
		}
		// 删除一个或多个模型
		if (request.getRequestURI().endsWith("model/delete")) {
			return true;
		}
		// 获取所有用户答案
		if (request.getRequestURI().endsWith("userAnswers/getAll")) {
			return true;
		}
		// 删除一个或多个用户答案
		if (request.getRequestURI().endsWith("userAnswers/delete")) {
			return true;
		}
		// 获取所有训练集属性
		if (request.getRequestURI().endsWith("trainArrayAttributes/getAll")) {
			return true;
		}
		// 请求到登录页面 放行
		if (request.getRequestURI().endsWith("/login") || request.getRequestURI().endsWith("/loginOff")) {
			Cookie cookie = new Cookie("authCookie", "");
			cookie.setPath("/");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
			return true;
		}
		String url = request.getHeader("referer");
		if (url != null) {
			if (url.contains("home.html") || url.contains("_error.html"))
				return true;
		}
		Cookie[] cookies = request.getCookies();
		String cookieValue = CookieUtil.findCookieValue(cookies, "user");
		// 校验登陆数据是否完整
		if (cookieValue == null) {
			response.setStatus(CommonUtil.UNLOGIN_CODE);
			return false;
		} else {
			String[] objArry = null;
			try {
				objArry = CookieUtil.legality(cookieValue, request);
			} catch (Exception e) {
				return executeInterceptor(cookies, request, response);
			}
			if (objArry == null) {
				return executeInterceptor(cookies, request, response);
			} else {
				String authValue = CookieUtil.findCookieValue(cookies, "authCookie");
				String allActions = CookieUtil.findCookieValue(cookies, "allActions");
				// 校验权限信息是否完整
				if (allActions != null && !"".equals(allActions) && authValue != null && !"".equals(authValue)) {
					String auVal = CookieUtil.validateAuthority(authValue, request);
					String actVal = CookieUtil.validateAuthority(allActions, request);
					if (actVal == null || auVal == null) {
						return executeInterceptor(cookies, request, response);
					} else {
						return validateAuthority(auVal, actVal, request, response);
					}
				}
				return true;
			}
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

	/**
	 * 公用执行拦截操作
	 * 
	 * @author yangzq
	 * @param cookies
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private boolean executeInterceptor(Cookie[] cookies, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 异步请求
		if (isAjaxRequest(request)) {
			CookieUtil.deleteCookie(cookies, "user", response);
			CookieUtil.deleteCookie(cookies, "authCookie", response);

			HttpSession session = request.getSession();
			session.removeAttribute("userId");

			response.setStatus(CommonUtil.AJAX_COOKIE_ERROR);// 跳转到COOKIE验证失败页面
			return false;
		}
		// 非异步请求
		else {
			response.sendRedirect(LOGIN_URL);
			return false;
		}
	}

	/**
	 * 判断请求是否来自ajax
	 * 
	 * @author yangzq
	 * @param request
	 * @return
	 */
	private boolean isAjaxRequest(HttpServletRequest request) {
		boolean flag = false;
		if (request.getHeader("X-Requested-With") != null
				&& request.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest")) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 检验请求权限是否合法
	 * 
	 * @author yangzq
	 * @param request
	 * @param auVal
	 *            用户拥有的权限白名单
	 * @param actVal
	 *            需要拦截的权限黑名单
	 * @return
	 */
	private boolean validateAuthority(String auVal, String actVal, HttpServletRequest request,
			HttpServletResponse response) {
		request.getCookies();
		String url = request.getHeader("referer");
		String actionUrl = request.getRequestURI();
		String authority = "";
		String secondCol = "";
		if (url.contains("sysman_Column.html")) {
			return true;
		}
		if (url.indexOf("secondCol=") < 0) {
			response.setStatus(CommonUtil.ILLEGAL_OPERATION);
			return false;
		}
		String colIds = url.substring(url.indexOf("secondCol=") + 10, url.length());
		secondCol = colIds.substring(0, colIds.indexOf("&secondCol=") <= 0 ? colIds.length() : colIds.indexOf("&"));
		if (url.matches(".+_.+_.+")) {
			actionUrl = url.substring(url.indexOf("static/html/") + 12, url.indexOf(".html") + 5);
		} else if (url.indexOf("secondCol=") > 0) {
			actionUrl = actionUrl.substring(actionUrl.indexOf("handler") + 7, actionUrl.length());
		}
		authority = secondCol + actionUrl;
		/*
		 * if (actVal.contains(secondCol)) { if (!auVal.contains(secondCol)) {
		 * response.setStatus(CommonUtil.AUTHORITY_REFUSE); return false; } } if
		 * (actVal.contains(actionUrl)) { if (!auVal.contains(actionUrl)) {
		 * response.setStatus(CommonUtil.AUTHORITY_REFUSE); return false; } }
		 */
		if (actVal.contains(authority)) {
			if (auVal.contains(authority)) {
				return true;
			} else {
				response.setStatus(CommonUtil.AUTHORITY_REFUSE);
				return false;
			}
		} else {
			return true;
		}
	}
}
