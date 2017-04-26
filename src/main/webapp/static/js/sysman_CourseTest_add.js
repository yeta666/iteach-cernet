var optionCount = 1;// 记录当前的选项数
var isHaveTitle = false;// 是否已存在该题
var hasSameName = false;// 测试名称不能相同
var testTimeIsNum = false;// 测试时间是否是正整数
var params;// 请求的参数
var viewaction = '';// 分页请求的action
$(document).ready(function() {
	ShowColumn();// Show Column
	//backToTop();// back To Top
	paginationPage();// 分頁
	clearInfo();// 清除信息
	timepicker("exin_begintime");
	timepicker("exin_endtime");
	// 验证输入考试时长为正整数
	$('#testTime').blur(function() {
		if (/^[0-9]*$/g.test($('#testTime').val().trim()) && $('#testTime').val() > 0) {
			$('#timeTips').html("<img src='../img/ok.png' width='13px'>");
			$('#timeTips').css("display", "");
			$('#testTime').val(clearZeroStart($('#testTime').val()));
			testTimeIsNum = true;
		} else {
			testTimeIsNum = false;
			$('#timeTips').html("<img src='../img/cancel.png' width='13px'>请输入正整数");
			$('#timeTips').css("display", "");
		}
	});

	// 判断数据库中是否存在该测试名称
	$('#testName').blur(function() {
		if ($('#testName').val().trim() == "") {
			$('#nameTips').html("<img src='../img/cancel.png' width='13px'>测试名称不能为空");
			$('#nameTips').css("display", "");
		} else {
			$('#nameTips').html("<img src='../img/ok.png' width='13px'>");
			$('#nameTips').css("display", "");
			var url = '../../handler/train/checkTestName';// 检验课程测试名称是否重复
			$.post(url, {
				"name" : $('#testName').val().trim()
			}, function(data) {
				if (data.ret) {
					hasSameName = false;
					$('#nameTips').html("<img src='../img/ok.png' width='13px'>");
					$('#nameTips').css("display", "");
				} else {
					hasSameName = true;
					$('#nameTips').html("<img src='../img/cancel.png' width='13px'>测试名称不能相同");
					$('#nameTips').css("display", "");
				}
			});
		}
	});

	// 绑定题库选择下拉列表
	bindCourseList();

	// 获取课程对应试卷
	bindCoursePapers();

});

//清除数字前面的0
function clearZeroStart(str) {
	if (/^[0-9]*$/g.test(str) && str.length > 1) {
		if (str.charAt(0) == 0)
			return clearZeroStart(str.substring(1));
		else
			return parseInt(str);
	} else {
		return parseInt(str);
	}
}

//绑定题库下拉列表
function bindCourseList() {
	$.post("../../handler/course/dropDownCourse", {
		"departId":departId,
		"userId":userId,
		"departType":departmentTypeID,
		"userType":userType
	}, function(data) {
		var courseList = data.data.courseList;
		var courseDropDown = "";
		$.each(courseList, function(index, item) {
			courseDropDown += "<option value='" + item.courId + "'>" + item.courName + "</option>";
		});
		$("#quCourse").append(courseDropDown);
	});
}

//加载指定ID的试题信息
function bindCoursePapers() {
	var courId = $("#quCourse").val();
	viewaction = '../../handler/train/viewPapers';
	params = {
			"paperId" : 0,
			"quoteType" : 1,
			"departId":0,
			"userId":0,
			"departType":0,
			"userType":0,
			"courseId" : courId,
			"startTime" : "",
			"endTime" : "",
			"searchWord" : "",
			"searchType" : -1,
			"pageArray" : new Array(),
			"recordPerPage" : 10
	};
	// 获取页面查询参数
	getParams();
	// 绑定分页的一些操作响应
	initialBind();

	initSearch();
}

//设置参数
function getParams() {
	var courId = $("#quCourse").val();
	var searchWord = "";
	var paperId = 0;
	var startTime = "";
	var endTime = "";
	var searchType = 0;
	// 点击搜索，一切重新查找
	params.searchWord = searchWord;
	params.paperId = paperId;
	params.courseId = courId, params.startTime = startTime;
	params.endTime = endTime;
	params.searchType = searchType;
	params.departId = departId;
	params.departType = departmentTypeID;
	params.userId = userId;
	params.userType = userType;
}

//清除输入信息
function clearInfo() {
	$('#testName').val("");
	$('#testDescribe').val("");
	$('#testTime').val("");
	$("#exin_begintime").val("");
	$("#exin_endtime").val("");
}

//加载数据到数据区域
function refreshContent(pageRecords) {
	$.ajaxSettings.async = false;
	$("#mainTable").empty();
	var paperHtml = "";
	if (pageRecords.data.length == 0) {
		$('#mainTable')
		.append(
		"<tr><td colspan=\"10\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>");
	} else {
		$.each(pageRecords.data, function(index, item) {
			var paperDifficult = "";
			if (item.expaDifficulty == 0) {
				paperDifficult = "容易";
			} else if (item.expaDifficulty == 1) {
				paperDifficult = "一般";
			} else if (item.expaDifficulty == 2) {
				paperDifficult = "偏难";
			} else if (item.expaDifficulty == 3) {
				paperDifficult = "困难";
			} else {
				paperDifficult = "未定义";
			}
			paperHtml += "<tr id='" + item.expaId + "_tr'><td><input type='radio' name='choosePa' value='"
			+ item.expaId + "'></td><td class='tdcenter'>" + (index + 1) + "</td>"
			+ "<td class='tdcenter' id='" + item.expaId + "'>" + item.expaName + "</td><td class='tdcenter'>"
			+ item.courName + "</td><td class='tdcenter'>" + item.expaTotalscore + "</td><td class='tdcenter'>"
			+ paperDifficult + "</td>" + "<td class='tdcenter'>" + item.expaCreatetimeFormat + "</td>"
			+ "<td class='tdcenter' title='" + item.expaDescribe + "'>" + titleFormat(item.expaDescribe, 10)
			+ "</td></tr>";
		});
		$('#mainTable').append(paperHtml);
	}
	return false;
}

//初步检查输入数据
function checkSubmit() {
	var testName = $('#testName').val().trim();
	if (testName.length == 0) {
		infoNotice("error", "", "请输入试卷测试名称!");
		$('#testName').focus();
		return false;
	}
	var testTime = $('#testTime').val().trim();
	if (testTime.length == 0) {
		infoNotice("error", "", "请输入试卷测试时间!");
		$('#testTime').focus();
		return false;
	}
	var choosePa = $("input[type='radio'][name='choosePa']:checked").val();
	if (choosePa == null || choosePa == "") {
		alert("请选择测试试卷!");
		return false;
	}
	return true;
}
//传递数据至后台保存
function storeExamInfo(id) {
	if (!checkSubmit()) {
		return false;
	}
	var courseId = $('#quCourse').val();
	var choosePa = $("input[type='radio'][name='choosePa']:checked").val();
	var param = {
			"exinName" : $('#testName').val(),
			"exinExpaId" : choosePa,
			"exinDescribe" : $('#testDescribe').val(),
			"exinTotaltime" : $('#testTime').val(),
			"exinBgtime" : $("#exin_begintime").val(),
			"exinEdtime" : $("#exin_endtime").val(),
			"courseId" : courseId
	};
	var url = '../../handler/train/newExaminfo';
	$.post(url, param, function(data) {
		if (data != null) {
			if (data.ret) {
				infoNotice("success", "", "添加课程测试成功！");
				history.go(-1);
			} else {
				infoNotice("error", "", "添加课程测试失败！");
			}
		} else {
			window.location.reload();
		}
	});
}

//on change 函数 改变课程变化
function changeFun() {
	getParams();
	initSearch();
}