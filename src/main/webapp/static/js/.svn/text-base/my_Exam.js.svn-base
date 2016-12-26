/**
 * 考试试卷
 * 
 * @author yangZq
 */
var m = 90; // 设置考试时间(分钟单位)
var TimeNum = m * 60;
var startFlag = false;
///是否提交
var issubmit = false;
//是否屏蔽键盘按钮
var isforbidden = false;
//考试编号
var examinId = 0;
//考试名称
var examinName = "";
//考试开始时间
var exstartTime = null;
//考试分数
var testscore = 100;
//考試答案
var answer = "";
//对应的分数
var score = "";
//试题编号
var original = "";
//试题类型
var examType = "";
//得分
var gainscore = 0;
//是否自动评卷,同时根据此标识决定试卷是否能完成isfinished
var autoScore = true;
//学生答题
var stuAnswer;
$(document).ready(function() {
	ShowColumn();// Show Column
	backToTop();// back To Top
	if($.cookie("StuMyExam")=="1"){
		alert("现在是考试中，禁止刷新考试页面，禁止同时打开两个考试页面！如有问题，请退出后重新登录或联系管理员。");
		$.cookie('StuMyExam','0');//有数据时关闭当前窗口
		closeit();
	}
	$.cookie('StuMyExam', '');//初始化cookie
	$('#modal').modal('show');
	// 隱藏考試時間
	$('#testTime').hide();
	$("#grade").hide();
	$("#backButton").hide();
	// back To Top
	backToTop();
	// 考試須知
	var examInfoHtml = "<div class='alert alert-info'><h1 class='text-center'>考试须知!</h1><strong>注意!</strong>";
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : '../../handler/sypaController/attainValueByEnName',
		data : {
			"enName" : "examInfo"
		},
		dataType : 'json',
		success : function(data) {
			if (data.ret) {
				var info = data.data.value;
				examInfoHtml += info;
			}
		}
	});
	examInfoHtml += "<div class='text-center'><a id='beginTest' class='btn btn-success' type='button'>开始考试</a>"
		+ "&nbsp;&nbsp;&nbsp;&nbsp;<a href='my_Test.html?" + urlColHtml()
		+ "' class='btn' type='button'>返回</a></div></div>";
	$("#mainTable").html(examInfoHtml);
	// 获取考试试卷数据
	viewExamPaper();
	// 当前时间
	tick();
	// 交卷按钮
	$('#subPaper').click(function() {
		issubmit = true;
		$('#remainingTime').html("<span style=\"color:#FF0000;\">考试结束</span>");
		alert("已经提交试卷！若全是客观题，您可以点击“评分按钮”查看自己成绩！！");
		testscore = GetScore();
		$("#subPaper").attr("disabled", "disabled");
		$("#mainTable select").attr("disabled", "disabled");
		$("#mainTable input").attr("disabled", "disabled");
		$("#mainTable textarea").attr("disabled", "disabled");
		// 若能自动评分则可以按此按钮
		if (autoScore) {
			$("#grade").show();
		}
		$("#backButton").show();
		// 提交
		submitResult();
		return false;
	});
	// 评分按钮
	$('#grade').click(function() {
		if (issubmit) {
			alert("您的得分：" + gainscore + "分！！！");
		} else
			alert("请先提交试卷提交！！！");
		// /已经阅读
		return false;
	});
	//返回
	$("#backButton").click(function() {
		window.location = "my_Test.html?firstCol=4&secondCol=24";
	});
	// 初始化
	stuAnswer = {
			"userId" : userId,
			"examinId" : examinId,
			"startTime" : null,
			"submitTime" : null,
			"totalScore" : gainscore,
			"solveArr" : new Array(),
			"isfinished" : true,
			"examinName" : examinName,
			"courseId" : 0
	};
});

/**
 * 关闭窗口
 */
function closeit(){
	//FF中需要修改配置window.close方法才能有作用，为了不需要用户去手动修改，所以用一个空白页面显示并且让后退按钮失效
	//Opera浏览器旧版本(小于等于12.16版本)内核是Presto，window.close方法有作用，但页面不是关闭只是跳转到空白页面，后退按钮有效，也需要特殊处理
	var userAgent = navigator.userAgent;
	if (userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Presto") != -1) {
		window.location.replace("about:blank");
	} else {
		window.opener = null;
		window.open("", "_self");
		window.close();
		location.reload();
		// $.cookie('fileUploadStu','');
	}
}
//加载试卷的信息与卷面题目
function viewExamPaper() {
	var paperId = getRequest("paperId");
	examinId = getRequest("examinId");
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : '../../handler/train/viewExamPaper',
		data : {
			paperId : paperId,
			examinId : examinId
		},
		dataType : 'json',
		success : function(data) {
			if (data.ret) {
				var query = data.data.pageData;
				if (query.f == "f") {
					alert(query.msg);
				} else {
					TimeNum = query.examInfo.exinTotaltime * 60;
					examinName = query.examInfo.exinName;
					var examPaper = query.examPaper;
					// 加载试卷信息
					loadPaperInfo(examPaper);
					var examQueList = query.examQueList;
					var singel_num = query.singel_num;
					var multi_num = query.multi_num;
					var judge_num = query.judge_num;
					var fill_num = query.fill_num;
					var answer_num = query.answer_num;
					$("#beginTest").click(
							function() {
								$.post("../../handler/train/logBeginTest", {
									"testName" : examinName
								}, function(data) {
									if (data.ret) {
										startFlag = true;
										// 是否禁止刷新
										isforbidden = true;
										$('#testTime').show();
										loadPaperDetail(examQueList, singel_num, multi_num, judge_num, fill_num,
												answer_num, query.examInfo);
										// 剩余时间显示
										ChangeTime();
										exstartTime = new Date(); // 当前时间
									} else {
										alert("日志记录错误!");
									}
								});
								if($.cookie("StuMyExam") == null || $.cookie("StuMyExam") == ""){
									$.cookie('StuMyExam', '1'); //控制上考试界面 唯一开启!!没有数据时，无需关闭。
								} 	
							});
					// 加载试题信息
				}
			} else {
				alert(data.errmsg);
			}
		}
	});
}

//加载试卷信息
function loadPaperInfo(data) {
	var difficult = "";
	if (data.expaDifficulty == 0) {
		difficult = "容易";
	} else if (data.expaDifficulty == 1) {
		difficult = "一般";
	} else if (data.expaDifficulty == 2) {
		difficult = "偏难";
	} else if (data.expaDifficulty == 3) {
		difficult = "困难";
	} else {
		difficult = "未定义";
	}
	var paperHtml = "<p>试卷名字：<span style=\"color:#FF0000;\">" + data.expaName
	+ "</span></p><p>试卷总分：<span style=\"color:#FF0000;\">" + data.expaTotalscore
	+ "分</span></p><p>考试时间：<span style=\"color:#FF0000;\">" + TimeNum / 60
	+ "分钟</span></p><p>试卷描述：<span style=\"color:#FF0000;\">" + data.expaDescribe
	+ "</span></p><p>试卷难度：<span style=\"color:#FF0000;\">" + difficult + "</span></p>";
	$("#testInfo").html(paperHtml);
}

//加载试卷试题
function loadPaperDetail(data, singel_num, multi_num, judge_num, fill_num, answer_num, examInfo) {
	var header ="" ;/*"<ul class='breadcrumb'>"
		+ "<li><a href='my_Test.html?firstCol=4&secondCol=24'>测试练习</a></li><span class='divider'>/</span></li class='active'>在线考试</li></ul>";*/
	var singleHtml = "";
	var multiHtml = "";
	var judgeHtml = "";
	var fillHtml = "";
	var answerHtml = "";
	var headNum = 1;
	if (singel_num > 0) {
		singleHtml = "<div class='well'><h5 class='badge badge-important'>" + headNum + "、单选(选择题 共" + singel_num
		+ "题)</h5>";// 单选
		headNum++;
	}
	if (multi_num > 0) {
		multiHtml = "<div class='well'><h5 class='badge badge-important'>" + headNum + "、多选(多选题 共" + multi_num
		+ "题)</h5>";// 多选
		headNum++;
	}
	if (judge_num > 0) {
		judgeHtml = "<div class='well'><h5 class='badge badge-important'>" + headNum + "、判断(判断题 共" + judge_num
		+ "题)</h5>";// 判断
		headNum++;
	}
	if (fill_num > 0) {
		fillHtml = "<div class='well'><h5 class='badge badge-important'>" + headNum + "、填空(填空题 共" + fill_num
		+ "题)</h5>";// 填空
		headNum++;
	}
	if (answer_num > 0) {
		answerHtml = "<div class='well'><h5 class='badge badge-important'>" + headNum + "、问答(问答题 共" + answer_num
		+ "题)</h5>";// 问答题
	}
	var intI = 1;
	$.each(data, function(index, item) {
		var options = "A";
		var code = options.charCodeAt(0);// 转换成ASCII码
		// 单选
		if (item.exType == 1) {
			// 拼装正确答案
			answer += item.correctAnswer + ",";
			// 拼装分值
			score += item.score + ",";
			// 拼装试题编号
			original += item.exOrdinal + ",";
			// 拼装试题类型
			examType += item.exType + ",";
			singleHtml += "<dl><span class='qk' id='" + item.exOrdinal + "'></span>"
			+ "<dt><span class='badge badge-inverse'>" + intI + "</span>." + item.exDescribe
			+ "<span class='label'>(分值:" + item.score + "分)</span></dt>";
			// 选项
			$.each(item.options, function(index, option) {
				var testOption = String.fromCharCode(code);
				singleHtml += "<dd><label class='radio'>" + " <input type='radio' name='question_" + item.exOrdinal + "' id='" + item.exOrdinal + "_"
				+ option.optiOrdinal + "' value='" + option.optiOrdinal + "' class='qclazz'/>"+ testOption + "." + option.optiDescribe
				+"</label></dd>";
				code++;
			});
			singleHtml += "</dl>";
		}
		// 多选
		else if (item.exType == 2) {
			// 拼装正确答案
			answer += item.correctAnswer + ",";
			// 拼装分值
			score += item.score + ",";
			// 拼装试题编号
			original += item.exOrdinal + ",";
			// 拼装试题类型
			examType += item.exType + ",";
			multiHtml += "<dl><span class='qk' id='" + item.exOrdinal + "'></span>"
			+ "<dt><span class='badge badge-inverse'>" + intI + "</span>." + item.exDescribe
			+ "<span class='label'>(分值:" + item.score + "分)</span></dt>";
			// 选项
			$.each(item.options, function(index, option) {
				var testOption = String.fromCharCode(code);
				multiHtml += "<dd><label class='checkbox'>" + testOption + "." + option.optiDescribe
				+ " <input type='checkbox' name='question_" + item.exOrdinal + "' id='" + item.exOrdinal + "_"
				+ option.optiOrdinal + "' value='" + option.optiOrdinal + "' class='qclazz'/></label></dd>";
				code++;
			});
			multiHtml += "</dl>";
		}
		// 判断
		else if (item.exType == 3) {
			// 拼装正确答案
			answer += item.correctAnswer + ",";
			// 拼装分值
			score += item.score + ",";
			// 拼装试题编号
			original += item.exOrdinal + ",";
			// 拼装试题类型
			examType += item.exType + ",";
			judgeHtml += "<dl><span class='qk' id='" + item.exOrdinal + "'></span>"
			+ "<dt><span class='badge badge-inverse'>" + intI + "</span>." + item.exDescribe
			+ "<span class='label'>(分值:" + item.score + "分)</span></dt>";
			judgeHtml += "<dd><label class='radio' >对 <input type='radio' name='question_" + item.exOrdinal
			+ "' id='yes_" + item.exOrdinal + "' value='1' class='qclazz'/></label></dd>";
			judgeHtml += "<dd><label class='radio'>错 <input type='radio' name='question_" + item.exOrdinal
			+ "' id='no_" + item.exOrdinal + "' value='0' class='qclazz'/></label></dd>";
			judgeHtml += "</dl>";
		}
		// 填空
		else if (item.exType == 4) {
			// 拼装试题编号
			original += item.exOrdinal + ",";
			// 拼装试题类型
			examType += item.exType + ",";
			autoScore = false;
			fillHtml += "<dl><span class='qk' id='" + item.exOrdinal + "'></span>"
			+ "<dt><span class='badge badge-inverse'>" + intI + "</span>." + item.exDescribe
			+ "<span class='label'>(分值:" + item.score + "分)</span></dt>";
			fillHtml += "<dd><label><input type=text name='question_" + item.exOrdinal + "' " + "id=fill_"
			+ item.exOrdinal + " class='fillBlank'/></label></dd>";
			fillHtml += "</dl>";
		}
		// 问答
		else if (item.exType == 5) {
			// 拼装试题编号
			original += item.exOrdinal + ",";
			// 拼装试题类型
			examType += item.exType + ",";
			autoScore = false;
			answerHtml += "<dl><span class='qk' id='" + item.exOrdinal + "'></span>"
			+ "<dt><span class='badge badge-inverse'>" + intI + "</span>." + item.exDescribe
			+ "<span class='label'>(分值:" + item.score + "分)</span></dt>";
			answerHtml += "<dd><label><textarea rows='5' type=text name='question_" + item.exOrdinal + "' " + "id=ans_"
			+ item.exOrdinal + " class='fillBlank'/></label></dd>";
			answerHtml += "</dl>";
		}
		intI++;
	});
	singleHtml += "</div>";
	multiHtml += "</div>";
	judgeHtml += "</div>";
	fillHtml += "</div>";
	answerHtml += "</div>";
	$("#mainTable").html(header + singleHtml + multiHtml + judgeHtml + fillHtml + answerHtml);
	// 快速跳转的生成
	bindFastToBar(examInfo);
}

//获得用户答案,所有单选和多选
function getUserKey(n) {
	var Check = "";
	var checkedObj = $("[name='" + n + "']:checked");
	checkedObj.each(function() {
		var isCheck = $(this).val() + "";
		Check += isCheck;
	});
	return Check;
}

//绑定快速跳转题目工具
function bindFastToBar(examInfo) {
	var n = 1;
	var html = "<p>快速跳转：</p>";
	$(".qk").each(
			function(i) {
				var td_key_id = $(this).attr("id");
				html += '<a href="javascript:;" style="height:20px;width:20px" id="fastto_question_' + td_key_id + '" onclick="moveto('
				+ $(this).parent().offset().top + ')">' + n + '</a>';
				n++;
			});
	$("#div_processor_fastto").html(html);
	$("#exinBgtime").html("开始时间：" + examInfo.exinBgtime);
	$("#exinEdtime").html("结束时间：" + examInfo.exinEdtime);
	// 单选复选的快速标示
	$(".qclazz").click(function() {
		var thename = $(this).attr("name");
		if (getUserKey(thename) != '') {
			$("#fastto_" + thename).attr("class", "finished");
		} else {
			$("#fastto_" + thename).attr("class", "");
		}
	});

	// 文本框的快速标示
	$(".fillBlank").keyup(function() {
		var thename = $(this).attr("name");
		if ($(this).val() != null && $(this).val() != "") {
			$("#fastto_" + thename).attr("class", "finished");
		} else {
			$("#fastto_" + thename).attr("class", "");
		}
	});

	// // 填空题的快速提示
	// $(".qinfo .qclazz").keyup(function() {
	// var thename = $(this).attr("name");
	// if (BlankQuestionAllFilled(thename)) {
	// $("#fastto_" + thename).attr("class", "finished");
	// } else {
	// $("#fastto_" + thename).attr("class", "");
	// }
	// });
}
function tick() {
	var $Clock = $('#Clock');
	var years, months, days, hours, minutes, seconds;
	var intYears, intMonths, intDays, intHours, intMinutes, intSeconds;
	var today;
	today = new Date(); // 系统当前时间
	intYears = today.getFullYear(); // 得到年份,getFullYear()比getYear()更普适
	intMonths = today.getMonth() + 1; // 得到月份，要加1
	intDays = today.getDate(); // 得到日期
	intHours = today.getHours(); // 得到小时
	intMinutes = today.getMinutes(); // 得到分钟
	intSeconds = today.getSeconds(); // 得到秒钟
	years = intYears + "年";
	if (intMonths < 10) {
		months = "0" + intMonths + "月";
	} else {
		months = intMonths + "月";
	}
	if (intDays < 10) {
		days = "0" + intDays + "日 ";
	} else {
		days = intDays + " ";
	}

	if (intHours == 0) {
		hours = "00:";
	} else if (intHours < 10) {
		hours = "0" + intHours + ":";
	} else {
		hours = intHours + ":";
	}
	if (intMinutes < 10) {
		minutes = "0" + intMinutes + ":";
	} else {
		minutes = intMinutes + ":";
	}

	if (intSeconds < 10) {
		seconds = "0" + intSeconds + " ";
	} else {
		seconds = intSeconds + " ";
	}
	timeString = years + months + days + hours + minutes + seconds;
	$Clock.html(timeString);
	window.setTimeout("tick();", 1000);
}

//开始考试后禁用键盘的刷新和退格键
$(document).keydown(function(event) {
	if (isforbidden) {
		switch (event.keyCode) {
		// case 8:
		// return false; // 屏蔽退格删除键
		case 82:
			return false;// 屏蔽Ctrl+R
		case 116:
			return false;// 屏蔽F5刷新键
		default:
			return true;
		}
		document.oncontextmenu = new Function("event.returnValue=false;");
	}
});
//开始考试后禁用鼠标右键防止刷新
$(document).bind("contextmenu", function() {
	if (isforbidden) {
		return false;
	}
});
function ChangeTime() {
	var $remainingTime = $('#remainingTime');
	if (startFlag == true && issubmit == false) {
		TimeNum--;
		if (TimeNum > 0) {
			timeStr = setTimeout("ChangeTime()", 1000);
			if (TimeNum == 300) {
				alert('距离系统自动提交时间还剩5分钟，请尽快做好提交准备');
			}
			if (TimeNum == 30) {
				alert('距离系统自动提交时间还剩30秒，请马上交卷');
			}
		} else {
			// 系统提交
			// GetScore();
			startFlag = false;
			$testtime.html("<span style=\"color:#FF0000;\">考试结束</span>");
			// 交卷
			submitResult();
			window.location = "my_Test.html?" + urlColHtml();
		}
		$remainingTime.html("系统提示：剩余时间<span style=\"color:#FF0000;\">" + Math.floor(TimeNum / 60) + "分" + TimeNum % 60
				+ "秒</span>");
	}
}

var solveStr = new Array();
//获取考试获得分数
function GetScore() {
	// 试题
	var originals = original.split(",");
	// 类型
	var examTypes = examType.split(",");
	// 答案
	var answers = answer.split(",");
	// 分值
	var scores = score.split(",");

	// 遍历试题
	for ( var i = 0; i < originals.length; i++) {
		// 学生答题
		var stuAnswerValue = "";
		if (originals[i] != "") {
			// 拼接题号，题型
			stuAnswerValue += '{"ordinal":' + originals[i] + ',"examType":' + examTypes[i] + ',"answer":';
			// 客观题
			if (examTypes[i] == 1 || examTypes[i] == 2 || examTypes[i] == 3) {
				var stuAnswer = GetRadioValue("question_" + originals[i]);
				// 拼接答案
				stuAnswerValue += '"' + stuAnswer + '","score":';
				if (stuAnswer == answers[i]) {
					gainscore = gainscore + parseInt(scores[i]);
					stuAnswerValue += scores[i] + "}";
				} else if (examTypes[i] == 2 && answers[i].indexOf(stuAnswer) >= 0) {
					// 如果是多选,答案又不完全相同
					gainscore = gainscore + parseInt(scores[i]) / 2;
					stuAnswerValue += parseInt(scores[i]) / 2 + "}";
				} else {
					stuAnswerValue += "0}";
				}
			}
			// 主观题
			else {
				var textValue = getTextValue("question_" + originals[i]);
				stuAnswerValue += '"' + textValue + '","score":';
				stuAnswerValue += "0}";
			}
		}
		if (stuAnswerValue != "") {
			solveStr.push(stuAnswerValue);
		}
	}
}

//radio checkbox获取值
function GetRadioValue(RadioName) {
	var obj;
	obj = document.getElementsByName(RadioName);
	if (obj != null) {
		var i;
		var result = "";
		for (i = 0; i < obj.length; i++) {
			if (obj[i].checked) {
				result += obj[i].value;
			}
		}
		return result;
	}
	return null;
}

//获得主观题值
function getTextValue(inputName) {
	var obj;
	obj = document.getElementsByName(inputName);
	if (obj != null) {
		var i;
		var result = "";
		for (i = 0; i < obj.length; i++) {
			if (obj[i].value != null) {
				result += obj[i].value;
			}
		}
		return result;
	}
	return null;
}

//提交考试结果
function submitResult() {
	// 初始化学生提交数据
	stuAnswer.startTime = exstartTime;
	stuAnswer.totalScore = gainscore;// 总分
	stuAnswer.submitTime = new Date();
	stuAnswer.solveArr = solveStr.toString();
	stuAnswer.isfinish = autoScore;
	stuAnswer.examinName = examinName;
	stuAnswer.courseId = getRequest("courseId");
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : '../../handler/train/submitResult',
		data : stuAnswer,
		dataType : 'json',
		success : function(data) {
			if (data.ret) {
				var f = data.data.pageData.f;
				if (f == "s") {
					alert("提交成功！");
				} else {
					alert("提交失败！数据出错！");
				}
				$.cookie("StuMyExam", null, {
					path : '/'  //考试期间判定是否重复打开浏览器
				});
			}
		}
	});
}