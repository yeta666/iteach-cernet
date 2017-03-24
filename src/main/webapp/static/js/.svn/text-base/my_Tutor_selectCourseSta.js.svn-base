/*
 *  author：郭海蓉 
 *  time:2013-07
 */
var viewaction = '';// 分页请求的action
var departIdFirset = "", departIdtemp = "";
var params;// 请求的参数
var gradeId = 0, classId = 0, orignalDepartID = 0, passOrNot = 0;// 筛选课程id
var searchType = "", searchWord = "";
var startTime = "", endTime = "";
var selecteCourseData = null, evaMethodsData = null;
var courseId = 0, isMainorTour = 0;
$(document).ready(function() {
	orignalDepartID = departId;
	// 获取课程id和辅导/主讲标识值
	courseId = location.search.split("?")[1].split("&")[0].split("=")[1];
	isMainorTour = location.search.split("?")[1].split("&")[1].split("=")[1];
	ShowColumn();// 显示功能栏
	backToTop();// 回顶部
	if (userId <= 0) {
		/** 加载分页 */
		$('#pagination').hide();
	} else {
		paginationPage();
	}
	// 新的时间插件显示示例
	timepicker("startTime");
	timepicker("endTime");
	$('#myTutor').attr("class", "active");
	initialBind();// 绑定分页的一些操作响应
	departIdtemp = departId;
	// 获取考核方式
	selceteEvaluate();
	if (departmentTypeID == 1 || isMainorTour == 1) {// 市级
		departIdFirset = 0;
		$("#seleteSchoolID").show();
		selceteSchool();
		selceteGrade();
		selceteClass();
		$("#seleteSchoolID").change(function() {
			$("#grade").empty();
			$("#class").empty();
			departId = $("#seleteSchoolID").val();
			selceteGrade();
			selceteClass();
			$("#grade").change(function() {
				gradeId = $("#grade").val();
				selceteClass();
			});
		});
	} else {
		$("#seleteSchoolID").hide();
		departIdFirset = departIdtemp;
		departId = departIdtemp;
		selceteGrade();
		selceteClass();
		$("#grade").change(function() {
			gradeId = $("#grade").val();
			selceteClass();
		});

	}
	// 显示数据
	viewaction = '../../handler/selectCourse/viewList';
	params = {
		"scoreType" : 0,
		"departId" : departIdFirset,// 市级传0，校级传本身
		"courseId" : courseId,
		"ismainortour" : isMainorTour,// 主讲为1，辅导为2，既为主讲又为辅导为1，为0表示教务管理
		"gradeId" : 0,
		"classId" : 0,
		
		"searchWord" : '',
		"searchType" :'',
		"courseType":'',
		"confirmOrNot":0,
		"courseYear":-1,
		"courseTerm":-1,
		"courseTermPhase":-1,
		"courseArtScience":-1,
		
		"userId" : userId,
		"queryType" : 0,
		"pageArray" : new Array(),
		"recordPerPage" : 10
	};
	initSearch();

	$("#search").click(function() {
		searchCourseSta();// 搜索
	});

});
// 获取考核方式
function selceteEvaluate() {
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/evaluateMethod/viewAllEvaMethods",
		dataType : 'json',
		data : {},
		success : function(data) {
			if (data.ret) {
				if ($.isEmptyObject(data.data.evaMethods)) {
					alert("考核方式为空！");
				} else {
					evaMethodsData = data.data.evaMethods;
				}

			} else {
				alert(data.errmsg);
			}
		}
	});
}

// 通过关键词，搜索选修课统计
function searchCourseSta() {
	if (departmentTypeID == 1 || isMainorTour == 1) {
		departId = $("#seleteSchoolID").val();
	} else {
		departId = orignalDepartID;
	}
	gradeId = $("#grade").val();
	classId = $("#class").val();
	searchType = $("#searchType").val();
	searchWord = $("#keyword").val();
	startTime = $("#startTime").val();
	endTime = $("#endTime").val();
	passOrNot = $("#passOrNot").val();
	viewaction = '../../handler/selectCourse/viewList';
	params = {
		"scoreType" : passOrNot,
		"searchWord" : searchWord,
		"searchType" : searchType,
		"startTime" : startTime,
		"endTime" : endTime,
		"departId" : departId,
		"ismainortour" : isMainorTour,// 主讲为1，辅导为2，既为主讲又为辅导为1

		"courseType":'',
		"confirmOrNot":0,
		"courseYear":-1,
		"courseTerm":-1,
		"courseTermPhase":-1,
		"courseArtScience":-1,
		
		
		"courseId" : courseId,
		"userId" : userId,
		"classId" : classId,
		"gradeId" : gradeId,
		"pageArray" : new Array(),
		"recordPerPage" : 10
	};
	initSearch();
}

// 加载数据到数据区域
function refreshContent(pageRecords) {
	var time = "";
	$.ajaxSettings.async = false;
	var startIndex = (currentPage - 1) * 10 + 1;
	var tableContent = "";
	if (pageRecords.data.length == 0) {
		tableContent = "<tr><td colspan=\"10\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>";
	} else {
		selecteCourseData = pageRecords.data;
		$.each(pageRecords.data, function(itemIndex, item) {
			var courseCredit = 0;
			if (numberHandler(item.totalScore, 2) >= 60) {
				courseCredit = item.courseCredit;
			}
			time = item.formarttedTime.split(" ")[0];
			tableContent += "<tr><td>" + startIndex + "</td>" + "<td>" + item.courseName + "</td>" + "<td>"
					+ item.stuName + "</td>" + "<td>" + item.stuNum + "</td>" + "<td>" + item.schoolName + "</td>"
					+ "<td>" + item.stuGrade + "</td>" + "<td>" + item.stuClass + "</td>" + "<td title='"
					+ item.formarttedTime + "'>" + time + "</td>"
					+ "<td><a type='button' href='#addOrMod' data-toggle='modal' " + "onclick='operateCourseSelecte("
					+ item.selectCourseId + "," + item.tepaId + ")'>" + numberHandler(item.totalScore, 2) + "</a></td>"
					+ "</td><td>" + courseCredit + "</td></tr>";
			startIndex++;
		});
	}
	$('#viewListTable tbody:nth-child(2)').empty().append(tableContent);
	return false;
}
var aEvmePattern = new Array();
var aevmeThrehold = new Array();
// 显示对话框信息 evaMethodsData
function operateCourseSelecte(selectCourseId, tepaId) {
	var baseInfo = "";
	$.each(selecteCourseData, function(itemIndex, item) {
		if (item.selectCourseId == selectCourseId) {
			$.each(evaMethodsData,
					function(itemIndex2, item2) {
						if (item2.evmeId == tepaId) {
							tableContentselecte = "";
							aEvmePattern = item2.evmePattern.split(",");// 考核比重
							aevmeThrehold = item2.evmeThrehold.split(",");// 满分要求
							baseInfo = "<span><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;课程名称：</strong>" + item.courseName
									+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "<span><strong>学生姓名：</strong>"
									+ item.stuName + "</span>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
									+ "<span><strong>考核方式：</strong>" + item2.evmeName + "</span>";
							tableContentselecte = 0;
							var mylearnTime = numberHandler(item.learnTime / 60, 2) + "分钟" + "（其中集中学习"
									+ numberHandler(item.massedLearnTime / 60, 2) + "分钟）";
							showStudySituation("学习次数", aEvmePattern[0], aevmeThrehold[0], "次", item.learnNum,
									item.learnNumScore);

							showStudySituation("学习时间", aEvmePattern[1], aevmeThrehold[1], "", mylearnTime,
									item.learnTimeScore);

							showStudySituation("论坛讨论", aEvmePattern[2], aevmeThrehold[2], "次", item.bbsDiscuss,
									item.bbsDiscussScore);

							showStudySituation("在线自测", aEvmePattern[3], aevmeThrehold[3], "分", item.testSourceScore,
									item.testScore);

							tableContentselecte += "<tr class='info'>" + "<td>总成绩</td>" + "<td> — </td>" +

							"<td> — </td>" + "<td> — </td>" + "<td>" + numberHandler(item.totalScore, 2) + "</td>"
									+ "</tr>";
							return false;
						}
					});
			return false;
		}
	});
	$("#baseInfo").html(baseInfo);
	$('#scoreList tbody:nth-child(2)').empty().append(tableContentselecte);
}
