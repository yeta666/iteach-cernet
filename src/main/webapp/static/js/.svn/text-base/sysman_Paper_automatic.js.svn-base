var outAmount = 0;// 记录每次添加题的数目
var judgeSubmit = 0;// 是否可以提交的标志
var isModify = false;// 保存当前是否为修改状态
var modifyNo = 0;// 保存当前修改数据的编号
var hasSameName = true;// 是否已有相同的考试名称
var scoreIsNum = true;// 分数是否正确输入
// var copiedIsNum = true;// 份数是否正确
// var timeIsNum = true;// 时间是否整数
$(document).ready(function() {
	ShowColumn();// Show Column
	backToTop();// back To Top
	// ** 加载分页 *//
	if (userId <= 0) {
		$('#pagination').hide();
	} else
		paginationPage();
	clearInfor();
	// 清空原来的数据
	clearAllInput();
	// 题型序号重置为0
	recordNo = 0;
	// 绑定题库选择下拉列表
	bindCourseList();
	// 清除上次保存的禁用题目
	// $.post('clearForbidQues', null, function(data) {
	//
	// });
	// 检查考试名称是否重复
	$('#exam_name').blur(function() {
		if ($('#exam_name').val().trim() == "") {
			$('#nameTips').html("<img src='../img/cancel.png' width='13px'>考试名称不能为空");
			$('#nameTips').css("display", "");
		} else {
			$('#nameTips').html("<img src='../img/ok.png' width='13px'>");
			$('#nameTips').css("display", "");
			var url = '../../handler/train/checkExamInfoTitle';// 检验考试名称是否重复
			$.post(url, {
				"name" : $('#exam_name').val().trim()
			}, function(data) {
				if (data.ret) {
					hasSameName = false;
					$('#nameTips').html("<img src='../img/ok.png' width='13px'>");
					$('#nameTips').css("display", "");
				} else {
					hasSameName = true;
					$('#nameTips').html("<img src='../img/cancel.png' width='13px'>考试名称不能相同");
					$('#nameTips').css("display", "");
				}
			});
		}
	});
	$('#exam_total_score').blur(function() {
		if (/^[0-9]*$/g.test($('#exam_total_score').val().trim()) && $('#exam_total_score').val() > 0) {
			$('#scoreTips').html("<img src='../img/ok.png' width='13px'>");
			$('#scoreTips').css("display", "");
			$('#exam_total_score').val(clearZeroStart($('#exam_total_score').val()));
			scoreIsNum = true;
		} else {
			scoreIsNum = false;
			$('#scoreTips').html("<img src='../img/cancel.png' width='13px'>请输入正整数");
			$('#scoreTips').css("display", "");
		}
	});
	$('#exam_course').blur(function() {
		if ($('#exam_course').val() > 0) {
			$('#courseTips').html("<img src='../img/ok.png' width='13px'>");
			$('#courseTips').css("display", "");
			$('#exam_course').val(clearZeroStart($('#exam_course').val()));
		} else {
			$('#courseTips').html("<img src='../img/cancel.png' width='13px'>请选择课程");
			$('#courseTips').css("display", "");
		}
	});
	// $('#exam_total_copies').blur(function() {
	// if (/^[0-9]*$/g.test($('#exam_total_copies').val().trim()) &&
	// $('#exam_total_copies').val() > 0) {
	// $('#copiesTips').html("<img src='../img/ok.png' width='13px'>");
	// $('#copiesTips').css("display", "");
	// $('#exam_total_copies').val(clearZeroStart($('#exam_total_copies').val()));
	// copiedIsNum = true;
	// } else {
	// $('#copiesTips').html("<img src='../img/cancel.png'
	// width='13px'>请输入正整数");
	// $('#copiesTips').css("display", "");
	// copiedIsNum = false;
	// }
	// });
	// $('#examSpendTime').blur(function() {
	// if (/^[0-9]*$/g.test($('#examSpendTime').val().trim()) &&
	// $('#examSpendTime').val() > 0) {
	// $('#timeTips').html("<img src='../img/ok.png' width='13px'>");
	// $('#timeTips').css("display", "");
	// $('#examSpendTime').val(clearZeroStart($('#examSpendTime').val()));
	// timeIsNum = true;
	// setEndTime();
	// } else {
	// $('#timeTips').html("<img src='../img/cancel.png' width='13px'>请输入整数");
	// $('#timeTips').css("display", "");
	// timeIsNum = false;
	// }
	// });
	// $('#begintime').blur(function() {
	// if (judgeTime($('#begintime').val().trim())) {
	// $('#beginTimeTips').html("<img src='../img/ok.png' width='13px'>");
	// setEndTime();
	// } else {
	// $('#beginTimeTips').html("<img src='../img/cancel.png'
	// width='13px'>开放格式时间错误");
	// $('#beginTimeTips').css("display", "");
	// }
	// });
	// 当组卷失败时，关闭提示框时重载本页面
	$('#combinationFailDialog').on('hide', function() {
		window.location.reload();
	});
	// var currentTime = new Date().format('YYYY-MM-dd hh:mm:ss');
	// $('#begintime').val(currentTime.substring(0, currentTime.length - 3));
	// 新的时间插件显示示例
	// timepicker("begintime");
	// timepicker("endTime");
	timepicker("show_begintime");
	timepicker("show_endtime");
	$("#ques_num").blur(function() {
		var amount = $('#ques_num').val();
		if (/^[0-9]*$/g.test(amount)) {
			if (amount <= 0) {
				$('#tipMsg').html("请输入合适的数字");
				$("#ques_num").focus();
			} else {
				$('#tipMsg').html("");
				outAmount = amount;
				judgeSubmit++;
			}
		} else {
			$('#tipMsg').html("请输入数字");
			$("#ques_num").focus();
		}
	});
	// $("#ques_distributed_base").blur(function() {
	// var inAmount = $('#ques_distributed_base').val();
	// if (!/^[0-9]*$/g.test($('#ques_num').val().trim()) ||
	// $('#ques_num').val().trim() == "") {
	// $('#tipMsg').html("请先输入题目数");
	// $('#ques_num').focus();
	// return;
	// }
	// if (/^[0-9]*$/g.test(inAmount)) {
	// inAmount = clearZeroStart(inAmount);
	// if (inAmount < 0 || inAmount > outAmount) {
	// $('#tipMsg').html("输入范围[0," + outAmount + "]");
	// $("#ques_distributed_base").focus();
	// } else {
	// $('#tipMsg').html("");
	// var current = outAmount - inAmount;
	// if (current < 0) {
	// $('#tipMsg').html("输入范围~[0," + outAmount + "]");
	// $("#ques_distributed_base").focus();
	// } else {
	// $("#ques_distributed_app").attr("value", outAmount - inAmount);
	// judgeSubmit++;
	// }
	// }
	// } else {
	// $('#tipMsg').html("请输入数字");
	// $("#ques_distributed_base").focus();
	// }
	// });
	// 取消组卷返回试卷管理
	$("#cancel_combi").click(function() {
		if (confirm("取消组卷将返回试卷管理页面，您未提交的数据将丢失！是否继续？")) {
			history.go(-1);
		}
	});
});

// 已经添加的类型
var addedType = new Array();

// 初始化题型设置参数
function initialInputs() {
	outAmount = 0;
	if ($("#exam_course").val() == -1) {
		alert("请先选择试题题库！");
		$("#exam_course").blur();
		return false;
	}
	$('#tionType').modal('show');
	// 清空输入框
	clearAllInput();
}

// 获取一个还未选择的题型的选项值
function getSelectValue() {
	var selectSize = $("select[name=ques_type] option").size();
	for ( var i = 0; i < selectSize; i++) {
		if (!isHave(i)) {
			return i;
		}
	}
	return -1;
}
// 判断数组中是否已添加该题型编号
function isHave(val) {
	for ( var i = 0; i < addedType.length; i++) {
		if (addedType[i] == val)
			return true;
	}
	return false;
}

// 初步检查题型设置中的参数
function parametersCheck() {
	var type = $('#ques_type').val();
	if (isHave(type) && !isModify) {
		$('#tipMsg').html("请选择其他题型，此题型已添加");
		return false;
	}
	var quesScore = $('#ques_score').val().trim();
	if (quesScore != "") {
		if (/^[0-9]*$/g.test($('#ques_score').val().trim())) {
		} else {
			$('#tipMsg').html("请输入整数分值");
			setTimeout("clearTips()", 2000);
			$("#ques_score").focus();
			return false;
		}
	} else {
		$('#tipMsg').html("分值不能为空");
		setTimeout("clearTips()", 2000);
		$("#ques_score").focus();
		return false;
	}
	var amount = $('#ques_num').val().trim();
	if (amount != "") {
		if (/^[0-9]*$/g.test(amount)) {
			if (amount <= 0) {
				$('#tipMsg').html("请输入合适的数字");
				setTimeout("clearTips()", 2000);
				$("#ques_num").focus();
			}
		} else {
			$('#tipMsg').html("请输入数字");
			setTimeout("clearTips()", 2000);
			$("#ques_num").focus();
			return false;
		}
	} else {
		$('#tipMsg').html("请输入值");
		setTimeout("clearTips()", 2000);
		$("#ques_num").focus();
		return false;
	}
	outAmount = $('#ques_num').val();
	// var inAmount = $('#ques_distributed_base').val();
	// if (/^[0-9]*$/g.test(inAmount)) {
	// inAmount = clearZeroStart(inAmount);
	// if (inAmount < 0 || inAmount > outAmount) {
	// $('#tipMsg').html("请~输入范围[0," + outAmount + "]");
	// $("#ques_distributed_base").focus();
	// return false;
	// } else {
	// $('#tipMsg').html("");
	// var baseAmount = $('#ques_distributed_base').val();
	// var current = outAmount - baseAmount;
	// if (current < 0) {
	// $('#tipMsg').html("请-输入范围[0," + outAmount + "]");
	// $("#ques_distributed_base").focus();
	// return false;
	// } else {
	// $("#ques_distributed_app").attr("value", outAmount - baseAmount);
	// judgeSubmit++;
	// }
	// }
	// } else {
	// $('#tipMsg').html("请输入数字");
	// $("#ques_distributed_base").focus();
	// return false;
	// }
	return true;
}

// 选好题型后保存到table表格中
function addQuestion() {
	if (parametersCheck()) {
		// 校验用户所选题型有足够的题供组卷
		var difficulty = $("#ques_difficulty").val();
		var validateParams = {
			"quesType" : $('#ques_type').val(),
			"quesNums" : $('#ques_num').val(),
			"quesDifficult" : difficulty,
			"quesCourId" : $("#exam_course").val()
		};
		$.post("../../handler/train/validateQuesTypeNums", validateParams, function(data) {
			if (!data.ret) {
				var quesNum = data.data.quesNum;
				alert("该条件下试题数为"+quesNum+"!"+data.errmsg);
				return;
			} else {
				if (addQues() != 0) {
					if (!isModify) {
						addedType.push($('#ques_type').val());// 将对应添加的题型的value值存入数组
					}
					// 将修改状态改回false
					isModify = false;
					clearAllInput();
				}
			}
		});
	}
}

// 清除输入框值
function clearAllInput() {
	$('#ques_num').val("");
	$('#ques_score').val("");
	$('#ques_difficulty').val("0");
	$('#show_begintime').val("");
	$('#show_endtime').val("");
	$('#tipMsg').html("");
}

// 清除数字前面的0
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

var tempArr = new Array();
var storeArray = new Array();// 数组，储存添加的题型
var recordNo = 0;

// 将添加的信息展示出来
function addQues() {
	var ques_sel_type_index = $("#ques_type").val();
	var ques_input_number = clearZeroStart($("#ques_num").val());
	var ques_input_score = clearZeroStart($('#ques_score').val());
	var ques_difficult = clearZeroStart($('#ques_difficulty').val());
	var ques_show_endtime = $('#show_endtime').val().trim();
	var ques_show_begintime = $('#show_begintime').val().trim();
	var tempData = {
		"quesTypeIndex" : ques_sel_type_index,
		"quesInputNumber" : ques_input_number,
		"quesInputScore" : ques_input_score,
		"quesDifficulty" : ques_difficult,
		"quesShowBegintime" : ques_show_begintime,
		"quesShowEndtime" : ques_show_endtime,
		"quesTypeNo" : recordNo++
	};
	if (!isModify) {
		storeArray.push(tempData);
	} else {
		for ( var i = 0; i < storeArray.length; i++) {
			if (storeArray[i].quesTypeNo == modifyNo) {
				var temp = storeArray[i];
				temp.quesTypeIndex = $('#ques_type').val();
				temp.quesInputNumber = $('#ques_num').val();
				temp.quesInputScore = $('#ques_score').val();
				temp.quesDifficulty = $('#ques_difficulty').val();
				temp.quesShowBegintime = $('#show_begintime').val();
				temp.quesShowEndtime = $('#show_endtime').val();
				temp.quesTypeNo = recordNo--;
			}
		}
	}
	// 将填写的参数添加到table中去
	showAllAdded();
	$('#tionType').modal('hide');
}
// 删除一条题型记录，item为该题目的序号
function deleteItem(item) {
	for ( var i = 0; i < storeArray.length; i++) {
		if (storeArray[i].quesTypeNo == item) {
			for ( var j = 0; j < addedType.length; j++) {
				if (storeArray[i].quesTypeIndex == addedType[j]) {
					addedType.splice(j, 1);
				}
			}
			storeArray.splice(i, 1);
		}
	}
	showAllAdded();
}
// 展示已经添加的题型参数设置
function showAllAdded() {
	var $records = $('#dataArea').children('tbody');
	var tempHtml = "";
	for ( var i = 0; i < storeArray.length; i++) {
		var temp = storeArray[i];
		// 题目类型处理
		var questionType = "";
		if (temp.quesTypeIndex == "1")
			questionType = "单选题";
		else if (temp.quesTypeIndex == "2")
			questionType = "多选题";
		else if (temp.quesTypeIndex == "3")
			questionType = "判断题";
		else if (temp.quesTypeIndex == "4")
			questionType = "填空题";
		else if (temp.quesTypeIndex == "5")
			questionType = "简单题";
		else
			questionType = "类型错误";
		// 时间记录处理
		var examPeriod = "无时间限制";
		var ques_show_endtime = temp.quesShowEndtime;
		var ques_show_begintime = temp.quesShowBegintime;
		if (ques_show_endtime != "" && ques_show_begintime == "")
			examPeriod = ques_show_endtime + " 以前所有";
		else if (ques_show_endtime == "" && ques_show_begintime != "")
			examPeriod = ques_show_begintime + " 以后所有";
		else if (ques_show_endtime != "" && ques_show_begintime != "") {
			examPeriod = ques_show_begintime + '<span style="margin-left:10px;margin-right:10px">至</span>'
					+ ques_show_endtime;
			if (!timeCompare(ques_show_begintime, ques_show_endtime)) {
				$('#tipMsg').html("时间区间选择错误！");
				return 0;
			}
		}
		var diff = "";
		if (temp.quesDifficulty == 0) {
			diff = "容易";
		} else if (temp.quesDifficulty == 1) {
			diff = "一般";
		} else if (temp.quesDifficulty == 2) {
			diff = "偏难";
		} else if (temp.quesDifficulty == 3) {
			diff = "困难";
		} else {
			diff = "未定义";
		}
		tempHtml += "<tr id='showPara'" + temp.quesTypeNo + "'><td>" + (i + 1) + "</td><td>" + questionType
				+ "</td><td>" + temp.quesInputNumber + "</td><td>" + temp.quesInputScore + "</td><td>" + diff + "</td>"
				+ "<td>" + examPeriod + "</td><td>" + "<a href='#' onclick = 'deleteItem(" + temp.quesTypeNo
				+ ")'>删除</a>" + "<a href='#' onclick = 'modifyItem(" + temp.quesTypeNo + ")'>修改</a></td></tr>";
	}
	$records.html(tempHtml);
}

// 修改一条记录
function modifyItem(no) {
	for ( var i = 0; i < storeArray.length; i++) {
		if (storeArray[i].quesTypeNo == no) {
			var temp = storeArray[i];
			$('#ques_type').val(temp.quesTypeIndex);
			$('#ques_num').val(temp.quesInputNumber);
			$('#ques_score').val(temp.quesInputScore);
			$('#ques_difficulty').val(temp.quesDifficulty);
			$('#show_begintime').val(temp.quesShowBegintime);
			$('#show_endtime').val(temp.quesShowEndtime);
			$('#tionType').modal('show');
			isModify = true;// 设置当前状态为修改
			modifyNo = no;// 设置为当前修改的序号
		}
	}
}

// 将添加的题型数组storeArray转换为json格式字符串
function getJson() {
	var arrDataStr = "[";
	for ( var i = 0; i < storeArray.length; i++) {
		var temp = storeArray[i];
		arrDataStr += "{\"quesTypeIndex\":\"" + temp.quesTypeIndex + "\",\"quesInputNumber\":\"" + temp.quesInputNumber
				+ "\",\"quesInputScore\":\"" + temp.quesInputScore + "\",\"quesDifficulty\":\"" + temp.quesDifficulty
				+ "\",\"quesShowBegintime\":\"" + temp.quesShowBegintime + "\",\"quesShowEndtime\":\""
				+ temp.quesShowEndtime + "\"}";
		if (i < storeArray.length - 1)
			arrDataStr += ",";
	}
	return (arrDataStr + ']');
}

function clearTips() {
	$('#submitTips').html("");
	$('#tipMsg').html("");
}
// 检查提交数据
function checkSubmit() {
	var totalScore = $('#exam_total_score').val();
	var currentScore = 0;
	for ( var i = 0; i < storeArray.length; i++) {
		currentScore += storeArray[i].quesInputScore * storeArray[i].quesInputNumber;
	}
	if (currentScore < totalScore) {
		$('#submitTips').html("选择的题型分数不够");
		setTimeout("clearTips()", 2000);
		return false;
	} else if (currentScore > totalScore) {
		$('#submitTips').html("选择的题型超过了所设置的分数");
		setTimeout("clearTips()", 2000);
		return false;
	} else {
		return true;
	}
}
// 开始组卷
function startCombinate() {
	// if (!compareTime($('#begintime').val().trim(),
	// $('#endtime').val().trim())) {
	// $('#submitTips').html("请选择合适的开始区间！");
	// setTimeout("clearTips()", 2000);
	// return;
	// }
	if ($('#exam_name').val().trim() == "") {
		$('#submitTips').html("请输入考试名称！");
		setTimeout("clearTips()", 2000);
		$('#exam_name').focus();
		return;
	}
	if ($('#exam_course').val() == -1) {
		$('#submitTips').html("请选择题库！");
		setTimeout("clearTips()", 2000);
		$('#exam_course').focus();
		return;
	}
	if (storeArray.length == 0) {
		$('#submitTips').html("请选择题目设置添加条件自动组卷");
		setTimeout("clearTips()", 2000);
		return;
	}
	if (hasSameName) {
		$('#exam_name').focus();
		return;
	}
	if (!scoreIsNum) {
		$('#exam_total_score').focus();
		return;
	}
	// if (!copiedIsNum) {
	// $('#exam_total_copies').focus();
	// return;
	// }
	// if (!timeIsNum) {
	// $('#examSpendTime').focus();
	// return;
	// }
	if (!checkSubmit()) {
		return;
	}
	var param = getJson();
	var sendParam = {
		"param" : param,
		"examName" : $('#exam_name').val().trim(),
		"examDescribe" : $("#exam_describe").val(),
		"examCourId" : $("#exam_course").val(),
		"examTotalScore" : $('#exam_total_score').val().trim() == "" ? 100 : $('#exam_total_score').val().trim()
	// "examSpendTime" : $('#examSpendTime').val().trim() == "" ? 90 :
	// $('#examSpendTime').val().trim()
	};
	$('#combinationTipsDialog').modal({
		keyboard : false,
		backdrop : false
	});
	$('#setQuestion').css("display", "none");
	$('#buttonArea').css("display", "none");
	$.post("../../handler/train/autoGroupPaper", sendParam, function(data) {
		$('#combinationTipsDialog').modal('hide');
		$('#setQuestion').css("display", "");
		$('#buttonArea').css("display", "");
		if (data.ret) {
			alert("组卷成功！将返回试卷列表！");
			window.location.href = "sysman_Paper.html?"+urlColHtml();
		} else {
			if (data.data.failFlag == 1) {
				alert("组卷参数有误！系统未能解析！请重新试一试！");
			} else if (data.data.failFlag == 2) {
				alert("您选了单选题参数有误！系统未能解析！请更正！");
			} else if (data.data.failFlag == 3) {
				alert("您选了多选题参数有误！系统未能解析！请更正！");
			} else if (data.data.failFlag == 4) {
				alert("您选了判断题参数有误！系统未能解析！请更正！");
			} else if (data.data.failFlag == 5) {
				alert("您选了填空题参数有误！系统未能解析！请更正！");
			} else if (data.data.failFlag == 6) {
				alert("您选了问答题参数有误！系统未能解析！请更正！");
			} else if (data.data.failFlag == 7) {
				alert("系统错误，请联系管理员！");
			} else if (data.data.failFlag == 8) {
				alert("数据保存出错，请联系管理员！");
			} else if (data.data.failFlag == 100) {
				alert("您输入参数有误！请检查！");
			} else {
				alert("未知错误，请联系管理员！");
			}
		}
	});
}

var judgeClick = 0;

function compareTime(timeB, timeE) {
	if (timeB == "" || timeE == "")
		return true;
	timeB = timeB.replaceAll("-", "/");
	timeE = timeE.replaceAll("-", "/");
	var dateB = new Date(Date.parse(timeB));
	var dateE = new Date(Date.parse(timeE));
	if (dateB < dateE)
		return true;
	else
		$('#submitTips').html("请正确选择或者输入考试开放时间区间");
}

function timeCompare(timeB, timeE) {
	if (timeB == "" || timeE == "")
		return true;
	timeB = timeB.replaceAll("-", "/");
	timeE = timeE.replaceAll("-", "/");
	var dateB = new Date(Date.parse(timeB));
	var dateE = new Date(Date.parse(timeE));
	if (dateE > dateB)
		return true;
	else
		return false;
}

// 替换所有正则表达式匹配的的方法
String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
};

// 格式化时间
Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	};
	if (/(Y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
	return format;
};

// 清除输入的考试名称等信息
function clearInfor() {
	$('#exam_name').val("");
	$('#exam_total_score').val("100");
	$('#endtime').val("");
}
// 设置考试结束时间
// function setEndTime() {
// var beginT = $('#begintime').val();
// if (!judgeTime(beginT)) {
// $('#beginTimeTips').html("<img src='../img/cancel.png'
// width='13px'>开放格式时间错误");
// $('#beginTimeTips').css("display", "");
// return;
// }
// if (!timeIsNum) {
// return;
// }
// var cTime = StringToDate(beginT);
// var addValue = $('#examSpendTime').val();
// cTime.setMinutes(cTime.getMinutes() + (addValue * 1));
// var cTimeStr = cTime.format('YYYY-MM-dd hh:mm:ss');
// $('#endtime').val(cTimeStr.substring(0, cTimeStr.length - 3));
// }
// 判断是否为时间格式的字符串
function judgeTime(str) {
	var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2})$/;
	var r = str.match(reg);
	if (r == null)
		return false;
	else
		return true;
}
// 将对应的字符串转换为Date类型
function StringToDate(dateStr) {
	var separator = "-";
	if (dateStr.indexOf("/") > -1)
		separator = "/";
	if (dateStr.indexOf(".") > -1)
		separator = ".";
	var converted = Date.parse(dateStr);
	var myDate = new Date(converted);
	if (isNaN(myDate)) {
		var times = dateStr.substring(dateStr.length - 5, dateStr.length);
		dateStr = dateStr.substring(0, dateStr.length - 6);
		var arys = dateStr.split(separator);
		var time = times.split(":");
		myDate = new Date(arys[0], arys[1] - 1, arys[2], time[0], time[1]);
	}
	return myDate;
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
		$("#exam_course").append(courseDropDown);
	});
}