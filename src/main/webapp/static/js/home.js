var title="";
$(document).ready(function() {
	showWelcomeMsg();//获取首页顶部栏目条（由于样式不同于其他栏目，单独构建）
	showStatistic();// 获取统计信息
	showLinks();// 友情链接显示
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
				if(index.sypaEnName=="systemName")
					title1 = index.sypaValue;
			});
			if ($.cookie("user") == null || $.cookie("user") == "") {
				unLoginData = {
						"userName" : "登录",
						"userLoginname" : ""
				};
				//加载系统标题
				$("#header_title").html(title1);
				//加载顶部右边功能按钮
				$("#header_menu").html('<li><a href="#login_modal" data-toggle="modal">登录</a></li><li><a href="register_home.html">注册</a></li>');
			} else {
				//加载系统标题
				$("#header_title").html(title1);
				//加载顶部右边功能按钮
				$("#header_menu").html('<li><a href="userCenter.html?firstCol=1&secondCol=14">进入系统</a></li>');
			}
		   }
	    }
	});
}


//获取统计信息
function showStatistic() {
	$.ajax({
		type : "POST",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",// 发送信息至服务器时内容编码类型
		url : '../../handler/courseStatistic/totalPlatform',
		async : false, // 需要同步请求数据
		data : {},
		dataType : 'json',
		success : function(data) {
			if (data.ret) {
				var result = data.data.staResult;
				var statistics_num = $(".statistics_num");
				statistics_num[0].innerHTML = result.onlineUserNum;
				statistics_num[1].innerHTML = result.studentNum;
				statistics_num[2].innerHTML = result.teacherNum;
				statistics_num[3].innerHTML = result.learnNum;
				statistics_num[4].innerHTML = result.learnTime;
				statistics_num[5].innerHTML = result.bbsNum;
				statistics_num[6].innerHTML = result.courseNum;
				statistics_num[7].innerHTML = result.resourceNum;
			}
		}
	});
}

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
				var result = data.data.result;
				for(var i = 0; i < result.length; i++) {
					$('<a href="' + result[i].sypaValue + '">' + result[i].sypaName + '</a>').appendTo($("#links"));
				}
			}
			
		}
	});
}

function showAPP(){
	//app效果
	$("#tablet_pc").popover({
		trigger: 'hover', //鼠标移入触发弹出提示框
		html: true, //开启html 为true的话，data-content里就能放html代码了
		title: "Android平板",
		content: "<img style='width:95px; height:auto;' src='../img/app/server-androidHD.png'>"
	});
	$("#mobile_phone").popover({
		trigger: 'hover', //鼠标移入触发弹出提示框
		html: true, //开启html 为true的话，data-content里就能放html代码了
		title: "Android手机",
		content: "<img style='width:95px; height:auto;' src='../img/app/server-androidHD.png'>"
	});
}


