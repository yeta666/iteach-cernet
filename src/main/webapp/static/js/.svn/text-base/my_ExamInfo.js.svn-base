/**
 * 考试试卷
 * 
 * @author yangZq
 */
var m = 90; // 设置考试时间(分钟单位)
var TimeNum = m * 60;
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
//是否自动评卷
var autoScore = true;
//学生答题
var stuAnswer;
$(document).ready(function() {
	ShowColumn(); // Show Column
	backToTop();// back To Top
	viewExamPaper();// 获取考试试卷数据
});
//加载试卷的信息与卷面题目
function viewExamPaper() {
	var paperId = getRequest("paperId");
	examinId = getRequest("examinId");
	$
	.ajax({
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
					startFlag = true;
					// 是否禁止刷新
					isforbidden = true;
					$('#testTime').show();
					loadPaperDetail(examQueList, singel_num, multi_num, judge_num, fill_num, answer_num,
							query.examInfo);
					exstartTime = new Date(); // 当前时间
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
	+ "</span>&nbsp;&nbsp;试卷总分：<span style=\"color:#FF0000;\">" + data.expaTotalscore
	+ "分</span>&nbsp;&nbsp;考试时间：<span style=\"color:#FF0000;\">" + TimeNum / 60
	+ "分钟</span>&nbsp;&nbsp;试卷描述：<span style=\"color:#FF0000;\">" + data.expaDescribe
	+ "</span>&nbsp;&nbsp;试卷难度：<span style=\"color:#FF0000;\">" + difficult + "</span></p>";
	$("#examInfo").append(paperHtml);
}

//加载试卷试题
function loadPaperDetail(data, singel_num, multi_num, judge_num, fill_num, answer_num, examInfo) {
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
			var correctOrder = parseInt(item.correctAnswer) + 64;
			var correctOption = String.fromCharCode(correctOrder);
			singleHtml += "<dl><span class='qk' id='" + item.exOrdinal + "'></span>"
			+ "<dt><span class='badge badge-inverse'>" + intI + "</span>." + item.exDescribe
			+ "<span class='label'>(分值:" + item.score
			+ "分)</span>&nbsp;&nbsp;<span class='label label-warning'>正确答案：" + correctOption + "</span></dt>";
			// 选项
			$.each(item.options, function(index, option) {
				var testOption = String.fromCharCode(code);
				singleHtml += "<label class='radio'>" + testOption + "." + option.optiDescribe
				+ " <input type='radio' name='question_" + item.exOrdinal + "' id='optionId" + option.optiId
				+ "' value='" + option.optiOrdinal + "' class='qclazz'/></label>";
				code++;
			});
			singleHtml += "</dl>";
		}
		// 多选
		else if (item.exType == 2) {
			var correctOrders = item.correctAnswer.split("");
			var correctOptions = "";
			for ( var i = 0; i < correctOrders.length; i++) {
				var correctOrder = parseInt(correctOrders[i]) + 64;
				correctOptions += String.fromCharCode(correctOrder);
			}
			multiHtml += "<dl><span class='qk' id='" + item.exOrdinal + "'></span>"
			+ "<dt><span class='badge badge-inverse'>" + intI + "</span>." + item.exDescribe
			+ "<span class='label'>(分值:" + item.score
			+ "分)</span>&nbsp;&nbsp;<span class='label label-warning'>正确答案：" + correctOptions + "</span></dt>";
			// 选项
			$.each(item.options, function(index, option) {
				var testOption = String.fromCharCode(code);
				multiHtml += "<dd><label class='checkbox'>" 
				+ " <input type='checkbox' name='question_" + item.exOrdinal + "' id='optionId" + option.optiId
				+ "' value='" + option.optiOrdinal + "' class='qclazz'/>"  + testOption + "." + option.optiDescribe+"</label></dd>";
				code++;
			});
			multiHtml += "</dl>";
		}
		// 判断
		else if (item.exType == 3) {
			judgeHtml += "<dl><span class='qk' id='" + item.exOrdinal + "'></span>"
			+ "<dt><span class='badge badge-inverse'>" + intI + "</span>." + item.exDescribe
			+ "<span class='label'>(分值:" + item.score
			+ "分)</span>&nbsp;&nbsp;<span class='label label-warning'>正确答案："
			+ (item.correctAnswer == 1 ? "对" : "错") + "</span></dt>";
			judgeHtml += "<label class='radio' >对 <input type='radio' name='question_" + item.exOrdinal + "' id='yes_"
			+ item.exquId + "' value='1' class='qclazz'/></label>";
			judgeHtml += "<label class='radio'>错 <input type='radio' name='question_" + item.exOrdinal + "' id='no_"
			+ item.exquId + "' value='0' class='qclazz'/></label>";
			judgeHtml += "</dl>";
		}
		// 填空
		else if (item.exType == 4) {
			fillHtml += "<dl><span class='qk' id='" + item.exOrdinal + "'></span>"
			+ "<dt><span class='badge badge-inverse'>" + intI + "</span>." + item.exDescribe
			+ "<span class='label'>(分值:" + item.score + "分)</span><br><span class='label label-warning'>正确答案："
			+ item.correctAnswer + "</span></dt>";
			fillHtml += "<label><input type=text class='span10' name='question_" + item.exOrdinal + "' " + "id=blankId"
			+ item.exquId + " class='fillBlank'/></label>";
			fillHtml += "</dl>";
		}
		// 问答
		else if (item.exType == 5) {
			answerHtml += "<dl><span class='qk' id='" + item.exOrdinal + "'></span>"
			+ "<dt><span class='badge badge-inverse'>" + intI + "</span>." + item.exDescribe
			+ "<span class='label'>(分值:" + item.score + "分)</span><br><span class='label label-warning'>正确答案："
			+ item.correctAnswer + "</span></dt>";
			answerHtml += "<label><textarea rows='5' class='span10' type=text name='question_" + item.exOrdinal + "' "
			+ "id=answerId" + item.exquId + " class='fillBlank'/></label>";
			answerHtml += "</dl>";
		}
		intI++;
	});
	singleHtml += "</div>";
	multiHtml += "</div>";
	judgeHtml += "</div>";
	fillHtml += "</div>";
	answerHtml += "</div>";
	$("#examInfoTable").html(singleHtml + multiHtml + judgeHtml + fillHtml + answerHtml);
	// 加载我的测试考试结果
	bindMyExamResult(examInfo.exinId);
	// 快速跳转的生成
	bindFastToBar(examInfo);
}

//加载我的测试考试结果
function bindMyExamResult(exinId) {
	var resultHtml = "没有相关数据！";
	$.post("../../handler/train/loadExamResult", {
		"exinId" : exinId,
		"examinName" : examinName,
		"userId" : userId,
		"postCol" : 1
	}, function(data) {
		if (data.ret) {
			var resultData = data.data.result;
			var answerData = resultData.solves;
			resultHtml = "<p>得分：<span style=\"color:#FF0000;\">" + resultData.totalScore
			+ "分</span>&nbsp;&nbsp;&nbsp;&nbsp;开考时间：<span style=\"color:#FF0000;\">"
			+ resultData.startTimeFormat
			+ "</span>&nbsp;&nbsp;&nbsp;&nbsp;交卷时间：<span style=\"color:#FF0000;\">"
			+ resultData.submitTimeFormat + "</span></p>";
			if (answerData != null && answerData.length > 0) {
				loadMyAnswer(answerData);
			} else {
				$("#examInfoTable").html("<div class='alert alert-success'><h3>数据为导入数据，沒有您的答题记录！</h3></div>");
			}
		}
	});
	$("#resultInfo").append(resultHtml);
}

//绑定快速跳转题目工具
function bindFastToBar(examInfo) {
	var n = 1;
	var html = "<span class='badge badge-important'>快速跳转：</span ><br>";
	$(".qk").each(
			function(i) {
				var td_key_id = $(this).attr("id");
				html += '<a href="javascript:;" id="fastto_question_' + td_key_id + '" onclick="moveto('
				+ $(this).parent().offset().top + '-80)">' + n + '</a>';
				n++;
			});
	html += "<br/>";
	$("#myExamInfo").html(html);
	// 单选复选的快速标示
	// $(".qclazz").click(function() {
	// var thename = $(this).attr("name");
	// if (getUserKey(thename) != '') {
	// $("#fastto_" + thename).attr("class", "finished");
	// } else {
	// $("#fastto_" + thename).attr("class", "");
	// }
	// });
	// 文本框的快速标示
	// $(".fillBlank").keyup(function() {
	// var thename = $(this).attr("name");
	// if ($(this).val() != null && $(this).val() != "") {
	// $("#fastto_" + thename).attr("class", "finished");
	// } else {
	// $("#fastto_" + thename).attr("class", "");
	// }
	// });
	// 填空题的快速提示
	// $(".qinfo .qclazz").keyup(function() {
	// var thename = $(this).attr("name");
	// if (BlankQuestionAllFilled(thename)) {
	// $("#fastto_" + thename).attr("class", "finished");
	// } else {
	// $("#fastto_" + thename).attr("class", "");
	// }
	// });
}

function display() {
	document.getElementById("box").style.display = "block";
}
function disappear() {
	document.getElementById("box").style.display = "none";
}
function loadMyAnswer(answerData) {
	$.each(answerData, function(index, option) {
		var optionOrder = option.solvOrdinal;
		var solvScore = option.solvScore;
		$('input[name="question_' + optionOrder + '"]').each(
				function() {
					$(this).attr("disabled", "disabled");
					if (this.type == "radio" || this.type == "checkbox") {
						var answer = option.solvAnswer.split("");
						for ( var i = 0; i < answer.length; i++) {
							if (this.value == answer[i]) {
								$(this).attr("checked", "checked");
								$(this).parent().parent().append(
										"<span class='label label-info'>得分：" + solvScore + "</span>");
							}
						}
					} else if (this.type == "text") {
						var answer = option.solvAnswer;
						$(this).val(answer);
						$(this).parent().append(
								"<span class='label label-info'>得分：" + solvScore + "</span>");
					}
				});
		$('textarea[name="question_' + optionOrder + '"]').each(function() {
			$(this).attr("disabled", "disabled");
			if (this.type == "textarea") {
				var answer = option.solvAnswer;
				$(this).text(answer);
				$(this).parent().append(
						"<span class='label label-info'>得分：" + solvScore + "</span>");
			}
		});
	});
}