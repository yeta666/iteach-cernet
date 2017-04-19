package com.swust.kelab.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swust.kelab.domain.ExamQuestion;
import com.swust.kelab.domain.Option;
import com.swust.kelab.model.UserModel;

/**
 * 解析上传的用户信息的excel数据 处理xlsx格式excel
 * 
 * @author lujoCom
 * 
 */
public class ExcelResolver_07 {

	final static Logger logger = LoggerFactory
			.getLogger(ExcelResolver_07.class);

	final static Integer STUDENT = 1;
	final static Integer TEACHER = 2;
	final static Integer ACADEMICADMINI = 3;
	final static Integer ADMINISTRATOR = 4;

	private static ExcelResolver_07 excelResolver_07;

	private XSSFWorkbook workbook;

	private ExcelResolver_07() {
	}

	/*
	 * public static void main(String[] args) throws FileNotFoundException {
	 * XSSFSheet sheet = ExcelResolver_07.newInstance().createWorkBook(new
	 * FileInputStream(new File("C:\\temp\\综合导入测试数据3.xlsx"))).getSheet(0);
	 * 
	 * ExcelResolver_07.newInstance().getUserInfoFromExcel(sheet);
	 * 
	 * 
	 * }
	 */

	public static ExcelResolver_07 newInstance() {
		if (excelResolver_07 == null) {
			excelResolver_07 = new ExcelResolver_07();
		}
		return excelResolver_07;
	}

	/**
	 * 将数据流转化成工作簿
	 * 
	 * @param inputStream
	 *            前台传过来的excel文件流
	 * @return
	 */
	public ExcelResolver_07 createWorkBook(InputStream inputStream) {
		try {
			workbook = new XSSFWorkbook(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return excelResolver_07;
	}

	/**
	 * 选择工作表格
	 * 
	 * @param index
	 *            工作簿的表格索引
	 * @return
	 */
	public XSSFSheet getSheet(int index) {
		return workbook.getSheetAt(index);
	}

	/**
	 * 从excel工作表中获取用户信息
	 * 
	 * @param sheet
	 * @return
	 */
	public Map<String, Object> getUserInfoFromExcel(XSSFSheet sheet) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<UserModel> users = new ArrayList<UserModel>();
		int beginRow = sheet.getFirstRowNum();
		int endRow = sheet.getLastRowNum();
		UserModel user = null;
		String errorDataRow = "";
		for (int index = beginRow; index <= endRow; index++) {
			XSSFRow row = sheet.getRow(index);
			user = new UserModel();
			if (judgeType(row.getCell(0)) == null
					|| judgeType(row.getCell(0)).equals("序号")) {
				continue;
			}
			if(!authenticationRow(row)){
				errorDataRow += (index+1)+",";
				continue;
			}
			users.add(readRow(row, user));
		}
		data.put("errorDataRow", errorDataRow);
		data.put("userData", users);
		return data;
	}
	/**
	 * 验证每一行的某些列的信息是否符合标准
	 * 
	 * @param row
	 * @return
	 */
	private boolean authenticationRow(XSSFRow row) {
		if (!AuthenticationInformation.isSecurityNumber(judgeType(row
				.getCell(0)))) {
			return false;
		}
		if (!AuthenticationInformation.isSecurityGender(judgeType(row
				.getCell(3)))) {
			return false;
		}
		if (!AuthenticationInformation.isSecurityUserType(judgeType(row
				.getCell(4)))) {
			return false;
		}
		if (!AuthenticationInformation.isSecurityClassName(judgeType(row
				.getCell(5)))) {
			return false;
		}
		if (!AuthenticationInformation.isSecurityIdNumber(judgeType(row
				.getCell(10)))) {
			return false;
		}
		if (!AuthenticationInformation.isSecurityEmail(judgeType(row
				.getCell(13)))) {
			return false;
		}
		return true;
	}
	/**
	 * 读取一行中的数据，并将数据设置到user中
	 * 
	 * @param row
	 * @param user
	 * @return
	 */
	private UserModel readRow(XSSFRow row, UserModel user) {
		user.setUseLoginName(judgeType(row.getCell(1)));
		user.setUserRealName(judgeType(row.getCell(2)));
		user.setUserGender(judgeType(row.getCell(3)));
		user.setUserType(Integer.parseInt(judgeType(row.getCell(4))));
		String className = judgeType(row.getCell(5));
		if (user.getUserType().equals(STUDENT) && className != null
				&& !"".equals(className)) {
			int beginIndex = className.indexOf("级");
			user.setUserGrade(className.substring(0, beginIndex));
			user.setUserClassName(className.substring(beginIndex + 1));

			user.setUserAdasExamNum(judgeType(row.getCell(7)));
			user.setUserYearOfEntrance(judgeType(row.getCell(9)));
		}
		// logger.debug("学校" + judgeType(row.getCell(6)));
		user.setUserDepatmentName(judgeType(row.getCell(6)));
		user.setUserWorkUnit(judgeType(row.getCell(8)));
		user.setUserIdNum(judgeType(row.getCell(10)));
		user.setUserPhoneNum(judgeType(row.getCell(11)));
		user.setUserAddress(judgeType(row.getCell(12)));
		user.setUserEmail(judgeType(row.getCell(13)));
		user.setUserRemark(judgeType(row.getCell(14)));

		return user;
	}

	/**
	 * 判断是否是用户信息的excel表格
	 * 
	 * @param sheet
	 * @return
	 */
	public static boolean isUserInfoExcel(XSSFSheet sheet) {
		int start = sheet.getFirstRowNum();
		int end = sheet.getLastRowNum();

		for (int index = start; index <= end; index++) {
			XSSFRow row = sheet.getRow(index);
			int lastCellIndex = row.getLastCellNum();
			if (lastCellIndex != 15)
				continue;
			// 遇到空行跳出循环进行下一次循环
			if (judgeType(row.getCell(0)) == null)
				continue;
			if (judgeType(row.getCell(1)).equals("学籍号/工号")
					&& judgeType(row.getCell(2)).equals("姓名")
					&& judgeType(row.getCell(3)).equals("性别")
					&& judgeType(row.getCell(4)).equals("用户类型")
					&& judgeType(row.getCell(5)).equals("班级")
					&& judgeType(row.getCell(6)).equals("学校")
					&& judgeType(row.getCell(7)).equals("考籍号")
					&& judgeType(row.getCell(8)).equals("工作单位")
					&& judgeType(row.getCell(9)).equals("入学时间")
					&& judgeType(row.getCell(10)).equals("身份证号")
					&& judgeType(row.getCell(11)).equals("电话号码")
					&& judgeType(row.getCell(12)).equals("通讯地址")
					&& judgeType(row.getCell(13)).equals("邮箱")
					&& judgeType(row.getCell(14)).equals("备注")) {
				return true;
			}

		}
		return false;
	}

	/**
	 * 
	 * 判断当前Cell存储数据的类型，统一转换成String返回
	 * 
	 * @param cell
	 * @return cell content
	 */
	private static String judgeType(XSSFCell cell) {
		if (cell == null) {
			return null;
		}
		Object v_excelData = null;
		try {
			int vCellType = cell.getCellType();
			switch (vCellType) {
			case Cell.CELL_TYPE_NUMERIC:
				cell.setCellType(Cell.CELL_TYPE_STRING);
				v_excelData = cell.getStringCellValue();
			case Cell.CELL_TYPE_STRING: // 字串型
				v_excelData = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_FORMULA: // 公式型
				v_excelData = cell.getNumericCellValue() + "";
				break;
			case Cell.CELL_TYPE_BLANK: // 空白型
				v_excelData = null;
				break;
			case Cell.CELL_TYPE_BOOLEAN: // boolean型
				v_excelData = cell.getBooleanCellValue() + "";
				break;
			case Cell.CELL_TYPE_ERROR: // Error
				v_excelData = "";
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return v_excelData == null ? null : v_excelData + "";
	}

	/**
	 * 校验当前文档是否是试题导入文档
	 * 
	 * @author yangzq
	 * @param sheet
	 * @return
	 */
	public static boolean isQuesInfoExcel(XSSFSheet sheet) {
		int start = sheet.getFirstRowNum();
		int end = sheet.getLastRowNum();

		for (int index = start; index <= end; index++) {
			XSSFRow row = sheet.getRow(index);
			int lastCellIndex = row.getLastCellNum();
			if (lastCellIndex != 11)
				continue;
			// 遇到空行跳出循环进行下一次循环
			if (judgeType(row.getCell(0)) == null)
				continue;
			if (judgeType(row.getCell(1)).equals("序号")
					&& judgeType(row.getCell(2)).equals("试题类型")
					&& judgeType(row.getCell(3)).equals("题干")
					&& judgeType(row.getCell(4)).equals("选项A")
					&& judgeType(row.getCell(5)).equals("选项B")
					&& judgeType(row.getCell(6)).equals("选项C")
					&& judgeType(row.getCell(7)).equals("选项D")
					&& judgeType(row.getCell(8)).equals("答案")
					&& judgeType(row.getCell(9)).equals("难度")
					&& judgeType(row.getCell(10)).equals("对应题库")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 07版本的excel导入试题
	 * 
	 * @author yangzq
	 * @param sheet
	 * @return
	 */
	public List<ExamQuestion> quesInfoFromExcel(XSSFSheet sheet) {
		List<ExamQuestion> ques = new ArrayList<ExamQuestion>();

		int end = sheet.getLastRowNum();
		ExamQuestion que = null;
		// 从第三行开始，前两行是表头
		for (int index = 2; index <= end; index++) {
			XSSFRow row = sheet.getRow(index);
			que = new ExamQuestion();
			// 遇到空行或者非数据行跳出循环进行下一次循环
			if (judgeType(row.getCell(1)) == null
					|| judgeType(row.getCell(1)).equals("序号")) {
				continue;
			}
			ques.add(readQuestion(row, que));
		}
		return ques;
	}

	/**
	 * 将excel试题内容读入对象
	 * 
	 * @author yangzq
	 * @param row
	 * @param que
	 * @return
	 */
	private static ExamQuestion readQuestion(XSSFRow row, ExamQuestion que) {
		que.setExquType(Integer.parseInt(judgeType(row.getCell(2)) == "" ? "0"
				: judgeType(row.getCell(2))));// 试题类型
		que.setExquDescribe(judgeType(row.getCell(3)));// 试题题干
		List<Option> opList = new ArrayList<Option>();// 选项默认不设置附件
		String optionValidate = "";
		if (judgeType(row.getCell(4)) != null
				&& !"".equals(judgeType(row.getCell(4)))) {
			Option option1 = new Option();
			option1.setOptiOrdinal(1);
			option1.setOptiAttachment(false);
			option1.setOptiQuesId(0);
			option1.setOptiDescribe(judgeType(row.getCell(4)));
			optionValidate += "A";
			opList.add(option1);
		}
		if (judgeType(row.getCell(5)) != null
				&& !"".equals(judgeType(row.getCell(5)))) {
			Option option2 = new Option();
			option2.setOptiOrdinal(2);
			option2.setOptiAttachment(false);
			option2.setOptiQuesId(0);
			option2.setOptiDescribe(judgeType(row.getCell(5)));
			optionValidate += "B";
			opList.add(option2);
		}
		if (judgeType(row.getCell(6)) != null
				&& !"".equals(judgeType(row.getCell(6)))) {
			Option option3 = new Option();
			option3.setOptiOrdinal(3);
			option3.setOptiAttachment(false);
			option3.setOptiQuesId(0);
			option3.setOptiDescribe(judgeType(row.getCell(6)));
			optionValidate += "C";
			opList.add(option3);
		}
		if (judgeType(row.getCell(7)) != null
				&& !"".equals(judgeType(row.getCell(7)))) {
			Option option4 = new Option();
			option4.setOptiOrdinal(4);
			option4.setOptiAttachment(false);
			option4.setOptiQuesId(0);
			option4.setOptiDescribe(judgeType(row.getCell(7)));
			optionValidate += "D";
			opList.add(option4);
		}
		que.setOptions(opList);
		// 根据题型设置正确答案
		switch (que.getExquType()) {
		case CommonUtil.SINGLE:// 单选题
			if (validateAnswerlegelity(optionValidate,
					judgeType(row.getCell(8)))) {
				que.setExquCorrectanswer(convertOption(judgeType(row.getCell(8))));// 单选答案合法则填入
			} else {
				que.setExquCorrectanswer("");// 单选答案不合法则不填入
			}
			break;
		case CommonUtil.MULTI:// 多选题
			if (validateAnswerlegelity(optionValidate,
					judgeType(row.getCell(8)))) {
				que.setExquCorrectanswer(convertOption(judgeType(row.getCell(8))));// 多选答案合法则填入
			} else {
				que.setExquCorrectanswer("");// 多选答案不合法则不填入
			}
			break;
		case CommonUtil.JUDGE:// 判断题
			if (judgeType(row.getCell(8)) == "0"
					|| "0".equals(judgeType(row.getCell(8)))) {
				que.setExquCorrectanswer("0");// 错误
			} else if (judgeType(row.getCell(8)) == "1"
					|| "1".equals(judgeType(row.getCell(8)))) {
				que.setExquCorrectanswer("1");// 正确
			} else {
				que.setExquCorrectanswer("");// 用户录入答案非法
			}
			break;
		case CommonUtil.Fill:// 填空
			que.setExquCorrectanswer(judgeType(row.getCell(8)));// 用户录入答案非法
			break;
		case CommonUtil.Q_A:// 问答
			que.setExquCorrectanswer(judgeType(row.getCell(8)));// 用户录入答案非法
			break;
		default:
			que.setExquType(0);
			break;
		}
		que.setExquDifficulty(Integer.parseInt(judgeType(row.getCell(9)) == "" ? "-1"
				: judgeType(row.getCell(9))));
		que.setExquCourId(Integer.parseInt(judgeType(row.getCell(10))));
		que.setExquAttachment(false);// 不设置附件，附件换到编辑去
		return que;
	}

	/**
	 * 将用户填写的选项格式转换成数据库匹配的数字 适用课程测试的单选和多选
	 * 
	 * @author yangzq
	 * @param excelOption
	 * @return
	 */
	private static String convertOption(String excelOption) {
		String[] ops = excelOption.split("");
		String converOps = "";
		for (String op : ops) {
			if (op.equals("A") || op.equals("1")) {
				converOps += "1";
			} else if (op.equals("B") || op.equals("2")) {
				converOps += "2";
			} else if (op.equals("C") || op.equals("3")) {
				converOps += "3";
			} else if (op.equals("D") || op.equals("4")) {
				converOps += "4";
			}
		}
		return converOps;
	}

	/**
	 * 检验用户录入答案是否合法
	 * 
	 * @author yangzq
	 * @param option
	 * @param ans
	 * @return
	 */
	public static boolean validateAnswerlegelity(String option, String ans) {
		boolean flag = true;
		String[] an = ans.split("");
		for (String a : an) {
			// 设置大小写不敏感
			if (option.indexOf(a.toUpperCase()) >= 0) {
				continue;
			} else {
				flag = false;
			}
		}
		return flag;
	}
}
