var studyStaData = null;
var evaMethodsData = null;
$(document).ready(function() {
	 Highcharts.setOptions({
	        colors: ['#008E8E'] //#1ab394
	    });
	colIds = urlColHtml();
	ShowColumn();
	getAnnounce();
	selceteEvaluate();// 获取考核方式
	switch (userType) {
	case '1':
		dealStudent();
		break;
	case '2':
		dealTeacher();
		break;
	case '3':
		dealTeacherMama();
		break;
	case '4':
		dealSysman();
		break;
	default:
		break;
	}
});

function dealStudent() {
	$("#student").removeAttr("style");
	showStudyprogressInfo();
}

function dealTeacher() {
	$("#teacher").removeAttr("style");
	showInfo();
}

function dealTeacherMama() {
	$("#teacherMana").removeAttr("style");
	showInfo1();
}

function dealSysman() {
	var statData = "";
	showuserStat(statData, 3);
	getserverStatus();
	$("#serverStatus").removeAttr("style");

}

/**
 * 获取通知
 */
function getAnnounce() {
	$.ajax({
		type : "post",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		url : '../../handler/noticeAnnouncement/viewNoticeAnnouncementList.do',
		async : false,
		data : {
			"startTime" : "",
			"endTime" : "",
			"keyword" : "",
			"pageArray" : 1,
			"recordPerPage" : 10
		},
		dataType : 'json',
		success : function(result) {
			var m = result.data.pageData;
			showAnnounce(m);
		}
	});
}

function getserverStatus() {
	$.ajax({
		type : "post",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		url : '../../handler/sc/serverStatus',
		async : false,
		data : {},
		dataType : 'json',
		success : function(result) {
			var msg = result.data.searchResult;
			$("#osname").append(msg.osname + " V" + msg.osversion + " " + msg.ostype);
			$("#jdkpath").append(msg.jdkpath + " V" + msg.jdkversion);
			$("#userhome").append(msg.userhome);
			$("#appdir").append(msg.appdir);
			$("#servipaddr").append(msg.servipaddr);
			$("#freememory").append(msg.freememory + " MB");
			$("#totalmemory").append(msg.totalmemory + " MB");
			$("#maxmemory").append(msg.maxmemory + " MB");
			$("#appsize").append(msg.appsize);
			$("#uploadsize").append(msg.uploadsize);
			$("#swfsize").append(msg.swfsize);
		}
	});
}

/**
 * 通知的展示
 * 
 * @author ZhangXin
 */
function showAnnounce(data) {
	var html = "";
	if (null == data || data.length == 0) {
		html += "";
	} else {
		var arr = new Array("class='info'", "class='info'", "class='info'", "class='info'");
		for ( var i = 0; i < data.length; i++) {
			var time = new Date(data[i].noanPubtime);
			html += "<tr "
				+ arr[i % 4]
			+ "><td>"
			+ (i + 1)
			+ "</td><td>"
			+ data[i].noanTitle
			+ "</td><td>"
			+ time.toLocaleString()
			+ "</td><td>"
			+ "<a href='#myModal' role='button' class='btn btn-small btn-info' data-toggle='modal' name='announce-"
			+ i + "'>查看</a>" + "</td></tr>";
		}
	}

	announcementInfo = data;
	$("#announcement-table").html(html);
	$("a[name^='announce-']").click(function() {
		var id = $(this).attr("name");
		id = id.split("announce-")[1];
		var HTML = "";
		var foo = parseInt(id);

		var announ = announcementInfo[foo];
		HTML += announ.noanTitle;
		$("#announce-title").html(HTML);
		$("#announce-content").html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + announ.noanContent);
		var time = new Date(announ.noanPubtime);
		$("#announce-time").html(time.toLocaleString());
	});
}

/**
 * 个人中心通知列表
 * 
 * @author 杨春明
 */
function showAnnounce(data) {
	var html = "";
	if (data != null || data != "") {
		var noticedata = data[0].data;
		if (data.length > 0 && noticedata != 0) {

			if (noticedata.length <= 5 && noticedata.length > 0) {
				for ( var i = 0; i < noticedata.length; i++) {
					var time = noticedata[i].noanPubtime;
					html += "<p><span class='label label-warning'>"+(i+1)+"</span>"+" <a class=\"liststyle-doc\"><a href=\"#myModal\" data-toggle=\"modal\" name=\"announce-"
						+ i + "\">" + titleFormat(noticedata[i].noanTitle, 30) +"</a><small> [ " + time
						+ "]</small> </a></p>";
				}
			} else {
				for ( var i = 0; i < 5; i++) {
					var time = noticedata[i].noanPubtime;
					html += "<p><span class='label label-warning'>"+(i+1)+"</span>"+" <a class=\"liststyle-doc\"><a href=\"#myModal\" data-toggle=\"modal\" name=\"announce-"
						+ i + "\">" + titleFormat(noticedata[i].noanTitle, 30) + "</a><small> [ " + time
						+ "]</small> </a></p>";
				}
			}
		} else {
			html = "<a>暂无最新通知</a>";
		}
	}
	$("#usernotice").empty().append(html);
	$("a[name^='announce-']").click(function() {
		var id = $(this).attr("name");
		id = id.split("announce-")[1];
		var HTML = "";
		var foo = parseInt(id);

		var announ = noticedata[foo];
		HTML += announ.noanTitle;
		$("#announce-title").html(HTML);
		$("#announce-content").html("&nbsp;&nbsp;&nbsp;&nbsp;" + announ.noanContent);
		var time = announ.noanPubtime;
		$("#announce-time").html(announ.userName + " 于 " + time + " 发布");
	});
}

/**
 * 个人中心统计信息
 * 
 * @author : 杨春明
 * @param statData:统计数据，usertype:用户类型1，学生，2老师，3管理员
 */
function showuserStat(statData, usertype) {

	var userhtml = "";
	if (usertype == 1) {
		userhtml += "<h4 class=\"page-header\"  >学习统计</h4>";
		userhtml += "<span><i class=' icon-book'></i> 学习课程 " + statData[0] + " 门</span><br />";
		userhtml += "<span><i class=' icon-calendar'></i> 学习时间 " + statData[1] + " 分钟</span><br />";
		userhtml += "<span><i class=' icon-headphones'></i> 共计学习 " + statData[2] + " 次</span><br />";
		userhtml += "<span><i class=' icon-comment'></i> 课程讨论次数 " + statData[3] + "次</span><br />";
	} else if (usertype == 2) {
		userhtml += "<h4 class=\"page-header\"  >课程辅导统计</h4>";
		userhtml += "<span><i class=' icon-book'></i> 辅导课程 " + statData[0] + " 门</span><br />";
		userhtml += "<span><i class=' icon-user'></i> 学生总数 " + statData[1] + " 人</span><br />";
		userhtml += "<span><i class=' icon-headphones'></i> 论坛发帖 " + statData[2] + " 条</span><br />";
		userhtml += "<span><i class=' icon-comment'></i> 论坛回帖 " + statData[3] + "条</span><br />";
	} else if (usertype == 3) {
		userhtml += "<h4 class=\"page-header\">平台应用统计</h4>";
		$.ajax({
			type : "POST",
			contentType : "application/x-www-form-urlencoded;charset=UTF-8",// 发送信息至服务器时内容编码类型
			url : '../../handler/courseStatistic/totalPlatform',
			async : false, // 需要同步请求数据
			data : {},
			dataType : 'json',
			success : function(data) {
				if (data.ret) {
					var resultData = data.data.staResult;
					userhtml = userhtml + "<span><i class=' icon-volume-up'></i> 当前在线 " + resultData.onlineUserNum
					+ " 人</span><br />" + "<span><i class=' icon-user'></i> 注册学员 " + resultData.studentNum
					+ " 人</span><br />" + "<span><i class=' icon-user'></i> 注册教师 " + resultData.teacherNum
					+ " 人</span><br />" + "<span><i class=' icon-headphones'></i> 总学习次数 " + resultData.learnNum
					+ " 次</span><br />" + "<span><i class=' icon-calendar'></i> 总学习时间 " + resultData.learnTime
					+ " 分钟</span><br />" + "<span><i class=' icon-comment'></i> 总交流次数 " + resultData.bbsNum
					+ " 次</span><br />" + "<span><i class=' icon-book'></i> 共开设课程 " + resultData.courseNum
					+ " 门</span><br />" + "<span><i class=' icon-facetime-video'></i> 共有学习资源 "
					+ resultData.resourceNum + " 个</span>";
				}
			}
		});
	}

	$("#userStatics").empty().append(userhtml);
}

/**
 * 学生学习情况统计 author:杨春明
 */
function showStudySta(courData, scoreData) {
	$('#stuStaBar').highcharts({
		chart : {
			type : 'column'
		},
		credits : {
			enabled : false
		},
		title : {
			text : '各课程当前成绩分布'
		},
		xAxis : {
			categories : courData
		},
		yAxis : {
			min : 0,
			title : {
				text : '分'
			},
		},
		legend : {
			enabled : false
		},
		tooltip : {
			formatter : function() {
				return '<b>' + this.x + '</b><br/>' + this.series.name + ': ' + this.y + '<br/>';
			}
		},
		series : [ {
			name : '成绩',
			data : scoreData,			
			dataLabels : {
				enabled : true,
				rotation : -90,
				color : '#fff',
				align : 'right',
				x : 4,
				y : 10,
				style : {
					fontSize : '13px',
					fontFamily : 'Verdana, sans-serif'
				}
			}
		} ]
	});
}
/**
 * 计算当前考核项的分数 author：杨春明
 */
function compuScore(currnum, maxnum, percent) {
	var currpercent = 0;
	if (maxnum > 0)
		currpercent = currnum / maxnum;
	if (currpercent > 1)
		currpercent = 1;
	return currpercent * percent;
}
/**
 * 获取考核方式 author：郭海蓉
 */
function selceteEvaluate() {
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/evaluateMethod/viewAllEvaMethods",
		dataType : 'json',
		async : false,
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

/**
 * 学生学习进度统计 author：郭海蓉 modify by 杨春明
 */
function showStudyprogressInfo() {
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : '../../handler/courseStatistic/progress',
		dataType : 'json',
		async : false,
		data : {
			userId : userId
		},
		success : function(data) {
			var tableContent;
			if (data.ret) {
				if ($.isEmptyObject(data.data.progressInfo)) {
					tableContent = "<tr><td colspan=\"9\"><h5>对不起，你还没有选择课程学习，请到学习中心选择课程开始学习！</h5></td></tr>";
				} else {
					tableContent = "";
					startIndex = 0;
					studyStaData = data.data.progressInfo;
					var courseData = new Array();
					var scoreData = new Array();
					var stustaData = new Array();
					var totalCredit = 0;//学分总数
					var courseNum = 0;
					var totaltime = 0;
					var studytimes = [ 0, 0 ];// 学习总次数/课程数
					var bbsnum = [ 0, 0 ];// 讨论总次数
					var masstotaltime = [ 0, 0 ];// 集中学习总时间
					var vediototaltime = [ 0, 0 ];// 视频学习总时间
					var testtotal = [ 0, 0 ];// 在线自测总分数
					var subjecttotal = [ 0, 0 ];// 主观评价总分数
					var allscore = 0;// 所有课程总成绩
					var judgeQuery = 0;//判断是否确认
					var courseType = "ABCDFWE3212431";//初始数据库中没有的值
					$.each(data.data.progressInfo, function(i, val) {
						totaltime += (val.learnTimeScore + val.massedLearnScore);
						courseNum++;
						studytimes[0] += val.learnNumScore;
						bbsnum[0] += val.bbsDiscussScore;
						masstotaltime[0] += val.massedLearnScore;
						vediototaltime[0] += val.learnTimeScore;
						testtotal[0] += val.testScore;
						subjecttotal[0] += val.subAssessScore;
						judgeQuery = val.rscoState;
						$.each(evaMethodsData, function(itemIndex, item) {
							if (item.evmeId == val.evaluateMethodId) {

								aevmeThrehold = item.evmeThrehold.split(",");// 满分要求
								var aevmePattern = item.evmePattern.split(",");// 分之比例
								var totalScore = 0;
								var courseCredit = 0;
								if(courseType != val.courseType){
									courseType = val.courseType;
									if (val.courseType==""||val.courseType==null){
										val.courseType = "暂无课程类别";
									}
									tableContent += "<tr class=\"info\"><th colspan=9>"+val.courseType+"</th></tr>";
								}


								tableContent += "<tr>" + "<td>" + courseNum + "</td><td title=\"" + val.courseName
								+ "\"><a href=\"Learning_chooseCourse.html?courId=" + val.courseId
								+ "&selectedType=1&" + colIds + "\">" + titleFormat(val.courseName, 6)
								+ "</a></td>";
								/*
								 * if(aevmePattern[0]==0) tableContent +="<td>-</td>";
								 * else{ tableContent +="<td>"+val.massedLearnScore+"/"+aevmeThrehold[0]+"/"+aevmePattern[0]+"%</td>";
								 * totalScore +=
								 * compuScore(val.massedLearnScore,aevmeThrehold[0],aevmePattern[0]);
								 * masstotaltime[1] ++; }
								 */
								if (judgeQuery == 0){
									if (aevmePattern[0] == 0)
										tableContent += "<td>-</td>";
									else {
										tableContent += "<td>" + val.learnNumScore + "/" + aevmeThrehold[0] + "/"
										+ aevmePattern[0] + "%</td>";
										totalScore += compuScore(val.learnNumScore, aevmeThrehold[0], aevmePattern[0]);
										studytimes[0]++;
									}
									if (aevmePattern[1] == 0)
										tableContent += "<td>-</td>";
									else {
										tableContent += "<td>" + val.learnTimeScore + "/" + aevmeThrehold[1] + "/"
										+ aevmePattern[1] + "%</td>";
										totalScore += compuScore(val.learnTimeScore, aevmeThrehold[1], aevmePattern[1]);
										vediototaltime[1]++;
									}
									if (aevmePattern[2] == 0)
										tableContent += "<td>-</td>";
									else {
										tableContent += "<td>" + val.bbsDiscussScore + "/" + aevmeThrehold[2] + "/"
										+ aevmePattern[2] + "%</td>";
										totalScore += compuScore(val.bbsDiscussScore, aevmeThrehold[2], aevmePattern[2]);
										bbsnum[2]++;
									}
								}
								else {
									tableContent += "<td colspan=3 Style=\"text-align:center;\">成绩已确认</td>";
								}

								if (aevmePattern[3] == 0)
									tableContent += "<td>-</td>";
								else {
									tableContent += "<td>" + val.testScore + "/" + aevmeThrehold[3] + "/"
									+ aevmePattern[3] + "%</td>";
									totalScore += compuScore(val.testScore, aevmeThrehold[3], aevmePattern[3]);
									testtotal[3]++;
								}
								/*
								 * if(aevmePattern[5]==0) tableContent +="<td>-</td>";
								 * else{ tableContent +="<td>"+val.subAssessScore+"/"+aevmeThrehold[5]+"/"+aevmePattern[5]+"%</td>";
								 * totalScore +=
								 * compuScore(val.subAssessScore,aevmeThrehold[5],aevmePattern[5]);
								 * subjecttotal[1] ++; }
								 */
								tableContent += "<td><a title=\"点击查看详情\" href='#myScore' data-toggle='modal' "
									+ "onclick='operateUserCenterStudy(" + val.courseId + ","
									+ val.evaluateMethodId + ")'> " + Math.round(totalScore) + "</a></td>";
								if (Math.round(totalScore) >= 60) {
									courseCredit = val.courseCredit;
								}
								totalCredit += courseCredit;
								tableContent += "<td>" + courseCredit + "</td>";
                                var courseRscoState = "";
								if(val.rscoState==0){
									courseRscoState = "未确认";
                                }
                                else{
                                	courseRscoState = "已确认";
                                }
								tableContent += "<td>" + courseRscoState + "</td></tr>";
								allscore += Math.round(totalScore);
								courseData[i] = val.courseName;
								scoreData[i] = Math.round(totalScore);
								return false;
							}
						});
					});
					tableContent += "<tr class=\"info\"><td> <b>平均/小计</b></td>";
					/*	if (masstotaltime[1] > 0)
						tableContent += "<td>" + (masstotaltime[0] / masstotaltime[1]).toFixed(2) + "/"
						+ masstotaltime[0] + "</td>";
					else*/
					tableContent += "<td>-</td>";
					/*if (studytimes[1] > 0)
						tableContent += "<td>" + (studytimes[0] / studytimes[1]).toFixed(2) + "/" + studytimes[0]
					+ "</td>";
					else*/
					tableContent += "<td>-</td>";
					/*if (vediototaltime[1] > 0)
						tableContent += "<td>" + (vediototaltime[0] / vediototaltime[1]).toFixed(2) + "/"
						+ vediototaltime[0] + "</td>";
					else*/
					tableContent += "<td>-</td>";
					/*if (bbsnum[1] > 0)
						tableContent += "<td>" + (bbsnum[0] / bbsnum[1]).toFixed(2) + "/" + bbsnum[0] + "</td>";
					else*/
					tableContent += "<td>-</td>";
					/*if (testtotal[1] > 0)
						tableContent += "<td>" + (testtotal[0] / testtotal[1]).toFixed(2) + "/" + testtotal[0]
					+ "</td>";
					else*/
					tableContent += "<td>-</td>";
					/*
					 * if(subjecttotal[1]>0) tableContent +="<td>"+(subjecttotal[0]/subjecttotal[1]).toFixed(2)+"/"+subjecttotal[0]+"</td>";
					 * else tableContent += "<td>-</td>";
					 */
					tableContent += "<td>" + changeNum((allscore / courseNum).toFixed(2),1)+ "</td>";
					tableContent += "<td>" + totalCredit + "</td><td>-</td></tr>";
					// <td>"+studytimes+"</td><td>"+vediototaltime+"</td><td>"+bbsnum+"</td><td>-</td><td>-</td><td>-</td></tr>";
					stustaData[0] = courseNum;// 课程数量
					stustaData[1] = totaltime;// 学习总时长
					stustaData[2] = studytimes;
					stustaData[3] = bbsnum;
					// showuserStat(stustaData,1);
					showStudySta(courseData, scoreData);
				}

			} else {
				tableContent = "<tr><td colspan=\"9\"><h5>" + data.errmsg + "</h5></td></tr>";
			}
			$("#viewListTable tbody:nth-child(2)").append(tableContent);
		}
	});
}
/**
 * 更改小数位数
 * @param number  输入的小数
 * @param fractionDigits  需要保留的小数位数的个数
 * @returns {Number}
 */
function changeNum(number,fractionDigits){   
	with(Math){   
		return round(number*pow(10,fractionDigits))/pow(10,fractionDigits);   
	}   
}   

/**
 * 学习进度，成绩查看对话框 author：郭海蓉
 */
function operateUserCenterStudy(courseId, evaluateMethodId) {
	var baseInfo = "";
	$.each(studyStaData, function(itemIndex, item) {
		if (item.courseId == courseId) {
			$.each(evaMethodsData, function(itemIndex2, item2) {
				if (item2.evmeId == evaluateMethodId) {
					aEvmePattern = item2.evmePattern.split(",");
					aevmeThrehold = item2.evmeThrehold.split(",");
					baseInfo = "<span><strong>课程名称：</strong>" + item.courseName
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "<span><strong>考核方式：</strong>"
					+ item2.evmeName + "</span>";
					tableContentselecte = "";
					var mylearnTime = item.learnTimeScore + "分钟" + "（其中集中学习" + item.massedLearnScore + "分钟）";
					// showStudySituation("集中学习",aEvmePattern[0],aevmeThrehold[0],"分钟",item.massedLearnScore,item.real_massedLearnScore);

					showStudySituation("学习次数", aEvmePattern[0], aevmeThrehold[0], "次", item.learnNumScore,
							item.real_learnNumScore);

					showStudySituation("视频教学", aEvmePattern[1], aevmeThrehold[1], "", mylearnTime,
							item.real_learnTimeScore);

					showStudySituation("交互次数", aEvmePattern[2], aevmeThrehold[2], "次", item.bbsDiscussScore,
							item.real_bbsDiscussScore);

					showStudySituation("考试分数", aEvmePattern[3], aevmeThrehold[3], "分", item.testScore,
							item.real_testScore);

					// showStudySituation("教师评分",aEvmePattern[5],aevmeThrehold[5],"分",item.subAssessScore,item.real_subAssessScore);
					tableContentselecte += "<tr class='info'>" + "<td>总成绩</td>" + "<td> — </td>" + "<td> — </td>"
					+ "<td> — </td>" + "<td>" + numberHandler(item.totalScore, 2) + "</td>" + "</tr>";
					return false;
				}
			});
			return false;
		}
	});
	$("#baseInfo").html(baseInfo);
	$('#scoreList tbody:nth-child(2)').empty().append(tableContentselecte);
}

/**
 * show teacher course info
 * 
 * @author anny
 */
function showInfo() {
	$.post("../../handler/course/viewAllCourseByTeacher.do", {}, function(data) {
		var Data = data.data.myCourseList;
		var HTML = "";
		var teachData = new Array();
		var coursenum = 0;
		var stusum = 0;
		for ( var i = 0; i < Data.length; i++) {
			HTML += "<tr><td>" + (coursenum + 1) + "</td><td>" + Data[i].cour_name + "</td><td>" + Data[i].cour_code
			+ "</td><td>" + Data[i].count + "</td></tr>";
			coursenum++;
			stusum = stusum + Data[i].count;
		}
		teachData[0] = coursenum;
		teachData[1] = stusum;
		teachData[2] = 0;
		teachData[3] = 0;
		showuserStat(teachData, 2);
		$("#teachcourse-info").html(HTML);
	}, "json");
}

/**
 * show teacherMana Res info
 * 
 * @author anny
 */
function showInfo1() {
	$.post("../../handler/resource/viewRecentResourceList.do", {},
			function(data) {
		var Data = data.data.recentResoList;
		var HTML = "";
		for ( var i = 0; i < Data.length; i++) {
			var type = "";
			if (Data[i].resoType == 1) {
				type = "链接资源";
			}
			if (Data[i].resoType == 2) {
				type = "文档资源";
			}
			if (Data[i].resoType == 3) {
				type = "视频资源";
			}
			HTML += "<tr><td>" + Data[i].resoTitle + "</td><td>" + type + "</td><td>" + Data[i].courName
			+ "</td></tr>";
		}
		$("#teachMana-Res").html(HTML);
	}, "json");
}