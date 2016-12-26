var showCourseName = "";
var showCourseId = 0;
var searchType = 1;
var courseId = -1;
var startTime = '';// 查询的开始时间
var endTime = '';// 查询的结束时间
var viewaction = '';// 分页请求的action
var params;// 请求的参数
var colIds = "";
var cookieString = "Search=";
var secondCol = 0;
$(document).ready(function() {
	colIds = urlColHtml();
	ShowColumn();// Show Column
	backToTop();// back To Top
	// 新的时间插件显示示例
	timepicker("startTime");
	timepicker("endTime");
	if (userId <= 0) {
		$('#pagination').hide();// ** 加载分页 *//
	} else
		paginationPage();
	bindCourseList();// 绑定题库选择下拉列表
	loadCourseTable();
	// 绑定搜索按钮
	bindSearch();
	//initSearch();
});

// 加载我的课程
function loadCourseTable() {
	secondCol = getRequest("secondCol");
	viewaction = '../../handler/train/viewAllTest';
	params = {
		"startTime" : "",
		"endTime" : "",
		"courseId" : courseId,
		"userId" : userId,
		"searchType" : searchType,
		"searchWord" : "",
		"recordPerPage" : recordPerPage,
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
			$('#searchType').val(params.searchType);
			$('#courseId').val(params.courseId);
		} else {
			// 删除cookie
			document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		}
	}
	//
	initialBind();// 绑定分页的一些操作响应
	initSearch();
}
// 加载数据到数据区域
function refreshContent(pageRecords) {
	$.ajaxSettings.async = false;
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	$("#mainTable").empty();
	var courseHtml = "<table class='table table-hover table-bordered table-condensed'><thead><tr>"
			+ "<th>序</th><th>测试名称</th><th>课程名称</th><th>开放时间</th><th>状态</th><th>测试</th></tr></thead><tbody>";
	if (pageRecords.data.length == 0) {
		$('#mainTable')
				.append(
						"<tr><td colspan=\"10\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>");
	} else {
		$.each(pageRecords.data, function(itemIndex, item) {
			var buttonDesc = "测试未开放";
			var examType = "";
			var buttType = "";
			var visiable = "disabled='disabled'";
			if (item.state == 1) {
				if (item.qualification == 1) {
					buttonDesc = "点击参加考试";
					examType = "info";
					buttType = "btn-info";
					visiable = "";
				} else {
					examType = "";
					buttType = "";
					visiable = "disabled='disabled'";
					buttonDesc = " 不满足参加考试条件，课程成绩达到条件方能参加考试！";
				}
			}
			if (item.exinDescribe == null || item.exinDescribe == undefined) {
				item.exinDescribe = "";
			}
			courseHtml += "<tr class='" + examType + "'><td>" + startIndex + "</td>" + "<td id='" + item.exinId
					+ "'><a rel='popover' data-original-title='<strong>测试描述</strong>' data-content='"
					+ item.exinDescribe + "' href='#'>" + item.exinName + "</a></td><td id='" + item.exinId + "' >"
					+ item.courseName + "</td><td>" + item.exinBgtime + " ~ " + item.exinEdtime + "</td><td>"
					+ item.exinState + "</td><td><button title='" + buttonDesc + "' id='" + item.exinId
					+ "' class='btn btn-small " + buttType + "'" + visiable
					+ "' type='button' onclick='javascript:startMyTest(" + item.courseId + "," + item.exinExpaId + ","
					+ item.exinId + ")'>测试</button></td></tr>";
			startIndex++;
		});
		courseHtml += "</tbody></table>";
		$('#mainTable').append(courseHtml);
	}
	// popover工具
	$("[rel=popover]").popover({
		placement : 'top',
		trigger : 'hover',
		html : 'true', // needed to show html of course
	});
	return false;
}

// 开始我的测试
function startMyTest(courseId, expaId, exinId) {
	window.location.href = "my_Exam.html?courseId=" + courseId + "&paperId=" + expaId + "&examinId=" + exinId + "&"
			+ colIds;
}

// 绑定题库下拉列表
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
		$("#courseId").append(courseDropDown);
	});
}
// on change 函数 改变课程变化
function changeFun() {
	getParams();
	initSearch();
	return;
}
// 设置参数
function getParams() {
	// 删除cookie
	document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
	searchWord = $('#keyword').val();
	courseId = $('#courseId').val();
	startTime = $('#startTime').val();
	endTime = $('#endTime').val();
	searchType = $('#searchType').val();
	// 点击搜索，一切重新查找
	params.currentPage = 1;
	params.userId = userId;
	params.courseId = courseId;
	params.searchType = searchType;
	params.searchWord = searchWord;
	params.startTime = startTime;
	params.endTime = endTime;
	// 添加cookie
	var cookieString = "Search=";
	cookieString = cookieString + JSON.stringify(params);
	document.cookie = cookieString;
}
// 绑定搜索
function bindSearch() {
	$('#search').click(function() {
		getParams();
		initSearch();
		return;
	});
}