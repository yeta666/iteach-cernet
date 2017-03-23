var title="";
$(document).ready(function() {
	showStatistic();// 获取统计信息
//	loadSystemInfo();// 加载系统信息参数
	showLinks();// 友情链接显示
	showWelcomeMsg();//获取首页顶部栏目条（由于样式不同于其他栏目，单独构建）
	showAPP();//关于首页app下载部分的显示
});

//获取首页栏目条
//首页欢迎信息
function showWelcomeMsg(){
	$.ajax({
		type : "post",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		url : '../../handler/sypaController/viewAllSypa',
		async : true,
		data : {
			"pType" : 3
		},
		dataType : 'json',
		success : function(data) {
			if(data==""&&data==null){
				alert("获取数据失败，请联系数据库管理员！");
				return;
			}else{
			var resultData = data.data.result;
			var welcomeHtml = "";
			
			$.each(resultData, function(DataIndex, index) {
				if(index.sypaEnName=="welcomeMsg")
					welcomeHtml += index.sypaValue;
				if(index.sypaEnName=="systemName")
					title1 = index.sypaValue;
			});
			if ($.cookie("user") == null || $.cookie("user") == "") {
				unLoginData = {
						"userName" : "登录",
						"userLoginname" : ""
				};
				$("#getnav").html("<div class=\"setTitle nav navbar-nav \">"+title1+"</div><ul class=\"nav navbar-nav navbar-right \">"
						+"<li ><a class='' href=\"#\" data-toggle=\"modal\" data-target=\"#myModal\">登录</a></li>" +
								"<li><a href=\"register_home.html\">注册</a></li></ul>");
			} else {
				$("#getnav").html("<div class=\"setTitle nav navbar-nav \">"+title1+"</div><ul class=\"nav navbar-nav navbar-right \">"
						+"<li><a class='changeTitleright' href=\"userCenter.html?firstCol=1&secondCol=14\">进入系统</a></li></ul>");
			}
			$("#welcomemsg").html(welcomeHtml);
		   }
	    }
	});

	if ($.cookie("parameters") != null && $.cookie("parameters") != "") {
		var parCookie = $.cookie("parameters");
		var parameters = eval(parCookie);
		var welcomeHtml = "ttt";
		for ( var i = 0; i < parameters.length; i++) {
			if (parameters[i].sypaEnName == "welcomeMsg") {
				welcomeString = parameters[i].sypaValue;
				welcomeHtml += welcomeString;
				$("#welcomemsg").empty().append(welcomeHtml);
			}
		}
	}
}


//获取统计信息
function showStatistic() {
	var tableHtml1 = "";
	var tableHtml2 = "";
	var tableHtml3 = "";
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
				tableHtml1 = "<li class=\"media\"><div class=\"media-left\"><i class=\"fa fa-user\"></i></div>"
					+"<div class=\"media-body\"><h6 class=\"media-heading\">当前在线人数"+ resultData.onlineUserNum+ "人</h6></div></li>"
					+"<li class=\"media\"><div class=\"media-left\"><i class=\"fa fa-users\"></i></div>"
					+"<div class=\"media-body\"><h6 class=\"media-heading\">注册学员"+ resultData.studentNum+ "人</h6></div></li>"
					+"<li class=\"media\"><div class=\"media-left\"><i class=\"fa fa-male\"></i></div>"
					+"<div class=\"media-body\"><h6 class=\"media-heading\">注册教师"+ resultData.teacherNum+ "人</h6></div></li>";
				
				tableHtml2 = "<li class=\"media\"><div class=\"media-left\"><i class=\"fa fa-bar-chart-o\"></i></div>"
					+"<div class=\"media-body\"><h6 class=\"media-heading\">总学习次数 "+ resultData.learnNum+ "次</h6></div></li>"
					+"<li class=\"media\"><div class=\"media-left\"><i class=\"fa fa-calendar\"></i></div>"
					+"<div class=\"media-body\"><h6 class=\"media-heading\">总学习时间"+ resultData.learnTime+ "分钟</h6></div></li>"
					+"<li class=\"media\"><div class=\"media-left\"><i class=\"fa fa-comments\"></i></div>"
					+"<div class=\"media-body\"><h6 class=\"media-heading\"> 总交流次数"+ resultData.bbsNum+ "次</h6></div></li>";
				
				tableHtml3 = "<li class=\"media\"><div class=\"media-left\"><i class=\"fa fa-book\"></i></div>"
					+"<div class=\"media-body\"><h6 class=\"media-heading\">共开设课程  "+ resultData.courseNum+ "门</h6></div></li>"
					+"<li class=\"media\"><div class=\"media-left\"><i class=\"fa fa-folder-open\"></i></div>"
					+"<div class=\"media-body\"><h6 class=\"media-heading\">共有学习资源 "+ resultData.resourceNum+ "个</h6></div></li>";
			}
			$('#medialist1').html(tableHtml1);
			$('#medialist2').html(tableHtml2);
			$('#medialist3').html(tableHtml3);
		}
	});
}
/**
 * 加载系统参数并且 loadSystemInfo

function loadSystemInfo() {
	if ($.cookie("parameters") == null || $.cookie("parameters") == "") {
		$.ajax({
			type : "post",
			contentType : "application/x-www-form-urlencoded;charset=UTF-8",
			url : '../../handler/sypaController/viewAllSypa',
			async : false,
			data : {
				"pType" : 3
			},
			dataType : 'json',
			success : function(data) {
				
			}
		});
	}
} */


function showLinks() {
	$.ajax({
		type : "post",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		url : '../../handler/sypaController/viewAllSypa',
		async : false,
		data : {
			"pType" : 10
		},
		dataType : 'json',
		success : function(data) {
			if(data==""&&data==null){
				alert("获取数据失败，请联系数据库管理员！");
				return;
			}else{
				var resultData = data.data.result;
				var linkHtml = "";
				$.each(resultData, function(DataIndex, index) {
					linkHtml += "<li class='liststyle-doc'><a target='_blank' id='" + index.sypaId + "' name='" + index.sypaName
					+ "' href='http://" + index.sypaValue + "'>" + index.sypaName + "</a></li>";
				});
				$("#link").html(linkHtml);
			}
			
		}
	});
}

function showAPP(){
	$("#android").popover({
		trigger:'manual',
		placement : 'top', //placement of the popover. also can use top, bottom, left or right
		title : '<div style="text-align:center; font-size:14px;">Android手机</div>', //this is the top title bar of the popover. add some basic css
		html: 'true', //needed to show html of course
		content : '<div><img class="appImg" src="../img/app/192-168-1-106.png"></div>', //this is the content of the html box. add the image here or anything you want really.
		animation: false
	}).on("mouseenter", function () {
		var _this = this;
		$(this).popover("show");
		$(this).siblings(".popover").on("mouseleave", function () {
			$(_this).popover('hide');
		});
	}).on("mouseleave", function () {
		var _this = this;
		setTimeout(function () {
			if (!$(".popover:hover").length) {
				$(_this).popover("hide");
			}
		}, 100);
	});
	$("#androidHD").popover({
		trigger:'manual',
		placement : 'top', //placement of the popover. also can use top, bottom, left or right
		title : '<div style="text-align:center; font-size:14px;">Android平板</div>', //this is the top title bar of the popover. add some basic css
		html: 'true', //needed to show html of course
		content : '<div><img class="appImg" src="../img/app/server-androidHD.png"></div>', //this is the content of the html box. add the image here or anything you want really.
		animation: false
	}).on("mouseenter", function () {
		var _this = this;
		$(this).popover("show");
		$(this).siblings(".popover").on("mouseleave", function () {
			$(_this).popover('hide');
		});
	}).on("mouseleave", function () {
		var _this = this;
		setTimeout(function () {
			if (!$(".popover:hover").length) {
				$(_this).popover("hide");
			}
		}, 100);
	});
	$("#apple").popover({
		trigger:'manual',
		placement : 'top', //placement of the popover. also can use top, bottom, left or right
		title : '<div style="text-align:center; font-size:14px;">iPhone手机</div>', //this is the top title bar of the popover. add some basic css
		html: 'true', //needed to show html of course
		content : '<div><img class="appImg" src="../img/app/server-iTeach_iPhone.png"></div>', //this is the content of the html box. add the image here or anything you want really.
		animation: false
	}).on("mouseenter", function () {
		var _this = this;
		$(this).popover("show");
		$(this).siblings(".popover").on("mouseleave", function () {
			$(_this).popover('hide');
		});
	}).on("mouseleave", function () {
		var _this = this;
		setTimeout(function () {
			if (!$(".popover:hover").length) {
				$(_this).popover("hide");
			}
		}, 100);
	});
	$("#appleHD").popover({
		trigger:'manual',
		placement : 'top', //placement of the popover. also can use top, bottom, left or right
		title : '<div style="text-align:center; font-size:14px;">iPad平板</div>', //this is the top title bar of the popover. add some basic css
		html: 'true', //needed to show html of course
		content : '<div><img class="appImg" src="../img/app/server-iTeach_ipad.png"></div>', //this is the content of the html box. add the image here or anything you want really.
		animation: false
	}).on("mouseenter", function () {
		var _this = this;
		$(this).popover("show");
		$(this).siblings(".popover").on("mouseleave", function () {
			$(_this).popover('hide');
		});
	}).on("mouseleave", function () {
		var _this = this;
		setTimeout(function () {
			if (!$(".popover:hover").length) {
				$(_this).popover("hide");
			}
		}, 100);
	});
}