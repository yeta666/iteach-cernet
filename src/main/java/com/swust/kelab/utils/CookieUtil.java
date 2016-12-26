package com.swust.kelab.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import com.swust.kelab.domain.User;

public class CookieUtil {

    /**
     * 拼装cookie信息防止用户进行cookie欺骗 拼装用户名+ip地址+登陆时间
     * 
     * @param cookies
     * @param cookieKey
     * @return
     */
    public static String buildCookieInfo(User user, HttpServletRequest request, int departType, String depaName) {
        StringBuilder sb = new StringBuilder();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        sb.append(user.getUserId()).append("-");// 登陆人id[0]
        sb.append(user.getUserLoginname().trim()).append("-");// 登录名字 [1]
        sb.append(user.getUserDepaId()).append("-");// 用户部门[2]
        sb.append(departType).append("-");// 部门类型[3]
        sb.append(depaName).append("-");// 部门名字[4]
        sb.append(user.getUserType()).append("-");// 用户角色[5]
        sb.append(user.getUserRealname().trim()).append("-");// 真实姓名[6]
        // 通过ＭＤ５不可逆加密，方面后面对这些数据进行核对。从而防止它人通过cookie破解网站。
        // 同时这也起到不需要将要核对的数据放到数据库，从而提交了效率，节省了存储空间。
        sb.append(MD5.getMD5((user.getUserLoginname() + ip + user.getUserDepaId() + departType + depaName + user
                .getUserType()).getBytes())); // 校验位[7]
        return sb.toString();
    }

    /**
     * 传入cookie里的value值进行校验合法性
     * 
     * @param cookieValue
     * @return
     */
    public static String[] legality(String cookieValue, HttpServletRequest request) {
        try {
            cookieValue = Base64Util.decodeStr(URLDecoder.decode(cookieValue, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        String[] objArry = cookieValue.split("-");
        // 获取客户端IP,防止客户ip被修改
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 当前登录ip和cookie传参数据拼装
        String compare = "";
        try {
            compare = MD5.getMD5((objArry[1] + ip + objArry[2] + objArry[3] + objArry[4] + objArry[5]).getBytes());
        } catch (Exception e) {
            return null;
        }
        // cookie传过来末位的校验码
        String validate = objArry[7].substring(0, compare.length());
        if (compare == validate || compare.equalsIgnoreCase(validate)) {
            return objArry;
        }
        return null;
    }

    /**
     * 查找cookie里的value值
     * 
     * @param cookies
     * @param cookieKey
     * @return
     */
    public static String findCookieValue(Cookie[] cookies, String cookieKey) {
        if (ArrayUtils.isEmpty(cookies)) {
            return null;
        }
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(cookieKey)) {
                return cookies[i].getValue();
            }
        }
        return null;
    }

    /**
     * 将拼装的cookie信息装载到response中
     * 
     * @param sb
     * @param name
     * @param path
     * @param domain
     * @param maxAge
     * @param response
     */
    public static void addCookieToResponse(String value, String name, String path,/* String domain, */int maxAge,
            HttpServletResponse response) throws UnsupportedEncodingException {
        value.replace("\r\n", "<br>");
        value.replace("\n", "<br>");
        value = URLEncoder.encode(Base64Util.encodeStr(value), "UTF-8");// 对传入的value进行64位编码
        String key = URLEncoder.encode(name, "UTF-8");
        Cookie cookie = new Cookie(key, value);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        // cookie.setDomain(domain);
        response.addCookie(cookie);
    }

    /**
     * 删除cookie，把cookie生命周期设置为0即可
     * 
     * @param cookies
     * @param name
     * @param response
     */
    public static void deleteCookie(Cookie[] cookies, String name, HttpServletResponse response) {
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                // 找到需要删除的Cookie
                if (cookie.getName().compareTo(name) == 0) {
                    // 设置生存期为0
                    cookie.setPath("/");
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    // 设回Response中生效
                    response.addCookie(cookie);
                }
            }
        }
    }

    /**
     * 通过request对象，解析出cookie中user用户<br>
     * 
     * @param request
     * @return 登陆的user对象
     */
    public static User getCookieUser(HttpServletRequest request) {
        try {
            Cookie[] cookies = request.getCookies();
            String cookieValue = CookieUtil.findCookieValue(cookies, "user");
            if (cookieValue != null && !"".equals(cookieValue)) {
                // 校验数据是否完整
                String[] objArry = CookieUtil.legality(cookieValue, request);
                // 校验结果
                if (objArry != null) {
                    User loggedInUser = new User();
                    loggedInUser.setUserId(Integer.valueOf(objArry[0]));
                    loggedInUser.setUserLoginname(objArry[1]);
                    loggedInUser.setUserDepaId(Integer.valueOf(objArry[2]));
                    loggedInUser.setUserDepartType(Integer.valueOf(objArry[3]));
                    loggedInUser.setUserType(Integer.valueOf(objArry[5]));
                    loggedInUser.setUserRealname(objArry[6]);
                    return loggedInUser;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 对角色权限创建权限cookie
     * 
     * @author yangzq
     * @param request
     * @param str
     * @param request
     * @return
     */
    public static String authorityCookieVal(String str, HttpServletRequest request) {
        String val = new String();
        val = str + "&&" + encryMD5(str + CommonUtil.AUTHORITY_STR + RemoteIpUtil.getRemoteIpAddr(request));
        return val;
    }

    /**
     * 
     * @param cookieValue
     * @param request
     * @return
     */
    public static String validateAuthority(String cookieValue, HttpServletRequest request) {
        String[] auths = null;
        try {
            String decompressStr = CompressUtil.decompressData(cookieValue);
            auths = decompressStr.split("&&");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (auths.length == 2) {
            String compareStr = encryMD5(auths[0] + CommonUtil.AUTHORITY_STR + RemoteIpUtil.getRemoteIpAddr(request));
            if (compareStr == auths[1] || compareStr.equalsIgnoreCase(compareStr)) {
                return auths[0];
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    /**
     * 栏目添加到cookie
     * 
     * @author yangzq
     * @param value
     * @param name
     * @param path
     * @param domain
     * @param maxAge
     * @param response
     * @throws UnsupportedEncodingException
     */
    public static void colsCookie(String value, String name, String path, String domain, int maxAge,
            HttpServletResponse response) throws UnsupportedEncodingException {
        value.replace("\r\n", "<br>");
        value.replace("\n", "<br>");
        value = URLEncoder.encode(value, "UTF-8");// 对传入的value进行64位加密
        String key = URLEncoder.encode(name, "UTF-8");
        Cookie cookie = new Cookie(key, value);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * 加密md5
     * 
     * @author yangzq
     * @param val
     * @return
     */
    public static String encryMD5(String val) {
        return MD5.getMD5(val.getBytes());
    }
}
