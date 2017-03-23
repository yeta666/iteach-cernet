var isExist = 1;
var isClickMethod = false;
var urlPor;
$(document).ready(function(){
	ShowColumn();
	urlPor = document.referrer ;
	AjaxJson("../../handler/department/viewDepartments", {
		type : 4
	}, showSchoolToSelect);
	$("#submit-btn").click(submit);
	$("#add-number").blur(function(){
		checkStudentNumber($(this));
	});
	AjaxJson("../../handler/role/findAllRole",{},backRoles);
	$("#add-role").click(function(){
		if(!isClickMethod){
			isClickMethod = true;
			$("#show-button").attr("class","icon-chevron-up");
			showFoo($(this),$("#content"));
		}
		else
		{
			$("#show-button").attr("class","icon-chevron-down");
			$("#content").removeAttr("style");
			$("#content").attr("style","position: absolute;display: none;");
			isClickMethod = false;
		}
	});
	$("#dropdownToggle").click(function(){
		$("#content").show();
	});
	$("#close").click(function(){
		document.getElementById("content").style.display='none';
	});
});

/**
 * 简化ajax
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
/**
 * 显示学校
 * @param data
 */
function showSchoolToSelect(data) {
	var data1 = data.data.departments;
	var Html = "<option value='-1'>选择工作单位</option>";
	for ( var i = 0; i < data1.length; i++) {
		Html += "<option value=" + data1[i].depaId + ">" + data1[i].depaName
		+ "</option>";
	}
	$("#units-select").html(Html);
}
/**
 * 检查用户名是否可用
 * @param target
 */
function checkStudentNumber(target)
{
	var number = target.val();
	if(number=="")
		return;
	AjaxJson("../../handler/adminUserInfo/isUserExist",{userLoginname:number}, checkUsernameback);
}
function checkUsernameback(data)
{
	if(data.data.status==1)
	{
		isExit = 1;
		$("#user-add-check").html("用户名已存在").css({"font-size":"15px","color":"red"});
	}
	else
	{
		isExist = 2;
		$("#user-add-check").html("用户名可用").css({"font-size":"15px","color":"green"});
	}
}
/**
 * 提交前的验证
 */
function submit()
{
	
	//用户名
	var name1 = $("#add-number").val();
	if(""===name1)
	{
		state = 'error';
		header = "提交失败";
		message = "请输入用户名！";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return;
	}
	if(name1.length>20||name1.length<4)
	{
		state = 'error';
		header = "提交失败";
		message = "用户名必须在4-20位之间";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return;
	}
	//密码
	var password = $("#add-password").val();
	if(""===password)
	{
		state = 'error';
		header = "提交失败";
		message = "请输入密码！";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return;
	}
	if(password.length>20||password.length<4)
	{
		state = 'error';
		header = "提交失败";
		message = "密码位数不符合";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return;
	}
	//再次密码
	var password2 = $("#add-oncemore").val();
	if(""===password2)
	{
		state = 'error';
		header = "提交失败";
		message = "请再次输入密码！";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return;
	}

	if(password!=password2)
	{
		state = 'error';
		header = "提交失败";
		message = "两次密码不一致";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return;
	}
	//真实姓名
	var realname = $("#add-realname").val();
	if(""===realname)
	{
		state = 'error';
		header = "提交失败";
		message = "请输入真实姓名！";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return;
	}
	//性别
	var sex = $("input[type='radio']").attr("value");
	if(""===sex)
	{
		state = 'error';
		header = "提交失败";
		message = "请选择性别！";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return;
	}
	//学校
	var units = $("#units-select").val();
	if(units<=0)
	{
		state = 'error';
		header = "提交失败";
		message = "请选择工作单位！";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return;
	}
	//判定用户角色
	var userRoles = "";
	$("input[name='role-method']").each(function(){
		if($(this).is(":checked"))
			userRoles +=$(this).val()+",";
	});
	if(userRoles=="")
	{
		state = 'error';
		header = "提交失败";
		message = "请勾选用户角色";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return "";
	}
	userRoles = userRoles.substring(0, userRoles.length - 1);
	//alert(userRoles);
	//身份证号
	var idcard = $("#add-idnumber").val();
	if(""===idcard||!(idcard.length==18||idcard.length==15))
	{
		state = 'error';
		header = "提交失败";
		message = "请输入正确身份证号码,15位或18位";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return;
	}
	//电话
	var phone = $("#add-phone").val();
	if(phone.length > 16)
	{
		state = 'error';
		header = "提交失败";
		message = "电话号码过长！";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return;
	}
	//通讯地址
	var address = $("#add-address").val();
	if(address.length>20)
	{
		state = 'error';
		header = "提交失败";
		message = "地址位数为0-20！";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return;
	}

	//邮箱
	var email = $("#add-email").val();
	if(email.charAt("@")<0||email.length>40)
	{
		state = 'error';
		header = "提交失败";
		message = "请输入正确的邮箱，位数不能超过40";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return;
	}

	//备注
	var remark = $("#add-remark").val();
	if(remark.length>400)
	{
		state = 'error';
		header = "提交失败";
		message = "备注在0-400之间";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return;
	}
	if(isExist==1)
		return;
	password = hex_md5(password);
	var param = {
			userLoginname:name1,
			userDepaId:units,
			userRemark:remark,
			userEmail:email,
			userAddress:address,
			userPhoneNum:phone,
			userIdNum:idcard,
			userGender:sex,
			userRealname:realname,
			userPwd:password,
			userType:3,
			userVerify:1,
			userRoles:userRoles
	};
	AjaxJson("../../handler/adminUserInfo/addNewUser", param, addUserCallBack);
}
/**
 * 添加用户返回信息
 */
function addUserCallBack(backInfo)
{ //alert(backInfo);
	if(backInfo.data.authenticationInfo==null ){
		alert(backInfo.data.addNewUserInfo);
	}else{
		alert(backInfo.data.authenticationInfo);
	}
	window.location=urlPor;
}

/**
 * 显示角色
 * @param backinfo
 */
function backRoles(backinfo)
{
	var data = backinfo.data.roles;
	var Html = "<table class='table'><tbody>";
	for(var i = 0;i< data.length;i+=1)
	{
		if(i%3==0)
			Html +="<tr>";
		
		Html += "<td><input type='checkbox' name='role-method' value='"+data[i].roleId+"'/><p>"+data[i].roleName+"</p></td>";
		if(i%3==2 || i == data.length-1)
			Html += "</tr>";
	}
	Html +="</tbody></table>";
	$("#roles").html(Html);

	$("input[name='role-method']").each(function(){
		$(this).bind("click",function(){
			var info = "";
			$("input[name='role-method']").each(function(){
				if($(this).is(":checked"))
					info +=$(this).next().html()+"  ";
			});
			$("#role-input").prop("value",info);

		});
	});
}
/**
 * 弹出框
 * @param This
 * @param target
 */
function showFoo(This,target)
{
	//获取源控件位置
	var xThis = This.offset().left;
	var yThis = This.offset().top;
	var heightThis = This.height();
	var widthThis = This.width();
	//获取目标控件位置
	var widthTarget = target.width();
	var xTarget = xThis + widthThis/2 - widthTarget/2 + 10;
	var yTarget = yThis + heightThis + 10;
	//修改样式
	$("#content").removeAttr("style");
	var style = "position:absolute;top:"+yTarget+"px; left:"+xTarget+"px; display:block;";

	target.attr("style",style);
	target.show();
};