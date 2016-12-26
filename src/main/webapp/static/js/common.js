var userLoginname = "";
var userName = "";
var userId = 0;
var userType = 0;
var postId = ""; // 记录本页的postid值
var departId = 0;
var departmentTypeID = 0;
var depaName = "";// 部門
var userPhoto = "";// 用户头像
var attachesIds = ''; // 保存附件id,多个id用逗号隔开
var attachesName = ""; // 保存附件name
var attachmentState = 0;// 表示是否有上传的图片(课程管理部分)
var courCoverPictureId = 0;// 记录图片id
//var systemName = "绵阳市普通高中选修课学习平台";
var systemName = " ";
var footerString = "";
var cacarouselTime = 6000;
var actionAuthority;
var openRegister = "否";
var IeMsg="请使用ie9及以上版本浏览器";  
var flag = true;  
if(navigator.userAgent.indexOf("MSIE")>0)  
{   
    if(navigator.userAgent.indexOf("MSIE 6.0")>0)  
    {   
    flag = false;  
    }   
    if(navigator.userAgent.indexOf("MSIE 7.0")>0)  
    {  
    flag = false;  
    }  
    if(navigator.userAgent.indexOf("MSIE 8.0")>0)  
    {  
    flag = false;  
    }  
}  
if(!flag){  
    alert(IeMsg); 
} 
//全局权限判断
$(document).ajaxComplete(function(event, XMLHttpRequest, settings) {
	if (XMLHttpRequest.status == "602") {
		window.location = "home.html";
		return false;
	} else if (XMLHttpRequest.status == "603") {
		window.location = "cookie_error.html";
		return false;
	} else if (XMLHttpRequest.status == "604") {
		window.location = "authority_error.html";
		return false;
	} else if (XMLHttpRequest.status == "605") {
		window.location = "authority_error.html";
		return false;
	}
});

$(document).ready(function() {
	
	showSystemInfo();// 系统信息
	loadNavibarInfo();// 加载导航菜单数据
	showCarousel();// 幻灯片
	$('.carousel').carousel({
		interval : cacarouselTime
	});
	changeTitle();
	var url = window.location.href;
	if (url.indexOf("home.html") < 0) {
		$("#pictures").hide();
	}
	judgecomvideo();// 视频唯一开启
	
	notification();
	
	//百度统计
	/*
	var _hmt = _hmt || [];
	(function() {
		var hm = document.createElement("script");
		hm.src = "//hm.baidu.com/hm.js?912b0875fd57ba87f84775a183a18a3b";
		var s = document.getElementsByTagName("script")[0]; 
		s.parentNode.insertBefore(hm, s);
	})();
	 */
	
	//网站换肤
	    var $li = $("ul.styles li");  //查找到元素
	    $li.click(function () {   //给元素添加事件
	        switchSkin(this.id);//调用函数
	    });
	    //保存Cookie完毕以后就可以通过Cookie来获取当前的皮肤了
	    var cookie_skin = $.cookie("MyCssSkin");     //获取Cookie的值
	    if (cookie_skin) {                          //如果确实存在Cookie
	        switchSkin(cookie_skin);     //执行
	    }
	    else{
	    	$(".styles li#color").addClass("cur");
	    }
	});
	function switchSkin(skinName) {   
	    $("#" + skinName).addClass("cur").siblings().removeClass("cur");  //当前&lt;li&gt;元素选中 //去掉其他同辈&lt;li&gt;元素的选中
	    $("#cssfile").attr("href", "../css/ncss/" + skinName + ".css"); //设置不同皮肤
	    $.cookie("MyCssSkin", skinName, { path: '/', expires: 10 });  //保存Cookie
	return false;
	}



var unLoginData = new Object();
//获取导航菜单数据
function loadNavibarInfo() {
	if ($.cookie("user") == null || $.cookie("user") == "") {
		unLoginData = {
				"userName" : "登录",
				"userLoginname" : ""
		};
		getNavibar(unLoginData);
	} else {
		if ($.cookie("photo") != null && $.cookie("photo") != "") {
			var parCookie = $.cookie("photo");
			userPhoto = parCookie;
		} else {
			userPhoto = "user.jpg";
		}
		$.ajax({
			type : 'GET',
			contentType : 'application/x-www-form-urlencoded;charset=UTF-8',// 发送信息至服务器时内容编码类型
			url : '../../handler/home/navibar',
			async : false, // 需要同步请求数据
			dataType : 'json',
			success : function(data, status) {
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
	//ajax 获取系统标题 （系统名字为空需要判定）此处需修改    systemName
	
	
	$.ajaxSettings.async = false;
	userName = data.userName;
	userLoginname = data.userLoginname;
	var notLogin = "onclick='entrancePerson()'";
	var userHtml = "";
	var userCenterHtml = "";
	if (userLoginname.length > 0) {
		notLogin = "";
		userCenterHtml = "<li><a class='judgevideo' href='userCenter.html?firstCol=1&secondCol=14'><i class='icon-home  icon-white'></i> 个人中心</a></li>";
		userHtml = "<ul class='dropdown-menu'><li><a class='judgevideo' href='modifyPassword_home.html'><i class='icon-cog'></i>密码设置</a></li><li><a class='judgevideo' href='personalInformation_home.html'><i class='icon-wrench'></i>个人资料</a></li><li class='divider'></li>"
			+ "<li><a class='judgevideo' href='aboutSystem_home.html'><i class='icon-eye-open'></i>关于系统</a></li><li><a class='judgevideo' href='aboutHelp_home.html'><i class='icon-question-sign'></i>联机帮助</a></li><li class='divider'></li>"
			+ "<li><a href='#' onclick='loginOff();'><i class='icon-off'></i>退出</a></li></ul>";
	} else {
		userHtml = "<ul class='dropdown-menu'><li><a class='judgevideo' href='aboutSystem_home.html'><i class='icon-eye-open'></i>关于系统</a></li><li><a class='judgevideo' href='aboutHelp_home.html'><i class='icon-question-sign'></i>联机帮助</a></li>"
			+ "</ul>";
	}
	
	//加载系统标题--yeta
	$("#system_title_by_yeta").html(systemName);
	//加载系统标题--yeta
	
	//暂时未做登录与未登录区分
	var headerHtml = "<li class=\"navbar-left\"style=\"margin-top:-17px\"><a href=\"home.html\" title=\"首页\"><h1 >"+ systemName+ "</h1></a></li><li class=\"navbar-right\"><a href='#' onclick='loginOff();'><i class=\"fa fa-sign-out\" href='#' onclick='loginOff()'></i> 退出</a></li>"
	+"<li class=\"navbar-right\"><ul class='styles'><li class='' id='color' ></li><li class='' id='green' ></li><li class='' id='red' ></li></ul></li>";
	 
	$("#navibartitle").html(headerHtml);
}
function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}
//读取cookies
function getCookie(name)
{
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
 
    if(arr=document.cookie.match(reg))
 
        return unescape(arr[2]);
    else
        return null;
}


//分页内容
function paginationPage() {
	var pageHtml = "<ul><li><a>当前第<span id='currentPage' style='color:#669999;'></span>页</a></li>"
		+ "<li><a>共<span id='totalPage' style='color:#669999;'></span>页</a></li>"
		+ "<li><a>共<span id='recordCount' style='color:#669999;'></span>条</a></li></ul>"
		+ "<ul><li><a title='上一组' id='backward' href='#'>«</a></li></ul><ul id='pages'></ul>"
		+ "<ul><li><a title='下一组' id='forward' href='#'>»</a></li></ul>";
	$("#pagination").html(pageHtml);
}

//未登录功能实现
function entrancePerson() {
	var url = window.location.href;
	if (url.indexOf("home.html") >= 0 && url.indexOf("_home.html") < 0) {
		moveto($("#login_div").parent().parent().offset().top);
	} else if (url.indexOf("_home.html") >= 0) {
		return false;
	} else
		window.location.href = "home.html";
}
//login Out
function loginOff() {
	if (userId > 0) {
		$.ajax({
			type : 'POST',
			contentType : 'application/x-www-form-urlencoded;charset=UTF-8',// 发送信息至服务器时内容编码类型
			url : '../../handler/user/loginOff',
			async : false, // 需要同步请求数据
			data : {
				userId : userId
			},
			dataType : 'json',
			success : function(data) {
				if (data.ret) {
					alert("退出成功！谢谢使用！");
					$.cookie("colVideo", null, {
						path : '/'
					});
					$.cookie("user", null, {
						path : '/'
					});
					$.cookie("authCookie", null, {
						path : '/'
					});
					$.cookie("parameters", null, {
						path : '/'
					});// 清除栏目信息
					$.cookie("Banners", null, {
						path : '/'
					});// 清除栏目信息
					$.cookie("photo", null, {
						path : '/'
					});
					$.cookie("fileUploadStu", null, {
						path : '/'  //上传学生信息判定是否重复打开浏览器
					});
					$.cookie("StuMyExam", null, {
						path : '/'  //考试期间判定是否重复打开浏览器
					});
					// 清除photo信息
					// 删除cookie
					document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
					userId = 0;
				}
			}
		});
	}
	window.location.href = "home.html";
	return;
}
//获取缩放效果
function Scaling(){
	var str = location.href;
	var arr = str.split("/");
	var scal_Url = arr[arr.length-1];
	var scal_Str = "<a class=\"navbar-minimalize minimalize-styl-2 btn btn-primary\" href=\""+scal_Url+"#\" style='margin-top: 22px;'><i class=\"fa fa-bars\"></i></a>";
	$("#scaling").append(scal_Str);
}

//获取栏目
function ShowColumn() {
	var subcolsde = "";
	var type = "用户";
	switch (userType) {
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
	Scaling();//获取缩放效果
	var firstCol = getRequest("firstCol");
	if (firstCol == null || firstCol == "undefine") {
		firstCol = 0;
	}
	var imgUrl = "../../upload/portrait/user.jpg";
	if (userPhoto != "" && userPhoto.length > 0) {
		imgUrl = "../../upload/portrait/" + userPhoto;
	}
	if (!CheckImgExists(imgUrl)) {
		imgUrl = "../../upload/portrait/user.jpg";
	}//dropdown profile-element
	var tmp = "firstCol=1&secondCol=14";
	//
	//设置用户头像--yeta
	for(var i = 0; i < $(".user_img").length; i++){
		$(".user_img")[i].src = imgUrl;
	}
	//设置用户姓名--yeta
	for(var i = 0; i < $(".user_name").length; i++){
		$(".user_name").html(userName);
	}
	//
	var columnHtml = "<li class=\"nav-header\"><div class=\"dropdown profile-element fadeInRight m-t-xs\"><span><img alt=\"image\" class=\"photo\" src='" + imgUrl + "' /></div><p class='userInfo'/></span> <a data-toggle=\"dropdown\" class=\"dropdown-toggle\" href=\"home.html#\"> <span class=\"clear\"> " +
	"<span class=\"text-muted text-xs block\">"+ userName +" <b class=\"caret\"></b></span></span></a><ul class=\"dropdown-menu animated fadeInRight m-t-xs\"><li><a href='modifyPassword_home.html?"+tmp+"'>密码设置</a></li>"
	+"<li><a href='personalInformation_home.html?"+tmp+"'>个人资料</a></li><li><a href='aboutSystem_home.html?"+tmp+"'>关于系统</a></li><li><a href='aboutHelp_home.html?"+tmp+"'>联机帮助</a></li><li class='divider'></li><li><a href='#' onclick='loginOff();'>安全退出</a></li></ul></div><div class=\"logo-element\">"+ userName +" </div></li>";
	if ($.cookie("user") != null && $.cookie("user") != "") {
		// 为空时,才请求后台,避免出现重复请求
		var url = "../../handler/column/loadColumns";
		$.post(url, {
			"firstCol" : firstCol
		}, function(data, status) {
			if (data.ret) {
				var colVal = data.data.colval;
				if (colVal != null) {
					//动态加载左边导航--yeta
					$.each(colVal, function(index, item) {
						var isMenuList = '';
						if (item.subcols != null && item.subcols.length > 0 && item.subcols != "") {
							isMenuList = 'class="menu-list"';
						}
						var left_side_html_by_yeta = '<li '+isMenuList+' id="firstCol'+item.colid
						+'"><a href="'+item.colurl+'"><i class="fa '+item.colicon
						+'"></i><span>'+item.colname
						+'</span></a>';
						if (item.subcols != null && item.subcols.length > 0 && item.subcols != "") {
							left_side_html_by_yeta += '<ul class="sub-menu-list">';
							$.ajaxSettings.async = false;
							var firColId = getRequest("firstCol");
							var secondColID = getRequest("secondCol");
							$.each(item.subcols, function(index, it) {
								var isActive = "";
								if(secondColID == it.colid){
									isActive = 'class="active"';
								}
								left_side_html_by_yeta += '<li id="secondCol'+it.colid+'" '+isActive+'><a href="'+it.colurl
								+'?firstCol='+firColId+'&secondCol='+it.colid+'">'+it.colname
								+'</a></li>';
							});
							left_side_html_by_yeta += '</ul>';
						}
						left_side_html_by_yeta += '</li>';
						$(left_side_html_by_yeta).appendTo($("#left_side"));
					});
					//动态加载左边导航--yeta
							
					$.each(colVal, function(index, item) {//fa fa-globe 
						columnHtml += "<li id='firstCol" + item.colid + "'><a href='" + item.colurl+ "'><i class=\""+ item.colicon +"\"></i> <span class=\"nav-label\">" + item.colname + "</span>";
						if (item.subcols != null && item.subcols.length > 0 && item.subcols != "") {
							columnHtml += "<span class=\"fa arrow\"></span>";
						}
						columnHtml += "</a> ";
						if (item.subcols != null && item.subcols.length > 0 && item.subcols != "") {
							subcolsde = ShowSubColumn(item.subcols);
						}
						columnHtml +=  subcolsde + "</li>";
						subcolsde = "";
					});
				}
			} else {
				alert("栏目获取失败！");
			}
		});
	}
	$("#side-menu").html(columnHtml);
	highlightColum();//暂未检查
}

//获取二级栏目
function ShowSubColumn(subColJson) {
	$.ajaxSettings.async = false;
	var firColId = getRequest("firstCol");
	var columnHtml = "<ul class=\"nav nav-second-level\">";
	var secondColID = getRequest("secondCol");
	$.each(subColJson, function(index, item) {
		var isActive = "";
			if(secondColID==item.colid){
				isActive = "class='active'";
			}
			columnHtml += "<li id='secondCol" + item.colid + "' " + isActive + "><a href='" + item.colurl + "?firstCol="
			+ firColId + "&secondCol=" + item.colid + "'>" + item.colname + "</a></li>";
	});
	columnHtml += "</ul>";
	return columnHtml;
}

function highlightColum() {
	$.ajaxSettings.async = false;
	var columnId = getRequest("firstCol");
	if (columnId > 0)
		$("#firstCol" + columnId + "").addClass("active"); // 添加菜单选中样式
	var columnId2 = getRequest("secondCol");
	if (columnId2 > 0) {
		$("#subCols").find('li').each(function() {
			$(this).removeClass();
		});
		$("#secondCol" + columnId2 + "").addClass("active"); // 添加菜单选中样式
	}
	return;
}
//Banners
function showCarousel() {
	console.log("cookie中是否保存了Banners? " + $.cookie("Banners"));
	if ($.cookie("Banners") != null && $.cookie("Banners") != "") {
		var bannerCookie = $.cookie("Banners");
		var banners = eval(bannerCookie);
		var carouselHtml = "<div class='carousel slide' id='carousel-1'>";
		var img = "1.jpg";
		var liHtml = "<ol class='carousel-indicators'>";
		var imgHtml = "<div class='carousel-inner'>";
		$.each(banners, function(entryIndex, entry) {
			console.log(entryIndex + " " + entry);
			img = entry;
			if (entryIndex == 0) {
				liHtml += "<li class='active' data-slide-to='" + entryIndex + "' data-target='#carousel-1'></li>";
				imgHtml += "<div class='item active'><img alt='" + img + "' src='../img/banner/" + img + "' /></div>";
			} else {
				liHtml += "<li data-slide-to='" + entryIndex + "' data-target='#carousel-1'></li>";
				imgHtml += "<div class='item'><img alt='" + img + "' src='../img/banner/" + img + "' /></div>";
			}
		});
		liHtml += "</ol>";
		imgHtml += "</div>";
		var endHtml = "</div>";
		$("#pictures").append(carouselHtml + liHtml + imgHtml + endHtml);
	} else {
		var url = "../../handler/banner/viewAllBanner";
		var carouselHtml = "<div class='carousel slide' id='carousel-1'>";
		var img = "1.jpg";
		var liHtml = "<ol class='carousel-indicators'>";
		var imgHtml = "<div class='carousel-inner'>";
		$.get(url, function(data, status) {
			console.log(data);
			console.log("从后台获取轮播图片出错，可能是数据库的原因！");
			if(data.data){
				$.each(data.data.result, function(entryIndex, entry) {
					img = entry.filename;
					if (entryIndex == 0) {
						liHtml += "<li class='active' data-slide-to='" + entryIndex + "' data-target='#carousel-1'></li>";
						imgHtml += "<div class='item active'><img alt='" + img + "' src='../img/banner/" + img
						+ "' /></div>";
					} else {
						liHtml += "<li data-slide-to='" + entryIndex + "' data-target='#carousel-1'></li>";
						imgHtml += "<div class='item'><img alt='" + img + "' src='../img/banner/" + img + "' /></div>";
					}
				});
				liHtml += "</ol>";
				imgHtml += "</div>";
				var endHtml = "</div>";
				$("#pictures").append(carouselHtml + liHtml + imgHtml + endHtml);
			}
		}, "json");
	}
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
		type : "post",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		url : url,
		async : false,
		data : param,
		dataType : 'json',
		success : success
	});
}

function AjaxJsonGet(url, param, success) {
	$.ajax({
		type : "get",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		url : url,
		async : false,
		data : param,
		dataType : 'json',
		success : success
	});
}
function showSystemInfo() {
	// $.ajaxSettings.async = false;
	if ($.cookie("parameters") != null && $.cookie("parameters") != "") {
		var parCookie = $.cookie("parameters");
		var parameters = eval(parCookie);
		var footerHtml = "";
		for ( var i = 0; i < parameters.length; i++) {
			// id=20的为版本信息
			if (parameters[i].SypaEnName == "copyright") {
				footerString = parameters[i].SypaValue;
				footerHtml +="<div class=\"text-center\">"+footerString+"</div>";
				//footerHtml += "<div class='footer text-center'><section>" + footerString + "</section></div>";
				$("#footer").append(footerHtml);
			}
		
			// id=19 name==systemName
			if (parameters[i].SypaEnName == "systemName") {
				systemName = parameters[i].SypaValue;
				
			}
			// id=13 name==carouselTime
			if (parameters[i].SypaEnName == "carouselTime") {
				cacarouselTime = parameters[i].SypaValue;
			}
			if (parameters[i].SypaEnName == "register") {
				openRegister = parameters[i].SypaValue;
			}
		}
		return;
	}
}
function changeTitle() {
	$(document).attr("title", systemName);// 修改title值
}

/**
 * 判断common.js里面的
 * 
 */
function judgecomvideo() {
	var url = window.location.href;
	var juid = url.indexOf("learning_video");
	if (juid != -1) {
		$(".judgevideo").click(function() {
			$.cookie('colVideo', '0');
		});
	}
}

//讨论回复提示功能
function notification(){
	var viewaction = '../../handler/bbsPost/viewBbsPostList';
	var params = {
			"queryType" : 2,
			"departId" : -1,
			"courseId" : -1,
			"userId" : userId,
			"pageArray" : new Array(),
			"recordPerPage" : 10,
			"currentPage" : 1,
			"secondCol" : getRequest("secondCol")
	};
	$.post(viewaction, params, function(data) {
		var resultHTML = '';
		var totalNum = 0;
		if(data.ret){
			var result = data.data.pageData[0].data;
			for(var i = 0; i < data.data.totalCount; i++){
				if(result[i].bbpoReplynum > 0){
					totalNum++;
					resultHTML += '<li class="new"><a href="javascript:onclick=visitPostDetail('+result[i].bbpoId
					+');"><span class="label label-info"><i class="fa fa-check"></i></span><span class="name">您在课程 ['
					+result[i].courseName+'] 创建的讨论有了回复</span></a></li>';
				}
			}
		}else{
			resultHTML += '获取失败！';
		}
		$($($($(".notification-menu > li")[2]).children()[0]).children()[1]).html(totalNum);
		$($($($(".notification-menu > li")[2]).children()[1]).children()[1]).html(resultHTML);
	});
}

function visitPostDetail(id) {
	$.ajaxSettings.async = false;
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/bbsPost/addVisitNum",
		dataType : 'json',
		data : {
			bbsPostId : id
		},
		success : function(data) {
			if (data.ret) {
				window.location.href = "bbs_viewPost.html?id="+ id+"&page=1"+"&firstCol=3&secondCol=17";
			} else {
				alert("data.errmsg");
			}
		}
	});
	return false;
}



