var userLoginname = "";
var userName = "";
var userId = 0;
var imgUrl = "";
var userType = 0;
var postId = ""; // 记录本页的postid值
var departId = 0;
var departmentTypeID = 0;
var depaName = ""; // 部門
var userPhoto = ""; // 用户头像
var attachesIds = ''; // 保存附件id,多个id用逗号隔开
var attachesName = ""; // 保存附件name
var attachmentState = 0; // 表示是否有上传的图片(课程管理部分)
var courCoverPictureId = 0; // 记录图片id
// var systemName = "绵阳市普通高中选修课学习平台";
var systemName = " ";
var footerString = "";
var cacarouselTime = 6000;
var actionAuthority;
var openRegister = "否";
var IeMsg = "请使用ie9及以上版本浏览器";
var flag = true;
if(navigator.userAgent.indexOf("MSIE") > 0) {
	if(navigator.userAgent.indexOf("MSIE 6.0") > 0) {
		flag = false;
	}
	if(navigator.userAgent.indexOf("MSIE 7.0") > 0) {
		flag = false;
	}
	if(navigator.userAgent.indexOf("MSIE 8.0") > 0) {
		flag = false;
	}
}
if(!flag) {
	alert(IeMsg);
}
// 全局权限判断
$(document).ajaxComplete(function(event, XMLHttpRequest, settings) {
	if(XMLHttpRequest.status == "602") {
		window.location = "home.html";
		return false;
	} else if(XMLHttpRequest.status == "603") {
		window.location = "cookie_error.html";
		return false;
	} else if(XMLHttpRequest.status == "604") {
		window.location = "authority_error.html";
		return false;
	} else if(XMLHttpRequest.status == "605") {
		window.location = "authority_error.html";
		return false;
	}
});

$(document).ready(function() {
	// 系统信息
	showSystemInfo(); 
	// 加载导航菜单数据
	loadNavibarInfo(); 
	// 幻灯片
	showCarousel(); 
	$('.carousel').carousel({
		interval: cacarouselTime
	});
	//改变系统标题
	changeTitle();
	// 视频唯一开启
	judgecomvideo(); 
	// 加载logo，如果要修改logo的话在这里直接修改
	$(".logo > a > img").attr("src", "/iteach/static/img/iteach-logo-small.png");
	$(".logo-icon > a > img").attr("src", "/iteach/static/img/iteach-logo-small.png");
});

var unLoginData = new Object();

// 获取导航菜单数据
function loadNavibarInfo() {
	if($.cookie("user") == null || $.cookie("user") == "") {
		// cookie里面没有保存user信息
		unLoginData = {
			"userName": "登录",
			"userLoginname": ""
		};
		getNavibar(unLoginData);
	} else {
		// cookie里面保存了user信息
		$.ajax({
			type: 'GET',
			contentType: 'application/x-www-form-urlencoded;charset=UTF-8', // 发送信息至服务器时内容编码类型
			url: '../../handler/home/navibar',
			async: false, // 需要同步请求数据
			dataType: 'json',
			success: function(data, status) {
				var resultData = data.data;
				getNavibar(resultData);
				userId = resultData.userId;
				userType = resultData.userType;
				departId = resultData.userDepaId;
				departmentTypeID = resultData.userDepaType;
				depaName = resultData.depaName;
			}
		});
	}
}

function getNavibar(data) {
	// ajax 获取系统标题 （系统名字为空需要判定）此处需修改 systemName
	$.ajaxSettings.async = false;
	userName = data.userName;
	userLoginname = data.userLoginname;
	// 加载系统标题--yeta
	$("#system_title_by_yeta").html(systemName);

}

function setCookie(cname, cvalue, exdays) {
	var d = new Date();
	d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
	var expires = "expires=" + d.toUTCString();
	document.cookie = cname + "=" + cvalue + "; " + expires;
}
// 读取cookies
function getCookie(name) {
	var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
	if(arr = document.cookie.match(reg))
		return unescape(arr[2]);
	else
		return null;
}

// 分页内容
function paginationPage() {
	var pageHtml = "<ul><li><a>当前第<span id='currentPage' style='color:#669999;'></span>页</a></li>" +
		"<li><a>共<span id='totalPage' style='color:#669999;'></span>页</a></li>" +
		"<li><a>共<span id='recordCount' style='color:#669999;'></span>条</a></li></ul>" +
		"<ul><li><a title='上一组' id='backward' href='#'>«</a></li></ul><ul id='pages'></ul>" +
		"<ul><li><a title='下一组' id='forward' href='#'>»</a></li></ul>";
	$("#pagination").html(pageHtml);
}

// 未登录功能实现
function entrancePerson() {
	var url = window.location.href;
	if(url.indexOf("home.html") >= 0 && url.indexOf("_home.html") < 0) {
		moveto($("#login_div").parent().parent().offset().top);
	} else if(url.indexOf("_home.html") >= 0) {
		return false;
	} else
		window.location.href = "home.html";
}
// login Out
function loginOff() {
	if(userId > 0) {
		$.ajax({
			type: 'POST',
			contentType: 'application/x-www-form-urlencoded;charset=UTF-8', // 发送信息至服务器时内容编码类型
			url: '../../handler/user/loginOff',
			async: false, // 需要同步请求数据
			data: {
				userId: userId
			},
			dataType: 'json',
			success: function(data) {
				if(data.ret) {
					alert("退出成功！谢谢使用！");
					$.cookie("colVideo", null, {
						path: '/'
					});
					$.cookie("user", null, {
						path: '/'
					});
					$.cookie("authCookie", null, {
						path: '/'
					});
					$.cookie("parameters", null, {
						path: '/'
					}); // 清除栏目信息
					$.cookie("Banners", null, {
						path: '/'
					}); // 清除栏目信息
					$.cookie("photo", null, {
						path: '/'
					});
					$.cookie("fileUploadStu", null, {
						path: '/' // 上传学生信息判定是否重复打开浏览器
					});
					$.cookie("StuMyExam", null, {
						path: '/' // 考试期间判定是否重复打开浏览器
					});
					// 清除photo信息
					// 删除cookie
					document.cookie = "Search=;expires=" +
						(new Date(0)).toGMTString();
					userId = 0;
				}
			}
		});
	}
	window.location.href = "home.html";
	return;
}
// 获取缩放效果
function Scaling() {
	var str = location.href;
	var arr = str.split("/");
	var scal_Url = arr[arr.length - 1];
	var scal_Str = "<a class=\"navbar-minimalize minimalize-styl-2 btn btn-primary\" href=\"" +
		scal_Url + "#\" style='margin-top: 22px;'><i class=\"fa fa-bars\"></i></a>";
	$("#scaling").append(scal_Str);
}

// 获取栏目
function ShowColumn() {
	var subcolsde = "";
	var type = "用户";
	switch(userType) {
		case "1":
			type = "学生";
			break;
		case "2":
			type = "教师";
			break;
		case "3":
			type = "教务管理员";
			break;
		case "4":
			type = "系统管理员";
			break;
	}
	Scaling(); // 获取缩放效果
	var firstCol = getRequest("firstCol");
	if(firstCol == null || firstCol == "undefine") {
		firstCol = 0;
	}
	// 请求一下用户信息，后台把用户头像存在cookie里面
	$.ajax({
		type: "post",
		contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		url: '../../handler/user/findUserById',
		async: false,
		data: {
			userId: userId
		},
		dataType: 'json',
		success: function(data) {
			var img;
			if(data.ret) {
				if($.cookie("photo") != null && $.cookie("photo") != "" &&
					$.cookie("photo") != '""') {
					img = $.cookie("photo");
				} else {
					img = "user.jpg";
				}
			} else {
				img = "user.jpg";
			}
			imgUrl = "../../upload/portrait/" + img;
			// 设置用户头像--yeta
			for(var i = 0; i < $(".user_img").length; i++) {
				$(".user_img")[i].src = imgUrl;
			}
		}
	});
	// dropdown profile-element
	var tmp = "firstCol=1&secondCol=14";
	// 设置用户姓名--yeta
	for(var i = 0; i < $(".user_name").length; i++) {
		$(".user_name").html(userName);
	}
	if($.cookie("user") != null && $.cookie("user") != "") {
		// 为空时,才请求后台,避免出现重复请求
		var url = "../../handler/column/loadColumns";
		$.post(
			url, {
				"firstCol": firstCol
			},
			function(data, status) {
				if(data.ret) {
					var colVal = data.data.colval;
					if(colVal != null) {
						// 动态加载左边导航--yeta，以下是个性化模块的导航
						var $accordion = $('<div id="accordion"></div>');
						$.each(colVal,function(index, item) {
							var $group = $('<div class="group" style="zoom:1;"></div>');
							var $h3 = $('<h3><a href="'+item.colurl+'" style="display:block; padding:5px 0; padding-left:25px;"><i class="fa '+item.colicon+'"></i> <span>'+item.colname+'</span></a></h3>');
							var $div = $('<div style="padding: 0;"></div>');
							var $ul = $('<ul style="padding: 0; margin: 0;" class="group_ul"></ul>');
							if(item.subcols != null && item.subcols.length > 0 && item.subcols != "") {
								$.ajaxSettings.async = false;
								var firColId = getRequest("firstCol");
								var secondColID = getRequest("secondCol");
								$.each(item.subcols, function(index, it) {
									if(secondColID == it.colid) {
										var $li = $('<li style="list-style: none;" class="group-active"><a href="'+it.colurl+'?firstCol='+firColId+'&secondCol='+it.colid+
												'" style="display: block; text-decoration: none; padding: 10px 5px 10px 60px; ">'+it.colname+'</a></li>');
									}else{
										var $li = $('<li style="list-style: none;"><a href="'+it.colurl+'?firstCol='+firColId+'&secondCol='+it.colid+
												'" style="display: block; text-decoration: none; padding: 10px 5px 10px 60px;">'+it.colname+'</a></li>').hover(function(){
													$(this).addClass("group-active");
												}, function(){
                                            		$(this).removeClass("group-active");
												});
									}
									$li.appendTo($ul);
								});
							}
							$ul.appendTo($div);
							$h3.appendTo($group);
							$div.appendTo($group);
							$group.appendTo($accordion);
							$accordion.appendTo($("#left_side"));
							
							//以下注释部分是之前的导航，
								/*var isMenuList = '';
								if(item.subcols != null && item.subcols.length > 0 && item.subcols != "") {
									isMenuList = 'class="menu-list"';
								}
								var left_side_html_by_yeta = '<li ' + isMenuList + ' id="firstCol' + item.colid + '"><a href="' + item.colurl +
									'"><i class="fa ' + item.colicon + '"></i><span>' + item.colname + '</span></a>';
								if(item.subcols != null && item.subcols.length > 0 && item.subcols != "") {
									left_side_html_by_yeta += '<ul class="sub-menu-list">';
									$.ajaxSettings.async = false;
									var firColId = getRequest("firstCol");
									var secondColID = getRequest("secondCol");
									$.each(item.subcols, function(index,it) {
											var isActive = "";
											if(secondColID == it.colid) {
												isActive = 'class="active"';
											}
											left_side_html_by_yeta += '<li id="secondCol' + it.colid + '" ' + isActive + '><a href="' + it.colurl +
												'?firstCol=' + firColId + '&secondCol=' + it.colid + '">' + it.colname + '</a></li>';
										});
									left_side_html_by_yeta += '</ul>';
								}
								left_side_html_by_yeta += '</li>';
								$(left_side_html_by_yeta).appendTo($("#left_side"));*/
						});
					}
				} else {
					alert("栏目获取失败！");
				}
			});
	}
	/* $("#side-menu").html(columnHtml); */
	$("#accordion").accordion({
		collapsible: true,
		icons: null,
		heightStyle: "content",
		header: "> div > h3"
	}).sortable({
		axis: "y",
		handle: "h3",
		stop: function(event, ui) {
			// 当排序时，IE 不能注册 blur，所以触发 focusout 处理程序来移除 .ui-state-focus
			ui.item.children("h3").triggerHandler("focusout");
		}
	});
	highlightColum(); // 暂未检查
}

// 获取二级栏目
/*function ShowSubColumn(subColJson) {
	$.ajaxSettings.async = false;
	var firColId = getRequest("firstCol");
	var columnHtml = "<ul class=\"nav nav-second-level\">";
	var secondColID = getRequest("secondCol");
	$.each(subColJson, function(index, item) {
		var isActive = "";
		if(secondColID == item.colid) {
			isActive = "class='active'";
		}
		columnHtml += "<li id='secondCol" + item.colid + "' " + isActive +
			"><a href='" + item.colurl + "?firstCol=" + firColId +
			"&secondCol=" + item.colid + "'>" + item.colname +
			"</a></li>";
	});
	columnHtml += "</ul>";
	return columnHtml;
}*/

function highlightColum() {
	$.ajaxSettings.async = false;
	var columnId = getRequest("firstCol");
	/*if(columnId > 0)
		$("#firstCol" + columnId + "").addClass("active"); // 添加菜单选中样式
	var columnId2 = getRequest("secondCol");
	if(columnId2 > 0) {
		$("#subCols").find('li').each(function() {
			$(this).removeClass();
		});
		$("#secondCol" + columnId2 + "").addClass("active"); // 添加菜单选中样式
	}*/
	$("#accordion").accordion({
		active: columnId -1
	});
	return;
}
// Banners
function showCarousel() {
	var url = "../../handler/banner/viewAllBanner";
	var img = '1.jpg';
	var liHtml = '';
	var imgHtml = '';
	$.get(
		url,
		function(data, status) {
			if(data.data) {
				$.each(data.data.result,function(entryIndex, entry) {
					img = entry.filename;
					if(entryIndex == 0) {
						liHtml += '<li data-target="#carousel-example-generic" data-slide-to="' + entryIndex + '" class="active"></li>';
						imgHtml += '<div class="item active"><img src="../img/banner/' + img + '" alt="' + img + '"></div>'
					} else {
						liHtml += '<li data-target="#carousel-example-generic" data-slide-to="' + entryIndex + '"></li>';
						imgHtml += '<div class="item"><img src="../img/banner/' + img + '" alt="' + img + '"></div>'
					}
				});
			$("#carousel-inner").html(imgHtml);
			$("#carousel-indicators").html(liHtml);
		}
	}, "json");
	return;
}

/**
 * 异步请求简化书写
 * 
 * @param url
 * @param param
 * @param success
 */
function AjaxJson(url, param, success) {
	$.ajax({
		type: "post",
		contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		url: url,
		async: false,
		data: param,
		dataType: 'json',
		success: success
	});
}

function AjaxJsonGet(url, param, success) {
	$.ajax({
		type: "get",
		contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		url: url,
		async: false,
		data: param,
		dataType: 'json',
		success: success
	});
}

function showSystemInfo() {
	// $.ajaxSettings.async = false;
	if($.cookie("parameters") != null && $.cookie("parameters") != "") {
		var parCookie = $.cookie("parameters");
		var parameters = eval(parCookie);
		var footerHtml = "";
		for(var i = 0; i < parameters.length; i++) {
			// id=20的为版本信息
			if(parameters[i].SypaEnName == "copyright") {
				footerString = parameters[i].SypaValue;
				footerHtml += "<div class=\"text-center\">" + footerString +
					"</div>";
				// footerHtml += "<div class='footer text-center'><section>" +
				// footerString + "</section></div>";
				$("#footer").append(footerHtml);
			}

			// id=19 name==systemName
			if(parameters[i].SypaEnName == "systemName") {
				systemName = parameters[i].SypaValue;

			}
			// id=13 name==carouselTime
			if(parameters[i].SypaEnName == "carouselTime") {
				cacarouselTime = parameters[i].SypaValue;
			}
			if(parameters[i].SypaEnName == "register") {
				openRegister = parameters[i].SypaValue;
			}
		}
		return;
	}
}

function changeTitle() {
	$(document).attr("title", systemName); // 修改title值
}

/**
 * 判断common.js里面的
 * 
 */
function judgecomvideo() {
	var url = window.location.href;
	var juid = url.indexOf("learning_video");
	if(juid != -1) {
		// 当前是视频页面
		$(".judgevideo").click(function() {
			$.cookie('colVideo', '0');
		});
	}
}

function visitPostDetail(id) {
	$.ajaxSettings.async = false;
	$.ajax({
		type: 'post',
		contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
		url: "../../handler/bbsPost/addVisitNum",
		dataType: 'json',
		data: {
			bbsPostId: id
		},
		success: function(data) {
			if(data.ret) {
				window.location.href = "bbs_viewPost.html?id=" + id + "&page=1" +
					"&firstCol=3&secondCol=17";
			} else {
				alert("data.errmsg");
			}
		}
	});
	return false;
}