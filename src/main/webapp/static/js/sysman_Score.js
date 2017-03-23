var showCourseName = "";
var showCourseId = 0;
var startTime = '';// 查询的开始时间
var endTime = '';// 查询的结束时间
var viewaction = '';// 分页请求的action
var params;// 请求的参数
var courseId = 0;// 課程id
var searchWord = "";// 试题类型
var colIds = "";// 栏目ids
var cookieString = "Search=";
var secondCol = 0;
$(document).ready(function() {
	colIds = urlColHtml();// 栏目ids
	ShowColumn();// Show Column
	backToTop(); // back To Top
	paginationPage();// ** 加载分页 *//
	// 新的时间插件显示示例
	timepicker("startTime");
	timepicker("endTime");
	// -----------------分页相关设置---------------------
	// 绑定全选/取消全选按钮
	/*$('#chooseall').toggle(function() {
		$('input[name="chooseRecord"]').attr("checked", "true");
		$(this).text("反选");
	}, function() {
		$('input[name="chooseRecord"]').each(function() {
			if ($(this).attr("checked")) {
				$(this).removeAttr("checked");
			} else
				$(this).attr("checked", "true");
		});
		$(this).text("全选");
	});*/
	$('#chooseall').click(function() {
		if($(this).html()=="全选"){
			$('input[name="chooseRecord"]').prop("checked", true);
			$(this).text("反选");
		}
		else{
		$('input[name="chooseRecord"]').prop("checked",false);
		$(this).text("全选");
		}
	});
	bindCourseList(); // 绑定题库选择下拉列表
	loadRecord(); // 加载数据
	bindSearch();// 绑定搜索按钮
	bindDelRecord();// 绑定删除按钮
});

// 加载我的试题
function loadRecord() {
	secondCol = getRequest("secondCol");
	viewaction = '../../handler/train/showAllScore';
	params = {
		"departId" : 0,
		"userId" : 0,
		"departType" : 0,
		"userType" : 0,
		"courId" : courseId,
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
			$('#quCourse').val(params.courId);
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
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	$("#mainTable").empty();
	var courseHtml = "";
	if (pageRecords.data.length == 0) {
		$('#mainTable')
				.append(
						"<tr><td colspan=\"10\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>");
	} else {
		$.each(pageRecords.data, function(itemIndex, item) {
			var status = "";
			var classInfo = "";
			if (item.rateIsFinished == 0) {
				status = "评卷";
				classInfo = "info";
			} else {
				status = "完成";
				classInfo = "";
			}
			courseHtml += "<tr class='" + classInfo + "'><td><input type='checkbox' name='chooseRecord' id='"
					+ item.rateId + "' value='" + item.rateId + "'></td><td>" + startIndex + "</td><td title="
					+ item.rateCourName + "><a href='sysman_Score_mark.html?isFinished=" + item.rateIsFinished
					+ "&paperId=" + item.paperId + "&examinId=" + item.examinId + "&rateId=" + item.rateId + "&userId="
					+ item.userId + "&" + colIds + "'>" + titleFormat(item.rateCourName, 10) + "</a></td><td>" + status
					+ "</td><td title=" + item.courName + ">" + titleFormat(item.courName, 5) + "</td><td>"
					+ item.rateUserIdNum + "</td><td>" + item.rateUserName + "</td><td>" + item.rateScore + "</td><td>"
					+ item.rateTimeFomat + "</td></tr>";
			startIndex++;
		});
		$('#mainTable').append(courseHtml);
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
	courseId = $('#quCourse').val();
	startTime = $('#startTime').val();
	endTime = $('#endTime').val();
	// 点击搜索，一切重新查找
	params.currentPage = 1;
	params.searchWord = searchWord;
	params.courId = courseId;
	params.startTime = startTime;
	params.endTime = endTime;
	params.userId = userId;
	params.userType = userType;
	params.departId = departId;
	params.departType = departmentTypeID;
	var cookieString = "Search=";
	cookieString = cookieString + JSON.stringify(params);
	document.cookie = cookieString;
}

// 绑定删除
function bindDelRecord() {
	$('#delButton').click(function() {
		if (confirm("删除试题将删除试题所包括的信息，该操作不能恢复，请确认是否删除！")) {
			// 获取选择的用户id序列
			var delRecords = new Array();
			$("input[name='chooseRecord']").each(function() {
				var $current = $(this);
				if ($current.is(':checked') != undefined)
					delRecords.push($current.val());
			});
			var state;
			var message;
			var header;
			// 如果选择的id为空，则提示错误信息
			if (delRecords.length == 0) {
				state = 'error';
				header = "删除失败";
				message = "未选择任何试题进行删除";
				infoNotice(state, header, message, $('#data-grid'));
			}
			// 否则，向后台发送删除请求并显示结果信息
			else {
				// alert(delRecords.toString());
				$.post("../../handler/train/deleteScore", {
					"deleteScores" : delRecords.toString().replace(/\s/g, "")
				}, function(data) {
					if (data.ret) {
						state = 'success';
						header = "删除成功";
						message = "";
					} else {
						state = "error";
						header = "删除失败";
						message = data.errmsg;
					}
					infoNotice(state, header, message, $('#data-grid'));
					// 删除记录后，重新请求
					if (state == 'success')
						initSearch();
				}, "json");
			}
			return false;
		}
	});
}

// 检验所要删除的试题是否已经存卷
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
// on change 函数 改变课程变化
function changeFun() {
	
	getParams();
	initSearch();
	return false;
}