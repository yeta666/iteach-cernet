package com.swust.kelab.model;

/**
 * 统计每个年级的学生人数的model
 * @author WuXianhui
 *
 */
public class GradeStaticModel {
    private int gradeId;
    
    private int studentNum;

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public int getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(int studentNum) {
        this.studentNum = studentNum;
    }
    
}
