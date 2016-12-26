var showCourseName = "";
var showCourseId = 0;
var startTime = '';// 查询的开始时间
var endTime = '';// 查询的结束时间
var viewaction = '';// 分页请求的action
var params;// 请求的参数
var quCourse = 0;// 課程id
var searchWord = "";// 试题类型
$(document).ready(function() {
	// Show Column
	ShowColumn();
	// back To Top
	backToTop();
	// ** 加载分页 *//
	paginationPage();
	$('#sysmanQuestions').attr("class", "active");
	// 新的时间插件显示示例
	timepicker("startTime");
	timepicker("endTime");
	// -----------------分页相关设置---------------------
	// 绑定全选/取消全选按钮
	$('#chooseall').toggle(function() {
		$('input[name="chooseTest"]').attr("checked", "true");
		$(this).text("反选");
	}, function() {
		$('input[name="chooseTest"]').each(function() {
			if ($(this).attr("checked")) {
				$(this).removeAttr("checked");
			} else
				$(this).attr("checked", "true");
		});
		$(this).text("全选");
	});
	// 加载试题数据
	loadTests();
	// 绑定搜索按钮
	bindSearch();
	// 绑定删除按钮
	bindRecord();
	// 绑定题库选择下拉列表
	bindCourseList();
});

// 加载我的试题
function loadTests() {
	viewaction = '';
	params = {
		"quCourse" : quCourse,
		"startTime" : "",
		"endTime" : "",
		"searchWord" : "",
		"pageArray" : new Array(),
		"recordPerPage" : 10
	};
	// 获取页面查询参数
	getParams();
	// 绑定分页的一些操作响应
	initialBind();
	// 加载
	initSearch();
}
// 加载数据到数据区域
function refreshContent(pageRecords) {
	$.ajaxSettings.async = false;
	$("#mainTable").empty();
	var courseHtml = "";
	if (pageRecords.data.length == 0) {
		$('#mainTable')
				.append(
						"<tr><td colspan=\"10\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>");
	} else {
		$.each(pageRecords.data, function(itemIndex, item) {
			alert("data");
		});
		$('#mainTable').append(courseHtml);
	}
	return false;
}

function loadCourseInfoTable(obj) {
	var courId = obj.id;
	window.location.href = "courseTest.html?courseId=" + courId;
}

// 绑定题库下拉列表
function bindCourseList() {
	$.get("../../handler/course/dropDownCourse", function(data, status) {
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
	searchWord = $('#keyword').val();
	quCourse = $('#quCourse').val();
	quType = $('#quType').val();
	startTime = $('#startTime').val();
	endTime = $('#endTime').val();
	// 点击搜索，一切重新查找
	params.searchWord = searchWord;
	params.quCourse = quCourse;
	params.quType = quType;
	params.startTime = startTime;
	params.endTime = endTime;
}

// 绑定删除
function bindRecord() {
	$('#delButton').click(function() {
		if (confirm("删除试题将删除试题所包括的信息，该操作不能恢复，请确认是否删除！")) {
			// 获取选择的用户id序列
			var delQuestions = new Array();
			$("input[name='chooseTest']").each(function() {
				var $current = $(this);
				if ($current.attr('checked') != undefined)
					delQuestions.push($current.val());
			});
			var state;
			var message;
			var header;
			// 如果选择的id为空，则提示错误信息
			if (delQuestions.length == 0) {
				state = 'error';
				header = "删除失败";
				message = "未选择任何试题进行删除";
				infoNotice(state, header, message, $('#data-grid'));
			}
			// 否则，向后台发送删除请求并显示结果信息
			else {
				$.post("../../handler/train/delQuestions", {
					"delQuestions" : delQuestions.toString()
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
function changeQu() {
	getParams();
	initSearch();
}