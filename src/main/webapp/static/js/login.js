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
			$.cookie("userId", null);
			var userLoginname = $("#userLoginname").val();
			var userPwd = hex_md5($("#userPwd").val()); // 对密码进行MD5码加密
			var authcode = $("#authcode").val();
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
							$.cookie('colVideo', '0'); //控制视频 唯一开启设为否
							//获取userId
							$.ajax({
								type: 'GET',
								contentType: 'application/x-www-form-urlencoded;charset=UTF-8', // 发送信息至服务器时内容编码类型
								url: '../../handler/home/navibar',
								async: false, // 需要同步请求数据
								dataType: 'json',
								success: function(data, status) {
									if(data.ret){
										var resultData = data.data;
										$.cookie("userId", resultData.userId);
										//获取用户类型
										$.ajax({
											url : "../../handler/userType/getUserType",
											type : "post",
											data : {
												"userId": resultData.userId
											},
											dataType : "json",
											success : function(data) {
												//console.log(data);
												if(data.success){
													var result = JSON.parse(data.message);
													if(result.status){
														$.cookie("userType", result.data);
														$.cookie("toggle", "open");
														$.cookie('colVideo', '0');
														window.location.href = "userCenter.html?firstCol=1&secondCol=14";
													}else{
														//后台计算不出用户类型，计算不出的原因result.message
														if(confirm("请问是否愿意填写一张调查表，好让系统为您进行个性化定制？")){
															//设置是登陆时进入调查表
															$.cookie("questionary", "login");
															//调查表
															window.location.href = "questionary.html";
															return;
														}else{
															//默认定制
															$.cookie("userType", "default");
															window.location.href = "userCenter.html?firstCol=1&secondCol=14";
														}
													}
												}else{
													if(data.statusCode == -1){
														alert("安全服务未开启，请联系管理员！");
														$.cookie("userType", "default");
														window.location.href = "userCenter.html?firstCol=1&secondCol=14";
													}else if(data.statusCode == -2){
														alert("自适应界面服务未开启，请联系管理员！");
														$.cookie("userType", "default");
														window.location.href = "userCenter.html?firstCol=1&secondCol=14";
													}
												}
											},
											error : function(XHR) {
												alert("自适应界面服务没有开启，请联系管理员！错误码： " + XHR.status);
												$.cookie("userType", "default");
												window.location.href = "userCenter.html?firstCol=1&secondCol=14";
											}
										});
									}
								}
							});
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