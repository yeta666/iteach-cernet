package com.swust.kelab.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {
	/**
	 * 将 BASE64 编码的字符串 s 进行解码
	 * @param s
	 * @return
	 */
	public static String decodeStr(String s) {
		Base64 base64 = new Base64();  
        byte[] debytes = base64.decode(new String(s).getBytes());  
        return new String(debytes);
	}

	/**
	 *  将 s 进行 BASE64 编码
	 * @param s
	 * @return
	 */
	public static String encodeStr(String s) {
		Base64 base64 = new Base64();  
        byte[] enbytes = base64.encode(s.getBytes());  
        return new String(enbytes);
	}
	
	
	
	public static void main(String[] args) {
		String str= "中文，中文，中文，中文";
		Base64 base64 = new Base64();
		byte[] enbytes = base64.encode(str.getBytes());
		String urlcode = "";
		try {
			urlcode = URLEncoder.encode(new String(enbytes),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
          
        String str1="";
		try {
			str1 = URLDecoder.decode(urlcode,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String deStr = new String(base64.decode(str1));
		
        System.out.println("编码前:" + str);  
        System.out.println("URL转义:" + urlcode);  
        System.out.println("编码后:" + new String(enbytes));  
        System.out.println("解码后:" + deStr);
	}
}
