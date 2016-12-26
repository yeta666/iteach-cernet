package com.swust.kelab.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {

	/**
	 * 公共工具类
	 * @author yangzq
	 */
	
	//试题类型
	public static final int SINGLE = 1;	//单选题
	public static final int MULTI = 2;//多选题
	public static final int JUDGE = 3;//判断题
	public static final int Fill = 4;//填空题
	public static final int Q_A = 5;//问答题
	
	//试题难易
	public static final int EASY = 0;//容易
	public static final int GENERAL = 1;//一般
	public static final int HARD = 2;//偏难
	public static final int DIFFICULT = 3;//困难
	
	public static final int LOGIN = 1;//登陆
	public static final int OUT = 0;//未登陆
	
	public static final int AUTO_MARK = 0;//自动评卷
	public static final int MANUL_MARK = 1;//手动评卷
	
	public static final int FINISHED = 1;//已完成的试卷
	public static final int UNFINISHED = 0;//未完成的试卷
	
	public static final int UNLOGIN_CODE = 602;//未登陆状态
	public static final int AJAX_COOKIE_ERROR = 603;//异步请求验证cookie数据错误
	public static final int AUTHORITY_REFUSE = 604;//权限判断失败，无权限
	public static final int ILLEGAL_OPERATION = 605;//非法操作
	
	public static final String AUTHORITY_STR = "ITEACHAUTHORITY";//权限加密附加杂项防止篡改
	/**
	 * 日期格式化工具类
	 * @author EasonLian
	 */
	private static SimpleDateFormat sdf = null;
	
	public static String getFormatDateString(String pattern,Date date) {
		sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	public static Date getDateFromString(String pattern,String arg) {
		try {
			if(arg != null && !arg.equals("")) {
				sdf = new SimpleDateFormat(pattern);
				return sdf.parse(arg);
			}
			return null;
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * 获取随机数
	 * @author yangzq
	 * @param size
	 * @return
	 */
	public static int getRandom(int size){
		double ran = Math.random() * size;
		String temp = String.valueOf(ran);
		temp = temp.substring(0, temp.indexOf("."));
		return Integer.valueOf(temp);
	}
}
