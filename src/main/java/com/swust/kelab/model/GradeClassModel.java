package com.swust.kelab.model;

import java.util.List;

/**
 * 年级班级model
 * @author Wu
 *
 */
public class GradeClassModel {
    /**
     * 年级id
     */
    private int gradId;
    /**
     * 年级名称
     */
    private String gradName;
    /**
     * 年级所属的机构
     */
    private int gradDepaId;
    /**
     * 年级包含的班级
     */
    private List<ClassModel> classes;
    public int getGradId() {
        return gradId;
    }
    public void setGradId(int gradId) {
        this.gradId = gradId;
    }
    public String getGradName() {
        return gradName;
    }
    public void setGradName(String gradName) {
        this.gradName = gradName;
    }
    public int getGradDepaId() {
        return gradDepaId;
    }
    public void setGradDepaId(int gradDepaId) {
        this.gradDepaId = gradDepaId;
    }
    public List<ClassModel> getClasses() {
        return classes;
    }
    public void setClasses(List<ClassModel> classes) {
        this.classes = classes;
    }
    
}
