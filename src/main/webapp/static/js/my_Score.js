var showCourseName = "";
var showCourseId = 0;
var startTime = '';// 查询的开始时间
var endTime = '';// 查询的结束时间
var viewaction = '';// 分页请求的action
var params;// 请求的参数
var quCourse = 0;// 課程id
var searchWord = "";// 试题类型
var colIds = "";// 栏目ids
var cookieString = "Search=";
var secondCol = 0;
$(document).ready(function() {
	colIds = urlColHtml();// 栏目ids
	ShowColumn(); // Show Column
	backToTop();// back To Top
	paginationPage();// ** 加载分页 *//
	// 新的时间插件显示示例
	timepicker("startTime");
	timepicker("endTime");
	// -----------------分页相关设置---------------------//
	bindCourseList();// 绑定题库选择下拉列表
	loadRecord();// 加载数据
	bindSearch();// 绑定搜索按钮
});

//加载我的试题
function loadRecord() {
	secondCol = getRequest("secondCol");
	viewaction = '../../handler/train/viewAllMyGrades';
	params = {
			"quCourse" : quCourse,
			"startTime" : "",
			"endTime" : "",
			"searchWord" : "",
			"currentPage" : 1,
			"secondCol" : secondCol
	};
	// /判断cookie中是否有查询参数
	if ($.cookie("Search") != null && $.cookie("Search") != "") {
		var SearchCookie = $.cookie("Search");
		var SearchPar = JSON.parse(SearchCookie);
		if (SearchPar != null && SearchPar.secondCol == secondCol) {
			params = SearchPar;
			$('#keyword').val(params.searchWord);
			$('#startTime').val(params.startTime);
			$('#endTime').val(params.endTime);
			$('#quCourse').val(params.quCourse);
		} else {
			// 删除cookie
			document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		}
	}
	//
	initialBind();// 绑定分页的一些操作响应
	initSearch();// 加载
}
//加载数据到数据区域
function refreshContent(pageRecords) {
	$.ajaxSettings.async = false;
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	$("#mainTable").empty();
	var gradesHtml = "";
	if (pageRecords.data.length == 0) {
		$('#mainTable')
		.append(
		"<tr><td colspan=\"10\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>");
	} else {

		$.each(pageRecords.data, function(index, item) {
			var classType = "";
			var viewMyTest = "";
			var scoreInfo = "";
			if (item.isfinished == 1) {
				classType = "info";
				viewMyTest = "<a href='my_ExamInfo.html?paperId=" + item.paperId + "&examinId=" + item.exinId + "&"
				+ colIds + "'>" + item.exinName + "</a>";
				scoreInfo = item.myScore;
			} else {
				viewMyTest = item.exinName;
				scoreInfo = "待评卷";
			}
			gradesHtml += "<tr class='" + classType + "'><td class=\"tdcenter\">" + startIndex
			+ "</td><td class=\"tdcenter\">" + "<input type='hidden' id='courseId" + item.rateId
			+ "' name='courseId' value=" + item.courseId + "/>" + item.courseName + "</td>"
			+ "<td class=\"tdcenter\"><input type='hidden' id='exinId" + item.rateId + "' name='exinId' value="
			+ item.exinId + "/>" + viewMyTest + "</td>" + "<td class=\"tdcenter\" title=" + item.exinDescribe
			+ ">" + titleFormat(item.exinDescribe, 10) + "</td><td class=\"tdcenter\">" + scoreInfo + "</td>"
			+ "<td class=\"tdcenter\">" + item.exinTestFormatTime + "</td>";
			startIndex++;
		});
		$('#mainTable').append(gradesHtml);
	}
	return false;
}

//绑定题库下拉列表
function bindCourseList() {
	$.post("../../handler/course/dropDownCourse", {
		"departId" : departId,
		"userId" : userId,
		"departType" : departmentTypeID,
		"userType" : userType
	}, function(data) {
		var courseList = data.data.courseList;
		var courseDropDown = "";
		$.each(courseList, function(index, item) {
			courseDropDown += "<option value='" + item.courId + "'>" + item.courName + "</option>";
		});
		$("#quCourse").append(courseDropDown);
	});
}

//绑定搜索
function bindSearch() {
	$('#search').click(function() {
		getParams();
		initSearch();
		return false;
	});
}
//设置参数
function getParams() {
	// 删除cookie
	document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
	searchWord = $('#keyword').val();
	quCourse = $('#quCourse').val();
	startTime = $('#startTime').val();
	endTime = $('#endTime').val();
	// 点击搜索，一切重新查找
	params.currentPage = 1;
	params.searchWord = searchWord;
	params.quCourse = quCourse;
	params.startTime = startTime;
	params.endTime = endTime;
	// 添加cookie
	var cookieString = "Search=";
	cookieString = cookieString + JSON.stringify(params);
	document.cookie = cookieString;
}

//检验所要删除的试题是否已经存卷
function validateDelQuestions(delQues) {
	$.get("../../handler/train/validateDelQues", function(data, status) {
		var courseList = data.data.courseList;
		var courseDropDown = "";
		$.each(courseList, function(index, item) {
			courseDropDown += "<option value='" + item.courId + "'>" + item.courName + "</option>";
		});
		$("#quCourse").html(courseDropDown);
	});
	return false;
}
//on change 函数 改变课程变化
function changeFun() {
	getParams();
	initSearch();
	return;
}