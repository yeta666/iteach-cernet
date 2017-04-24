package com.swust.kelab.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * 获取远程连接客户机ip<br>
 * 当使用了反向代理或者apache整合,<br>
 * 就无法通过<code>request.getRemoteAddr()</code>,<br>
 * 获取客户机ip地址，只能从request的header找<br>
 * 
 * @author EasonLian
 *
 */
public class RemoteIpUtil {

	public static String getRemoteIpAddr(HttpServletRequest request) {  
		String ip = request.getHeader("x-forwarded-for");
//System.out.println("************************************************************");
//System.out.println(request.getHeader("x-forwarded-for"));
//System.out.println(request.getHeader("Proxy-Client-IP"));
//System.out.println(request.getHeader("WL-Proxy-Client-IP"));
//System.out.println(ip = request.getRemoteAddr());
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 获取本地ip地址
	 * 
	 * @return
	 */
	public static String getLocalIP() {
		String ip = null;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress().toString();
		} catch (UnknownHostException e) {
			System.out.println("get IP error!");
			e.printStackTrace();
		}
		return ip;
	}
}
