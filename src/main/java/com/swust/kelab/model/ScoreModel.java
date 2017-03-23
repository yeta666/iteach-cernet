package com.swust.kelab.model;

import java.util.Date;
/*
 * by lixw
 */
public class ScoreModel {
      /*
       * 测试ID
       */
    private int rateId;
    /*
     * 测试课程名称
     */
    private String rateCourName;
    /*
     * 测试学生学号
     */
    private String rateUserIdNum;
    /*
     * 测试用户姓名试
     */
    private String rateUserName;
    /*
     * 测试成绩
     */
    private float  rateScore;
    /*
     * 测试开始时间
     */
    private Date   rateTime;
    
    private String rateTimeFomat;
    /*
     * 测试课程名称
     */
    private String courName;
    /*
     * 用户ID
     */
    private int userId;
    /*
     * 试卷ID
     */
    private int paperId;
    /*
     * 测试ID
     */
    private int examinId;
    /*
     * 测试是否完成
     */
    private int rateIsFinished;
    
    
    
    public int getRateId() {
        return rateId;
    }
    public void setRateId(int rateId) {
        this.rateId = rateId;
    }
    public String getRateCourName() {
        return rateCourName;
    }
    public void setRateCourName(String rateCourName) {
        this.rateCourName = rateCourName;
    }
    public String getRateUserIdNum() {
        return rateUserIdNum;
    }
    public void setRateUserIdNum(String rateUserIdNum) {
        this.rateUserIdNum = rateUserIdNum;
    }
    public String getRateUserName() {
        return rateUserName;
    }
    public void setRateUserName(String rateUserName) {
        this.rateUserName = rateUserName;
    }
    public float getRateScore() {
        return rateScore;
    }
    public void setRateScore(float rateScore) {
        this.rateScore = rateScore;
    }
    public Date getRateTime() {
        return rateTime;
    }
    public void setRateTime(Date rateTime) {
        this.rateTime = rateTime;
    }
  
    public String getRateTimeFomat() {
        return rateTimeFomat;
    }
    public void setRateTimeFomat(String rateTimeFomat) {
        this.rateTimeFomat = rateTimeFomat;
    }
    public String getCourName() {
        return courName;
    }
    public void setCourName(String courName) {
        this.courName = courName;
    }
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getPaperId() {
		return paperId;
	}
	public void setPaperId(int paperId) {
		this.paperId = paperId;
	}
	public int getExaminId() {
		return examinId;
	}
	public void setExaminId(int examinId) {
		this.examinId = examinId;
	}
	public int getRateIsFinished() {
		return rateIsFinished;
	}
	public void setRateIsFinished(int rateIsFinished) {
		this.rateIsFinished = rateIsFinished;
	}
    
}
