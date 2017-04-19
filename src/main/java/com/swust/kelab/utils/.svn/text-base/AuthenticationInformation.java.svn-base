package com.swust.kelab.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.swust.kelab.domain.User;
import com.swust.kelab.model.UserModel;

/**
 * 用户信息的验证
 * 
 * @author lujoCom
 * 
 */
public class AuthenticationInformation {

	/**
	 * 验证用户名和密码
	 */
	private final static String COMMONREGULA = "[A-Za-z0-9]{3,15}";

	/**
	 * 验证email地址是否正确
	 */
	private final static String EMAIL = "[\\w[.-]]+@[\\w[.-]]+\\.[\\w]+";

	/**
	 * 验证身份证号码(含15位以及18位)
	 */
	private final static String IDNUMBER = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";

	/**
	 * 验证输入的日期是否符合
	 */
	private final static String DATE ="^\\d{6}$";
	private final static String YEAR ="^\\d{4}$";
	/*private final static String DATE = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}"
			+ "|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})"
			+ "-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|"
			+ "3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))"
			+ "|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})"
			+ "(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";*/

	private final static String GRADENAME = "^\\d+级";
	private final static String CLASSNAME = "^\\d+班";

	/**
	 * 验证注册用户信息
	 * 
	 * @param user
	 * @return
	 */
	public static Map<String, Object> authenticateUserInfo(User user) {
		Map<String, Object> resultMap = null;
		if (!isSecurityYear(user.getUserYearOfEntrance())) {
			resultMap = new HashMap<String, Object>();
			resultMap.put("authenticationInfo", "入学年份格式错误");
			return resultMap;
		}
		if (!isSecurityEmail(user.getUserEmail())) {
			resultMap = new HashMap<String, Object>();
			resultMap.put("authenticationInfo", "email有误");
			return resultMap;
		}
		if (!isSecurityIdNumber(user.getUserIdNum())) {
			resultMap = new HashMap<String, Object>();
			resultMap.put("authenticationInfo", "身份证不是十八位或者十五位");
			return resultMap;
		}

		return null;
	}

	public static boolean isSecurityEmail(String email) {
		if (email != null && !"".equals(email)) {
			return Pattern.compile(EMAIL).matcher(email).matches();
		}
		return true;
	}

	public static boolean isSecurityLoginName(String input) {
		return Pattern.compile(COMMONREGULA).matcher(input).matches();
	}

	/**
	 * 如果填写入学日期时验证日期是否验证通过，通过返回true，否则返回false 如果没有填写直接返回true
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isSecurityDate(String date) {
		if (date != null && !"".equals(date)) {
			return date.matches(DATE);
		}
		return true;
	}
	
	/**
	 * 如果填写入学日期时验证日期是否验证通过，通过返回true，否则返回false 如果没有填写直接返回true
	 * 
	 * @param year
	 * @return
	 */
	public static boolean isSecurityYear(String year) {
		if (year != null && !"".equals(year)) {
			return year.matches(YEAR);
		}
		return true;
	}

	/**
	 * 如果填写身份证号码时验证身份证号码是否验证通过，通过返回true，否则返回false 如果没有填写直接返回true
	 * 
	 * @param idNumber
	 * @return
	 */
	public static boolean isSecurityIdNumber(String idNumber) {
		if (idNumber != null && !"".equals(idNumber)) {
			return Pattern.compile(IDNUMBER).matcher(idNumber).matches();
		}
		return true;
	}

	public static boolean isSecurityNumber(String number) {
		if (number != null) {
			return Pattern.compile("^\\d+$").matcher(number).matches();
		}
		return false;
	}

	/**
	 * 验证年级数据格式
	 * 
	 * @param gradeName
	 * @return
	 */
	public static boolean isSecurityGradeName(String gradeName) {
		if (gradeName != null) {
			return Pattern.compile(GRADENAME).matcher(gradeName).matches();
		}
		return false;
	}

	/**
	 * 验证班级数据格式
	 * 
	 * @param className
	 * @return
	 */
	public static boolean isSecurityClassName(String className) {
		if (className != null) {
			return Pattern.compile(CLASSNAME).matcher(className).matches();
		}
		return false;
	}

	/**
	 * 验证性别格式
	 * 
	 * @param gender
	 * @return
	 */
	public static boolean isSecurityGender(String gender) {
		if (gender != null) {
			return Pattern.compile("[男|女]").matcher(gender).matches();
		}
		return false;
	}

	/**
	 * 验证用户类型是否为1-4的整数
	 * 
	 * @param userType
	 * @return
	 */
	public static boolean isSecurityUserType(String userType) {
		if (userType != null) {
			return Pattern.compile("[1-4]").matcher(userType).matches();
		}
		return false;
	}
	
	/**
	 * 入学年份验证
	 * @param className
	 * @return
	 */
	public static boolean isSecurityEntranceTime(String date) {
		String d = "^\\d{4}$";
		if (date != null && !"".equals(date)) {
			return Pattern.compile(d).matcher(date).matches();
		}
		return true;
	}
	
	public static void main(String[] args) {
        System.out.println(isSecurityDate("2012"));
    }
}
