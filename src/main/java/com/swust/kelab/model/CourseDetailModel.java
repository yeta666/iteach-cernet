package com.swust.kelab.model;

import java.util.Date;
import java.util.Map;

/**
 * 课程详细信息model
 * 
 * @author Wu
 * 
 */
public class CourseDetailModel {

	/**
	 * 课程id
	 */
	private int courId;

	/**
	 * 课程类别id,多个逗号隔开
	 */
	private String courCateids;

	/**
	 * 考核方式id
	 */
	private int courTepaId;

	/**
	 * 课程代码
	 */
	private String courCode;
	
	/**
	 * 课程类别
	 */
	private String courType;

	/**
	 * 课程名称
	 */
	private String courName;

	/**
	 * 课程学分
	 */
	private Float courCredit;

	/**
	 * 所属机构
	 */
	private String departName;

	/**
	 * 所属机构的类型
	 */
	private int departType;
	/**
	 * 创建者名称
	 */
	private String courCreatorName;

	/**
	 * 创建时间
	 */
	private Date courCreateTime;

	/**
	 * 课程描述
	 */
	private String courDescribe;

	/**
	 * 主讲教师
	 */
	private String courTeacherIds;

	/**
	 * 保存主讲教师的姓名与id
	 */
	private Map<Integer, Object> courTeacherIdMap;

	/**
	 * 主讲教师的姓名
	 */
	private String courTeacherName;

	/**
	 * 辅导教师
	 */
	private String courMentroTeaids;

	/**
	 * 辅导教师的姓名与id
	 */
	private Map<Integer, Object> courMentroTeaIdMap;

	/**
	 * 辅导教师的姓名
	 */
	private String courMentroTeaName;
	
	/**
	 * 辅导教师的机构
	 */
	private String courMentroTeaDepaIds;

	/**
	 * 课程计划
	 */
	private String courTimeSchedule;

	/**
	 * 课程状态,2表示关闭,1表示打开
	 */
	private int courState;

	/**
	 * 课程论坛状态,2表示关闭,1表示打开
	 */
	private boolean courPoston;

	/**
	 * 课程论坛名称,暂时未使用
	 */
	private String courForumName;

	/**
	 * 课程审核状态,0表示未审核,1表示审核通过,2表示审核未通过
	 */
	private int courVerify;

	/**
	 * 课程图片对应的附件id
	 */
	private int courCoverPictureId;
	
	/**
	 * 课程是否对外开放
	 */
	private Boolean courOpenToAll;
	
	/**
     * 参加该课程考试的分数限制
     */
    private Float courTestLimitScore;
    /**
     * 课程的学年
     */
    private int courYear;
    /**
     * 课程的学期
     */
    private int courTerm;
    /**
     * 课程的学段
     */
    private int courStudyPhase;
    /**
     * 课程的文理方向
     */
    private int courArtScience;
    
    public int getCourYear() {
        return courYear;
    }

    public void setCourYear(int courYear) {
        this.courYear = courYear;
    }

    public int getCourTerm() {
        return courTerm;
    }

    public void setCourTerm(int courTerm) {
        this.courTerm = courTerm;
    }

    public int getCourStudyPhase() {
        return courStudyPhase;
    }

    public void setCourStudyPhase(int courStudyPhase) {
        this.courStudyPhase = courStudyPhase;
    }

    public int getCourArtScience() {
        return courArtScience;
    }

    public void setCourArtScience(int courArtScience) {
        this.courArtScience = courArtScience;
    }
        
    public String getCourType() {
        return courType;
    }

    public void setCourType(String courType) {
        this.courType = courType;
    }

    public String getCourMentroTeaDepaIds() {
        return courMentroTeaDepaIds;
    }

    public void setCourMentroTeaDepaIds(String courMentroTeaDepaIds) {
        this.courMentroTeaDepaIds = courMentroTeaDepaIds;
    }

    public int getDepartType() {
        return departType;
    }

    public void setDepartType(int departType) {
        this.departType = departType;
    }

    public Float getCourTestLimitScore() {
        return courTestLimitScore;
    }

    public void setCourTestLimitScore(Float courTestLimitScore) {
        this.courTestLimitScore = courTestLimitScore;
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

	public String getCourCateids() {
		return courCateids;
	}

	public void setCourCateids(String courCateids) {
		this.courCateids = courCateids;
	}

	public int getCourTepaId() {
		return courTepaId;
	}

	public void setCourTepaId(int courTepaId) {
		this.courTepaId = courTepaId;
	}

	public String getCourCode() {
		return courCode;
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

	public Float getCourCredit() {
		return courCredit;
	}

	public void setCourCredit(Float courCredit) {
		this.courCredit = courCredit;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public String getCourCreatorName() {
		return courCreatorName;
	}

	public void setCourCreatorName(String courCreatorName) {
		this.courCreatorName = courCreatorName;
	}

	public Date getCourCreateTime() {
		return courCreateTime;
	}

	public void setCourCreateTime(Date courCreateTime) {
		this.courCreateTime = courCreateTime;
	}

	public String getCourDescribe() {
		return courDescribe;
	}

	public void setCourDescribe(String courDescribe) {
		this.courDescribe = courDescribe;
	}

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

	public String getCourTimeSchedule() {
		return courTimeSchedule;
	}

	public void setCourTimeSchedule(String courTimeSchedule) {
		this.courTimeSchedule = courTimeSchedule;
	}

	public int getCourState() {
		return courState;
	}

	public void setCourState(int courState) {
		this.courState = courState;
	}

	public boolean isCour_poston() {
		return courPoston;
	}

	public void setCour_poston(boolean courPoston) {
		this.courPoston = courPoston;
	}

	public String getCourForumName() {
		return courForumName;
	}

	public void setCourForumName(String courForumName) {
		this.courForumName = courForumName;
	}

	public int getCourVerify() {
		return courVerify;
	}

	public void setCourVerify(int courVerify) {
		this.courVerify = courVerify;
	}

	public int getCourCoverPictureId() {
		return courCoverPictureId;
	}

	public void setCourCoverPictureId(int courCoverPictureId) {
		this.courCoverPictureId = courCoverPictureId;
	}

	public Map<Integer, Object> getCourTeacherIdMap() {
		return courTeacherIdMap;
	}

	public void setCourTeacherIdMap(Map<Integer, Object> courTeacherIdMap) {
		this.courTeacherIdMap = courTeacherIdMap;
	}

	public Map<Integer, Object> getCourMentroTeaIdMap() {
		return courMentroTeaIdMap;
	}

	public void setCourMentroTeaIdMap(Map<Integer, Object> courMentroTeaIdMap) {
		this.courMentroTeaIdMap = courMentroTeaIdMap;
	}

	public boolean isCourPoston() {
		return courPoston;
	}

	public void setCourPoston(boolean courPoston) {
		this.courPoston = courPoston;
	}

	public String getCourTeacherName() {
		return courTeacherName;
	}

	public void setCourTeacherName(String courTeacherName) {
		this.courTeacherName = courTeacherName;
	}

	public String getCourMentroTeaName() {
		return courMentroTeaName;
	}

	public void setCourMentroTeaName(String courMentroTeaName) {
		this.courMentroTeaName = courMentroTeaName;
	}

}
