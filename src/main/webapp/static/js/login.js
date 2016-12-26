$(function() {
	//登录验证
	$("#login_form").validate({
		debug: true,
		rules: {
			userLoginname: {
				required: true,
				rangelength: [3, 11]
			},
			userPwd: {
				required: true,
				rangelength: [3, 11]
			},
			authcode: {
				required: true
			}
		},
		messages: {
			userLoginname: {
				required: "请填写此字段。",
				rangelength: "此字段长度为[3,11]。"
			},
			userPwd: {
				required: "请填写此字段。",
				rangelength: "此字段长度为[3,11]。"
			},
			authcode: {
				required: "请填写此字段。"
			}
		},
		highlight: function(element, errorClass, validClass) {
			$(element).addClass(errorClass).removeClass(validClass);
		},
		unhighlight: function(element, errorClass, validClass) {
			$(element).removeClass(errorClass).addClass(validClass);
		},
		submitHandler: function(form) {
			//data = $(form).serialize();
			var userLoginname = $("#userLoginname").val();
			var userPwd = hex_md5($("#userPwd").val()); // 对密码进行MD5码加密
			var authcode = $("#authcode").val();
			console.log(userPwd);
			$.ajax({
				type: 'POST',
				contentType: 'application/x-www-form-urlencoded;charset=UTF-8', // 发送信息至服务器时内容编码类型
				url: '../../handler/user/login',
				async: false, // 需要同步请求数据
				data: {
					userLoginname: userLoginname,
					userPwd: userPwd,
					authcode: authcode
				},
				dataType: 'json',
				success: function(data) {
					if(data.ret) {
						var resultData = data.data.result;
						if(resultData == "success") {
							$.cookie('colVideo', '0'); //控制视频 唯一开启
							window.location.href = "userCenter.html?firstCol=1&secondCol=14";
						} else if(resultData == "passwordError" || resultData == "null") {
							alert("用户或者密码错误！");
						} else if(resultData == "unVerify") {
							alert("此帐号未激活！");
						} else if(resultData == "isLogin") {
							alert("此帐号已登录！");
						}
					} else {
						alert(data.errmsg);
					}
					reloadcode();
				}
			});
		}
	});
})

//重新加载验证码
function reloadcode() {
	document.getElementById('imgcode').src = 'checkcode.jsp?' + Math.random();
}