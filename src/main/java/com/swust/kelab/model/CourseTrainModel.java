package com.swust.kelab.model;

import java.util.ArrayList;
import java.util.List;

import com.swust.kelab.domain.Chapter;

/**
 * CourseModel包含Course表内容，以及关联实体类<br>
 * 
 * @author Htx
 * @since 1.0
 * @see com.swust.kelab.domain.Course
 * @see com.swust.kelab.domain.Chapter
 */
public class CourseTrainModel {

    private Integer courId;
    private String courName;
    private String courDescribe;
    private Integer courState;
    private Float courCredit;
    private String courImg;
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

    public Integer getCourState() {
        return courState;
    }

    public void setCourState(Integer courState) {
        this.courState = courState;
    }

	public String getCourImg() {
		return courImg;
	}

	public void setCourImg(String courImg) {
		this.courImg = courImg;
	}
}
