$(function() {

	//获取header数据
	$.ajax({
		type: "post",
		contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		url: "../../handler/sypaController/viewAllSypa",
		async: true,
		data: {
			"pType": 3
		},
		dataType: "json",
		success: function(data) {
			if(data == "" && data == null) {
				console.log("获取数据失败，请联系数据库管理员！");
				return;
			} else {
				var resultData = data.data.result;
				$.each(resultData, function(DataIndex, index) {
					if(index.sypaEnName == "systemName")
						title1 = index.sypaValue;
				});
				$("#header_title").html(title1);
				if($.cookie("user") == null || $.cookie("user") == "") {
					unLoginData = {
						"userName": "登录",
						"userLoginname": ""
					};
					$("#header_operate1")[0].style.display = "block";
					$("#header_operate2")[0].style.display = "none";
				} else {
					$("#header_operate1")[0].style.display = "none";
					$("#header_operate2")[0].style.display = "block";
				}
			}
		}
	});

	//获取名师讲堂数据
	$.getJSON("../../handler/course/viewAllCourseList.do", {}, function(data) {
		var courseList = data.data.courseList;
		for(var i = 0; i < courseList.length; i++) {
			$('<div class="famous_teacher_course item" courCateIds="' + courseList[i].courCateIds + '" courCredit="' +
				courseList[i].courCredit + '" courDescribe="' + courseList[i].courDescribe + '" courTeacherIds="' +
				courseList[i].courTeacherIds + '" courseStuCount="' + courseList[i].courseStuCount + '" courTeacherescrible="' +
				courseList[i].courTeacherescrible + '"><a href="#courseModal" data-toggle="modal"> <img src="../../' +
				courseList[i].courImg + courseList[i].fileName + '" alt="' + courseList[i].courName + '" /></a><p>' +
				courseList[i].courName + '</p></div>').click(function() {
				//点击课程出现课程简介
				//为模态框加载数据
				$("#courCateIds").html($(this).attr("courCateIds"));
				$("#courCredit").html($(this).attr("courCredit"));
				$("#courDescribe").html($(this).attr("courDescribe"));
				$("#courTeacherIds").html($(this).attr("courTeacherIds"));
				$("#courseStuCount").html($(this).attr("courseStuCount"));
				$("#courTeacherescrible").html($(this).attr("courTeacherescrible"));
				$("#courName").html(this.childNodes[1].innerHTML);
				document.getElementById("courImg").src = this.childNodes[0].childNodes[1].src;
			}).appendTo($("#gallery"));
		}
	});

	//获取猜你喜欢数据
	$.ajax({
		type: "post",
		contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		url: "../../handler/courseReom",
		dataType: "json",
		success: function(data) {
			console.log(data);
			if(!data.ret) {
				console.log("数据加载出错，请联系管理员！");
				return;
			}
			var courseList = data.data.result;
			for(var i = 0; i < courseList.length; i++) {
				$('<div class="guess_you_like item" courCateIds="' + courseList[i].courCateIds + '" courCredit="' +
					courseList[i].courCredit + '" courDescribe="' + courseList[i].courDescribe + '" courTeacherIds="' +
					courseList[i].courTeacherIds + '" courseStuCount="' + courseList[i].courChoosedNum + '" courTeacherescrible="' +
					courseList[i].courTeacherescrible + '"><a href="#courseModal" data-toggle="modal"> <img src="../../static/img/homeImages/recourseimg.jpg" alt="' +
					courseList[i].courName + '" /></a><p>' +
					courseList[i].courName + '</p></div>').click(function() {
					//点击课程出现课程简介
					//为模态框加载数据
					$("#courCateIds").html($(this).attr("courCateIds"));
					$("#courCredit").html($(this).attr("courCredit"));
					$("#courDescribe").html($(this).attr("courDescribe"));
					$("#courTeacherIds").html($(this).attr("courTeacherIds"));
					$("#courseStuCount").html($(this).attr("courseStuCount"));
					$("#courTeacherescrible").html($(this).attr("courTeacherescrible"));
					$("#courName").html(this.childNodes[1].innerHTML);
					document.getElementById("courImg").src = this.childNodes[0].childNodes[1].src;
				}).appendTo($("#gallery"));
			}
		}
	});

	//过滤效果
	var $container = $('#gallery');
	$container.isotope({
		itemSelector: '.item',
		animationOptions: {
			duration: 750,
			easing: 'linear',
			queue: false
		}
	});

	// filter items when filter link is clicked
	$('#filters a').click(function() {
		var selector = $(this).attr('data-filter');
		$container.isotope({
			filter: selector
		});
		return false;
	});

	//获取平台应用统计数据
	$.ajax({
		type: "POST",
		contentType: "application/x-www-form-urlencoded;charset=UTF-8", // 发送信息至服务器时内容编码类型
		url: "../../handler/courseStatistic/totalPlatform",
		async: false, // 需要同步请求数据
		dataType: "json",
		success: function(data) {
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
	});

	//获取友情链接
	$.ajax({
		type: "post",
		contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		url: "../../handler/sypaController/viewAllSypa",
		async: false,
		data: {
			"pType": 10
		},
		dataType: "json",
		success: function(data) {
			var result = data.data.result;
			for(var i = 0; i < result.length; i++) {
				$('<a href="' + result[i].sypaValue + '">' + result[i].sypaName + '</a>').appendTo($("#links"));
			}
		}
	});

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
						console.log(data.errmsg);
					}
				}
			});
		}
	});
})