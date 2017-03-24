/**
 * @author
 * @version 用户管理-权限管理-主函数
 */
var colIds = "";
$(document).ready(function() {
	colIds = urlColHtml();
	backToTop(); // back To Top
	ShowColumn(); // 列表显示
	paginationPage();// ** 加载分页 *//
	// 绑定全选/取消全选按钮
	
	$('#chooseall').click(function() {
		if($(this).html()=="全选"){
			$('input[name="chooseRecord"]').prop("checked", true);
			$(this).text("反选");
		}
		else{
		$('input[name="chooseRecord"]').prop("checked",false);
		$(this).text("全选");
		}
	});
	
	bindDelRecord();// 绑定删除按钮
	loadAuthorities();// 加载权限管理信息
});

//加载所有权限管理数据
function loadAuthorities() {
	var url = '../../handler/authority/queryAuthority';
	$.get(url, function(data, status) {
		var roleHtml = "";
		$.each(data.data.pageData, function(itemIndex, item) {
			var describe = "";
			if (item.roleDescirbe != null && item.roleDescirbe != "undefined") {
				describe = item.roleDescirbe;
			}
			roleHtml += "<tr><td>";
			/*if ($.inArray(item.roleId, [ 1, 2, 3, 4, 5 ]) == -1) {
				roleHtml += "<input type='checkbox' name='chooseRecord' value='" + item.roleId + "' />";
			}*/
			roleHtml += "<input type='checkbox' name='chooseRecord' value='" + item.roleId + "' />";
			var href = "href='sysman_Competence_edit.html?roleId=" + item.roleId + "&roleName="
			+ encodeURI(encodeURI(item.roleName)) + "&roleDesc=" + encodeURI(encodeURI(item.roleDescirbe))
			+ "&" + colIds  + "'";
			roleHtml += "</td><td class='tdcenter'>" + (itemIndex + 1) + "</td>" + "<td class='tdcenter'><a " + href
			+ ">" + item.roleName + "</a></td>" + "<td class='tdcenter'>" + describe + "</td></tr>";
		});
		$('#mainTable').append(roleHtml);
	});
}
//绑定删除
function bindDelRecord() {
	$('#delButton').click(function() {
		if (confirm("将删除角色对应所有关联信息，该操作不能恢复，请确认是否删除！")) {
			// 获取选择的用户id序列
			var delRoles = new Array();
			$("input[name='chooseRecord']").each(function() {
				var $current = $(this);
				if ($current.is(":checked"))
					delRoles.push($current.val());
			});
			var state;
			var message;
			var header;
			// 如果选择的id为空，则提示错误信息
			if (delRoles.length == 0) {
				state = 'error';
				header = "删除失败";
				message = "未选择任何角色进行删除";
				infoNotice(state, header, message, $('#data-grid'));
			}
			// 否则，向后台发送删除请求并显示结果信息
			else {
				var url = "../../handler/authority/delRoleAuthority";
				$.post(url, {
					"delRoles" : delRoles.toString()
				}, function(data) {
					if (data.ret) {
						state = 'success';
						header = "删除成功";
						message = "";
					} else {
						state = "error";
						header = "删除失败";
						message = data.errmsg;
					}
					infoNotice(state, header, message, $('#data-grid'));
					// 删除记录后，重新请求
					if (state == 'success')
						window.location.reload();
				}, "json");
			}
			return false;
		}
	});
}