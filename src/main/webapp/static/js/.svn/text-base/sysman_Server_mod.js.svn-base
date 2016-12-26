/**
 * @author htx
 */
var veseId = 0;
var isOk = true;
var parameter = new Object();
var exp=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
var reg=/^(254|252|248|240|224|192|128|0)\.0\.0\.0$|^(255\.(254|252|248|240|224|192|128|0)\.0\.0)$|^(255\.255\.(254|252|248|240|224|192|128|0)\.0)$|^(255\.255\.255\.(254|252|248|240|224|192|128|0))$/;
$(document).ready(function() {
	ShowColumn(); // 添加左侧菜单显示
	showDept();// 显示所属部门
	verifyInfo();// 验证信息
	addServer();
});
function showServerInfo() {
	$.ajaxSettings.async = false;
	var url = "../../handler/videoServer/selectById";
	veseId = getRequest("veseId");
	$.post(url, {
		"veseId" : veseId
	}, function(data) {
		if (data.ret) {
			var resultData = data.data.data;
			$("#veseName").val(resultData.veseName);
			$("#veseInnerIp").val(resultData.veseInnerIp);
			$("#veseInnerMask").val(resultData.veseInnerMask);
			$("#veseOuterIp").val(resultData.veseOuterIp);
			$("#veseOuterMask").val(resultData.veseOuterMask);
			$("#veseRemark").val(resultData.veseRemark);
			$("#veseLoginName").val(resultData.veseLoginName);
			$("#vesePassword").val(resultData.vesePassword);
			$("#vesePassword1").val(resultData.vesePassword);
			$("#veseDepaId option[value='" + resultData.veseDepart + "']").attr("selected", "selected");
			$("#veseType option[value=" + resultData.veseType + "]").attr("selected", "selected");
			$("#veseState option[value=" + resultData.veseState + "]").attr("selected", "selected");
			$("#veseEnable option[value=" + resultData.veseEnable + "]").attr("selected", "selected");
		}
	}, "json");
}
function showDept() {
	var url = "../../handler/department/viewDepartments";
	var deptHtml = "";
	$.post(url, {
		type : 4
	}, function(data) {
		if (data.ret) {
			var resultData = data.data.departments;
			$.each(resultData, function(itemIndex, item) {
				deptHtml += "<option value=" + item.depaId + ">" + item.depaName + "</option>";
			});
			$("#veseDepaId").append(deptHtml);
		}
		showServerInfo();
	}, "json");
}

/**
 * 删除按钮点击事件
 */
function getParameter() {
	$("#infomsg").empty();
	isOk = true;
	if ($("#veseDepaId").val() <= 0) {
		infoNotice("error", "输入错误！", "服务器所属部门不能为空！", $('#data-grid'));
		isOk = false;
		return false;
	} else
		parameter.veseDepaId = $("#veseDepaId").val();
	if ($("#veseOuterIp").val().replace(/\s/g,"") == '' || $("#veseOuterIp").val().replace(/\s/g,"") == undefined) {
		infoNotice("error", "输入错误！", "客户端可访问IP为空或格式错误！", $('#data-grid'));
		isOk = false;
		return false;
	} else
		parameter.veseOuterIp = $("#veseOuterIp").val();
	if ($("#veseOuterMask").val().replace(/\s/g,"") == '' || $("#veseOuterMask").val().replace(/\s/g,"") == undefined||!reg.test($("#veseOuterMask").val().replace(/\s/g,""))) {
		infoNotice("error", "输入错误！", "客户端IP子网掩码不能为空！", $('#data-grid'));
		isOk = false;
		return false;
	} else
		parameter.veseOuterMask = $("#veseOuterMask").val().replace(/\s/g,"").replace(/\s/g,"");
	if ($("#veseLoginName").val().replace(/\s/g,"") == '' || $("#veseLoginName").val().replace(/\s/g,"") == undefined) {
		infoNotice("error", "输入错误！", "服务器用户名不能为空！", $('#data-grid'));
		isOk = false;
		return false;
	} else
		parameter.veseLoginName = $("#veseLoginName").val();
	if ($("#veseName").val().replace(/\s/g,"") == '' || $("#veseName").val().replace(/\s/g,"") == undefined){
		infoNotice("error", "输入错误！", "服务器名称不能为空！", $('#data-grid'));
		isOk = false;
		return false;
	} else
		parameter.veseName = $("#veseName").val();
	if ( $('#vesePassword').val().replace(/\s/g,"") == "" || $('#vesePassword').val().replace(/\s/g,"") == undefined || $('#vesePassword1').val().replace(/\s/g,"") == ""
			 || $('#vesePassword1').val().replace(/\s/g,"") == undefined || $('#vesePassword').val().replace(/\s/g,"") != $('#vesePassword1').val().replace(/\s/g,"")) {
		$('#checkPass1').html("<img src='../img/cancel.png' width='13px'>两次密码不一致");
		infoNotice("error", "", "两次密码不一致！", $('#data-grid'));
		isOk = false;
		return false;
	} else
		parameter.vesePassword = $('#vesePassword').val();
	parameter.veseRemark = $("#veseRemark").val();
	parameter.veseInnerIp = $("#veseInnerIp").val();
	parameter.veseInnerMask = $("#veseInnerMask").val();
	parameter.veseState = $("#veseState").val();
	parameter.veseEnable = $("#veseEnable").val();
	parameter.veseType = $("#veseType").val();
	parameter.veseId = veseId;
}
function addServer() {
	$("#modButton").click(function() {
		getParameter();// 获取参数
		if (isOk) {
			var url = "../../handler/videoServer/updateServer";
			$.post(url, parameter, function(data) {
				if (data.ret) {
					var state = data.data.state;
					var message = data.data.message;
					infoNotice(state,"", message);
				}
			}, "json");
		}
	});
}
function verifyInfo() {
	$('#vesePassword').blur(function() {
		if ($('#vesePassword').val().replace(/\s/g,"") == "" || $('#vesePassword').val().replace(/\s/g,"") == undefined) {
			$('#checkPass').html("<img src='../img/cancel.png' width='13px'>服务器密码不能为空");
			infoNotice("error", "", "服务器密码不能为空！", $('#data-grid'));
		} else {
			$("#infomsg").empty();
			$('#checkPass').html("<img src='../img/ok.png' width='13px'>");
		}
	});
	$('#vesePassword1').blur(
			function() {
				if ($('#vesePassword1').val().replace(/\s/g,"") == "" || $('#vesePassword1').val().replace(/\s/g,"") == undefined
						|| $('#vesePassword').val().replace(/\s/g,"") != $('#vesePassword1').val().replace(/\s/g,"")) {
					$('#checkPass1').html("<img src='../img/cancel.png' width='13px'>两次密码不一致");
					infoNotice("error", "", "两次密码不一致！", $('#data-grid'));
				} else {
					$("#infomsg").empty();
					$('#checkPass1').html("<img src='../img/ok.png' width='13px'>");
				}
			});
}