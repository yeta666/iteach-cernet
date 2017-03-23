package com.swust.kelab.model;
/**
 * 返回前台用户管理页面查询的model
 * @author 张鑫
 *
 */
public class UserManageBackInfo {
	private Integer userId;
	private String name;
	private String userNumber;
	private String examNum;
	private String className;
	private String gradeName;
	private String phoneNum;
	private String depaName;
	private String email;
	private String courseName;
	private Integer userCoverPictureId;
	private String attaLocation;
	private String attaFilename;
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public Integer getUserCoverPictureId() {
		return userCoverPictureId;
	}
	public void setUserCoverPictureId(Integer userCoverPictureId) {
		this.userCoverPictureId = userCoverPictureId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserNumber() {
		return userNumber;
	}
	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}
	public String getExamNum() {
		return examNum;
	}
	public void setExamNum(String examNum) {
		this.examNum = examNum;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getDepaName() {
		return depaName;
	}
	public void setDepaName(String depaName) {
		this.depaName = depaName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAttaLocation() {
		return attaLocation;
	}
	public void setAttaLocation(String attaLocation) {
		this.attaLocation = attaLocation;
	}
	public String getAttaFilename() {
		return attaFilename;
	}
	public void setAttaFilename(String attaFilename) {
		this.attaFilename = attaFilename;
	}
	
}
