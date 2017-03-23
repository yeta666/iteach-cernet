package com.swust.kelab.model;

import java.util.List;

public class CourseCheckModel {
	private int courId;
	//课程分类,需要再次查询
	private String courCateIds; 
	private String courCateName;
	//课程考核方式
	private  String courTepaStyle;
	//课程代码
	private String courCode;
	private String courName;
	private float courCredit;
	//课程任课教师
	private String courTeacherIds;
	private List<String> courTeacherName;
	//课程状态
	private int courState;
	//课程申请者名字
	private String courCreateUserName;
	//申请者的机构type
	private int courCreatorDepaType;
	private String courCreateTime;
	//标识课程是否通过审核
	private int courVerify; 
	private String description; //表述
	private String courMentorTeaIds;//需要再次查询
	private List <String>courMentorTeaName;//辅导教师
	private String timeSchelue; //时间规划
	private String coverPictureUrl;//封面图片
	private Boolean courOpenToAll;//是否对外开放
	
	public int getCourCreatorDepaType() {
        return courCreatorDepaType;
    }
    public void setCourCreatorDepaType(int courCreatorDepaType) {
        this.courCreatorDepaType = courCreatorDepaType;
    }
    public Boolean getCourOpenToAll() {
        return courOpenToAll;
    }
    public void setCourOpenToAll(Boolean courOpenToAll) {
        this.courOpenToAll = courOpenToAll;
    }
    public int getCourId() {
		return courId;
	}
	public void setCourId(int courId) {
		this.courId = courId;
	}
	public String getCourCateName() {
		return courCateName;
	}
	public void setCourCateName(String courCateName) {
		this.courCateName = courCateName;
	}
	public String getCourTepaStyle() {
		return courTepaStyle;
	}
	public void setCourTepaStyle(String courTepaStyle) {
		this.courTepaStyle = courTepaStyle;
	}
	public String getCourCode() {
		return courCode;
	}
	public String getCourCateIds() {
		return courCateIds;
	}
	public void setCourCateIds(String courCateIds) {
		this.courCateIds = courCateIds;
	}
	public String getCourTeacherIds() {
		return courTeacherIds;
	}
	public void setCourTeacherIds(String courTeacherIds) {
		this.courTeacherIds = courTeacherIds;
	}
	public String getCourMentorTeaIds() {
		return courMentorTeaIds;
	}
	public void setCourMentorTeaIds(String courMentorTeaIds) {
		this.courMentorTeaIds = courMentorTeaIds;
	}
	public void setCourCode(String courCode) {
		this.courCode = courCode;
	}
	public String getCourName() {
		return courName;
	}
	public void setCourName(String courName) {
		this.courName = courName;
	}
	public float getCourCredit() {
		return courCredit;
	}
	public void setCourCredit(float courCredit) {
		this.courCredit = courCredit;
	}
	public List<String> getCourTeacherName() {
		return courTeacherName;
	}
	public void setCourTeacherName(List<String> courTeacherName) {
		this.courTeacherName = courTeacherName;
	}
	public int getCourState() {
		return courState;
	}
	public void setCourState(int courState) {
		this.courState = courState;
	}
	public String getCourCreateUserName() {
		return courCreateUserName;
	}
	public void setCourCreateUserName(String courCreateUserName) {
		this.courCreateUserName = courCreateUserName;
	}
	public String getCourCreateTime() {
		return courCreateTime;
	}
	public void setCourCreateTime(String courCreateTime) {
		this.courCreateTime = courCreateTime;
	}
	public int getCourVerify() {
		return courVerify;
	}
	public void setCourVerify(int courVerify) {
		this.courVerify = courVerify;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<String> getCourMentorTeaName() {
		return courMentorTeaName;
	}
	public void setCourMentorTeaName(List<String> courMentorTeaName) {
		this.courMentorTeaName = courMentorTeaName;
	}
	public String getTimeSchelue() {
		return timeSchelue;
	}
	public void setTimeSchelue(String timeSchelue) {
		this.timeSchelue = timeSchelue;
	}
	public String getCoverPictureUrl() {
		return coverPictureUrl;
	}
	public void setCoverPictureUrl(String coverPictureUrl) {
		this.coverPictureUrl = coverPictureUrl;
	}
	
}
