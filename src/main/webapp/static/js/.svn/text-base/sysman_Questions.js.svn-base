var showCourseName = "";
var showCourseId = 0;
var startTime = '';// 查询的开始时间
var endTime = '';// 查询的结束时间
var viewaction = '';// 分页请求的action
var params;// 请求的参数
var quCourse = 0;// 課程id
var quType = 0;// 试题类型
var searchWord = "";// 试题类型
var type = "";//
var colIds = "";//
var cookieString = "Search=";
var secondCol = 0;
$(document).ready(function() {
	colIds = urlColHtml();
	ShowColumn();// Show Column
	backToTop();// back To Top
	// ** 加载分页 *//
	if (userId <= 0) {
		$('#pagination').hide();
	} else
		paginationPage();
	// 新的时间插件显示示例
	timepicker("startTime");
	timepicker("endTime");
	// -----------------分页相关设置---------------------
	// 绑定全选/取消全选按钮
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
	// 绑定题库选择下拉列表
	bindCourseList();
	// 加载试题数据
	loadQuestions();
	// 绑定搜索按钮
	bindSearch();
	// 绑定删除按钮
	bindDelQuestions();
	// 绑定上传文件按钮
	bindFileUpload();
});

// 加载我的试题
function loadQuestions() {
	secondCol = getRequest("secondCol");
	viewaction = '../../handler/train/viewQuestions';
	params = {
		"quCourse" : quCourse,
		"quType" : quType,
		"startTime" : "",
		"endTime" : "",
		"searchWord" : "",
		"departId" : 0,
		"userId" : 0,
		"departType" : 0,
		"userType" : 0,
		"postType" : 1,
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
			$('#quCourse').val(params.quCourse);
			$('#quType').val(params.quType);
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
				if (answerCode == "1") {
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
					+ "'></td><td class='tdcenter'>" + startIndex + "</td>" + "<td class='tdcenter' id='" + item.courId
					+ "'>" + item.courName + "</td><td class='tdcenter'>" + exTypeName
					+ "</td><td class='tdcenter'><a href='sysman_Questions_edit.html?quesId=" + item.exquId + "&"
					+ colIds + "' rel='popover'  data-original-title='<strong>" + item.exDescribe + "</strong>'";

			$.each(item.options, function(index, option) {
				var optionMark = String.fromCharCode(code);
				optionHtml += "<dd>" + optionMark + "." + option.optiDescribe + "</dd>";
				code++;
			});
			var correctHtml = "<p><strong>正确答案：" + correctAns + "</strong></p>";
			courseHtml += "data-content='" + optionHtml + correctHtml + "' >" + titleFormat(item.exDescribe, 10)
					+ "</a>";
			courseHtml += "</td><td class='tdcenter'>" + item.creatTimeFormat + "</td></tr>";
			startIndex++;
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
	quCourse = $('#quCourse').val();
	quType = $('#quType').val();
	startTime = $('#startTime').val();
	endTime = $('#endTime').val();
	// 点击搜索，一切重新查找
	params.currentPage = 1;
	params.searchWord = searchWord;
	params.quCourse = quCourse;
	params.quType = quType;
	params.startTime = startTime;
	params.endTime = endTime;
	params.departId = departId;
	params.userId = userId;
	params.departType = departmentTypeID;
	params.userType = userType;
	// 添加cookie
	var cookieString = "Search=";
	cookieString = cookieString + JSON.stringify(params);
	document.cookie = cookieString;
}

// 绑定删除试题
function bindDelQuestions() {
	$('#delQu').click(function() {
		// 获取选择的用户id序列
		var delQuestions = new Array();
		$("input[name='chooseQu']").each(function() {
			var $current = $(this);
			if ($current.is(':checked') != undefined)
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
			delInfoNotice(state, header, message);
			return false;
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
				delInfoNotice(state, header, message);
				// 删除记录后，重新请求
				if (state == 'success')
					initSearch();
			}, "json");
		}
		return false;
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
// 绑定上传文件按钮
function bindFileUpload(type) {
	$('#upload')
			.click(
					function() {
						var fileName = $("#file").val();
						var suffix = fileName.slice(fileName.lastIndexOf(".") + 1);
						if (fileName == "") {
							infoNotice("error", "", "上传文件不能为空！");
							return false;
						}
						if (!(suffix == "xls" || suffix == "xlsx")) {
							infoNotice("error", "", "文件格式错误，只能上传excel文件");
							return false;
						} else {
							$
									.ajaxFileUpload({
										url : "../../handler/train/importQuestionsInfo",
										secureuri : false,
										fileElementId : "file",
										dataType : "json",
										success : function(data, status) {
											if (data.ret) {
												infoNotice("success", "上传成功!", "批量上传试题成功!");
											} else {
												if (data.errcode == 1) {
													infoNotice("warning", "上传有误!",
															"传入题库信息有误，请检查修改！察看错误详情:<a href='../../handler/train/downloadErrorMsg?fileName=errorMsg.txt'>详细信息</a>");
												} else {
													infoNotice("error", "上传失败!", "批量上次试题失败，请检查导入数据或察看记录:<br>"
															+ data.errmsg);
												}
											}
										}
									});
						}
					});
}
function delInfoNotice(state, header, message) {
	var classes = 'alert alert-' + state;
	var infoHtml = "";
	// 消息框被关闭后，存在，需要重新创建
	infoHtml = "<div id='delInfo' class='" + classes + "'>" + "<a class='close' data-dismiss='alert'>×</a>"
			+ "<h4 class='alert-heading'>" + header + "</h4><p>" + message + "</p></div>";
	$('#delInfo').html(infoHtml);
}