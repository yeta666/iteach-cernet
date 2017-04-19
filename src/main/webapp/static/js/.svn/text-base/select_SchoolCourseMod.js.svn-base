/**
 *  author：郭海蓉 
 *  time:2013-07
 */

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
 * 增加教师
 */
var count=0;
function addTeacher1(){
	count++;
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
			"class='btn btn-sm btn-danger'><i class='fa fa-minus'></i></a></span>";
			$("#mainTeachers").append(str);
		}
}
//删除教师
function delTeacher(id){
	var temp=$("#"+id).get();
	if(temp!=null&&temp.length>0){
		$("#"+id).remove();
	}
}