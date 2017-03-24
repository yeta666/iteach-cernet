var showCourseName = "";
var showCourseId = 0;
var startTime = '';// 查询的开始时间
var endTime = '';// 查询的结束时间
var viewaction = '';// 分页请求的action
var params;// 请求的参数
var paperId = 0;// 试卷id
var searchWord = "";// 试卷描述
var searchType = 0;// 检索类型
var colIds = "";// 栏目ids
var cookieString = "Search=";
var secondCol = 0;
$(document).ready(function() {
	colIds = urlColHtml();// 栏目ids
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
	// 加载试卷数据
	loadPapers();
	// 绑定搜索按钮
	bindSearch();
	// 绑定删除按钮
	bindDelPapers();
});
//加载我的试题
function loadPapers() {
	secondCol = getRequest("secondCol");
	viewaction = '../../handler/train/viewPapers';
	params = {
			"paperId" : paperId,
			"departId":0,
			"userId":0,
			"departType":0,
			"userType":0,
			"startTime" : "",
			"courseId" : 0,
			"endTime" : "",
			"searchWord" : "",
			"searchType" : searchType,
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
			$('#searchType').val(params.searchType);
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
//加载数据到数据区域
function refreshContent(pageRecords) {
	$.ajaxSettings.async = false;
	$("#mainTable").empty();
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	var paperHtml = "";
	if (pageRecords.data.length == 0) {
		$('#mainTable')
		.append(
		"<tr><td colspan=\"10\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>");
	} else {
		$.each(pageRecords.data, function(itemIndex, item) {
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
			paperHtml += "<tr><td><input type='checkbox' name='chooseQu' value='" + item.expaId
			+ "'></td><td class='tdcenter'>" + startIndex + "</td>" + "<td class='tdcenter' id='"
			+ item.expaId + "' title=" + item.expaName + "><a href='sysman_PaperInfo.html?paperId=" + item.expaId + "&examinId=0&" + colIds
			+ "'>" + titleFormat(item.expaName, 10) + "</a></td><td class='tdcenter' title=" + item.courName + ">" + titleFormat(item.courName, 5)
			+ "</td><td class='tdcenter'>" + item.expaTotalscore + "</td><td class='tdcenter'>"
			+ paperDifficult + "</td>" + "<td class='tdcenter'>" + item.expaCreatetimeFormat + "</td>"
			+ "<td class='tdcenter' title='" + item.expaDescribe + "'>" + titleFormat(item.expaDescribe, 10)
			+ "</td></tr>";
			startIndex ++;
		});
		$('#mainTable').append(paperHtml);
	}
	return false;
}

//绑定搜索
function bindSearch() {
	$('#search').click(function() {
		getParams();
		initSearch();
		return false;
	});
}
//设置参数
function getParams() {
	// 删除cookie
	document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
	searchWord = $('#keyword').val();
	paperId = $('#quPaper').val();
	startTime = $('#startTime').val();
	endTime = $('#endTime').val();
	searchType = $('#searchType').val();
	// 点击搜索，一切重新查找
	params.currentPage = 1;
	params.searchWord = searchWord;
	params.paperId = paperId;
	params.startTime = startTime;
	params.endTime = endTime;
	params.searchType = searchType;
	params.departId = departId;
	params.departType = departmentTypeID;
	params.userId = userId;
	params.userType = userType;
	//添加cookie
	var cookieString = "Search=";
	cookieString = cookieString + JSON.stringify(params);
	document.cookie = cookieString;
}

//绑定删除试卷
function bindDelPapers() {
	// 获取选择的id序列
	var delQuestions = new Array();
	$('#delQu').click(function() {
		$("input[name='chooseQu']").each(function() {
			var $current = $(this);
			if ($current.is(':checked') != undefined)
				delQuestions.push($current.val());
		});
		// 如果选择的id为空，则提示错误信息
		if (delQuestions.length == 0) {
			alert("您未中选择任何试卷进行删除！");
			return false;
		}
		$('#delModal').modal('show');
	});
	$("#delPaper").click(function(){
		var state;
		var message;
		var header;
		// 如果选择的id为空，则提示错误信息
		if (delQuestions.length == 0) {
			alert("您未中选择任何试卷进行删除！");
			// state = 'error';
			// header = "删除失败";
			// message = "未选择任何用户进行删除";
			// infoNotice(state, header, message, $('#data-grid'));
		}
		// 否则，向后台发送删除请求并显示结果信息
		else {
			$.post("../../handler/train/delPapers", {
				"delPapers" : delQuestions.toString()
			}, function(data) {
				if (data.ret) {
					alert("删除成功！");
					initSearch();
				} else {
					alert(data.errmsg);
				}
			}, "json");
		}
		$('#delModal').modal('hide');
		return false;
	});
}