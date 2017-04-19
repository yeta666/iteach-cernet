/**
 * @author anny
 * @version 用户管理-权限管理-主函数
 */
var roleid;// 修改的角色id
var urlPor="";
$(document).ready(function() {
	urlPor = document.referrer ;
	backToTop();// back To Top
	ShowColumn();// 列表显示
	showContent();// 加载栏目
	// 绑定基本信息提交按钮
	$('#saveRole').click(function() {
		// 检验基本信息格式问题
		if (validateRoleInfo()) {
			url = "../../handler/authority/createRole";
			// 将基本信息发送到服务端
			$.post(url, getParams(), function(data) {
				var state = 'success';
				var header = "添加成功！";
				var errorMsg = "";
				if (data.ret) {
					window.location=urlPor;
				} else {
					state = "error";
					header = "添加失败！";
					errorMsg = data.errmsg;
				}
				// message = "修改后的权限需要重新登录后才能生效！！！";
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
//加载数据
function showContent() {
	var url = "../../handler/authority/queryAllCompetence";
	$.ajaxSettings.async = false;
	$("#mainTable").empty();
	var startIndex = 1;
	$.get(url, function(data, status) {
		$.each(data.data.json, function(itemIndex1, item1) {
			var columnHtml = "";
			var isFirst = true;
			columnHtml += "<tr><th rowspan='" + item1.subcols.length + "'><input type='checkbox' id='" + item1.colid
			+ "' value='" + item1.colid + "' name='1Record" + startIndex + "'  onchange='chooseall1("
			+ startIndex + ")'>" + item1.colname + "</th>";
			$.each(item1.subcols, function(itemIndex2, item2) {
				if (!isFirst)
					columnHtml += "<tr>";
				isFirst = false;
				columnHtml += "<td><input type='checkbox' id='" + item2.colid + "' value='" + item1.colid
				+ "' name='2Record" + startIndex + "' onchange='chooseall2(" + startIndex + ",this)'>"
				+ item2.colname + "</td><td>";
				if (item2.subcols != null) {
					$.each(item2.subcols, function(itemIndex3, item3) {
						columnHtml += "<label class='checkbox-inline'>" +
								" <input type='checkbox' id='" + item3.colid
						+ "' name='3Record" + startIndex + "' value='" + item2.colid + "' firstCol='"
						+ item1.colid + "' secondCol='" + item2.colid
						+ "'  coltype='action' onchange='chooseall3(" + startIndex + "," + item2.colid
						+ ",this)'>" + item3.colname + "</label>";
					});
				}
				columnHtml += "</td></tr>";
			});
			startIndex++;
			$("#mainTable").append(columnHtml);
		});
	});
	return false;
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
	params.rolename = $('#rolename').val();
	params.roledesc = $('#roledesc').val();
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
			if ($(this).is(":checked") ) {
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
		$('input[name="' + name1 + '"]').prop("checked", false);
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
			$(this).prop("checked", false);
	});
	// 绑定全选/取消全选按钮
	$('input[name="' + name3 + '"]').each(function() {
		if ($('input[name="' + name1 + '"]').is(":checked")) {
			$(this).prop("checked", true);
		} else
			$(this).prop("checked", false);
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
			$(this).prop("checked", false);
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
		$('input[id="' + parentId + '"]').prop("checked", false);
	chooseall(startIndex);
}