var showCourseName = "";
var showCourseId = 0;
var startTime = '';// 查询的开始时间
var endTime = '';// 查询的结束时间
var viewaction = '';// 分页请求的action
var params;// 请求的参数
var quCourse = 0;// 题库id
var quType = 0;// 试题类型
var searchWord = "";// 试题类型
// 试题类型数组
var quTypeArr = new Array();
$(document).ready(function() {
	// Show Column
	ShowColumn();
	// back To Top
	backToTop();
	// 清空搜索信息
	clearInfo();
	// ** 加载分页 *//
	if (userId <= 0) {
		$('#pagination').hide();
	} else
		paginationPage();
	// 新的时间插件显示示例
	timepicker("startTime");
	// 隐藏操作按钮
	$('#buttonGroup').hide();
	// 保存该标签中的值到变量中
	originalHtml = $('#droplistknow').html();
	// -----------------分页相关设置---------------------
	// 绑定全选/取消全选按钮
	/*$('#chooseall').toggle(function() {
		$('input[name="chooseQu"]').attr("checked", "true");
		$(this).text("反选");
	}, function() {
		$("[name='chooseQu']").each(function() {
			if ($(this).attr("checked")) {
				$(this).removeAttr("checked");
			} else {
				$(this).attr("checked", 'true');
			}
		});
		$(this).text("全选");
	});*/
	$('#chooseall').click(function() {
		if($(this).html()=="全选"){
			$('input[name="chooseQu"]').prop("checked", true);
			$(this).text("反选");
		}
		else{
		$('input[name="chooseQu"]').prop("checked",false);
		$(this).text("全选");
		}
	});
	// 加载试题数据
	loadQuestions();
	// 绑定搜索按钮
	bindSearch();
	// 绑定题库选择下拉列表
	bindCourseList();
	// 清除信息
	clearInfo();
});
// 清空搜索信息
function clearInfo() {
	$("#courseName").val("未选择");
	$("#startTime").val("");
	$("#endTime").val("");
	$("#keyword").val("");
	$("#isSelectedQues").val("未选择");
}
// 加载我的试题
function loadQuestions() {
	viewaction = '../../handler/train/viewQuestions';
	params = {
		"quCourse" : quCourse,
		"quType" : quType,
		"startTime" : "",
		"endTime" : "",
		"searchWord" : "",
		"pageArray" : new Array(),
		"recordPerPage" : 10,
		"departId":0,
		"userId":0,
		"departType":0,
		"userType":0,
		"postType":2
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
			// 题型名称
			var exTypeName = "";
			// 显示正确答案
			var correctAns = "";
			// 选项
			var optionHtml = "";
			if (item.exType == 1) {
				exTypeName = "单选题";
				// 正确答案编码
				var answerCode = item.correctAnswer.charCodeAt(0);// 转换成ASCII码
				correctAns = String.fromCharCode(answerCode + 16);
				optionHtml = "<p>选项：</p>";
			} else if (item.exType == 2) {
				exTypeName = "多选题";
				// 正确答案编码
				var answers = item.correctAnswer.split("");
				for ( var i = 0; i < answers.length; i++) {
					var answerCode = answers[i].charCodeAt(0);// 转换成ASCII码
					correctAns += String.fromCharCode(answerCode + 16);
				}
				optionHtml = "<p>选项：</p>";
			} else if (item.exType == 3) {
				exTypeName = "判断题";
				var answerCode = item.correctAnswer;
				if (answerCode == "YES") {
					correctAns = "对";
				} else {
					correctAns = "错";
				}
			} else if (item.exType == 4) {
				exTypeName = "填空题";
				correctAns = item.correctAnswer;
			} else if (item.exType == 5) {
				exTypeName = "问答题";
				correctAns = item.correctAnswer;
			} else {
				exTypeName = "未定义";
				correctAns = item.correctAnswer;
			}
			var options = "A";
			var code = options.charCodeAt(0);// 转换成ASCII码
			courseHtml += "<tr><td><input type='checkbox' name='chooseQu' value='" + item.exquId
					+ "'></td><td class='tdcenter'>" + item.exquId + "</td>" + "<td class='tdcenter' id='"
					+ item.courId + "'>" + item.courName + "</td><td class='tdcenter'>" + exTypeName
					+ "</td><td class='tdcenter'><a href='#' rel='popover'  data-original-title='<strong>"
					+ item.exDescribe + "</strong>'";
			$.each(item.options, function(index, option) {
				var optionMark = String.fromCharCode(code);
				optionHtml += "<dd>" + optionMark + "." + option.optiDescribe + "</dd>";
				code++;
			});
			var correctHtml = "<p><strong>正确答案：" + correctAns + "</strong></p>";
			courseHtml += "data-content='" + optionHtml + correctHtml + "' >" + titleFormat(item.exDescribe, 10)
					+ "</a>";
			courseHtml += "</td><td class='tdcenter'>" + item.creatTimeFormat + "</td>";
			var operationStr = "color:blue;' onclick='setSelected(" + item.exquId + ")'> 选择";
			var rowhtml = "<tr><td style='text-align:center'><input type='checkbox' name='chooseQu' ";
			if (isIdSelected(item.exquId)) {
				operationStr = "color:red;' onclick='cancelSelected(" + item.exquId + ")'> 取消选择 ";
				rowhtml += "checked='checked' ";
			}
			courseHtml += "<td class='tdcenter'><span id='questionShow" + item.exquId + "' style='cursor:pointer;"
					+ operationStr + "</span></td></tr>";
		});
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


// 绑定题库下拉列表
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
	params.departId = departId;
	params.userId = userId;
	params.departType = departmentTypeID;
	params.userType = userType;
}
// 判断该ID的题是否已经添加
function isIdSelected(id) {
	var array = $('#isSelectedQues').val().trim().split(";");
	for ( var i = 0; i < array.length; i++) {
		if (array[i] == id)
			return true;
	}
	return false;
}

// 设置该题已经选择
function setSelected(id) {
	if ($("#quCourse").val() > 0) {
		var tempStr = $('#isSelectedQues').val().trim() == "未选择" ? "" : $('#isSelectedQues').val().trim();
		tempStr += id + ";";
		var arr = tempStr.split(";");
		$('#showTotalMsg').html("已选择 " + (arr.length - 1) + "题");
		$('#isSelectedQues').val(tempStr);
		$('#questionShow' + id).css("color", "red");
		$("#questionShow" + id).attr("onclick", "cancelSelected(" + id + ")");
		$("#questionShow" + id).html("取消选择");
		// 设置对应的选择框
		$("input[name='chooseQu']").each(function() {
			if ($(this).val() == id) {
				$(this).is(":checked");
			}
		});	
	} else
		alert("请先选择课程");
}
// 取消该题已选
function cancelSelected(id) {
	var tempStr = $('#isSelectedQues').val().trim();
	var selectedArray = new Array();// 保存已经选择的题目
	selectedArray = tempStr.split(";");
	for ( var i = 0; i < selectedArray.length; i++) {
		if (selectedArray[i] == id)
			selectedArray.splice(i, 1);
	}
	tempStr = "";
	for ( var i = 0; i < selectedArray.length; i++) {
		if (i != selectedArray.length - 1)
			tempStr += selectedArray[i] + ";";
	}
	var arr = tempStr.split(";");
	$('#showTotalMsg').html("已选择 " + (arr.length - 1) + "题");
	if (arr.length == 1)
		$('#isSelectedQues').val("未选择");
	else
		$('#isSelectedQues').val(tempStr);
	$('#questionShow' + id).css("color", "blue");
	$("#questionShow" + id).attr("onclick", "setSelected(" + id + ")");
	$("#questionShow" + id).html("选择");
	// 取消对应的选择框
	$("input[name='chooseQu']").each(function() {
		if ($(this).val() == id) {
			$(this).removeAttr("checked");
		}
	});
}

// 根据条件查询对应题目
function condition_search() {
	var quesType = $('#questionType').val();
	var quesDifficulty = $('#quesDifficulty').val();
	var searchBegin = $('#search_begin').val().trim();
	var searchEnd = $('#search_end').val().trim();
	var quesSearchTitle = $('#quesSearchTitle').val().trim();
	if (!compareTime(searchBegin, searchEnd)) {
		alert("时间区间错误");
		return;
	}
	viewaction = "conditionSearch";
	params = {
		"pageArray" : new Array(),
		"recordPerPage" : 10,
		"quesType" : quesType,
		"quesDifficulty" : quesDifficulty,
		"searchBegin" : searchBegin,
		"searchEnd" : searchEnd,
		"quesSearchTitle" : quesSearchTitle,
		"knowIds" : chooseKnows.toString()
	};
	cacheRecords = new Array();// 缓存的记录清空
	initialBind();// 绑定分页的一些操作响应
	// 加载首页记录
	initSearch();
}
// 展示已经选择的题目信息，并未修改数据库信息
function selectedQuesShow() {
	var queIds = $('#isSelectedQues').val().trim();
	var arr = queIds.split(";");
	if (arr.length <= 1) {
		alert("未选择任何题目，请选择题目后点击下一步");
		return;
	}
	var url = '../../handler/train/selectedInfos';
	$.post(url, {
		"queIds" : queIds,
	}, function(data) {
		nextStepLoadTable(data.data.pageData, queIds);
		$("#pagination").hide();
	});
}

var quesObj = {};
function nextStepLoadTable(data, queIds) {
	quesObj = data;
	var nextTable = "<table class='table table--hover table-bordered'>"
			+ "<thead><tr><th>试题ID</th><th>课程</th><th>类型</th><th>题干</th><th>创建时间</th></tr></thead>" + "<tbody>";
	var singale = false;
	var mutil = false;
	var judge = false;
	var fill = false;
	var quesAndAns = false;
	$.each(data, function(itemIndex, item) {
		// 题型名称
		var exTypeName = "";
		// 显示正确答案
		var correctAns = "";
		// 选项
		var optionHtml = "";
		if (item.exType == 1) {
			exTypeName = "单选题";
			// 正确答案编码
			var answerCode = item.correctAnswer.charCodeAt(0);// 转换成ASCII码
			correctAns = String.fromCharCode(answerCode + 16);
			optionHtml = "<p>选项：</p>";
			singale = true;
		} else if (item.exType == 2) {
			exTypeName = "多选题";
			// 正确答案编码
			var answers = item.correctAnswer.split("");
			for ( var i = 0; i < answers.length; i++) {
				var answerCode = answers[i].charCodeAt(0);// 转换成ASCII码
				correctAns += String.fromCharCode(answerCode + 16);
			}
			optionHtml = "<p>选项：</p>";
			mutil = true;
		} else if (item.exType == 3) {
			exTypeName = "判断题";
			var answerCode = item.correctAnswer;
			if (answerCode == "YES") {
				correctAns = "对";
			} else {
				correctAns = "错";
			}
			judge = true;
		} else if (item.exType == 4) {
			exTypeName = "填空题";
			correctAns = item.correctAnswer;
			fill = true;
		} else if (item.exType == 5) {
			exTypeName = "问答题";
			correctAns = item.correctAnswer;
			quesAndAns = true;
		} else {
			exTypeName = "未定义";
			correctAns = item.correctAnswer;
		}
		var options = "A";
		var code = options.charCodeAt(0);// 转换成ASCII码
		nextTable += "<tr><td class='tdcenter'>" + item.exquId + "</td>" + "<td class='tdcenter' id='" + item.courId
				+ "'>" + item.courName + "</td><td class='tdcenter'>" + exTypeName
				+ "</td><td class='tdcenter'><a href='#' rel='popover'  data-original-title='<strong>"
				+ item.exDescribe + "</strong>'";
		$.each(item.options, function(index, option) {
			var optionMark = String.fromCharCode(code);
			optionHtml += "<dd>" + optionMark + "." + option.optiDescribe + "</dd>";
			code++;
		});
		var correctHtml = "<p><strong>正确答案：" + correctAns + "</strong></p>";
		nextTable += "data-content='" + optionHtml + correctHtml + "' >" + titleFormat(item.exDescribe, 10) + "</a>";
		nextTable += "</td><td class='tdcenter'>" + item.creatTimeFormat + "</td>";
		nextTable += "</tr>";
	});
	nextTable += "</tbody></table>";
	// 加载手动组卷试卷信息
	manualGroupPaperInfo(singale, mutil, judge, fill, quesAndAns, queIds, quTypeArr);
	// 确认组卷按钮
	$("#stepBtn").html("<input id='commitGroup' value='确认组卷' class='btn btn-primary' onclick='setAllOnes()'/>");
	$("#user-center-content").html(nextTable);
	// popover工具
	$("[rel=popover]").popover({
		placement : 'top',
		trigger : 'hover',
		html : 'true', // needed to show html of course
	});
}

// 加载手动组卷试卷信息
function manualGroupPaperInfo(singale, mutil, judge, fill, quesAndAns, queIds) {
	var manualHtml = "<form class='form-horizontal'><div class='form-group col-sm-12'><label class='control-label col-sm-2' for='selectedQuesGroup'><strong>已选择题目ID：</strong>"
			+ "</label><div class='controls col-sm-4'><input type='text' id='selectedQuesGroup' class='span2 form-control'"
			+ "disabled value='" + queIds + "'></div></div>";
	manualHtml += "<div class='form-group col-sm-12'><label class='control-label col-sm-2' for='groupPaName'>试卷名称：</label>"
			+ "<div class='controls col-sm-4'><input type='text' id='groupPaName' class='input-big valid form-control'>"
			+ "</div><div class='col-sm-4'><font color='red'> * </font>必填 <span class='help-inline'>请输入试卷名称</span>" + "</div></div>";
	manualHtml += "<div class='form-group col-sm-12'><label class='control-label col-sm-2' for='expaDescribe'>试卷描述：</label>"
			+ "<div class='controls col-sm-4'><input type='text' id='expaDescribe' class='input-big valid form-control'>"
			+ "</div><div class='col-sm-4'><span class='help-inline'>(选填)请输入试卷描述信息</span>" + "</div></div>";
	if (singale) {
		manualHtml += "<div class='form-group col-sm-12'><label class='control-label col-sm-2' for='perRadio'>单选题分数：</label>"
				+ "<div class='controls col-sm-4'><input type='text' id='perRadio' class='span2 form-control'>"
				+ "</div><div class='col-sm-4'><span class='help-inline'>填写每道单选题分数</span>" + "</div></div>";
	}
	if (mutil) {
		manualHtml += "<div class='form-group col-sm-12'><label class='control-label col-sm-2' for='perMulti'>多选题分数：</label>"
				+ "<div class='controls col-sm-4'><input type='text' id='perMulti' class='span2 form-control'>"
				+ "</div><div class='col-sm-4'><span class='help-inline'>填写每道多选题分数</span>" + "</div></div>";
	}
	if (judge) {
		manualHtml += "<div class='form-group col-sm-12'><label class='control-label col-sm-2' for='perJudge'>判断题分数：</label>"
				+ "<div class='controls col-sm-4'><input type='text' id='perJudge' class='span2 form-control'>"
				+ "</div><div class='col-sm-4'><span class='help-inline'>填写每道判断题分数</span>" + "</div></div>";
	}
	if (fill) {
		manualHtml += "<div class='form-group col-sm-12'><label class='control-label col-sm-2' for='perFill'>填空题分数：</label>"
				+ "<div class='controls col-sm-4'><input type='text' id='perFill' class='span2 form-control'>"
				+ "</div><div class='col-sm-4'><span class='help-inline'>填写每道填空题分数</span>" + "</div></div>";
	}
	if (quesAndAns) {
		manualHtml += "<div class='form-group col-sm-12'><label class='control-label col-sm-2' for='perAsk'>问答题分数：</label>"
				+ "<div class='controls col-sm-4'><input type='text' id='perAsk' class='span2 form-control'>"
				+ "</div><div class='col-sm-4'><span class='help-inline'>填写每道问答题分数</span>" + "</div></div>";
	}
	manualHtml += "<div class='form-group col-sm-12'><label class='control-label col-sm-2' for='difficulty'>难易程度：</label>"
			+ "<div class='controls col-sm-4'>" + "<select type='text' id='difficulty' class='form-control'> "
			+ "<option style='color: #00ff00' value='0'>容易</option>"
			+ "<option selected='selected' value='1'>一般</option>"
			+ "<option style='color: #ff9900' value='2'>偏难</option>"
			+ "<option style='color: #ff0000' value='3'>困难</option>" + "</select>" + "</div></div></form>";
	$("#myTabContent").html(manualHtml);
}

// 组卷提交题目到后台
function setAllOnes() {
	var perRadio = 0;
	var perMulti = 0;
	var perJudge = 0;
	var perFill = 0;
	var perAsk = 0;
	// 试卷类型，用于判定试卷是否能够自动评分
	var expaType = 0;
	if ($("#selectedQuesGroup").val() == "" || $("#selectedQuesGroup").val() == null) {
		alert("试卷选择出错，请填重新选择");
		$("#selectedQuesGroup").focus();
		return;
	}
	if ($("#groupPaName").val() == "" || $("#groupPaName").val() == null) {
		alert("未填写试卷名称，请填写后再进行提交");
		$("#groupPaName").focus();
		return;
	}
	if ($("#perRadio").length > 0) {
		if ($("#perRadio").val() == "" || $("#perRadio").val() == null) {
			alert("未填写单选分数，请填写");
			$("#perRadio").focus();
			return;
		} else if (!regFloat($("#perRadio").val())) {
			alert("分数为正数，包括小数");
			$("#perRadio").focus();
			return;
		}
		perRadio = $("#perRadio").val();
	}
	if ($("#perMulti").length > 0) {
		if ($("#perMulti").val() == "" || $("#perMulti").val() == null) {
			alert("未填写多选分数，请填写");
			$("#perMulti").focus();
			return;
		} else if (!regFloat($("#perMulti").val())) {
			alert("分数为正数，包括小数");
			$("#perMulti").focus();
			return;
		}
		perMulti = $("#perMulti").val();
	}
	if ($("#perJudge").length > 0) {
		if ($("#perJudge").val() == "" || $("#perJudge").val() == null) {
			alert("未填写判断分数，请填写");
			$("#perMulti").focus();
			return;
		} else if (!regFloat($("#perJudge").val())) {
			alert("分数为正数，包括小数");
			$("#perJudge").focus();
			return;
		}
		perJudge = $("#perJudge").val();
	}
	if ($("#perFill").length > 0) {
		if ($("#perFill").val() == "" || $("#perFill").val() == null) {
			alert("未填写填空分数，请填写");
			$("#perFill").focus();
			return;
		} else if (!regFloat($("#perFill").val())) {
			alert("分数为正数，包括小数");
			$("#perFill").focus();
			return;
		}
		perFill = $("#perFill").val();
		expaType = 1;
	}
	if ($("#perAsk").length > 0) {
		if ($("#perAsk").val() == "" || $("#perAsk").val() == null) {
			alert("未填写问答分数，请填写");
			$("#perAsk").focus();
			return;
		} else if (!regFloat($("#perAsk").val())) {
			alert("分数为正数，包括小数");
			$("#perAsk").focus();
			return;
		}
		perAsk = $("#perAsk").val();
		expaType = 1;
	}
	// 重装数据
	// rebuildQues(params,data);
	var totalScore = 0;
	var radio = "";
	var radioType = "";
	var multi = "";
	var multiType = "";
	var judge = "";
	var judgeType = "";
	var fill = "";
	var fillType = "";
	var ask = "";
	var askType = "";
	$.each(quesObj, function(index, item) {
		if (item.exType == 1) {
			totalScore = totalScore + parseInt(perRadio);
			if (radio != "") {
				radio += "," + item.exquId;
				radioType += "," + item.exType;
			} else {
				radio += item.exquId;
				radioType += item.exType;
			}
		} else if (item.exType == 2) {
			totalScore = totalScore + parseInt(perMulti);
			if (multi != "") {
				multi += "," + item.exquId;
				multiType += "," + item.exType;
			} else {
				multi += item.exquId;
				multiType += item.exType;
			}
		} else if (item.exType == 3) {
			totalScore = totalScore + parseInt(perJudge);
			if (judge != "") {
				judge += "," + item.exquId;
				judgeType += "," + item.exType;
			} else {
				judge += item.exquId;
				judgeType += item.exType;
			}
		} else if (item.exType == 4) {
			totalScore = totalScore + parseInt(perFill);
			if (fill != "") {
				fill += "," + item.exquId;
				fillType += "," + item.exType;
			} else {
				fill += item.exquId;
				fillType += item.exType;
			}
		} else if (item.exType == 5) {
			totalScore = totalScore + parseInt(perAsk);
			if (ask != "") {
				ask += "," + item.exquId;
				askType += "," + item.exType;
			} else {
				ask += item.exquId;
				askType += item.exType;
			}
		}
	});
	var quesStr = new Array();// 加装问题编号
	var quesTypeArr = new Array();// 加装对应的问题类型
	if(radio!=""){
		quesStr.push(radio);
		quesTypeArr.push(radioType);
	}
	if(multi!=""){
		quesStr.push(multi);
		quesTypeArr.push(multiType);
	}
	if(judge!=""){
		quesStr.push(judge);
		quesTypeArr.push(judgeType);
	}
	if(fill!=""){
		quesStr.push(fill);
		quesTypeArr.push(fillType);
	}
	if(ask!=""){
		quesStr.push(ask);
		quesTypeArr.push(askType);
	}
	var params = {
		"expaName" : $("#groupPaName").val(),
		"expaDescribe" : $("#expaDescribe").val(),
		"perRadio" : perRadio,
		"perMulti" : perMulti,
		"perJudge" : perJudge,
		"perFill" : perFill,
		"perAsk" : perAsk,
		"expaType" : expaType,
		"expaCourId" : quCourse,// 题库
		"expaDifficulty" : $("#difficulty").val(),// 难度
		"quTypeArr" : quesTypeArr.toString(),
		"expaTotalscore" : totalScore,
		"selectedQues" : quesStr.toString()
	};
	var url = '../../handler/train/commitSelectedQues';
	$.post(url, params, function(data) {
		if (data.ret) {
			alert("组卷成功！将返回试卷列表！");
			window.location.href = "sysman_Paper.html?"+urlColHtml();
		} else {
			alert("组卷失败！");
		}
	});
}

// 时间比较javascript:void(0);
function compareTime(timeB, timeE) {
	if (timeB.trim() == "" || timeE.trim() == "")
		return true;
	timeB = timeB.replaceAll("-", "/");
	timeE = timeE.replaceAll("-", "/");
	var dateB = new Date(Date.parse(timeB));
	var dateE = new Date(Date.parse(timeE));
	if (dateB < dateE)
		return true;
	else
		return false;
}

// 重新拼装试题数据
function rebuildQues(params, data) {

}

// 正则表达式，匹配分数是否为正数值
function regFloat(str) {
	var reg = /^[0-9]+[\.]?[0-9]*$/;
	return reg.test(str);
}

// 替换所有正则表达式匹配的的方法
String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
};
// 批量选择添加
function batchAdd() {
	var addQues = new Array();
	$("input[name='chooseQu']").each(function() {
		var $current = $(this);
		if ($current.is(':checked') != undefined)
			addQues.push($current.val());
	});
	var params = $('#isSelectedQues').val().trim();
	var arr = params.split(";");
	if (addQues.length == 0) {
		alert("请选择题目");
		return;
	}
	if (arr.length <= 1) {
		params = "";
		$.each(addQues, function(entryIndex, entry) {
			params += entry + ";";
			$('#questionShow' + entry).css("color", "red");
			$("#questionShow" + entry).attr("onclick", "cancelSelected(" + entry + ")");
			$("#questionShow" + entry).html("取消选择");
		});
	} else {
		$.each(addQues, function(entryIndex, entry) {
			if (!isIdSelected(entry)) {
				params += entry + ";";
				$('#questionShow' + entry).css("color", "red");
				$("#questionShow" + entry).attr("onclick", "cancelSelected(" + entry + ")");
				$("#questionShow" + entry).html("取消选择");
			}
		});
	}
	$('#isSelectedQues').val(params);
	var tempStr = $('#isSelectedQues').val().trim();
	var arr = tempStr.split(";");
	$('#showTotalMsg').html("已选择 " + (arr.length - 1) + "题");
}
// 查询数据库上次添加的试题并显示
function getSelectedQues() {
	var url = 'getSelectedQues';
	$.post(url, null, function(data) {
		if (data != null) {
			if (data.status == 0) {
				$('#isSelectedQues').val(data.result);
				var arr = $('#isSelectedQues').val().split(";");
				$('#showTotalMsg').html("已选择 " + (arr.length - 1) + "题");
			} else {
				// 将选择的题目刷新时其清空
				$('#isSelectedQues').val("未选择");
			}
		}
	});
}
// 清除所有已选题目
function clearAllSelected() {
	$('#isSelectedQues').val("未选择");
	window.location.reload();
}
// on change函數 选择课程
function chooseSourse() {
	var courseId = $("#quCourse").val();
	if (courseId > 0) {
		$("#buttonGroup").show();
		var courseName = $('#quCourse option:selected').text();
		$("#courseName").val(courseName);
		// 搜索
		getParams();
		initSearch();
		$("#quCourse").hide();
	}
	return false;
}
// on change 函数 修改课程
function changeCourse() {
	$("#quCourse").show();
	$("#quCourse").val("-1");
	$("#courseName").val("");
	changeQuType();
	return;
}
// on change 函数 改变试题类型
function changeQuType() {
	// 重新搜索
	getParams();
	initSearch();
	return;
}