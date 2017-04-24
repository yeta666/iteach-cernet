var userLoginname = "";
var password = "";
var authcode = "";
$(document).ready(function() {
	if (openRegister == "否") {
		$("#registeBtn").hide();
	} else {
		$("#registeBtn").show();
	}
	var getCookie = $.cookie("user");
	if (getCookie != null) {
		var imgUrl = "../../upload/portrait/user.jpg";
		if (userPhoto != null && userPhoto.length > 0) {
			imgUrl = "../../upload/portrait/"+userPhoto;
		}
		if(!CheckImgExists(imgUrl)){
			imgUrl = "../../upload/portrait/user.jpg";
		}
		/*var loginHtml = "<div class='img-polaroid'><img alt='' src='"
			+ imgUrl
			+ "' /></div><p class='text-info'><h4 class='text-center text-info'>"
			+ userName
			+ "  ("
			+ userLoginname
			+ ")</h4></p><p class='text-center text-info'>您好！欢迎登陆</p>"
			+ "<div class='account-control'>"
			+ "<p class='text-center'>"
			+ "<a class='btn btn-info' href='userCenter.html?firstCol=1&secondCol=14' type='button'>个人中心</a><button id='loginOff' class='btn btn-warning' type='button'>登出</button>"
			+ "</p></div>";
		$("#login_div").html(loginHtml);*/
	}
	// 登陆操作
	$("#loginbut").click(function() {
		login();
	});
	// 退出登陆操作
	$("#loginOff").click(function() {
		loginOff();
	});
	// 注册
	$("#registeBtn").click(function() {
		window.location.href = "register.html";
	});
	// 绑定回车事件
	$("#username,#password,#authcode").keydown(function(e) {
		if (e.keyCode == 13) {
			login();
		}
	});
});
function login() {
	authcode = $("#authcode").val();
	userLoginname = $("#username").val();
	password = $("#password").val();
	if (userLoginname == null || userLoginname == "") {
		$("#username").focus();
		$("#username").attr("placeholder", " 用户名不能为空!");
		$('#loginInfo').attr("class", "text-center text-error");
		$('#loginInfo').html("<strong> 用户名不能为空!</strong>");
		return false;
	}
	if (password == null || password == "") {
		$("#password").focus();
		$("#password").attr("placeholder", " 密码不能为空!");
		$('#loginInfo').attr("class", "text-center text-error");
		$('#loginInfo').html("<strong> 密码不能为空!</strong>");
		return false;
	}
	if (authcode == null || authcode == "") {
		$("#authcode").focus();
		$("#authcode").attr("placeholder", " 验证码不能为空!");
		$('#loginInfo').attr("class", "text-center text-error");
		$('#loginInfo').html("<strong> 验证码不能为空!</strong>");
		return false;
	}
	var informationHtml = "";
	password = hex_md5(password);// 对密码进行MD5码加密
	$.ajax({
		type : 'POST',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',// 发送信息至服务器时内容编码类型
		url : '../../handler/user/login',
		async : false, // 需要同步请求数据
		data : {
			userLoginname : userLoginname,
			authcode : authcode,
			userPwd : password
		},
		dataType : 'json',
		success : function(data) {
			if (data.ret) {
				var resultData = data.data.result;
				if (resultData == "success") {
					informationHtml = "<strong>正在登陆，请稍等……!</strong>";
					$('#loginInfo').attr("class", "text-center text-success");
					$.cookie('colVideo', '0');//控制视频 唯一开启
					window.location.href = "userCenter.html?firstCol=1&secondCol=14";
				} else if (resultData == "passwordError" || resultData == "null") {
					informationHtml = "<strong>用户或者密码错误!</strong>";
					$('#loginInfo').attr("class", "text-center text-error");
				} else if (resultData == "unVerify") {
					informationHtml = "<strong>此帐号未激活!</strong>";
					$('#loginInfo').attr("class", "text-center text-error");
				}else if (resultData == "isLogin") {
					informationHtml = "<strong>此帐号已登录!</strong>";
					$('#loginInfo').attr("class", "text-center text-error");
				}
			} else {
				informationHtml = "<strong>" + data.errmsg + "</strong>";
				$('#loginInfo').attr("class", "text-center text-error");
			}
		}
	});
	$('#loginInfo').html(informationHtml);
	reloadcode();
	return false;
}
//重新加载验证码
function reloadcode() {
	document.getElementById('imgcode').src = 'checkcode.jsp?' + Math.random();
}