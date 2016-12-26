var announcementInfo;
var isSwitch = true;
$(document).ready(function() {
	switch(userType)
	{
	case '1':isSwitch=false;break;
	case '2':window.location="teacher_Center.html";break;
	case '3':window.location="teachingMana_Center.html";break;
	case '4':window.location="sysman_Center.html";break;
	default:break;
	}
	if(!isSwitch)
	{
		backToTop();// back To Top
		ShowColumn();//显示左边栏目
		getAnnounce();//获取近期通知
		clickItem();//绑定导航点击事件
		showStudyprogressInfo();//展示学习进度
		showStudySta();//展示学习统计信息
		$("#remark-info").click(changeInfo);//获取个人资料
		getInfo(userId);
		$("#btn-save").removeAttr("style").css("display","none");
	}
});
/**
 * 导航栏点击事件监听
 * 
 * @author ZhangXin
 */
function clickItem() {
	$('#user-center a').click(function(e) {
		e.preventDefault();
		$(this).tab('show');
	});

	$('#study-status a').click(function(e) {
		e.preventDefault();
		$(this).tab('show');
	});
}

/**
 * 修改用户信息，界面上发生的变化
 */
function changeInfo()
{
	$("input").each(function(index){
		$(this).removeAttr("readonly");
	});

	$("textarea").each(function(index){
		$(this).removeAttr("readonly");
	});
	$("#btn-save").removeAttr("style").css("text-align","center");
	$("span").filter("[class='info']").html("可编辑").css("color","green").css("font-size","15px");
}

/**
 * 获取学生个人信息并展示
 * 
 * @author ZhangXin
 * @param id
 */
function getInfo(id) {
	$.ajax({
		type : "post",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		url : '../../handler/sc/studentinfo',
		async : false,
		data : {
			userId : id
		},
		dataType : 'json',
		success : function(ba) {
			var info = ba.data.result;
			showUserInfo(info);
		}
	});
}
/**
 * 获取通知信息
 * 
 * @author ZhangXin
 */
function getAnnounce() {
	$.ajax({
		type : "post",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		url : '../../handler/sc/announcement',
		async : false,
		data : {},
		dataType : 'json',
		success : function(result) {
			var m = result.data.result;
			showAnnounce(m);
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
 * 获取帖子回复信息
 * 
 * @author ZhangXin
 * 
 * @param id
 */
function getReply(id) {
	$.ajax({
		type : "post",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		url : '../../handler/sc/reply',
		async : false,
		data : {
			userId : id
		},
		dataType : 'json',
		success : function(result) {
			var m = result.data.result;
			var ht = "<h4 class='page-header'><i class='icon-pencil'></i>我的学习论坛</h4><dl><dt>好友动态：</dt>";
			if (null != m) {
				for ( var i = 0; i < m.length; i++) {
					ht += "<dd>" + m[i].replyUserName + "（回复）:" + m[i].replyContent
					+ "</dd><dd><a href=''>点击查看</a></dd>";
				}
			} else {
				ht += "<div class='hero-unit'><h5>您还没有通知</h5><p>Tagline</p></div>";
			}
			ht += "</dl>";
			$("div#reply").html(ht);
		}
	});
}
/**
 *  学生学习情况统计
 */
function showStudySta() {
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : '../../handler/courseStatistic/student',
		dataType : 'json',
		success : function(data) {
			var tableContent = "";
			if (data.ret) {
				if ($.isEmptyObject(data.data.staResult)) {
					tableContent = "<tr><td colspan=\"4\"><h4>对不起，没有相关统计数据！</h4></td></tr>";
				} else {
					$.each(data.data.staResult, function(i, val) {
						var totalBbsNum = val.postNum + val.replyNum;
						tableContent += "<tr><td>" + val.courseName + "</td><td>" + val.totalLearningNum + "</td><td>"
						+ val.totalLearningTime + "</td><td>" + totalBbsNum + "</td></tr>";
					});
				}
				$("#studentStudySta tbody:nth-child(2)").append(tableContent);
			} else {
				tableContent = "<tr><td colspan=\"4\"><h5>"+data.errmsg+"</h5></td></tr>";
				$("#studentStudySta tbody:nth-child(2)").append(tableContent);
			}

		}
	});
}

/**
 * 学生学习进度统计
 */
function showStudyprogressInfo() {
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : '../../handler/courseStatistic/progress',
		dataType : 'json',
		data:{userId:userId},
		success : function(data) {
			var tableContent;
			if (data.ret) {
				if ($.isEmptyObject(data.data.progressInfo)) {
					tableContent = "<tr><td colspan=\"8\"><h4>对不起，没有相关统计数据！</h4></td></tr>";
				} else {
					$.each(data.data.progressInfo, function(i, val) {
						var totalBbsNum = val.postNum + val.replyNum;
						tableContent += "<tr><td>" + val.courseName+ "</td><td>" + val.massedLearnScore + "</td><td>"
						+ val.learnNumScore + "</td><td>" + val.learnTimeScore + "</td><td>"
						+ val.bbsDiscussScore + "</td><td>" + val.subAssessScore + "</td><td>" + val.testScore
						+ "</td><td>" + val.totalScore + "</td></tr>";
					});
				}

			} else {
				tableContent = "<tr><td colspan=\"8\"><h5>" + data.errmsg + "</h5></td></tr>";
			}
			$("#studyProgress tbody:nth-child(2)").append(tableContent);
		}
	});
}
