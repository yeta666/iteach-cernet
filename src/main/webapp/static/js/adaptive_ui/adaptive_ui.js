$(function() {
	//此js的目的是所有个性化模块的初始化

	// Menu Toggle
	jQuery('.toggle-btn').click(function() {
		var body = jQuery('body');
		var bodyposition = body.css('position');
		if (bodyposition != 'relative') {
			if (!body.hasClass('left-side-collapsed')) {
				body.addClass('left-side-collapsed');
				jQuery(this).addClass('menu-collapsed');
			} else {
				body.removeClass('left-side-collapsed chat-view');
				jQuery(this).removeClass('menu-collapsed');
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
		} else {
			jQuery('body').css({
				left : '',
				marginRight : ''
			});
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
				'<a href="#" onclick="loginOff();"><i style="font-size: 16px;" class="fa fa-sign-out"></i> <span>安全退出</span></a>'+
		'</div></li>';
	
	//屏幕 < 768px 时，左边导航显示的用户操作
	$("#user_menu_left").children().remove();
	$("#user_menu_left").html(user_menu).menu();
	
	//顶部导航右边显示的用户操作
	$("#user_menu_right").children().remove();
	var $user_menu_right = $('<li style="height: 48px;">'+
			'<div style="height: 100%; width: 100px;">'+
				'<img style="height: 100%;" class="user_img" src="'+imgUrl+'" alt="user" />'+
				'<span class="user_name" style="margin-left:7px;">'+userName+'</span>'+
			'</div>'+
		'</li>');
	var $ul = $('<ul style="width: 120px;"></ul>');
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
	
	var userType = JSON.parse($.cookie("userType"));
	console.log(userType);
	for(var i = 0; i < userType.length; i++){
		if(userType[i].type == "活跃型"){
			//活跃型对应的模块展示
		}else if(userType == "沉思型"){
			//沉思型对应的模块展示
		}else if(userType == "感悟型"){
			//感悟型对应的模块展示
		}else if(userType == "自觉型"){
			//自觉型对应的模块展示
		}else if(userType == "视觉型"){
			//视觉型对应的模块展示
		}else if(userType == "言语型"){
			//言语型对应的模块展示
		}else if(userType == "序列型"){
			//序列型对应的模块展示
		}else if(userType == "综合型"){
			//综合型对应的模块展示
		}
	}
})