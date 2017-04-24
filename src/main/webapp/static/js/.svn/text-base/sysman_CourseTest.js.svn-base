var showCourseName = "";
var showCourseId = 0;
var startTime = '';// 查询的开始时间
var endTime = '';// 查询的结束时间
var viewaction = '';// 分页请求的action
var params;// 请求的参数
var quCourse = 0;// 課程id
var searchWord = "";// 试题类型
var colIds = "";// 栏目ids参数
var cookieString = "Search=";
var secondCol = 0;
$(document).ready(function() {
	ShowColumn();// Show Column
	colIds = urlColHtml();
	backToTop();// back To Top
	// ** 加载分页 *//
	paginationPage();
	// 新的时间插件显示示例
	timepicker("startTime");
	timepicker("endTime");
	// -----------------分页相关设置---------------------
	// 绑定全选/取消全选按钮
	$('#chooseall').click(function() {
		if($(this).html()=="全选"){
			$('input[name="chooseExin"]').prop("checked", true);
			$(this).text("反选");
		}
		else{
		$('input[name="chooseExin"]').prop("checked",false);
		$(this).text("全选");
		}
	});
	bindCourseList();// 绑定题库选择下拉列表
	loadTests();// 加载试题数据
	bindSearch();// 绑定搜索按钮
	bindDelRecords();// 绑定删除按钮
});

// 加载我的试题
function loadTests() {
	var secondCol = getRequest("secondCol");
	var firstCol = getRequest("firstCol");
	viewaction = '../../handler/train/viewAllExamInfo';
	params = {
		"quCourse" : quCourse,
		"departId" : 0,
		"userId" : 0,
		"departType" : 0,
		"userType" : 0,
		"startTime" : "",
		"endTime" : "",
		"searchWord" : "",
		"currentPage" : 1,
		"secondCol" : secondCol,
         "firstCol":firstCol
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
	// 绑定分页的一些操作响应
	initialBind();
	// 加载
	initSearch();
}
// 加载数据到数据区域
function refreshContent(pageRecords) {
	$.ajaxSettings.async = false;
	$("#mainTable").empty();
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	var examInfoHtml = "";
	if (pageRecords.data.length == 0) {
		$('#mainTable')
				.append(
						"<tr><td colspan=\"10\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>");
	} else {
		$.each(pageRecords.data, function(index, item) {
			var examType = "";
			if (item.state == 1) {
				examType = "info";
			}//class='" + examType + "'
			examInfoHtml += "<tr><td><input type='checkbox' name='chooseExin' value='"
					+ item.exinId + "' /></td><td class='tdcenter'>" + startIndex + "</td><td class='tdcenter' title="
					+ item.courseName + ">" + titleFormat(item.courseName, 5) + "</td><td class='tdcenter' title="
					+ item.exinName + "><a href='sysman_CourseTest_edit.html?exinId=" + item.exinId + "&courId="
					+ item.courseId + "&" + colIds + "'>" + titleFormat(item.exinName, 10)
					+ "</a></td><td class='tdcenter' title=" + item.exinDescribe + ">"
					+ titleFormat(item.exinDescribe, 10) + "</td>" + "<td class='tdcenter'>" + item.exinState
					+ "</td><td class='tdcenter'>" + item.exinBgtime + " ~ " + item.exinEdtime + "</td></tr>";
			startIndex++;
		});
		$('#mainTable').append(examInfoHtml);
	}
	return false;
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
		$("#quCourse").append(courseDropDown);
	});
}

// 绑定搜索
function bindSearch() {
	$('#search').click(function() {
		getParams();
		initSearch();
		return false;
	});
}
// 设置参数
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
	params.departId = departId;
	params.departType = departmentTypeID;
	params.userId = userId;
	params.userType = userType;
	// 添加cookie
	var cookieString = "Search=";
	cookieString = cookieString + JSON.stringify(params);
	document.cookie = cookieString;
}

// 绑定删除
function bindDelRecords() {
	$('#delButton').click(function() {
		var delRecords = new Array();
		$("input[name='chooseExin']").each(function() {
			var $current = $(this);
			if ( $current.is(":checked"))
				delRecords.push($current.val());
		});
		// 如果选择的id为空，则提示错误信息
		if (delRecords.length == 0) {
			alert("未选择任何测试");
			return false;
		}
		if (confirm("该操作会删除课程测试相关的所有数据，包括测试信息、学生成绩。该操作不能恢复，请确认是否删除！")) {
			// 如果选择的id为空，则提示错误信息
			if (delRecords.length == 0) {
				alert("未选择任何测试");
			}
			// 否则，向后台发送删除请求并显示结果信息
			else {
				$.post("../../handler/train/delExin", {
					"delRecords" : delRecords.toString()
				}, function(data) {
					if (data.ret) {
						alert("删除成功！");
						window.location.reload();
					} else {
						alert("删除失败！");
					}
				}, "json");
			}
			return false;
		}
	});
}

// on change 函数 改变课程变化
function changeFun() {
	getParams();
	initSearch();
}