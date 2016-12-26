/*
 *  author：郭海蓉 
 *  time:2013-07
 */
var viewaction = '';// 分页请求的action
var departIdFirset = "", departIdtemp = "";

var courseId = 0, gradeId = 0, classId = 0, orignalDepartID = 0;// 筛选课程id
var passOrNot = 0;
var searchType = "", searchWord = "";
var startTime = "", endTime = "";
var selecteCourseData = null, evaMethodsData = null;

$(document).ready(function() {
	orignalDepartID = departId;
	ShowColumn();// 显示功能栏
	backToTop();// 回顶部
	if (userId <= 0) {
		/** 加载分页 */
		$('#pagination').hide();
	} else {
		paginationPage();
	}


	/*	$("#isCheck").click(function(){
		if($("#isCheck").attr("checked")){
			alert("hello");
		}else{
			alert("im");
		}
	});*/
	// 新的时间插件显示示例
	$('#myTutor').attr("class", "active");
	initialBind();// 绑定分页的一些操作响应
	departIdtemp = departId;
	if (departmentTypeID == 1) {// 市级
		departIdFirset = 0;
		$("#seleteSchoolID").show();
		selceteSchool();
		selceteCourse();
		$("#seleteSchoolID").change(function() {
			$("#selecteCourserID").empty();
			departId = $("#seleteSchoolID").val();
			selceteCourse();
		});
	} else {
		$("#seleteSchoolID").hide();
		departIdFirset = departIdtemp;
		departId = departIdtemp;
		selceteCourse();

	}
	// 显示数据
	secondCol = getRequest("secondCol");
	viewaction = '../../handler/courseStatistic/staCourseLearning';
	var filter=0;
	if($("#isCheck").attr("checked")){
		filter=1;
	}
	params = {
			"departId" : departIdFirset,
			"courseId" : 0,
			"noStuFilter":filter,
			"passOrNot": 0,
			"pageArray" : new Array(),
			"recordPerPage" : 10,
			"currentPage" : 1,
			"secondCol" : secondCol
	};
	// /判断cookie中是否有查询参数
	if ($.cookie("Search") != null && $.cookie("Search") != "") {
		var SearchCookie = $.cookie("Search");
		var SearchPar = JSON.parse(SearchCookie);
		if (SearchPar.secondCol == secondCol) {
			params = SearchPar;
			if (departmentTypeID == 1){
				$('#seleteSchoolID').val(params.departId);
			}
			$('#selecteCourserID').val(params.courseId);
			if(params.noStuFilter==1){
				$("#isCheck").attr("checked","checked");
			}else{
				$("#isCheck").attr("checked","");
			}
			$('#passOrNot').val(params.passOrNot);
		} else {
			// 删除cookie
			document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		}
	}
	initSearch();
	$("#isCheck").click(function(){
		$("#search").trigger("click");
	});
	$("#search").click(function() {
		$('#courseStaForAdmin tbody:nth-child(2)').empty().append(
		"<tr><td colspan=\"10\" class=\"tdcenter\"><img src=\"../img/octocat-spinner.gif\" />正在加载数据...</td></tr>");
		if (departmentTypeID == 1) {
			departId = $("#seleteSchoolID").val();
		} else {
			departId = orignalDepartID;
		}
		courseId = $("#selecteCourserID").val();
		//passOrNot = $("#passOrNot").val();
		passOrNot = 0;
		var filter=0;
		if($("#isCheck").attr("checked")){
			filter=1;
		}
		params = {
				"noStuFilter":filter,
				"departId" : departId,
				"courseId" : courseId,
				"passOrNot": passOrNot,
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

//导出一门课程的成绩
/*function exportCourseSta(courseID) {
	var depaId = departId;
	if (departmentTypeID == 1) {
		depaId = $("#seleteSchoolID").val();
	}
	courseId = courseID;
	passOrNot = $("#passOrNot").val();
	// 其他的参数都填0或者空
	window.location.href = "../../handler/selectCourse/exportCountScore?searchType=0"
		+ "&searchWord=''&courseId="
		+ courseId
		+ "&departId="
		+ depaId
		+ "&passOrNot=" + passOrNot + "&gradeId=0&classId=0&userId=0";
}*/

//导出课程选修情况
function exportCourseLearning(){
	//导出时参数
	if (departmentTypeID == 1) {
		departId = $("#seleteSchoolID").val();
	} else {
		departId = orignalDepartID;
	}
	courseId = $("#selecteCourserID").val();
	var filter=0;
	if($("#isCheck").attr("checked")){
		filter=1;
	}
	//发送导出请求
	//alert("导出可能较慢，请稍后……");
	window.location.href = "../../handler/courseStatistic/exportCourseLearningState?"
		+ "courseId="
		+ courseId
		+ "&departId="
		+ departId
		+ "&filter=" + filter;
}

//加载数据到数据区域
function refreshContent(pageRecords) {
	$.ajaxSettings.async = false;
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	var tableContent = "";

	if (pageRecords.data.length == 0) {
		tableContent = "<tr><td colspan=\"8\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>";
	} else {
		$.each(
				pageRecords.data,
				function(itemIndex, item) {
					if( item.courseType==""|| item.courseType==null){
						 item.courseType="暂无类别";
					}
					var totalNum=item.bbsPostNum+item.bbsReplyNum;
					tableContent += "<tr><td>" + startIndex + "</td>"
					+ "<td>" + item.courseCode + "</td>"
					+ "<td>" + item.courseName + "</td>"
					+ "<td>" + item.courseType + "</td>"
					+ "<td>" + item.selectNum + "</td>"
					+ "<td>" + item.learnTime + "</td>"
					+ "<td>" + item.learnNum + "</td>"
					+ "<td>" + totalNum + "</td>";
					/*if (item.selectNum > 0) {
						tableContent += "<td><a type='button' class='btn btn-mini btn-info' onclick='exportCourseSta("
							+ item.courseId + ")'>导出</a></td>";
					} else {
						tableContent += "<td> — </td>";
					}*/
					tableContent += "</tr>";
					startIndex++;
				});
	}
	$('#courseStaForAdmin tbody:nth-child(2)').empty().append(tableContent);
	return false;
}

