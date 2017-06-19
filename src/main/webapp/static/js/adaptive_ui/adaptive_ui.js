$(function() {
	//此js的目的是所有个性化模块的初始化
	
	if($.cookie("toggle") == "open"){
		//展开
		$("body").removeClass('left-side-collapsed chat-view');
		$(".toggle-btn").removeClass('menu-collapsed');
		//改变footer的宽度
		$("#footer").css("width", $(".wrapper").outerWidth());
		$("#footer").css("left", "240px");
	}else{
		//如果之前用户选择了折叠左边导航栏
		$("body").addClass('left-side-collapsed');
		$(".toggle-btn").addClass('menu-collapsed');
		//改变footer的宽度
		$("#footer").css("width", "100%");
		$("#footer").css("left", "0");
	}
	
	// Menu Toggle
	jQuery('.toggle-btn').click(function() {
		var body = jQuery('body');
		var bodyposition = body.css('position');
		if (bodyposition != 'relative') {
			if (!body.hasClass('left-side-collapsed')) {
				body.addClass('left-side-collapsed');
				jQuery(this).addClass('menu-collapsed');
				$.cookie("toggle", "close");
				//改变footer的宽度
				$("#footer").css("width", "100%");
				$("#footer").css("left", "0");
			} else {
				body.removeClass('left-side-collapsed chat-view');
				jQuery(this).removeClass('menu-collapsed');
				$.cookie("toggle", "open");
				//改变footer的宽度
				$("#footer").css("width", $(".wrapper").outerWidth());
				$("#footer").css("left", "240px");
			}
		} else {
			if (body.hasClass('left-side-show'))
				body.removeClass('left-side-show');
			else
				body.addClass('left-side-show');
		}
	});

	jQuery(window).resize(function() {
		if (jQuery('body').css('position') == 'relative') {
			jQuery('body').removeClass('left-side-collapsed');
			//改变footer的宽度
			$("#footer").css("width", $(".wrapper").outerWidth());
			$("#footer").css("left", "0");
		} else {
			jQuery('body').css({
				left : '',
				marginRight : ''
			});
			//改变footer的宽度
			$("#footer").css("width", $(".wrapper").outerWidth());
			$("#footer").css("left", "240");
		}
	});
	
	//初始化用户操作menu
	var user_menu = 
		'<li><div>'+
			'<a href="personalInformation_home.html?firstCol=1&secondCol=14"><i style="font-size: 16px;" class="fa fa-user"></i> <span>个人资料</span></a>'+
		'</div></li>'+
		'<li><div>'+
				'<a href="modifyPassword_home.html?firstCol=1&secondCol=14"><i style="font-size: 16px;" class="fa fa-cog"></i> <span>密码设置</span></a>'+
		'</div></li>'+
		'<li><div>'+
				'<a href="aboutSystem_home.html?firstCol=1&secondCol=14">&nbsp;<i style="font-size: 16px;" class="fa fa-info"></i> &nbsp;<span>关于系统</span></a>'+
		'</div></li>'+
		'<li><div>'+
				'<a href="aboutHelp_home.html?firstCol=1&secondCol=14"><i style="font-size: 16px;" class="fa fa-google"></i> &nbsp;<span>联机帮助</span></a>'+
		'</div></li>'+
		'<li><div>'+
				'<a href="questionary.html" onclick="$.cookie(\'questionary\', \'user\');"><i style="font-size: 16px;" class="fa fa-book"></i>  <span>填调查表</span></a>'+
		'</div></li>'+
		'<li><div>'+
				'<a href="#" onclick="loginOff();"><i style="font-size: 16px;" class="fa fa-sign-out"></i> <span>安全退出</span></a>'+
		'</div></li>';
	
	//屏幕 < 768px 时，左边导航显示的用户操作
	$("#user_menu_left").children().remove();
	$("#user_menu_left").html(user_menu).menu();
	
	//顶部导航右边显示的用户操作
	$("#user_menu_right").children().remove();
	var $user_menu_right = $('<li style="height: 48px;">'+
			'<div style="height: 100%; width: 120px;">'+
				'<img style="height: 100%;" class="user_img" src="'+imgUrl+'" alt="user" />'+
				'<span class="user_name" style="margin-left: 7px;">'+userName+'</span>'+
			'</div>'+
		'</li>');
	var $ul = $('<ul style="width: 130px;"></ul>');
	$ul.html(user_menu);
	$user_menu_right.append($ul);
	$("#user_menu_right").append($user_menu_right).menu({
		position : {
			at : "left-1 bottom"
		},
		icons : {
			submenu : "ui-icon-caret-1-s"
		}
	});
	
	var userType = $.cookie("userType");
	//console.log(userType);
	if(userType == "活跃型"){
		//活跃型对应的模块展示
		$("#adaptive_ui").attr("href", "../css/adaptive_ui/jquery-ui.theme14.css");
		//$("#adaptive_ui").attr("href", "../css/adaptive_ui/jquery-ui.theme11.css");
	}else if(userType == "沉思型"){
		//沉思型对应的模块展示
		$("#adaptive_ui").attr("href", "../css/adaptive_ui/jquery-ui.theme12.css");
	}else if(userType == "感悟型"){
		//感悟型对应的模块展示
		$("#adaptive_ui").attr("href", "../css/adaptive_ui/jquery-ui.theme13.css");
		//$("#adaptive_ui").attr("href", "../css/adaptive_ui/jquery-ui.theme15.css");
	}else if(userType == "直觉型"){
		//直觉型对应的模块展示
		$("#adaptive_ui").attr("href", "../css/adaptive_ui/jquery-ui.theme16.css");	
	}else if(userType == "default"){
		//默认型对应的模块展示
		$("#adaptive_ui").attr("href", "../css/adaptive_ui/jquery-ui.theme17.css");
	}
	
})