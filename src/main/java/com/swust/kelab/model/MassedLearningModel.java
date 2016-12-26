package com.swust.kelab.model;

import java.util.Date;

/**
 * 集中学习列表model
 * 
 * @author lujoCom
 * 
 */
public class MassedLearningModel {

	/**
	 * 集中学习记录id
	 */
	private Integer maleId;

	/**
	 * 集中学习名字
	 */
	private String maleName;

	/**
	 * 集中学习课程id
	 */
	private Integer maleCourseId;

	/**
	 * 集中学习课程名称
	 */
	private String maleCourseName;

	/**
	 * 课程所对应的章节id
	 */
	private Integer maleCouChapterId;

	/**
	 * 集中学习资源
	 */
	private Integer maleResouseId;

	/**
	 * 集中学习描述
	 */
	private String maleDescribe;

	/**
	 * 集中学习创建时间
	 */
	private Date maleCreateTime;

	/**
	 * 集中学习创建时间字符串类型
	 */
	private String maleCreateDate;

	/**
	 * 集中学习的开始时间
	 */
	private Date maleStartDate;

	/**
	 * 集中学习的开始时间的字符串类型
	 */
	private String maleStartTime;

	/**
	 * 集中学习的终止时间
	 */
	private Date maleEndDate;

	/**
	 * 集中学习的终止时间的字符串类型
	 */
	private String maleEndTime;

	/**
	 * 集中学习的持续时间
	 */
	private Integer maleDuration;

	/**
	 * 集中学习学校id
	 */
	private Integer maleSchoolId;

	/**
	 * 集中学习学校名称
	 */
	private String maleSchoolName;

	/**
	 * 集中学习辅导教师id
	 */
	private Integer maleTeacherId;

	/**
	 * 集中学习辅导教师姓名
	 */
	private String maleTeacherName;

	/**
	 * 集中学习的最大学习时间
	 */
	private Integer mlmaMaxTime;

	/**
	 * 集中学习的最多人数
	 */
	private Integer mlmaMaxStudentNum;
	
	/**
	 * 实际上该集中学习已选择的学生数
	 */
	private Integer maleActualStuNum;
	
	/**
	 * 集中学习状态
	 */
	private Integer maleState;

	public Integer getMaleId() {
		return maleId;
	}

	public void setMaleId(Integer maleId) {
		this.maleId = maleId;
	}

	public String getMaleName() {
		return maleName;
	}

	public void setMaleName(String maleName) {
		this.maleName = maleName;
	}

	public Integer getMaleCourseId() {
		return maleCourseId;
	}

	public void setMaleCourseId(Integer maleCourseId) {
		this.maleCourseId = maleCourseId;
	}

	public String getMaleCourseName() {
		return maleCourseName;
	}

	public void setMaleCourseName(String maleCourseName) {
		this.maleCourseName = maleCourseName;
	}

	public Integer getMaleResouseId() {
		return maleResouseId;
	}

	public void setMaleResouseId(Integer maleResouseId) {
		this.maleResouseId = maleResouseId;
	}

	public String getMaleDescribe() {
		return maleDescribe;
	}

	public void setMaleDescribe(String maleDescribe) {
		this.maleDescribe = maleDescribe;
	}

	public Date getMaleCreateTime() {
		return maleCreateTime;
	}

	public void setMaleCreateTime(Date maleCreateTime) {
		this.maleCreateTime = maleCreateTime;
	}

	public Integer getMaleSchoolId() {
		return maleSchoolId;
	}

	public void setMaleSchoolId(Integer maleSchoolId) {
		this.maleSchoolId = maleSchoolId;
	}

	public String getMaleSchoolName() {
		return maleSchoolName;
	}

	public void setMaleSchoolName(String maleSchoolName) {
		this.maleSchoolName = maleSchoolName;
	}

	public Integer getMaleTeacherId() {
		return maleTeacherId;
	}

	public void setMaleTeacherId(Integer maleTeacherId) {
		this.maleTeacherId = maleTeacherId;
	}

	public String getMaleTeacherName() {
		return maleTeacherName;
	}

	public void setMaleTeacherName(String maleTeacherName) {
		this.maleTeacherName = maleTeacherName;
	}

	public Integer getMaleState() {
		return maleState;
	}

	public void setMaleState(Integer maleState) {
		this.maleState = maleState;
	}

	public String getMaleCreateDate() {
		return maleCreateDate;
	}

	public void setMaleCreateDate(String maleCreateDate) {
		this.maleCreateDate = maleCreateDate;
	}

	public Integer getMlmaMaxTime() {
		return mlmaMaxTime;
	}

	public void setMlmaMaxTime(Integer mlmaMaxTime) {
		this.mlmaMaxTime = mlmaMaxTime;
	}

	public Integer getMlmaMaxStudentNum() {
		return mlmaMaxStudentNum;
	}

	public void setMlmaMaxStudentNum(Integer mlmaMaxStudentNum) {
		this.mlmaMaxStudentNum = mlmaMaxStudentNum;
	}

	public Date getMaleStartDate() {
		return maleStartDate;
	}

	public void setMaleStartDate(Date maleStartDate) {
		this.maleStartDate = maleStartDate;
	}

	public String getMaleStartTime() {
		return maleStartTime;
	}

	public void setMaleStartTime(String maleStartTime) {
		this.maleStartTime = maleStartTime;
	}

	public Date getMaleEndDate() {
		return maleEndDate;
	}

	public void setMaleEndDate(Date maleEndDate) {
		this.maleEndDate = maleEndDate;
	}

	public String getMaleEndTime() {
		return maleEndTime;
	}

	public void setMaleEndTime(String maleEndTime) {
		this.maleEndTime = maleEndTime;
	}

	public Integer getMaleDuration() {
		return maleDuration;
	}

	public void setMaleDuration(Integer maleDuration) {
		this.maleDuration = maleDuration;
	}

	public Integer getMaleCouChapterId() {
		return maleCouChapterId;
	}

	public void setMaleCouChapterId(Integer maleCouChapterId) {
		this.maleCouChapterId = maleCouChapterId;
	}

	public Integer getMaleActualStuNum() {
		return maleActualStuNum;
	}

	public void setMaleActualStuNum(Integer maleActualStuNum) {
		this.maleActualStuNum = maleActualStuNum;
	}

}
