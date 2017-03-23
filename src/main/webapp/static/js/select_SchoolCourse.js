/**
 *  author：郭海蓉 
 *  time:2013-07
 */
//选择学校
function selceteSchool() {
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/department/viewDepartments",
		dataType : 'json',
		data:{type:3},
		success : function(data) {
			var schoolList;
			if (data.ret) {
				if ($.isEmptyObject(data.data.departments)) {
					schoolList = "<option value='-1'>学校数据为空！！</option>";
				} else {
					schoolList += "<option value=\"0\">所有机构</option>";
					$.each(data.data.departments, function(i, val) {
						schoolList += "<option value='" + val.depaId + "'>" + val.depaName + "</option>";
					});
				}

			} else {
				schoolList = "<option value='-1'>"+data.errmsg+"</option>";
			}
			$("#seleteSchoolID").empty().append(schoolList);
		}
	});
}
/**
 * 根据departid获取课程
 */
function selceteCourse() {
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/course/viewCourseListByDepart",
		dataType : 'json',
		data:{departId:departId,
			type:0},
		success : function(data) {
			var bbscourseList;
			if (data.ret) {			
				if ($.isEmptyObject(data.data.courses)) {
					bbscourseList+= "<option value='-1'>课程数据为空！！</option>";
				} else {
					bbscourseList+='<option value="0">所有课程</option>';
					$.each(data.data.courses, function(i, val) {
						bbscourseList+="<option value='" + val.courseId + "'>" + val.courseName + "</option>";
					});
				}
			} else {
				bbscourseList = "<option>"+data.errmsg+"</option>";
			}
			$("#selecteCourserID").empty().append(bbscourseList);
		}
	});
}

/**
 * 根据用户获取课程
 */
function selceteCourseByuserID() {
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/course/viewCoursesByUser",
		dataType : 'json',
		data:{userId:userId,
			type:0},
		success : function(data) {
			var bbscourseList;
			if (data.ret) {			
				if ($.isEmptyObject(data.data.courses)) {
					bbscourseList+= "<option value='-1'>课程数据为空！！</option>";
				} else {
					bbscourseList+='<option value="0">全部课程</option>';
					$.each(data.data.courses, function(i, val) {
						bbscourseList+="<option value='" + val.courseId + "'>" + val.courseName + "</option>";
					});
				}
			} else {
				bbscourseList = "<option>"+data.errmsg+"</option>";
			}
			$("#selecteCourserID").empty().append(bbscourseList);
		}
	});
}
/**
 * 根据选择的学校显示年级
 */
function selceteGrade(){
	if (departmentTypeID == 1&&departId==orignalDepartID){
		gradeList= "<option value=\"0\">所有年级</option>";
		$("#grade").empty().append(gradeList);
	}else{
		$.ajax({
			type : 'post',
			contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
			url : "../../handler/gradeClass/viewGradeClassByDepart",
			dataType : 'json',
			data:{departId:departId},
			success : function(data) {
				gradeList="";
				classList="";
				if (data.ret) {
					if ($.isEmptyObject(data.data.gradeClass)) {
						gradeList = "<option value='-1'>年级数据为空！！</option>";
					} else {
						gradeClassData=data.data.gradeClass;//存储年级和班级信息
						gradeList= "<option value=\"0\">所有年级</option>";
						if(departId>0){
							$.each(data.data.gradeClass, function(i, val) {
								gradeList += "<option value='" + val.gradId + "'>" + val.gradName + "</option>";
							});
						}
						
					}
				} else {
					gradeList = "<option value='-1'>"+data.errmsg+"</option>";
				}
				$("#grade").empty().append(gradeList);
			}
		});
	}
}
var gradeList="";
var classList="";
var gradeClassData=null;//记录年级和班级
/**
 * 根据选择的年级显示班级
 */
function selceteClass(){
	classList= "<option value=\"0\">所有班级</option>";
	if(gradeId>0){
		$.each(gradeClassData, function(itemIndex, item) {
			if (item.gradId==gradeId) {
				$.each(item.classes, function(itemIndex,item) {
					classList+= "<option value='" + item.clasId + "'>" + item.clasName + "</option>";
				});
			}
		});
	}
	$("#class").empty().append(classList);
}
/**
 * 根据departId获取教师列表，用于设置主讲教师和辅导教师
 * userType为1表示学生
 * userType为2表示老师
 */
function getTeachers(userType){
	$.post("../../handler/user/viewUsersByTypeAndDepa", {
		"userType" :userType,"depaId":departId
	}, function(data) {
		if (data.ret) {
			if(data.data.users!=undefined||data.data.users.length>0){
				var teachertStrs="";
				for(var i=0;i<data.data.users.length;i++){
					teachertStrs+="<option value='"
						+data.data.users[i].userId+"'>"
						+data.data.users[i].userRealname
						+"</option>";
				}
				$("#CourseTeacher").empty().append(teachertStrs);
			}
		} else {
			infoNotice("error", "", data.errmsg);
		}
	}, "json");
}
/**
 * 增加教师
 */
function addTeacher(){
		var newTeaId=$("#CourseTeacher").val();
		if(newTeaId==null||newTeaId==""||newTeaId<=0){
			alert("请先选择老师!");
			return;
		}
		//判重
		var temp=$("#main"+newTeaId).get();
		if(temp!=null&&temp.length>0){
			alert("教师不能重复添加!");
		}else{
			var str="<span id='main"+newTeaId+"' >";			
			str+="&nbsp;&nbsp;&nbsp;&nbsp;";			
			str+=$("#CourseTeacher option[value='"+newTeaId+"']").text()+"&nbsp;"+
			"<a onclick='delTeacher(\"main"+newTeaId+"\");' "+ 
			"class='btn btn-mini btn-warning'><i class=' icon-remove icon-white'></i></a></span>"
			$("#courseTeachers").append(str);
		}
}
//删除教师
function delTeacher(id){
	var temp=$("#"+id).get();
	if(temp!=null&&temp.length>0){
		$("#"+id).remove();
	}
}