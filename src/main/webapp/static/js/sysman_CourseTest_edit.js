var hasSameName = false;// 测试名称不能相同
var testTimeIsNum = false;// 测试时间是否是正整数
var params;// 请求的参数
var viewaction = '';// 分页请求的action
var exinId = 0;// 编辑的考试信息编号
var exinExpaId = 0;// 考试信息对应的试卷id
var examName = "";//测试原始名称！
$(document).ready(function() {
	ShowColumn();// Show Column
	backToTop();// back To Top
	clearInfo(); // 清除信息
	timepicker("exin_begintime");
	timepicker("exin_endtime");
	chacheCheck=false;
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
	$('#testName').change(function() {
		if ($('#testName').val().trim() == "") {
			$('#nameTips').html("<img src='../img/cancel.png' width='13px'>测试名称不能为空");
			$('#nameTips').css("display", "");
		} else {
			if($('#testName').val()==examName){
				$('#nameTips').html("<img src='../img/ok.png' width='13px'>");
				$('#nameTips').css("display", "");
			}else{
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
		}
	});
	// 获取考试信息
	queryExinById();
	// 获取课程对应试卷
	bindCoursePapers();
});

//查询考试信息
function queryExinById() {
	exinId = getRequest("exinId");
	var courId = getRequest("courId");
	var url = "../../handler/train/queryExinById";
	var iHtml="";
	$.post(url, {
		"exinId" : exinId
	}, function(data) {
		if (data.ret) {
			var examInfo = data.data.examInfo;
			//$("#quCourse").prop("value", courId);
			iHtml+="<option value='"+courId+"'>"+examInfo.exinName+"</option>"
			$("#quCourse").html(iHtml);
			$("#quCourse").prop("disabled", true);
			$("#testName").val(examInfo.exinName);
			examName=examInfo.exinName;
			exinExpaId = examInfo.exinExpaId;
			$("#testDescribe").val(examInfo.exinDescribe);
			$("#testTime").val(examInfo.exinTotaltime);
			$("#exin_begintime").val(examInfo.exinBgtime);
			$("#exin_endtime").val(examInfo.exinEdtime);
			//$("input[value='" + courId + "']").attr("selected", true);
		} else {
			alert(data.errmsg);
		}
	}, "json");
}

//加载指定ID的试题信息
function bindCoursePapers() {
	var courId = $("#quCourse").val();
	viewaction = '../../handler/train/viewPapers';
	params = {
			"paperId" : 0,
			"quoteType" : 1,
			"courseId" : courId,
			"departId":0,
			"userId":0,
			"departType":0,
			"userType":0,
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
			//alert(exinExpaId+"---"+item.expaId);
			if (exinExpaId == item.expaId) {
				paperHtml += "<tr id='" + item.expaId + "_tr'><td class='tdcenter' id='" + item.expaId + "'>"
				+ item.expaName + "</td><td class='tdcenter'>" + item.courName + "</td><td class='tdcenter'>"
				+ item.expaTotalscore + "</td><td class='tdcenter'>" + paperDifficult + "</td>"
				+ "<td class='tdcenter'>" + item.expaCreatetimeFormat + "</td>"
				+ "<td class='tdcenter' title='" + item.expaDescribe + "'>"
				+ titleFormat(item.expaDescribe, 10) + "</td></tr>";
				
			}
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
	return true;
}
//传递数据至后台保存
function updateExamInfo(id) {
	if (!checkSubmit()) {
		return false;
	}
	var param = {
			"exinId" : exinId,
			"exinName" : $('#testName').val(),
			"exinExpaId" : exinExpaId,
			"exinDescribe" : $('#testDescribe').val(),
			"exinTotaltime" : $('#testTime').val(),
			"exinBgtime" : $("#exin_begintime").val(),
			"exinEdtime" : $("#exin_endtime").val()
	};
	var url = '../../handler/train/updateExaminfo';
	$.post(url, param, function(data) {
		if (data != null) {
			if (data.ret) {
				infoNotice("success", "", "编辑课程测试成功！");
				window.location.href="sysman_CourseTest.html?"+urlColHtml();
			} else {
				infoNotice("error", "", "编辑课程测试失败！");
			}
		} else {
			window.location.reload();
		}
	});
}

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