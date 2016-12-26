/*
 *  author：郭海蓉 
 *  time:2013-07
 */
var departmentT;//部门类别
var userT;//用户种类
var viewaction = '';// 分页请求的action
var departIdFirset = "", departIdtemp = "";
var courseId = 0, gradeId = 0, classId = 0, orignalDepartID = 0, passOrNot = 0,isConfirmed=0,searchCourseYear=-1,searchCourTerm =-1,searchCourTermPhase = -1,searchArtScience=-1;// 筛选课程id
var searchType = "", searchWord = "", searchCourType="";
var selecteCourseData = null, evaMethodsData = null;
var result1 = "";
$(document).ready(function() {
	userT = userType;
	departmentT = departmentTypeID;
	if(userT==3&&departmentT==3){
		$("#isConfirmed").css("display", "block");
	}
	else if(userT==4){
		$("#isConfirmed").css("display", "block");
	}
	$("#Compression").collapse('hide');
	$('#Compression').on('hidden', function () {
		$("#controlIcon").removeClass("icon-chevron-up");
		$("#controlIcon").addClass("icon-chevron-down");
	});
	$('#Compression').on('shown', function () {
		$("#controlIcon").removeClass("icon-chevron-down");
		$("#controlIcon").addClass("icon-chevron-up");
	});
	$("#keyword").val("");
	$('#startTime').val("");
	$('#endTime').val("");
	orignalDepartID = departId;
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
	if (departmentTypeID == 1) {// 市级
		departIdFirset = 0;
		$("#seleteSchoolID").show();
		selceteSchool();
		selceteCourse();
		selceteGrade();
		selceteClass();
		$("#seleteSchoolID").change(function() {
			$("#grade").empty();
			$("#class").empty();
			$("#selecteCourserID").empty();
			departId = $("#seleteSchoolID").val();
			selceteCourse();
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
		selceteCourse();
		selceteGrade();
		selceteClass();
		$("#grade").change(function() {
			gradeId = $("#grade").val();
			selceteClass();
		});

	}
	// 显示数据
	var secondCol = getRequest("secondCol");
	courseId = $("#selecteCourserID").val();
	if (courseId == null)
		courseId = 0;
	viewaction = '../../handler/selectCourse/viewList';
	params = {
			"scoreType" : 0,
			"departId" : departIdFirset,
			"ismainortour" : 0,// 主讲为1，辅导为2，既为主讲又为辅导为1，为0表示教务管理
			"courseId" : courseId,
			"gradeId" : 0,
			"classId" : 0,
			"startTime" : '',
			"endTime" : '',
			"searchWord" : '',
			"searchType" :'',
			"courseType":'',
			"confirmOrNot":0,
			"courseYear":-1,
			"courseTerm":-1,
			"courseTermPhase":-1,
			"courseArtScience":-1,
			"userId" : -1,
			"pageArray" : new Array(),
			"recordPerPage" : 10,
			"currentPage" : 1,
			"secondCol" : secondCol
	};
	// /判断cookie中是否有查询参数
	if ($.cookie("Search") != null && $.cookie("Search") != "") {
		var SearchCookie = $.cookie("Search");
		var SearchPar = JSON.parse(SearchCookie);
		if (SearchPar.secondCol == secondCol) {
			params = SearchPar;
			if (departmentTypeID == 1){
				$('#seleteSchoolID').val(params.departId);
			}
			$('#selecteCourserID').val(params.courseId);
			$('#grade').val(params.gradeId);
			$('#class').val(params.classId);
			$('#searchCourType').val(params.searchCourType);
			$('#searchCourseYear').val(params.searchCourseYear);
			$('#searchCourseYear').val(params.searchCourTerm);
			$('#searchCourTermPhase').val(params.searchCourTermPhase);
			$('#searchArtScience').val(params.searchArtScience);
			$('#passOrNot').val(params.passOrNot);
			$('#isConfirmed').val(params.isConfirmed);
			$('#startTime').val(params.startTime);
			$('#endTime').val(params.endTime);
			$('#keyword').val(params.searchWord);
			$('#searchType').val(params.searchType);
		} else {
			// 删除cookie
			document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		}
	}
	initSearch();

	$("#search").click(function() {
		if (departmentTypeID == 1) {
			departId = $("#seleteSchoolID").val();
		} else {
			departId = orignalDepartID;
		}
		searchCourType = $("#searchCourType").val();
		courseId = $("#selecteCourserID").val();
		gradeId = $("#grade").val();
		classId = $("#class").val();
		startTime = $('#startTime').val();
		endTime = $('#endTime').val();
		searchType = $("#searchType").val();
		searchWord = $("#keyword").val();
		passOrNot = $("#passOrNot").val();
		searchCourseYear = $("#searchCourseYear").val();
		searchCourTerm = $("#searchCourTerm").val();
		searchCourTermPhase = $("#searchCourTermPhase").val();
		searchArtScience = $("#searchArtScience").val();
		isConfirmed = $("#isConfirmed").val();
		//
		$('#scoreInquiryTable tbody:nth-child(2)').empty().append(
		"<tr><td colspan=\"5\" class=\"tdcenter\"><img src=\"../img/octocat-spinner.gif\" />正在加载数据...</td></tr>");
		//
		viewaction = '../../handler/selectCourse/viewList';
		params = {
				"confirmOrNot":isConfirmed,
				"courseYear":searchCourseYear,
				"courseTerm":searchCourTerm,
				"courseTermPhase":searchCourTermPhase,
				"courseArtScience":searchArtScience,
				"courseType":searchCourType,
				"scoreType" : passOrNot,
				"searchWord" : searchWord,
				"ismainortour" : 0,// 主讲为1，辅导为2，既为主讲又为辅导为1，为0表示教务管理
				"searchType" : searchType,
				"departId" : departId,
				"courseId" : courseId,
				"startTime" : startTime,
				"endTime" : endTime,
				"userId" : -1,
				"classId" : classId,
				"gradeId" : gradeId,
				"pageArray" : new Array(),
				"recordPerPage" : 10,
				"currentPage" : 1,
				"secondCol" : secondCol
		};
		document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		var cookieString = "Search=";
		cookieString = cookieString + JSON.stringify(params);
		document.cookie = cookieString;
		initSearch();
	});
	$(".closeButton").click(function() {
		$("#exportModal").modal('hide');
	});

});
//获取考核方式
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
//批量导出
function exportScoreSta() {
	if (departmentTypeID == 1) {
		departId = $("#seleteSchoolID").val();
	}
	searchCourType =$("#searchCourType").val();
	isConfirmed = $("#isConfirmed").val();
	courseId = $("#selecteCourserID").val();
	gradeId = $("#grade").val();
	classId = $("#class").val();
	searchType = $("#searchType").val();
	searchWord = $("#keyword").val();
	passOrNot = $("#passOrNot").val();
	searchCourseYear= $("#searchCourseYear").val();
	searchCourTerm = $("#searchCourTerm").val();
	searchCourTermPhase = $("#searchCourTermPhase").val();
	searchArtScience= $("#searchArtScience").val();
	// alert("正在导出，请稍后……");
	$("#exportModal").modal('show');
	window.location.href = "../../handler/selectCourse/exportCountScore?searchType=" + searchType + "&searchWord="
	+ searchWord + "&courseId=" + courseId + "&departId=" + departId + "&courseYear=" + searchCourseYear + "&courseTermPhase=" + searchCourTermPhase  + "&courseTerm=" + searchCourTerm + "&courseArtScience=" + searchArtScience  + "&searchCourType=" + searchCourType+ "&confirmOrNot=" + isConfirmed + "&passOrNot=" + passOrNot + "&gradeId="
	+ gradeId + "&classId=" + classId + "&userId=0";
	//alert(result1);

	/*sendKeepSeeking();*/

}
/*function sendKeepSeeking(){
	//alert(window.opener);
		if(typeof (window.opener) == 'undefined'){
			$("#exportModal").modal('hide');
		}
	window.setTimeout("sendKeepSeeking();",10);//定时发送请求，监测是否已下载
}*/
//加载数据到数据区域
function refreshContent(pageRecords) {
	var time = "";
	$.ajaxSettings.async = false;
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	var tableContent = "";
	if (pageRecords.data.length == 0) {
		tableContent = "<tr><td colspan=\"11\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>";
	} else {
		selecteCourseData = pageRecords.data;
		$.each(pageRecords.data, function(itemIndex, item) {
			var courseCredit = 0;
			var courseRscoState = "";
			if (numberHandler(item.totalScore, 2) >= 60) {
				courseCredit = item.courseCredit;
			}
			if(item.courseType==""||item.courseType==null){
				item.courseType="暂无类别";
			}
			if(item.rscoState==0){
				courseRscoState = "未确认";
			}
			else{
				courseRscoState = "已确认";
			}
			tableContent += "<tr><td>" + startIndex + "</td>" + "<td>" + item.stuNum + "</td>" + "<td>" + item.stuName
			+ "</td>" + "<td>" + item.schoolName + "</td>" + "<td>" + item.stuGrade + "</td>" + "<td>"
			+ item.stuClass + "</td>" + "<td>" + item.courseName + "</td>"+"<td>"+item.courseType+"</td>"
			+ "<td><a type='button' href='#addOrMod' data-toggle='modal' onclick='operate" + "CourseSelecte("
			+ item.selectCourseId + "," + item.tepaId + ")'>" + numberHandler(item.totalScore, 2) + "</a></td>"
			+ "</td><td>" + courseCredit + "</td><td>"+courseRscoState+"</td></tr>";
			startIndex++;
		});
	}
	$('#scoreInquiryTable tbody:nth-child(2)').empty().append(tableContent);
	return false;
}
var aEvmePattern = new Array();
var aevmeThrehold = new Array();
//显示对话框信息 evaMethodsData
function operateCourseSelecte(selectCourseId, tepaId) {
	var baseInfo = "";
	$.each(selecteCourseData, function(itemIndex, item) {
		if (item.selectCourseId == selectCourseId) {
			$.each(evaMethodsData,
					function(itemIndex2, item2) {
				if (item2.evmeId == tepaId) {
					aEvmePattern = item2.evmePattern.split(",");
					aevmeThrehold = item2.evmeThrehold.split(",");
					baseInfo = "<span><strong>课程名称：</strong>" + item.courseName
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "<span><strong>学生姓名：</strong>"
					+ item.stuName + "</span>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ "<span><strong>考核方式：</strong>" + item2.evmeName + "</span>";
					tableContentselecte = 0;
					// showStudySituation("集中学习",aEvmePattern[0],aevmeThrehold[0],"分钟",item.massedLearnTime,item.massedLearnScore);
					var mylearnTime = item.learnTime + "分钟" + "（其中集中学习" + item.massedLearnTime + "分钟）";
					showStudySituation("学习次数", aEvmePattern[0], aevmeThrehold[0], "次", item.learnNum,
							item.learnNumScore);

					showStudySituation("学习时间", aEvmePattern[1], aevmeThrehold[1], "", mylearnTime,
							item.learnTimeScore);

					showStudySituation("论坛讨论", aEvmePattern[2], aevmeThrehold[2], "次", item.bbsDiscuss,
							item.bbsDiscussScore);

					showStudySituation("在线自测", aEvmePattern[2], aevmeThrehold[3], "分", item.testSourceScore,
							item.testScore);

					tableContentselecte += "<tr class='info'>" + "<td>总成绩</td>" + "<td> — </td>"
					+ "<td> — </td>" + "<td> — </td>" + "<td>" + numberHandler(item.totalScore, 2)
					+ "</td>" + "</tr>";
					return false;
				}
			});
			return false;
		}
	});
	$("#baseInfo").html(baseInfo);
	$('#scoreList tbody:nth-child(2)').empty().append(tableContentselecte);
}
