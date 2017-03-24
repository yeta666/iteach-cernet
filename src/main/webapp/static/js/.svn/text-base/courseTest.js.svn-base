/**
 * 考试试卷列表
 * 
 * @author
 */
// 课程信息数据
var courseInfo = "";
var courId = 0;
$(document).ready(function() {
	// back To Top
	backToTop();
	// 面包屑
	ShowSubColumn();
	if (userId <= 0) {
		/** 加载分页 */
		$('#pagination').hide();
	} else
		paginationPage();
	courId = getRequest("courseId");
	viewCourseInfo();
	viewaction = '../../handler/train/viewAllTest';
	params = {
		"courId" : courId,
		"startTime" : "",
		"endTime" : "",
		"searchWord" : "",
		"pageArray" : new Array(),
		"recordPerPage" : 20
	};
	initialBind();// 绑定分页的一些操作响应
	initSearch();
});
// 加载数据到数据区域
function refreshContent(pageRecords) {
	$.ajaxSettings.async = false;
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	$("#testlist").empty();
	var courseHtml = "<table class='table  table-hover'><thead><tr>"
			+ "<th>编号</th><th>试题名称</th><th>描述</th><th>状态</th><th>测试</th></tr></thead><tbody>";
	if (pageRecords.data.length == 0) {
		$('#testlist')
				.append(
						"<tr><td colspan=\"10\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>");
	} else {
		$.each(pageRecords.data, function(itemIndex, item) {
			var classType = "warning";
			var visiable = "disabled='disabled'";
			if (item.state == 1) {
				classType = "info";
				visiable = "";
			}
			var tableIndex = itemIndex + 1;
			courseHtml += "<tr class='" + classType + "'><td class='tdcenter'>" + tableIndex + "</td>"
					+ "<td class='tdcenter' id='" + item.exinId + "'>" + item.exinName + "</td><td class='tdcenter'>"
					+ item.exinDescribe + "</td><td class='tdcenter'>" + item.exinState
					+ "</td><td class='tdcenter'><a  id='" + item.exinId + "' href='my_test_paper.html?paperId="
					+ item.exinExpaId + "&examinId=" + item.exinId + "' class='btn btn-small btn-info' " + visiable
					+ "' type='button' >进行测试</a ></td></tr>";
			startIndex = startIndex + 1;
		});
		courseHtml += "</tbody></table>";
		$('#testlist').append(courseHtml);
	}
	return false;
}
function viewCourseInfo() {
	var courInfoHtml = "课程信息！";
	var courImgHtml = "课程信息！";
	$.ajaxSettings.async = false;
	var url = '../../handler/train/viewCourseInfo';
	$.post(url, {
		"courId" : courId
	}, function(data) {
		if (data.ret) {
			var state = "";
			courseInfo = data.data.data;
			if (courseInfo.courState == 1)
				state = "开放中……";
			else if (courseInfo.courState == 0)
				state = "未开放……";
			var credit = 0;
			if (courseInfo.courCredit != null) {
				credit = courseInfo.courCredit;
			}
			var courDescribe = "";
			if (courseInfo.courDescribe != null) {
				courDescribe = courseInfo.courDescribe;
			}
			var img = "upload/eduman/coursepic.jpg";
			if (courseInfo.courImg != "" && courseInfo.fileName != "" && courseInfo.courImg != null
					&& courseInfo.fileName != null) {
				img = courseInfo.courImg + courseInfo.fileName;
			}
			courInfoHtml = "<div><h4 class='header'>" + courseInfo.courName + "</h4><p>" + "课程介绍:</p><p>"
					+ courDescribe + ".</p>" + "<p>课程积分：" + credit + "</p><p>状  态：" + state + "</p></div>";
			$('#courseInfo').append(courInfoHtml);
			courImgHtml = '<img id="courseImg" alt="" src="../../' + img + '" class="img-rounded courseImg" />';
			$('#courseImg').append(courImgHtml);
		}
	});
	return false;
}