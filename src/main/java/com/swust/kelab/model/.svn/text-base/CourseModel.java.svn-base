package com.swust.kelab.model;

import java.util.Date;

import com.swust.kelab.utils.CommonUtil;

/**
 * CourseModel包含Course表内容，以及关联实体类<br>
 * 
 * @author EasonLian ZhangXin
 * @since 1.0
 * @see com.swust.kelab.domain.Course
 * @see com.swust.kelab.domain.Chapter
 */
public class CourseModel {
	private Integer courId;

	private String courName;

	private String courDescribe;

	private Integer courState;

	private Float courCredit;

	private String courImg;

	private Integer rscoId;

	private String courCode;

	private Integer courMaxStudentNum;

	private Integer courMaxTime;

	private String imgLocation;

	private Integer courChoosedNum;
	private String courTepaName;
	private Integer courTepaId;
	

	// 该课程选课时间的开放时间与结束时间Date类型
	private Date openDate;
	
	private Date closeDate;

	// 该课程选课时间的开放时间与结束时间String类型
	private String opentTime;
	
	private String closeTime;
	
	// 课程主讲老师列表
	private String courTeachers;
	// 课程类别列表
	
	private String courCates;
	//考核项比重
	private String courTepaPattern;
	//考核项满分指标
	private String courTepaThrehold;	
	/**
	 * 课程类别描述，如1A
	 */
	private String courType;
	
	public String getCourType() {
        return courType;
    }

    public void setCourType(String courType) {
        this.courType = courType;
    }

    public String getCourTepaPattern() {
        return courTepaPattern;
    }

    public void setCourTepaPattern(String courTepaPattern) {
        this.courTepaPattern = courTepaPattern;
    }

    public String getCourTepaThrehold() {
        return courTepaThrehold;
    }

    public void setCourTepaThrehold(String courTepaThrehold) {
        this.courTepaThrehold = courTepaThrehold;
    }

    public Integer getCourTepaId() {
		return courTepaId;
	}

	public void setCourTepaId(Integer courTepaId) {
		this.courTepaId = courTepaId;
	}

	public String getCourTeachers() {
		return courTeachers;
	}

	public String getCourTepaName() {
		return courTepaName;
	}

	public void setCourTepaName(String courTepaName) {
		this.courTepaName = courTepaName;
	}

	public void setCourTeachers(String courTeachers) {
		this.courTeachers = courTeachers;
	}

	public String getCourCates() {
		return courCates;
	}

	public void setCourCates(String courCates) {
		this.courCates = courCates;
	}

	public Integer getCourChoosedNum() {
		return courChoosedNum;
	}

	public void setCourChoosedNum(Integer courChoosedNum) {
		this.courChoosedNum = courChoosedNum;
	}

	private String fileName;

	private String courTeacherIds;
	private String courMentroTeaids;

	public String getCourTeacherIds() {
		return courTeacherIds;
	}

	public void setCourTeacherIds(String courTeacherIds) {
		this.courTeacherIds = courTeacherIds;
	}

	public String getCourMentroTeaids() {
		return courMentroTeaids;
	}

	public void setCourMentroTeaids(String courMentroTeaids) {
		this.courMentroTeaids = courMentroTeaids;
	}

	public String getCourCateIds() {
		return courCateIds;
	}

	public void setCourCateIds(String courCateIds) {
		this.courCateIds = courCateIds;
	}

	private String courCateIds;

	/** 选择该门课程的学生总数 **/
	private Integer courseStuCount;

	/** 所有的 Chapter章节 集合 **/
	// private List<Chapter> chapters = new ArrayList<Chapter>();
	/**
	 * 默认构造器
	 */
	public CourseModel() {
	}

	/**
	 * 通过构造器设置Id，确保Id首先被设置
	 * 
	 * @param courId
	 */
	public CourseModel(Integer courId) {
		this.courId = courId;
	}

	public CourseModel(Integer courId, String courName) {
		this.courId = courId;
		this.courName = courName;
	}

	/** setter getter **/
	public Integer getCourId() {
		return courId;
	}

	public void setCourId(Integer courId) {
		this.courId = courId;
	}

	public String getCourName() {
		return courName;
	}

	public void setCourName(String courName) {
		this.courName = courName;
	}

	// public List<Chapter> getChapters() {
	// return chapters;
	// }
	// public void setChapters(List<Chapter> chapters) {
	// this.chapters = chapters;
	// }
	public String getCourDescribe() {
		return courDescribe;
	}

	public void setCourDescribe(String courDescribe) {
		this.courDescribe = courDescribe;
	}

	public Float getCourCredit() {
		return courCredit;
	}

	public void setCourCredit(Float courCredit) {
		this.courCredit = courCredit;
	}

	public String getCourImg() {
		return courImg;
	}

	public void setCourImg(String courImg) {
		this.courImg = courImg;
	}

	public Integer getRscoId() {
		return rscoId;
	}

	public void setRscoId(Integer rscoId) {
		this.rscoId = rscoId;
	}

	public Integer getCourState() {
		return courState;
	}

	public void setCourState(Integer courState) {
		this.courState = courState;
	}

	public Integer getCourMaxStudentNum() {
		return courMaxStudentNum;
	}

	public void setCourMaxStudentNum(Integer courMaxStudentNum) {
		this.courMaxStudentNum = courMaxStudentNum;
	}

	public Integer getCourseStuCount() {
		return courseStuCount;
	}

	public void setCourseStuCount(Integer courseStuCount) {
		this.courseStuCount = courseStuCount;
	}

	public String getCourCode() {
		return courCode;
	}

	public void setCourCode(String courCode) {
		this.courCode = courCode;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getImgLocation() {
		return imgLocation;
	}

	public void setImgLocation(String imgLocation) {
		this.imgLocation = imgLocation;
	}

	public Integer getCourMaxTime() {
		return courMaxTime;
	}

	public void setCourMaxTime(Integer courMaxTime) {
		this.courMaxTime = courMaxTime;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
		this.opentTime = CommonUtil.getFormatDateString("yyyy-MM-dd", openDate);
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
		this.closeTime = CommonUtil.getFormatDateString("yyyy-MM-dd", closeDate);
	}

	public String getOpentTime() {
		return opentTime;
	}

	public void setOpentTime(String opentTime) {
		this.opentTime = opentTime;
	}

	public String getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

}
