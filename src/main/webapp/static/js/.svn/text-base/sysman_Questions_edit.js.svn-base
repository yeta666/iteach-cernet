var optionCount = 1;// 记录当前的选项数
var isHaveTitle = false;// 是否已存在该题
var quesId = 0;// 当前question编号
var courId = 0;// 课程编号（题库）
$(document).ready(function() {
	ShowColumn();// Show Column
	backToTop();// back To Top
	quesId = getRequest("quesId");
	loadQuesInfo(quesId);
});

//加载指定ID的试题信息
function loadQuesInfo(quesId) {
	var url = "../../handler/train/showOneQuestion";
	$.post(url,{"quesId" : quesId},function(data) {
		if (data.ret) {
			var ques = data.data.data;
			$('#qlevel').val(ques.exDifficulty);
			$('#ques_type').val(ques.exType);
			$('#ques_title').val(ques.exDescribe);
			$('#quCourse').val(ques.courName);
			courId = ques.courId;
			if (data.quesTitleAttach != "" && data.quesTitleAttach != undefined) {
				document.getElementById("attachTitle").innerHTML = "<a class='btn btn-danger' onclick='delAttach(\"d"
					+ data.quesTitleAttach + "\", \"attachTitle\")'>删除题干附件</a>";
				var attachHtml = "<a href='resources/question_attach/" + data.quesTitleAttach + "'>"
				+ data.quesTitleAttach + "</a>";
				$('#store_attachTitle').html(attachHtml);
			}
			var type = ques.exType;
			// 单选题
			if (type == 1 || type == 2) {
				// 设置选项数目
				optionCount = ques.options.length;
				// 依次添加选项
				$.each(ques.options,function(i, item) {
					if (i == 0) {
						$('#add_option1').val(item.optiDescribe);
						if (item.optiAttach != "" && item.optiAttach != undefined) {
							document.getElementById("attach_option1").innerHTML = "<a class='btn btn-danger' onclick='delAttach(\"d"
								+ data.quesTitleAttach
								+ "\", \"attach_option"
								+ (i + 1) + "\")'>删除选项附件</a>";
							var attachHtml = "<a href='resources/question_attach/"
								+ item.optiAttach + "'>" + item.optiAttach + "</a>";
							$('#store_attach_option1').html(item.optiAttach);
							$('#store_attach_option1').attr("href",
									"resources/question_attach/" + item.optiAttach);
						}
					} else {
						var iHtml = "<div class='form-group col-sm-12'><label name='optionShowText' for='add_option"
							+ (i + 1)
							+ "' class='control-label col-sm-2'>选项"
							+ String.fromCharCode((i + 1) + 64)
							+ ":</label>"
							+ "<div id='add"
							+ (i + 1)
							+ "' class='controls col-sm-4'><input class='form-control' name='option_content' type='text' id='add_option"
							+ (i + 1)
							+ "'/></div><div class='col-sm-4'>"
							+ "<a style='margin-left:4px' class='btn btn-primary' onclick='addOneOption()' id='add_bu_option"
							+ (i + 1)
							+ "'>"
							+ "添加选项</a>"
//							+ "<span style='margin-left:5px;margin-right:5px' id='attach_option"
//							+ (i + 1)
//							+ "'><a class='btn btn-info' data-toggle='modal' "
//							+ "data-keyboard='true' data-backdrop='true' href='#attachimportmodal' onclick='bindUploadAttach(\"attach_option"
//							+ (i + 1)
//							+ "\")'>上传选项附件</a>"
//							+ "</span>" 
							+ "<a class='btn btn-danger' onclick='delOneOption()' id='del_bu_option"
							+ (i + 1) + "'>" + "删除此项</a><a id='store_attach_option"
							+ (i + 1) + "'></a></div></div>";
						$('#addOptionsShow').append(iHtml);
						$('#add_bu_option' + i).css("display", "none");
						$('#del_bu_option' + i).css("display", "none");
						$('#add_option' + (i + 1)).val(item.optiDescribe);
						if (item.optiAttach != "" && item.optiAttach != undefined) {
							document.getElementById("attach_option" + (i + 1)).innerHTML = "<a class='btn btn-danger' onclick='delAttach(\"d"
								+ item.optiAttach
								+ "\", \"attach_option"
								+ (i + 1)
								+ "\")'>删除选项附件</a>";
							var attachHtml = "<a href='resources/question_attach/"
								+ item.optiAttach + "'>" + item.optiAttach + "</a>";
							$('#store_attach_option' + (i + 1)).html(item.optiAttach);
							$('#store_attach_option' + (i + 1)).attr("href",
									"resources/question_attach/" + item.optiAttach);
						}
					}
				});
			}
			// 刷新答案部分
			refreshAnswer();
			// //answer " + ques.correctAnswer + "
			if (type == 1) {
				$('#answer_option' + ques.correctAnswer).attr("checked", "checked");
			} else if (type == 2) {
				var answ = ques.correctAnswer.toString();
				for ( var i = 0; i < answ.length; i++) {
					$("[name='answer_option']").each(function() {
						if ($(this).attr("value") == answ[i]) {
							$(this).attr("checked", "checked");
						}
					});
				}
			} else if (type == 3) {
				var html = '<label for="addOptionA" class="control-label col-sm-2">答案：</label><div class="controls col-sm-4">'
					+ '<input type="radio" id="addOptionA" value="0" style="margin-left:15px" name="answer_option" checked="checked">'
					+ '<span id="addOptionAStr">错误</span><input type="radio" id="addOptionB" value="1" style="margin-left:15px" name="answer_option">'
					+ '<span id="addOptionBStr">正确</span></div>';
				$('#addAnswer').html(html);
				$('#addOptionsShow').css("display", "none");
				if (ques.correctAnswer == 0)
					$('#addOptionA').attr("checked", "checked");
				else if (ques.correctAnswer == 1)
					$('#addOptionB').attr("checked", "checked");
			} else if (type == 4) {
				var html = '<label for="ques_answer" class="control-label col-sm-2">答案：</label><div class="controls col-sm-4">'
					+ '<textarea wrap="wrap" name="ques_answer" id="ques_answer" rows="2" class="span7 form-control"></textarea>'
					+ '</div><div class="col-sm-4"><span class="help-inline">*不同的填空请标注清楚。</span></div>';
				$('#addAnswer').html(html);
				$('#addOptionsShow').css("display", "none");
				$('#ques_answer').val(ques.correctAnswer);
			} else if (type == 5) {
				var html = '<label for="ques_answer" class="control-label col-sm-2">答案：</label><div class="controls col-sm-4">'
					+ '<textarea wrap="wrap" name="ques_answer" id="ques_answer" rows="4" class="span7 form-control"></textarea>'
					+ '</div><div class="col-sm-4"><span class="help-inline">*填写可识别的答案。</span></div>';
				$('#addAnswer').html(html);
				$('#addOptionsShow').css("display", "none");
				$('#ques_answer').val(ques.correctAnswer);
			} else
				alert("ERROR!");
			$("[name='answer_option']").each(function() {
				if ($(this).attr("value") == data.correctAnswer) {
					$(this).attr("checked", "checked");
				}
			});
			// 修改文字
			$('#dialogTitle').html("修改试题");
		} else {
			window.location.href = "sysman_Questions.html?" + urlColHtml();
		}
	}, "json");
}
//刷新答案部分
function refreshAnswer() {
	var type = $('#ques_type').val();
	if (type == 1) {
		var inHtml = "<label for='addOption1' class='control-label col-sm-2'>答案：</label><div class='controls col-sm-4'>";
		for ( var i = 0; i < optionCount; i++) {
			inHtml += "<input type='radio' value='" + (i + 1)
			+ "' style='margin-left:5px' name='answer_option' id='answer_option" + (i + 1) + "'>"
			+ String.fromCharCode((i + 1) + 64) + "";
		}
		inHtml += "</div>";
		$('#addAnswer').html(inHtml);
	} else if (type == 2) {
		var inHtml = "<label class='control-label col-sm-2'>答案：</label><div class='controls col-sm-4'>";
		for ( var i = 0; i < optionCount; i++) {
			inHtml += "<input type='checkbox'  value='" + (i + 1) + "' style='margin-left:7px' name='answer_option'>"
			+ String.fromCharCode((i + 1) + 64) + ""
		}
		inHtml += "</div>";
		$('#addAnswer').html(inHtml);
	} else if (type == 3 || type == 4 || type == 5) {
	} else
		alert("ERROR!");
}
//增加题目时题型改变
function add_type_change() {
	var type = $('#ques_type').val();
	if (type == 1) {
		$('#addOptionsShow').css("display", "");
	} else if (type == 2) {
		$('#addOptionsShow').css("display", "");
	} else if (type == 3) {
		var html = '<label for="addOptionA" class="control-label col-sm-2">答案：</label><div class="controls col-sm-4">'
			+ '<input type="radio" id="addOptionA" value="0" style="margin-left:15px" name="answer_option" checked="checked"/>'
			+ '<span id="addOptionAStr">错误</span><input type="radio" id="addOptionB" value="1" style="margin-left:15px" name="answer_option">'
			+ '<span id="addOptionBStr">正确</span></div>';
		$('#addAnswer').html(html);
		$('#addOptionsShow').css("display", "none");
	} else if (type == 4) {
		var html = '<label for="ques_answer" class="control-label col-sm-2">答案：</label><div class="controls col-sm-4">'
			+ '<textarea wrap="wrap" name="ques_answer" id="ques_answer" rows="2" class="span7 form-control"></textarea>'
			+ '<div class="col-sm-4"><span class="help-inline">*不同的填空请标注清楚。</span></div></div>';
		$('#addAnswer').html(html);
		$('#addOptionsShow').css("display", "none");
	} else if (type == 5) {
		var html = '<label for="ques_answer" class="control-label col-sm-2">答案：</label><div class="controls col-sm-4">'
			+ '<textarea wrap="wrap" name="ques_answer" id="ques_answer" rows="4" class="span7 form-control"></textarea>'
			+ '<div class="col-sm-4"><span class="help-inline">*填写可识别的答案。</span></div></div>';
		$('#addAnswer').html(html);
		$('#addOptionsShow').css("display", "none");
	} else
		alert("ERROR!");
	// 刷新答案部分
	refreshAnswer();
}
//初步检查输入数据
function checkSubmit() {
	var title = $('#ques_title').val().trim();
	if (title.length == 0) {
		alert("请输入题干");
		$('#ques_title').focus();
		return false;
	}
	return true;
}
//传递数据至后台保存
function storeTheQues(actionType) {
	var quesValue = $('#ques_title').val();
	if (quesValue == null || quesValue == "") {
		$('#titleTips').html("题干不能为空");
		setTimeout("$('#titleTips').html('')", 3000);
		return;
	}
	if (checkSameOption()) {
		$('#optionTips').html("不能再同一题中添加多个相同选项");
		setTimeout("$('#optionTips').html('')", 3000);
		return;
	}
	quesId = quesId == undefined ? "0" : quesId;
	var options = "";// 记录添加了的选项
	var optionAttachs = new Array();// 记录添加了的选项附件
	for ( var i = 0; i < optionCount; i++) {
		if ($('#add_option' + (i + 1)).val().trim() == "") {
			if (options == "") {
				options += "";
			} else
				options += "&" + "";
		} else {
			if (options == "") {
				options += $('#add_option' + (i + 1)).val().trim();
			} else
				options += "&" + $('#add_option' + (i + 1)).val().trim();
		}
		if ($('#store_attach_option' + (i + 1)).html().trim() == "")
			optionAttachs.push("0");
		else {
			optionAttachs.push($('#store_attach_option' + (i + 1)).html().trim());
		}
	}
	var ans = "";
	var type = $('#ques_type').val();
	if (type == 1) {
		ans = $('input[name="answer_option"]:checked').val();
	} else if (type == 2) {
		var count = 0;
		$("[name='answer_option']").each(function() {
			if ($(this).attr("checked")) {
				count++;
				ans += $(this).val();
			}
		});
		if (count == 0) {
			alert("多选至少选中一个答案！");
			return;
		}
	} else if (type == 3) {
		ans = $('input[name="answer_option"]:checked').val();
	} else if (type == 4 || type == 5) {
		ans = $('#ques_answer').val();
	} else {
		alert("ERROR!");
		return;
	}
	var diff = $("#qlevel").val();
	var param = {
			"exquId" : quesId,
			"exquDescribe" : $('#ques_title').val().trim(),
			"optionTitles" : options.toString(),
			"exquCorrectanswer" : ans,
			"optionAttachs" : optionAttachs.toString(),
			"exquCourId" : courId,
			"exquType" : type,
			"exquDifficulty" : diff,
			"exquCreateUserid" : userId,
			"titleAttach" : $('#store_attachTitle').html()
	};
	var url = "";
	if (actionType == 1)
		url = '../../handler/train/editQuestions';
	else if (actionType == 2)
		url = '../../handler/train/saveQuestions';
	$.post(url, param, function(data) {
		if (data != null) {
			if (data.ret) {
				if (confirm("试题编辑成功!页面将返回试题管理!")) {
					window.location.href = "sysman_Questions.html?" + urlColHtml();
				}
			} else {
				alert("试题编辑失败!:" + data.errmsg);
				window.location.reload();
			}
		} else {
			alert("试题编辑失败!");
			window.location.reload();
		}
	});
}
//绑定上传文件的相关操作
function bindUploadAttach(uid) {
	var uploadPath = '';
	$('#upload').unbind("click"); // 移除绑定
	$('#upload').click(function() {
		var filename = $('#fileInput').val();
		var suffix = filename.slice(filename.indexOf('.') + 1);
		var msg = '';
		if (filename == '') {
			msg = '上传文件不能为空';
		} else {
			var fileSuffix = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
			if (fileSuffix == "png" || fileSuffix == "gif" || fileSuffix == "jpg" || fileSuffix == "mp3"
				|| fileSuffix == "wma" || fileSuffix == "flv") {
			} else
				msg = '请上传规定格式的文件';
		}
		if (msg != '')
			infoNotice("error", "", msg, $('#uploadform'));
		else {
			$('#upload').text("上传中...");
			$('#upload').attr("disabled");
			$.ajaxFileUpload({
				url : "questionuploadfile",
				secureuri : false,
				fileElementId : "fileInput",
				dataType : "json",
				success : function(data, status) {
					$('#upload').text("上传");
					$('#upload').removeAttr("disabled");
					var result = "success";
					var msg = data.message;
					if (data.status != "success" || msg != '')
						result = "error";
					if (data.status != 'error') {
						uploadPath = data.fileName;
						$('#' + uid).html(
								"<a class='btn btn-danger' onclick='delAttach(\"d" + uploadPath + "\", \""
								+ uid + "\")'>删除附件</a>");
						var attachHtml = "<a href='resources/question_attach/" + uploadPath + "'>" + uploadPath
						+ "</a>";
						$('#store_' + uid).html(uploadPath);
						$('#store_' + uid).attr("href", "resources/question_attach/" + uploadPath);
						$('#store_' + uid).attr("target", "_blank");
					}
					infoNotice(result, "", msg, $('#uploadform'));
					$('#attachimportmodal').modal('hide');
					$('#infomsg').remove();
				},
				error : function(data, status, e) {
					$('#upload').text("上传");
					$('#upload').removeAttr("disabled");
					infoNotice("error", "", "文件大小超出限制", $('#uploadform'));
				}
			});
		}
	});
}
//添加一个选项
function addOneOption() {
	var type = $('#ques_type').val();
	if (optionCount >= 9) {
		$('#add_tipMsg').html("不能添加超过9个选项");
		setTimeout("$('#add_tipMsg').html('')", 3000);
		return;
	}
	// 计数器自加
	optionCount++;
	if (type == 1) {
		var iHtml = "<div class='form-group col-sm-12'><label name='optionShowText' for='add_option"
			+ optionCount
			+ "' class='control-label col-sm-2'>选项"
			+ String.fromCharCode(optionCount + 64)
			+ ":</label>"
			+ "<div id='add"
			+ optionCount
			+ "'>"+"<div class='controls col-sm-4'><input name='option_content' type='text' class='form-control' id='add_option"
			+ optionCount
			+ "'/></div><div class='col-sm-4'>"
			+ "<a class='btn btn-primary' onclick='addOneOption()' id='add_bu_option"
			+ optionCount
			+ "'>"
			+ "添加选项</a>" 
			+ "<a class='btn btn-danger' onclick='delOneOption()' id='del_bu_option" + optionCount + "'>"
			+ "删除此项</a><a id='store_attach_option" + optionCount + "'></a></div></div></div>";
		$('#addOptionsShow').append(iHtml);
		if (optionCount == 9)
			$('#add_bu_option' + optionCount).css("display", "none");
		$('#add_bu_option' + (optionCount - 1)).css("display", "none");
		$('#del_bu_option' + (optionCount - 1)).css("display", "none");
	} else if (type == 2) {
		var iHtml = "<div class='form-group col-sm-12'><label name='optionShowText' for='add_option"
			+ optionCount
			+ "' class='control-label col-sm-2'>选项"
			+ String.fromCharCode(optionCount + 64)
			+ ":</label>"
			+ "<div id='add"
			+ optionCount
			+ "'>"+"<div class='controls col-sm-4'><input name='option_content' type='text' class='form-control' id='add_option"
			+ optionCount
			+ "'/></div>"
			+ "<div class='col-sm-4'><a class='btn btn-primary' onclick='addOneOption()' id='add_bu_option"
			+ optionCount
			+ "'>"
			+ "添加选项</a>" 
			+ "<a class='btn btn-danger' onclick='delOneOption()' id='del_bu_option" + optionCount + "'>"
			+ "删除此项</a>" 
			+ "<span id='store_attach_option" + optionCount + "' style='display:none'></span></div></div></div>";
		$('#addOptionsShow').append(iHtml);
		if (optionCount == 9)
			$('#add_bu_option' + optionCount).css("display", "none");
		$('#add_bu_option' + (optionCount - 1)).css("display", "none");
		$('#del_bu_option' + (optionCount - 1)).css("display", "none");
	}
	// 刷新答案部分
	refreshAnswer();
}
//删除最后一个选项
function delOneOption() {
	$("[name='optionShowText']").each(function() {
		if ($(this).attr("for") == ("add_option" + optionCount)) {
			$(this).remove();
		}
	});
	$('#add' + optionCount).remove();
	$('#add_bu_option' + (optionCount - 1)).css("display", "");
	$('#del_bu_option' + (optionCount - 1)).css("display", "");
	optionCount--;
	// 刷新答案部分
	refreshAnswer();
}

//删除附件
function delAttach(filePath, uid) {
	if (filePath.trim() == "") {
		$('#' + uid).html(
				"<span id='attachTitle'><a class='btn btn-info' data-toggle='modal'"
				+ "data-keyboard='true' data-backdrop='true'"
				+ "href='#attachimportmodal' onclick='bindUploadAttach('" + uid + "')'>上传附件</a></span>");
		return;
	} else {
		var url = 'delAttach';
		$.post(url, {
			"path" : filePath.substring(1)
		}, function(data) {
			if (data != null) {
				if (data.status == 0) {
					var tempHtml = "<span id='" + uid + "'><a class='btn btn-info' data-toggle='modal' "
					+ "data-keyboard='true' data-backdrop='true' "
					+ "href='#attachimportmodal' onclick='bindUploadAttach(\"" + uid + "\")'>上传附件</a></span>";
					$('#' + uid).html(tempHtml);
					$('#store_' + uid).html("");
				} else if (data.status == 2) {
					alert(data.string);
					$('#' + uid).html(
							"<span id='" + uid + "'><a class='btn btn-info' data-toggle='modal' "
							+ "data-keyboard='true' data-backdrop='true' "
							+ "href='#attachimportmodal' onclick='bindUploadAttach(\"" + uid
							+ "\")'>上传附件</a></span>");
					$('#store_' + uid).html("");
				} else
					alert(data.string);
			}
		});
	}
}
//检查是否有相同的选项
function checkSameOption() {
	for ( var i = 0; i < optionCount; i++) {
		if (i == 0)
			continue;
		var thisVal = $('#add_option' + (i + 1)).val().trim();
		for ( var j = 1; j <= i; j++) {
			var befVal = $('#add_option' + j).val().trim();
			if (befVal == thisVal)
				return true;
		}
	}
	return false;
}