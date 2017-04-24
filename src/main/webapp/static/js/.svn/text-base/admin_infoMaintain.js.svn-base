var announcementInfo;

$(document).ready(function() {
	// getCookie();
	ShowColumn();
	$('#studentCenter').attr("class", "active");
	// getReply(userId);
	// backTotop();
	clickItem();
	showStudyprogressInfo();
	showStudySta();
});
/**
 * 导航栏点击事件监听
 * 
 * @author ZhangXin
 */
function clickItem() {
	$('#user-infomaintain a').click(function(e) {
		e.preventDefault();
		$(this).tab('show');
	});

	$('#user-add a').click(function(e) {
		e.preventDefault();
		$(this).tab('show');
	});
}
/**
 * 获取cookie中的用户信息
 * 
 * @author ZhangXin
 * 
 */
/*
 * function getCookie() { var cookieArray=document.cookie.split("; ");
 * //得到分割的cookie名值对
 * 
 * for (var i=0;i<cookieArray.length;i++){
 * 
 * var arr=cookieArray[i].split("="); //将名和值分开
 *  // if(arr[0]==name)return unescape(arr[1]); //如果是指定的cookie，则返回它的值
 * alert(unescape(arr[1]));
 *  } }
 */
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
			/*
			 * if(null!=info){ var ht = "<h4 class='page-header'><i
			 * class='icon-volume-up'></i>个人资料</h4>"; ht +="<table
			 * class='table'>"+"<tbody>"+ "<tr class='success'>"+ "<td>姓名</td>"+ "<td>"+info.userRealname+"</td>"+ "</tr>"+
			 *  "<tr class='info'>"+ "<td>学号</td>"+ "<td>"+info.userLoginname+"</td>"+ "</tr>"+
			 *  "<tr class='warning'>"+ "<td>性别</td>"+ "<td>"+info.userGender+"</td>"+ "</tr>"+
			 *  "<tr class='error'>"+ "<td>入学年份</td>"+ "<td>"+info.userYearOfEntrance+"</td>"+ "</tr>"+
			 *  "<tr class='success'>"+ "<td>班级名称</td>"+ "<td>"+info.className+"</td>"+ "</tr>"+
			 *  "<tr class='info'>"+ "<td>考籍号</td>"+ "<td>"+info.userCadasExamNum+"</td>"+ "</tr>"+
			 *  "<tr class='warning'>"+ "<td>身份证</td>"+ "<td>"+info.userIdNum+"</td>"+ "</tr>"+
			 *  "<tr class='error'>"+ "<td>电子邮件</td>"+ "<td>"+info.userEmail+"</td>"+ "</tr>"+
			 *  "<tr class='success'>"+ "<td>联系电话</td>"+ "<td>"+info.userPhoneNum+"</td>"+ "</tr>"+
			 *  "<tr class='info'>"+ "<td>通讯地址</td>"+ "<td>"+info.userAddress+"</td>"+ "</tr>"+
			 *  "<tr class='error'>"+ "<td>备注</td>"+ "<td>"+info.userRemark+"</td>"+ "</tr>"+ "</tbody>"; /*
			 * ht += "<dt>学号</dt>" + "<dd>"+info.userLoginname+"</dd>" + "<dt>姓名</dt>" + "<dd>"+info.userRealname+"</dd>"+ "<dt>性别</dt>" + "<dd>"+info.userGender+"</dd>"+ "<dt>入学年份</dt>" + "<dd>"+info.userYearOfEntrance+"</dd>"+ "<dt>班级名称</dt>" + "<dd>"+info.className+"</dd>"+ "<dt>考籍号</dt>" + "<dd>"+info.userCadasExamNum+"</dd>"+ "<dt>身份证</dt>" + "<dd>"+info.userIdNum+"</dd>"+ "<dt>电子邮件</dt>" + "<dd>"+info.userEmail+"</dd>"+ "<dt>联系电话</dt>" + "<dd>"+info.userPhoneNum+"</dd>"+ "<dt>通讯地址</dt>" + "<dd>"+info.userAddress+"</dd>"+ "<dt>备注</dt>" + "<dd>"+info.userRemark+"</dd>";
			 *  }
			 */
			// $("#detinfo").html(ht);
		}
	});
}
/**
 * 展示用户信息
 * 
 * @author ZhangXin
 * @param data
 */
function showUserInfo(data) {
	if (null != data) {
		var arr = new Array(data.userRealname, data.userLoginname, data.userGender, data.userYearOfEntrance,
				data.className, data.userCadasExamNum, data.userIdNum, data.userEmail, data.userPhoneNum,
				data.userAddress, data.userRemark);
		for ( var i = 0; i < 11; i++) {
			var id = "#info-item" + i;
			$(id).html(arr[i] + "");
		}
	}
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
			/*
			 * var ht = "<h4 class='page-header'><i class='icon-volume-up'></i>近期通知</h4>";
			 * if(null == m) { ht+="<div class='hero-unit'><h1>没有信息更新</h1><p>Tagline</p></div>"; }
			 * else{ for(var i=0;i<m.length;i++) { ht += "<dl><dt>"+i+":</dt><dd>"+m[i].noanTitle+"</dd></dl>"; } }
			 * $("div#announcement").html(ht);
			 */
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
		var arr = new Array("class='success'", "class='error'", "class='warning'", "class='info'");
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
					+ "<a href='#myModal' role='button' class='btn btn-small btn-success' data-toggle='modal' name='announce-"
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
		$("#announce-content").html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + announ.noanContent);
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

	// 学生学习情况统计
	function showStudySta() {
		$.ajax({
			type : 'post',
			contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
			url : '../../handler/courseStatistic/student',
			dataType : 'json',
			success : function(data) {
				var tableContent;
				if (data.ret) {
					if ($.isEmptyObject(data.data.staResult)) {
						tableContent = "<tr><td colspan=\"4\"><h4>对不起，没有相关统计数据！</h4></td></tr>";
					} else {
						$.each(data.data.staResult, function(i, val) {
							var totalBbsNum = val.postNum + val.replyNum;
							tableContent += "<tr><td>" + val.courseName + "</td><td>" + val.totalLearningNum
									+ "</td><td>" + val.totalLearningTime + "</td><td>" + totalBbsNum + "<td></tr>";
						});
					}

				} else {
					tableContent = "<tr><td colspan=\"4\"><h5>" + data.errmsg + "</h5></td></tr>";
				}
				$("#studentStudySta tbody:nth-child(2)").append(tableContent);
			}
		});
	}
}
// 学生学习进度统计
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
						tableContent += "<tr><td>" + val.courseName + "</td><td>" + val.massedLearnScore + "</td><td>"
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
