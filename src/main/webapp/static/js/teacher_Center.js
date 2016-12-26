var announcementInfo;
$(document).ready(function() {
	// back To Top
	backToTop();
	//展示左边栏目
	ShowColumn();
	$('#studentCenter').attr("class", "active");
	//获取通知
	getAnnounce();
	//绑定导航点击事件
	clickItem();
	//展示老师授课信息
	showInfo();
	$("#remark-info").click(changeInfo);
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
}

/**
 * show teacher course info
 * @author anny
 */
function showInfo(){    
	$.post("../../handler/course/viewAllCourseByTeacher.do",{ 
	},function(data){
		var Data = data.data.myCourseList;
		var HTML = "";
		for (var i=0;i<Data.length;i++){
			HTML += "<tr><td>"+Data[i].courName+"</td><td>"+Data[i].courCode+"</td><td>"+Data[i].courseStuCount+"</td></tr>";
		}
		$("#teachcourse-info").html(HTML);
	},"json");
}
/**
 * 修改用户信息，界面上发生的变化
 * 
 * @author 张鑫
 */
function changeInfo()
{
	$("input").each(function(index){
		$(this).removeAttr("readonly");
	});

	$("textarea").each(function(index){
		$(this).removeAttr("readonly");
	});
	$("#btn-save").removeAttr("style").css("text-align","center").css("padding-top","130px");
	$("span").filter("[class='info']").html("可编辑").css("color","green").css("font-size","15px");
}
/**
 * 获取老师个人信息并展示
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
		for ( var i = 0; i < 8; i++) {
			var id = "#info-item" + i;
			$(id).html("<input id='"+i+"' type='text' readonly='readonly' value='"+arr[i]+"'><span class='info'></span>");
		}
		$("#info-item8").html("<textarea id='"+8+"' style='width:400px;height:100px;resize:none;' readonly='readonly' value='"+arr[8]+"'/><span class='info'></span>");
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
		//var arr = new Array("class='success'", "class='error'", "class='warning'", "class='info'");
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
