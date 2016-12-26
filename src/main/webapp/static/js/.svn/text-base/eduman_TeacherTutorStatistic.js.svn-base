var viewaction = '';// 分页请求的action
var params;// 请求的参数
var courseId = 0;// 筛选课程id
var teacherName = "";// 搜索教师姓名
$(document).ready(function() {
	// Show Column
	ShowColumn();
	// back To Top
	backToTop();
	if (userId <= 0) {
		/** 加载分页 */
		$('#pagination').hide();
	} else{
		paginationPage();
	}
	$('#teacherTutorStatistic').attr("class", "active");
	paginationPage();
	initialBind();// 绑定分页的一些操作响应
	loadTeacherAutorStaTable();
	if (departmentTypeID ==1) {// 市级
		$("#seleteSchool").show();
		selceteSchool();
	} else {
		$("#seleteSchool").hide();
	}
	selceteCourse();
	$("#searchTeacherTutor").click(function(){
		searchTeahcerSta();
	});
	

});
function searchTeahcerSta() {
	if (departmentTypeID ==1) {
		departId = $("#seleteDepartID").val();
	}
	courseId = $("#seleteCourseID").val();
	teacherName=$("#inputTeacherName").val();
	loadTeacherAutorStaTable();
}
function selceteCourse() {
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/course/viewCourseList",
		dataType : 'json',
		data : {
			departId : departId
		},
		success : function(data) {
			var corseList;
			if (data.ret) {
				if ($.isEmptyObject(data.data.courses)) {
					corseList = "<option value='-1'>课程数据为空！</option>";
				} else {
					$.each(data.data.courses, function(i, val) {
						corseList += "<option value='" + val.courseId + "'>" + val.courseName + "</option>";
					});
				}

			} else {
				corseList = "<option value='-1'>后台报错！！</option>";
			}
			$("#seleteCourseID").append(corseList);
		}
	});
}
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
					schoolList+="<option value="+departId+">全部学校</option>";
					$.each(data.data.departments, function(i, val) {
						schoolList += "<option value='" + val.depaId + "'>" + val.depaName + "</option>";
					});
				}

			} else {
				schoolList = "<option value='-1'>后台报错！！</option>";
			}
			$("#seleteDepartID").append(schoolList);
		}
	});
}

// 加载平台统计信息
function loadTeacherAutorStaTable() {
	viewaction = '../../handler/courseStatistic/teacher';
	params = {
		"departId" : departId,
		"courseId" : courseId,
		"teacherName" : teacherName,
		"pageArray" : new Array(),
		"recordPerPage" : 10
	};	
	initSearch();
}
// 加载数据到数据区域
function refreshContent(pageRecords) {
	$.ajaxSettings.async = false;
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	var tableContent;
	if (pageRecords.data.length==0) {
		tableContent = "<tr><td colspan=\"6\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>";
	} else {
		$.each(pageRecords.data, function(itemIndex, item) {
			tableContent += "<tr><td>" + item.teacherName + "</td><td>" + item.courseCode + "</td><td>"
					+ item.courseName + "</td><td>" + item.schoolName + "</td><td>" + item.studentNum + "</td><td>"
					+ item.postNum + "</td><td>" + item.replyNum + "</td></tr>";
		});
	}
	$('#teacherTutorSta tbody:nth-child(2)').empty().append(tableContent);
	return false;
}
