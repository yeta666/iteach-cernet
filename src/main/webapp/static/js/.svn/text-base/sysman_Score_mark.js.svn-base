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
var rateId=0;
$(document).ready(function() {
	ShowColumn(); // Show Column
	backToTop();// back To Top
	viewExamPaper();// 获取考试试卷数据
});
//加载试卷的信息与卷面题目
function viewExamPaper() {
	var paperId = getRequest("paperId");
	examinId = getRequest("examinId");
	$.ajax({type : 'post',
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
	+ "</span><br>试卷总分：<span style=\"color:#FF0000;\">" + data.expaTotalscore
	+ "分</span><br>考试时间：<span style=\"color:#FF0000;\">" + TimeNum / 60
	+ "分钟</span><br>试卷描述：<span style=\"color:#FF0000;\">" + data.expaDescribe
	+ "</span><br>试卷难度：<span style=\"color:#FF0000;\">" + difficult + "</span></p>";
	$("#examInfo").append(paperHtml);
}

//加载试卷试题
function loadPaperDetail(data, singel_num, multi_num, judge_num, fill_num, answer_num, examInfo) {
	$.ajaxSettings.async = false;
	var singleHtml = "";
	var multiHtml = "";
	var judgeHtml = "";
	var fillHtml = "";
	var answerHtml = "";
	var headNum = 1;
	if (singel_num > 0) {
		singleHtml = "<div class='row'><h3 class='label label-danger'>" + headNum + "、单选(选择题 共" + singel_num
		+ "题)</h3>";// 单选
		headNum++;
	}
	if (multi_num > 0) {
		multiHtml = "<div class='row'><h4 class='label label-danger'>" + headNum + "、多选(多选题 共" + multi_num
		+ "题)</h4>";// 多选
		headNum++;
	}
	if (judge_num > 0) {
		judgeHtml = "<div class='row'><h4 class='label label-danger'>" + headNum + "、判断(判断题 共" + judge_num
		+ "题)</h4>";// 判断
		headNum++;
	}
	if (fill_num > 0) {
		fillHtml = "<div class='row'><h4 class='label label-danger'>" + headNum + "、填空(填空题 共" + fill_num
		+ "题)</h4>";// 填空
		headNum++;
	}
	if (answer_num > 0) {
		answerHtml = "<div class='row'><h4 class='label label-danger'>" + headNum + "、问答(问答题 共" + answer_num
		+ "题)</h4>";// 问答题
	}
	var intI = 1;
	$.each(data,function(index, item) {
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
			var correctOrder = parseInt(item.correctAnswer) + 64;
			var correctOption = String.fromCharCode(correctOrder);
			singleHtml += "<dl><span class='qk' id='" + item.exOrdinal + "'></span>"
			+ "<dt style=\"margin-top: 40px;\"><span class='label label-warning'>" + intI + "</span>." + item.exDescribe
			+ "<span class='label'>(分值:" + item.score
			+ "分)</span>&nbsp;&nbsp;<span class='label label-danger'>正确答案：" + correctOption
			+ "</span></dt>";
			// 选项
			$.each(item.options, function(index, option) {
				var testOption = String.fromCharCode(code);
				singleHtml += "<label class='radio' style=\"padding-left: 22px;\">"
				+ " <input type='radio' name='question_" + item.exOrdinal + "' id='optionId"
				+ option.optiId + "' value='" + option.optiOrdinal + "' class='qclazz'/>" + testOption + "." + option.optiDescribe+"</label>";
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
			var correctOrders = item.correctAnswer.split("");
			var correctOptions = "";
			for ( var i = 0; i < correctOrders.length; i++) {
				var correctOrder = parseInt(correctOrders[i]) + 64;
				correctOptions += String.fromCharCode(correctOrder);
			}
			multiHtml += "<dl><span class='qk' id='" + item.exOrdinal + "'></span>"
			+ "<dt><span class='badge badge-inverse'>" + intI + "</span>." + item.exDescribe
			+ "<span class='label'>(分值:" + item.score
			+ "分)</span>&nbsp;&nbsp;<span class='label label-important'>正确答案：" + correctOptions
			+ "</span></dt>";
			// 选项
			$.each(item.options, function(index, option) {
				var testOption = String.fromCharCode(code);
				multiHtml += "<label class='checkbox'>" + testOption + "." + option.optiDescribe
				+ " <input type='checkbox' name='question_" + item.exOrdinal + "' id='optionId"
				+ option.optiId + "' value='" + option.optiOrdinal + "' class='qclazz'/></label>";
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
			+ "<span class='label'>(分值:" + item.score
			+ "分)</span>&nbsp;&nbsp;<span class='label label-important'>正确答案："
			+ (item.correctAnswer == 1 ? "对" : "错") + "</span></dt>";
			judgeHtml += "<label class='radio' >对 <input type='radio' name='question_" + item.exOrdinal
			+ "' id='yes_" + item.exquId + "' value='1' class='qclazz'/></label>";
			judgeHtml += "<label class='radio'>错 <input type='radio' name='question_" + item.exOrdinal
			+ "' id='no_" + item.exquId + "' value='0' class='qclazz'/></label>";
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
			fillHtml += "<label><input type=text class='span10' name='question_" + item.exOrdinal + "' "
			+ "id=blankId" + item.exquId + " class='fillBlank'/></label><span class='markScoreSpan'>"
			+ "评分：<input type=text id='markScore_"+item.exOrdinal+"' name='markScore_"+item.score+"_"+item.exOrdinal+"' value='' class='markScore'/>" +
			" 请填写一个小于或等于分值的数</span>";
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
			answerHtml += "<label><textarea rows='5' class='span10' type=text name='question_" + item.exOrdinal
			+ "' " + "id=answerId" + item.exquId + " class='fillBlank'/></label><span class='markScoreSpan'>"
			+"评分：<input type=text id='markScore_"+item.exOrdinal+"' name='markScore_"+item.score+"_"+item.exOrdinal+"' value='' class='markScore'/>" +
			" 请填写一个小于或等于分值的数</span>";
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
	var isFinished = getRequest("isFinished");
	if(isFinished == 0){
		var markScoreBtn = "<button onclick='markTestScore()' class='btn btn-primary'>评卷</button>";
		$("#autoMarkScore").html(markScoreBtn);
	}
	var returnBtn = "<button onclick='history.go(-1)' class='btn btn-warning'>返回</button>";
	$("#autoMarkScore").append(returnBtn);
	if(isFinished == 1){
		$(".markScoreSpan").each(function(){
			$(this).hide();
		});
	}
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
		"userId" : getRequest("userId"),
		"postCol":2
	}, function(data) {
		if (data.ret) {
			var resultData = data.data.result;
			var answerData = resultData.solves;
			var paperStatus = "完成";
			if(!resultData.isfinish){
				paperStatus = "评卷";
			}
			resultHtml = "<p>得分：<span style=\"color:#FF0000;\">" + resultData.totalScore
			+ "分</span><br>开考时间：<span style=\"color:#FF0000;\">" + resultData.startTimeFormat
			+ "</span><br>交卷时间：<span style=\"color:#FF0000;\">" + resultData.submitTimeFormat
			+ "</span><br>试卷状态：<span style=\"color:#FF0000;\">" + paperStatus + "</span></p>";
			if (answerData != null && answerData.length > 0) {
				loadMyAnswer(answerData);
			} else {
				$("#examInfoTable").html("<div class='alert alert-success'><h3>数据为导入数据，沒有该生的答题记录！</h3></div>");
			}
		}
	});
	$("#resultInfo").append(resultHtml);
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
	var html = "<span class='badge badge-important'>快速跳转：</span ><br>";
	$(".qk").each(function(i) {
		var td_key_id = $(this).attr("id");
		html += '<a href="javascript:;" id="fastto_question_' + td_key_id + '" onclick="moveto('
		+ $(this).parent().offset().top + '-80)">' + n + '</a>';
		n++;
	});
	html += "<br/>";
	$("#myExamInfo").html(html);
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
}
var solveStr = new Array();
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
function display() {
	document.getElementById("box").style.display = "block";
}
function disappear() {
	document.getElementById("box").style.display = "none";
}

//加载我考试填写的答案数据
function loadMyAnswer(answerData) {
	$.each(answerData, function(index, option) {
		var optionOrder = option.solvOrdinal;
		$('input[name="question_' + optionOrder + '"]').each(function() {
			$(this).attr("disabled", "disabled");
			if (this.type == "radio" || this.type == "checkbox") {
				var answer = option.solvAnswer.split("");
				for ( var i = 0; i < answer.length; i++) {
					if (this.value == answer[i]) {
						$(this).attr("checked", "checked");
					}
				}
			} else if (this.type == "text") {
				var answer = option.solvAnswer;
				$(this).val(answer);
			}
		});
		$('textarea[name="question_' + optionOrder + '"]').each(function() {
			$(this).attr("disabled", "disabled");
			if (this.type == "textarea") {
				var answer = option.solvAnswer;
				$(this).text(answer);
			}
		});
	});
}


/**
 * 手动评分操作
 */
function markTestScore(){
	var markScoreArr = new Array();
	var flag = true;
	rateId=getRequest("rateId");
	$(".markScore").each(function(){
		var thename = $(this).attr("name");
		var ordinal = thename.split("_")[2];
		var score = thename.split("_")[1];
		var value = $(this).attr("value");
		if(value == "" || isNaN(value)){
			alert("第"+ordinal+"题需要填写一个评卷分数");
			flag = false;
		}
		if(parseInt(value) > parseInt(score)){
			alert("第"+ordinal+"题评卷分数不能超过分值");
			flag =  false;
		}
		markScoreArr.push(ordinal+"&"+value);
	});
	if(flag){
		$.post("../../handler/train/markScore", {
			"userId" : getRequest("userId"),
			"exinId" : examinId,
			"rateId" : rateId,
			"examinName" : examinName,
			"markScoreArr" : markScoreArr.toString()
		}, function(data) {
			if (data.ret) {
				var returnBtn = "<button onclick='history.go(-1)' class='btn btn-info'><i class='fa fa-repeat'></i>返回</button>";
				$("#autoMarkScore").html(returnBtn);
				alert("评卷成功！请返回成绩管理察看！");
			} else {
				var returnBtn = "<button onclick='history.go(-1)' class='btn btn-info'><i class='fa fa-repeat'></i>返回</button>";
				$("#autoMarkScore").html(returnBtn);
				alert(data.errmsg);
			}
		});
	}
}