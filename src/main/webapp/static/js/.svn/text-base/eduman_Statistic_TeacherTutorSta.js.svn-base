/*
 *  author：郭海蓉 
 *  time:2013-07
 */
var viewaction = '';// 分页请求的action
var params;// 请求的参数
var courseId = 0;// 筛选课程id
var teacherName = "", departtemp = 0, orignalDepartID = 0;// 搜索教师姓名
$(document).ready(function() {

	// Show Column
	ShowColumn();
	// back To Top
	backToTop();
	if (userId <= 0) {
		/** 加载分页 */
		$('#pagination').hide();
	} else {
		paginationPage();
	}
	orignalDepartID = departId;
	$('#myTutor').attr("class", "active");
	paginationPage();
	initialBind();// 绑定分页的一些操作响应
	if (departmentTypeID == 1) {// 市级
		departtemp = 0;
		$("#seleteSchoolID").show();
		selceteSchool();
		selceteCourseBydepart();
		$("#seleteSchoolID").change(function() {
			departId = $("#seleteSchoolID").val();
			selceteCourseBydepart();
		});
	} else {
		departtemp = orignalDepartID;
		$("#seleteSchoolID").hide();
		selceteCourseBydepart();
	}
	// 默认显示
	secondCol = getRequest("secondCol");
	viewaction = '../../handler/courseStatistic/teacher';
	params = {
			"departId" : departId,
			"courseId" : courseId,
			"teacherName" : teacherName,
			"pageArray" : new Array(),
			"recordPerPage" : 10,
			"currentPage" : 1,
			"secondCol" : secondCol
	};
	// 判断cookie中是否有查询参数
	if ($.cookie("Search") != null && $.cookie("Search") != "") {
		var SearchCookie = $.cookie("Search");
		var SearchPar = JSON.parse(SearchCookie);
		if (SearchPar.secondCol == secondCol) {
			params = SearchPar;
			if (departmentTypeID == 1){
				$('#seleteSchoolID').val(params.departId);
			}
			$('#selecteCourserID').val(params.courseId);
			$('#inputTeacherName').val(params.teacherName);
		} else {
			// 删除cookie
			document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		}
	}
	initSearch();
	// 搜索
	$("#searchTeacherTutor").click(function() {
		$('#teacherTutorSta tbody:nth-child(2)').empty().append(
		"<tr><td colspan=\"5\" class=\"tdcenter\"><img src=\"../img/octocat-spinner.gif\" />正在加载数据...</td></tr>");
		if (departmentTypeID == 1) {
			departId = $("#seleteSchoolID").val();
		} else {
			departId = orignalDepartID;
		}
		courseId = $("#selecteCourserID").val();
		teacherName = $("#inputTeacherName").val();
		viewaction = '../../handler/courseStatistic/teacher';
		params = {
				"departId" : departId,
				"courseId" : courseId,
				"teacherName" : teacherName,
				"pageArray" : new Array(),
				"recordPerPage" : 10,
				"currentPage" : 1,
				"secondCol" : secondCol
		};
		document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		var cookieString = "Search=";
		cookieString = cookieString + JSON.stringify(params);
		document.cookie = cookieString;
		initSearch();
	});

});
function exportTeacherTourSta() {
	if (departmentTypeID == 1) {
		departId = $("#seleteSchoolID").val();
	} else {
		departId = orignalDepartID;
	}
	courseId = $("#selecteCourserID").val();
	teacherName = $("#inputTeacherName").val();
	window.location.href = "../../handler/courseStatistic/exportTeacherTutorInfor?searchType=1"
		+ "&teacherName="
		+ encodeURI(encodeURI(teacherName))
		+ "&courseId=" + courseId + "&departId=" + departId;
}

//根据departid获取课程
function selceteCourseBydepart() {
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/course/viewCourseListByDepart",
		dataType : 'json',
		data : {
			departId : departId,
			type : 0
		},
		success : function(data) {
			var bbscourseList = "";
			if (data.ret) {
				if ($.isEmptyObject(data.data.courses)) {
					bbscourseList += "<option value='-1'>课程数据为空！！</option>";
				} else {
					bbscourseList += '<option value="0">所有课程</option>';
					bbscourseList += '<option value="-1">暂无辅导课程</option>';
					$.each(data.data.courses, function(i, val) {
						bbscourseList += "<option value='" + val.courseId
						+ "'>" + val.courseName + "</option>";
					});
				}
			} else {
				bbscourseList = "<option>" + data.errmsg + "</option>";
			}
			$("#selecteCourserID").empty().append(bbscourseList);
		}
	});
}

//加载数据到数据区域
function refreshContent(pageRecords) {
	$.ajaxSettings.async = false;
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	var tableContent="";
	if (pageRecords.data.length == 0) {
		tableContent = "<tr><td colspan=\"8\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>";
	} else {
		$.each(pageRecords.data, function(itemIndex, item) {
			tableContent += "<tr><td>" + startIndex + "</td>" + "<td>"
			+ item.teacherName + "</td>" + "<td>" + item.courseCode
			+ "</td>" + "<td>" + item.courseName + "</td>" + "<td>"
			+ item.schoolName + "</td>" + "<td>" + item.studentNum
			+ "</td>" + "<td>" + item.postNum + "</td>" + "<td>"
			+ item.replyNum + "</td></tr>";
			startIndex++;
		});
	}
	$('#teacherTutorSta tbody:nth-child(2)').empty().append(tableContent);
	return false;
}
