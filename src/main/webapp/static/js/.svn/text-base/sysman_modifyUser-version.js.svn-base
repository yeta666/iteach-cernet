/**
 * 用户信息修改
 * 
 * @author ZhangXin
 */
var data;//用户接收和发送用户信息
var oldRole;
var modifyUserId = getRequest("modifyUserId");
$(document).ready(function(){
	AjaxJson("../../handler/role/findRoleById",{userId:modifyUserId},backOfUserRoles);
	AjaxJson("../../handler/user/findUserById",{userId:modifyUserId},backOfUserInfo);
	$("#submit-modify").click(function(){
		if($("#userNameNew").val()!="")
		{
			data.userLoginname = $("#userNameNew").val();
		}
		if($("#userRealNameNew").val()!="")
		{
			data.userRealname = $("#userRealNameNew").val();
		}
		if($("input[name='optionRadios']:checked").val()!=undefined)
		{
			data.userGender = $("input[name='optionRadios']:checked").val();
		}
		if($("#passwordNew")!="")
		{
			data.userPwd=hex_md5($("#passwordNew").val());
		}
		if($("#userIdNew").val()!="")
		{
			data.userIdNum = $("#userIdNew").val();
		}
		if($("#emailNew").val()!="")
		{
			data.userEmail = $("#emailNew").val();
		}
		if($("#phoneNumNew").val()!="")
		{
			data.userPhoneNum = $("#phoneNumNew").val();
		}
		if($("#addressNew").val()!="")
		{
			data.userAddress = $("#addressNew").val();
		}
		if($("#remarkNew").val())
		{
			data.userRemark = $("#remarkNew").val();
		}
		AjaxJson("../../handler/adminUserInfo/modefiedUserInfo",data,backOfModify);
	});
	
	$("#cancle-modify").click(function(){
		history.back();
	});
});

/**
 * 返回用户信息
 * @param backInfo
 */
function backOfUserInfo(backInfo)
{
	data = backInfo.data.data;
	$("#userName").val(data.userLoginname);
	$("#userRealName").val(data.userRealname);
	$("#sex").html(data.userGender);
	$("#password").val(data.userPwd);
	$("#userId").val(data.userIdNum);
	$("#email").val(data.userEmail);
	$("#phoneNum").val(data.userPhoneNum);
	$("#address").val(data.userAddress);
	$("#remark").val(data.userRemark);
}
/**
 * 修改用户信息后返回的信息
 * @param backInfo
 */
function backOfModify(backInfo)
{
	if(backInfo.errcode!=0)
	{
		alert("修改失败");
	}
	else
		alert("修改成功");
	history.back();
}

function backOfUserRoles(backInfo)
{
	oldRole = backInfo.data.roles;
	var Html = "";
	for(var i = 0;i<oldRole.length;i++)
	{
		Html+=oldRole[i].roleName+" ";
	}
	$("#roles").val(Html);
}