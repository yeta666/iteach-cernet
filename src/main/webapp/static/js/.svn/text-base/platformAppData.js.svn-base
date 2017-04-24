var viewaction = '';// 分页请求的action
var params;// 请求的参数
$(document).ready(function() {
	// Show Column
	ShowColumn();
	// back To Top
	backToTop();
	if (userId <= 0) {
		/** 加载分页 */
		$('#pagination').hide();
	} else{
		paginationPage();
	}
	$("#inputTeacherName").val("");
	loadStaTable();
	initialBind();// 绑定分页的一些操作响应
});

// 加载平台统计信息
function loadStaTable() {
	viewaction = '../../handler/courseStatistic/platform';
	params = {
		"pageArray" : new Array(),
		"recordPerPage" : 20
	};
	initSearch();
}
// 加载数据到数据区域
function refreshContent(pageRecords) {
	$.ajaxSettings.async = false;
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	var tableContent="";
	if (pageRecords.data.length == 0) {
		tableContent="<tr><td colspan=\"6\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>";
	} else {
		$.each(pageRecords.data, function(itemIndex, item) {
			var totalPost=item.postNum+item.replyNum;
			tableContent += "<tr><td>"+startIndex+"</td><td>" + item.schoolName + "</td><td>" + item.studentNum + "</td><td>" + item.onlineStuNum + "</td><td>"
			+ item.teacherNum + "</td><td>" +item.totalLearningTime + "</td><td>" + item.totalLearningNum + "</td><td>"+totalPost+"</td></tr>";
			startIndex++;
		});
		$('#platformInfo tbody:nth-child(2)').empty().append(tableContent);
	}
	return false;
}
/*function searchPlatInfo(){
	alert("进入");
	var searchWord=$("#inputTeacherName").val();
	alert(searchWord);
	viewaction = '../../handler/courseStatistic/platform';
	params = {
		"searchWord" : searchWord,
		"pageArray" : new Array(),
		"recordPerPage" : 20
	};
	
	initSearch();
}*/