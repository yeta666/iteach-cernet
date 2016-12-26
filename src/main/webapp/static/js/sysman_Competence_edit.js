/**
 * @author anny
 * @version 用户管理-权限管理-主函数
 */
// 一级栏目数目
var startIndex = 1;
// 修改的角色id
var roleId = 0;
var SubColId = new Array();
$(document).ready(function() {
	roleId = getRequest("roleId");
	backToTop(); // back To Top
	ShowColumn(); // 列表显示
	showContent();// 加载栏目
	// 加载角色对应的权限
	loadRoleInfo();
	// 绑定基本信息提交按钮
	$('#saveRole').click(function() {
		// 检验基本信息格式问题
		if (validateRoleInfo()) {
			url = "../../handler/authority/editAuthority";
			// 将基本信息发送到服务端
			$.post(url, getParams(), function(data) {
				var state = 'success';
				var header = "修改成功！";
				var errorMsg = "";
				if (data.ret) {
					window.history.go(-1);
				} else {
					state = "error";
					header = "修改失败！";
					errorMsg = data.errmsg;
				}
				infoNotice(state, header, errorMsg, $('#data-grid'));
			}, "json");
		}
		return false;
	});
	// 绑定返回按钮
	$('#cancel').click(function() {
		window.history.go(-1);
		return false;
	});
});
// 加载数据
function showContent() {
	var url = "../../handler/authority/queryAllCompetence";
	$.ajaxSettings.async = false;
	$("#mainTable").empty();
	var columnHtml = "";
	$.get(url, function(data, status) {
		$.each(data.data.json, function(itemIndex1, item1) {
			var isFirst = true;
			columnHtml += "<tr><th rowspan='" + item1.subcols.length + "'><input type='checkbox' id='" + item1.colid
					+ "' value='" + item1.colid + "' name='1Record" + startIndex + "'  onchange='chooseall1("
					+ startIndex + ")'>" + item1.colname + "</th>";
			$.each(item1.subcols, function(itemIndex2, item2) {
				SubColId.push(item2.colid);
				if (!isFirst)
					columnHtml += "<tr>";
				columnHtml += "<td><input type='checkbox' id='" + item2.colid + "' value='" + item1.colid
						+ "' name='2Record" + startIndex + "' onchange='chooseall2(" + startIndex + ",this)'>"
						+ item2.colname + "</td><td>";
				isFirst = false;
				if (item2.subcols != null) {
					$.each(item2.subcols, function(itemIndex3, item3) {
						columnHtml += "<label class='checkbox-inline'> <input type='checkbox' id='" + item3.colid
								+ "' name='3Record" + startIndex + "' value='" + item2.colid + "' firstCol='"
								+ item1.colid + "' secondCol='" + item2.colid
								+ "'  coltype='action' onchange='chooseall3(" + startIndex + "," + item2.colid
								+ ",this)'>" + item3.colname + "</label>";
					});
				}
				columnHtml += "</td></tr>";
			});
			startIndex++;
			$("#mainTable").html(columnHtml);
		});
	});
	return false;
}
function loadRoleInfo() {
	var roleName = decodeURI(getRequest("roleName"));
	var roleDesc = decodeURI(getRequest("roleDesc"));
	if (roleDesc == undefined || roleDesc == null) {
		roleDesc = "";
	}
	$('#rolename').val(roleName);
	$('#roledesc').val(roleDesc);
	var url = "../../handler/authority/queryRoleActions";
	$.post(url, {
		"roleId" : roleId
	}, function(data) {
		var resultData = data.data.actions;
		$.each(resultData, function(actionIndex, action) {
			$('input[id="' + action + '"]').attr("checked", "true");
		});
		// 加载功能模块及其对应的操作控制
	}, "json");
	showCkeckbox();
}
function showCkeckbox() {
	for ( var i = 1; i < startIndex; i++) {
		for ( var j = 0; j < SubColId.length; j++) {
			chooseall3(i, SubColId[j]);
		}
		chooseall(i);
	}
}
function validateRoleInfo() {
	var isValid = true;
	// 验证角色名称是否为空
	if ($('#rolename').val() == '') {
		infoNotice("error", "", "角色不能为空");
		isValid = false;
	}
	if (getAuthority() == null || getAuthority() == "") {
		infoNotice("error", "", "请选择模块权限对应的操作权限");
		isValid = false;
	}
	return isValid;
}
function getParams() {
	var params = new Object();
	params.roleName = $('#rolename').val();
	params.roleDesc = $('#roledesc').val();
	params.roleId = roleId;
	// 获取选择的栏目权限信息
	params.authority = getAuthority();
	return $.param(params, true);
}
function getAuthority() {
	var params = new Array();
	// 遍历功能
	$('#mainTable tr').each(function() {
		var actionId = new Array();
		var cli = {
			"firstCol" : 0,
			"secondCol" : 0,
			"actionId" : actionId
		};
		var isCheck = false;
		// 遍历该模块下所有操作的多选框
		$(this).find('input[coltype="action"]').each(function() {
			cli.firstCol = $(this).attr("firstCol");
			cli.secondCol = $(this).attr("secondCol");
			if ($(this).is(":checked")) {
				actionId.push($(this).attr('id').split("+")[1]);
				cli.actionId = actionId;
				isCheck = true;
			}
		});
		if (isCheck)
			params.push($.param(cli, true));
	});
	return params;

}
function chooseall(startIndex) {
	var name1 = "1Record" + startIndex;
	var name2 = "2Record" + startIndex;
	// 1 class
	var isAllChecked1 = true;
	for ( var i = 0; i < $('input[name="' + name2 + '"]').length; i++) {
		if (!$($('input[name="' + name2 + '"]')[i]).is(":checked")) {
			isAllChecked1 = false;
			break;
		}
	}
	if (isAllChecked1)
		$('input[name="' + name1 + '"]').prop("checked", true);
	else
		$('input[name="' + name1 + '"]').prop("checked",false);
}
function chooseall1(startIndex) {
	var name1 = "1Record" + startIndex;
	var name2 = "2Record" + startIndex;
	var name3 = "3Record" + startIndex;
	// 绑定全选/取消全选按钮
	$('input[name="' + name2 + '"]').each(function() {
		if ($('input[name="' + name1 + '"]').is(":checked")) {
			$(this).prop("checked", true);
		} else
			$(this).prop("checked",false);
	});
	// 绑定全选/取消全选按钮
	$('input[name="' + name3 + '"]').each(function() {
		if ($('input[name="' + name1 + '"]').is(":checked")) {
			$(this).prop("checked", true);
		} else
			$(this).prop("checked",false);
	});
//	chooseall(startIndex);
}
function chooseall2(startIndex, obj) {
	if ($('input[id="' + obj.id + '"]').is(":checked")) {
		$('input[value="' + obj.id + '"]').each(function() {
			$(this).prop("checked", true);
		});
	} else {
		$('input[value="' + obj.id + '"]').each(function() {
			$(this).prop("checked",false);
		});
	}
	chooseall(startIndex);
}
function chooseall3(startIndex, parentId) {
	var isAllChecked = true;
	for ( var int = 0; int < $('input[value="' + parentId + '"]').length; int++) {
		if (!$($('input[value="' + parentId + '"]')[int]).is(":checked")) {
			isAllChecked = false;
			break;
		}
	}
	if (isAllChecked)
		$('input[id="' + parentId + '"]').prop("checked", true);
	else
		$('input[id="' + parentId + '"]').prop("checked",false);
	chooseall(startIndex)
}