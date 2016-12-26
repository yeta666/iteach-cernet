package com.swust.kelab.model;

/**
 * 课程学习状态的model
 * 
 * @author Wu
 * 
 */
public class CourseLearnModel {
    /**
     * 课程id
     */
    private int courseId;
    /**
     * 课程名称
     */
    private String courseName;
    /**
     * 学分
     */
    private float courseCredit;
    /**
     * 课程代码
     */
    private String courseCode;
    /**
     * 课程类别
     */
    private String courseType;
    /**
     * 课程考核方式
     */
    private int courTepaId;
    /**
     * 机构名称
     */
    private String depaName;
    /**
     * 年级id
     */
    private int gradeId;
    /**
     * 年级名称
     */
    private String gradeName;
    /**
     * 注册的学生数
     */
    private int stuNum;
    /**
     * 选课人数
     */
    private int selectNum = 0;
    /**
     * 当前在线人数
     */
    private int onlineNum = 0;
    /**
     * 学习时间
     */
    private int learnTime=0;
    /**
     * 学习次数
     */
    private int learnNum=0;
    /**
     * 论坛讨论主贴数
     */
    private int bbsPostNum=0;
    /**
     * 论坛讨论回贴数
     */
    private int bbsReplyNum=0;
    /**
     * 自动生成的课程编码
     */
    private String encoding;
    
    public int getStuNum() {
        return stuNum;
    }

    public void setStuNum(int stuNum) {
        this.stuNum = stuNum;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public int getLearnTime() {
        return learnTime;
    }

    public void setLearnTime(int learnTime) {
        this.learnTime = learnTime;
    }

    public int getLearnNum() {
        return learnNum;
    }

    public void setLearnNum(int learnNum) {
        this.learnNum = learnNum;
    }

    public int getBbsPostNum() {
        return bbsPostNum;
    }

    public void setBbsPostNum(int bbsPostNum) {
        this.bbsPostNum = bbsPostNum;
    }

    public int getBbsReplyNum() {
        return bbsReplyNum;
    }

    public void setBbsReplyNum(int bbsReplyNum) {
        this.bbsReplyNum = bbsReplyNum;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public int getCourTepaId() {
        return courTepaId;
    }

    public void setCourTepaId(int courTepaId) {
        this.courTepaId = courTepaId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getSelectNum() {
        return selectNum;
    }

    public void setSelectNum(int selectNum) {
        this.selectNum = selectNum;
    }

    public int getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(int onlineNum) {
        this.onlineNum = onlineNum;
    }

    public String getDepaName() {
        return depaName;
    }

    public void setDepaName(String depaName) {
        this.depaName = depaName;
    }

    public float getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(float courseCredit) {
        this.courseCredit = courseCredit;
    }

}
