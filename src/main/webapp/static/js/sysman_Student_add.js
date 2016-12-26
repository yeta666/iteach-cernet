var isExist = 1;
var isClickMethod = false;
var urlPor;
$(document).ready(function(){
	ShowColumn();
	if(departmentTypeID!=3){
		$("#school").removeAttr("style");
		AjaxJson("../../handler/department/viewDepartments", {
			type : 3
		}, showSchoolToSelect);
	}
	else
	{
		updatedata(departId);
	}
	urlPor = document.referrer ;
	$("#submit-btn").click(submit);
	$("#add-snumber").blur(function(){
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
	var Html = "<option value='-1'>=选择学校=</option>";
	for ( var i = 0; i < data1.length; i++) {
		Html += "<option value=" + data1[i].depaId + ">" + data1[i].depaName
		+ "</option>";
	}
	$("#school-select").html(Html);
	$("#school-select").change(function() {
		var depid = $(this).val();
		//当学校被点击，请求年级
		updatedata(depid);
	});
}
/**
 * 发送年级请求
 * @param depid
 */
function updatedata(depid)
{
	AjaxJson("../../handler/register/gradeInfo", {
		depa_id:depid
	}, showGradeToSelect);
}
/**
 * 显示年级
 * @param data
 */
function showGradeToSelect(data)
{
	var data = data.data.gradeInfo;
	var Html = "<option value='-1'>=选择年级=</option>";
	for (var i = 0; i < data.length; i++) {
		Html += "<option value=" + data[i].gradId + ">" + data[i].gradName
		+ "</option>";
	}
	$("#grade-select").html(Html);
	$("#grade-select").change(function() {
		var gradeid = $(this).val();
		//当学校被点击，请求年级
		requestclass(gradeid);
	});
}
/**
 * 请求班级
 * @param classid
 */
function requestclass(gradeid)
{
	AjaxJson("../../handler/register/classesInfo", {
		grade_id:gradeid
	}, showClassToSelect);
}
/**
 * 显示班级
 * @param data
 */
function showClassToSelect(data)
{
	var data1 = data.data.classesInfo;
	var Html = "<option value='-1'>=选择班级=</option>";
	for ( var i = 0; i < data1.length; i++) {
		Html += "<option value=" + data1[i].clasId + ">" + data1[i].clasName
		+ "</option>";
	}
	$("#class-select").html(Html);
}
/**
 * 检查用户名是否可用
 * @param target
 */
function checkStudentNumber(target)
{
	var number = target.val();
	if(number==""||number.length>20||number.length<4){
		$("#user-add-check").html("用户名不符合规则").css({"font-size":"15px","color":"red"});
		return ;
		}else{
			AjaxJson("../../handler/adminUserInfo/isUserExist",{userLoginname:number}, checkUsernameback);
		}
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
	var name1 = $("#add-snumber").val();
	if(""===name1)
	{
		state = 'error';
		header = "提交失败";
		message = "请输入用户名";
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
		message = "密码位数不符合,4-20位";
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
	if(""===realname||realname.length>16)
	{
		state = 'error';
		header = "提交失败";
		message = "真实姓名必填，不能超过16位";
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
	var school = $("#school-select").val();
	if(userType!=3&&school<0)
	{
		state = 'error';
		header = "提交失败";
		message = "请选择学校！";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return;
	}
	if(userType==3)
	{
		school = departId;
	}
	//年级
	var grade = $("#grade-select").val();
	if(grade<0)
	{
		state = 'error';
		header = "提交失败";
		message = "请选择年级！";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return;
	}
	//班级
	var classId = $("#class-select").val();
	if(classId<0)
	{
		state = 'error';
		header = "提交失败";
		message = "请选择班级！";
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
	//考籍号
	var esamnumber = $("#add-examnumber").val();
	if(esamnumber.length>20)
	{
		state = 'error';
		header = "提交失败";
		message = "考籍号不能超过20位";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return;
	}
	//身份证号
	var idcard = $("#add-idnumber").val();
	if(""==idcard||!(idcard.length==18||idcard.length==15))
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

	//入学时间
	var timeTest = /^\d{4}$/;
	var intime = $("#add-startTime").val();
	if(!intime.match(timeTest))
	{
		state = 'error';
		header = "提交失败";
		message = "请输入正确的入学年份";
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
			userDepaId:school,
			userClasId:classId,
			userCadasExamNum:esamnumber,
			userYearOfEntrance:intime,
			userRemark:remark,
			userEmail:email,
			userAddress:address,
			userPhoneNum:phone,
			userLoginname:name1,
			userIdNum:idcard,
			userGender:sex,
			userRealname:realname,
			userPwd:password,
			userType:1,
			userVerify:1,
			userRoles:userRoles
	};

	AjaxJson("../../handler/adminUserInfo/addNewUser", param, addUserCallBack);
}
/**
 * 添加用户返回信息
 */
function addUserCallBack(backInfo)
{
	if(backInfo.data.addNewUserInfo!=null&&
			backInfo.data.addNewUserInfo == "添加用户成功!"){
		alert(backInfo.data.addNewUserInfo);
		window.location=urlPor;
	}else{
		alert(backInfo.data.authenticationInfo);
	}
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
