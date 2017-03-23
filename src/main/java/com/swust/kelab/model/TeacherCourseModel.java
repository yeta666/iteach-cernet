package com.swust.kelab.model;

/**
 * Teacher Course information
 * @author ZhangXin
 *
 */
public class TeacherCourseModel{
	private Integer courseId;
	public Integer getCourseId() {
		return courseId;
	}
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	private Integer rsco_cour_id;
	private String cour_name;
	private String cour_code;
	private String cour_type;
	private Integer cour_verify;
	private Integer cour_create_userid;
	private String cour_teacher_ids;
	private String cour_mentro_teaids;
	private Integer count;
	private boolean isDeleteInfo;
	
	public String getCour_type() {
        return cour_type;
    }
    public void setCour_type(String cour_type) {
        this.cour_type = cour_type;
    }
    public boolean isDeleteInfo() {
		return isDeleteInfo;
	}
	public void setDeleteInfo(boolean isDeleteInfo) {
		this.isDeleteInfo = isDeleteInfo;
	}
	public Integer getRsco_cour_id() {
		return rsco_cour_id;
	}
	public void setRsco_cour_id(Integer rsco_cour_id) {
		this.rsco_cour_id = rsco_cour_id;
	}
	public String getCour_name() {
		return cour_name;
	}
	public void setCour_name(String cour_name) {
		this.cour_name = cour_name;
	}
	public String getCour_code() {
		return cour_code;
	}
	public void setCour_code(String cour_code) {
		this.cour_code = cour_code;
	}
	public Integer getCour_verify() {
		return cour_verify;
	}
	public void setCour_verify(Integer cour_verify) {
		this.cour_verify = cour_verify;
	}
	public Integer getCour_create_userid() {
		return cour_create_userid;
	}
	public void setCour_create_userid(Integer cour_create_userid) {
		this.cour_create_userid = cour_create_userid;
	}
	public String getCour_teacher_ids() {
		return cour_teacher_ids;
	}
	public void setCour_teacher_ids(String cour_teacher_ids) {
		this.cour_teacher_ids = cour_teacher_ids;
	}
	public String getCour_mentro_teaids() {
		return cour_mentro_teaids;
	}
	public void setCour_mentro_teaids(String cour_mentro_teaids) {
		this.cour_mentro_teaids = cour_mentro_teaids;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
}
