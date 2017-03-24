/**
 * @author htx
 * @version 系统管理-友情链接管理-主函数
 */
var parameters = new Object();
$(document).ready(function() {
	backToTop();// back To Top
	ShowColumn();// 列表显示
	paginationPage(); // 加载分页
	loadLinkInfo();// 加载友情链接信息
	claenInfo();// 清空数据
	// 添加link
	$("#link-save").click(function() {
		getParameters();
		if (parameters.sypaName == null || parameters.sypaName == "" || parameters.sypaValue == null
				|| parameters.sypaValue == "") {
			infoEditNotice("error", "填写错误", "核对格式！");
			return false;
		}
		var reg = /^[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\:+!]*([^<>])*$/;
		if (!reg.test(parameters.sypaValue)) {
			infoEditNotice("error", "填写错误", "请输入正确的网址！");
			return false;
		}
		if (!(/(^[1-9]\d*$)/.test(parameters.sypaRemark))) {
			infoEditNotice("error", "填写错误", "顺序必须为数字且为正整数！");
			return false;
		}
		$('#editInfomsg').empty();
		/*moveto($("#navibar").parent().offset().top);*/
		var url = "";
		if (parameters.sypaId != null && parameters.sypaId > 0)
			url = "../../handler/sypaController/updateLink";
		else
			url = "../../handler/sypaController/addLink";
		$.post(url, parameters, function(data) {
			var result = data.data.state;
			if (result == "success")
				infoNotice("success", "操作成功", "");
			else
				infoNotice("error", "操作失败", "");
		});
		loadLinkInfo();
		claenInfo();
		return false;
	});
	// 取消link
	$("#link-cancel").click(function() {
		claenInfo();
		return false;
	});
});
/**
 * 加载系统参数并且 loadSystemInfo
 */
function loadLinkInfo() {
	$.ajax({
		type : "post",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		url : '../../handler/sypaController/viewAllSypa',
		async : false,
		data : {
			"pType" : 10
		},
		dataType : 'json',
		success : function(data) {
			var resultData = data.data.result;
			var linkHtml = "";
			var i = 1;
			$.each(resultData, function(DataIndex, index) {
				linkHtml += "<tr><td>" + i + "</td><td><a id='" + index.sypaId + "' name='"
				+ index.sypaName + "' type='" + index.sypaRemark + "' title='" + index.sypaValue
				+ "' onclick='modLink(this);' href='#'>" + index.sypaName + "</a></td><td>" + index.sypaValue
				+ "</td><td>" + index.sypaRemark
				+ "</td><td><a href='sysman_Links.html?"+urlColHtml()+"' class='btn btn-xs btn-danger' onclick='delLink("
				+ index.sypaId + ");'><i class='fa fa-times-circle'></i>&nbsp;删除</a></td></tr>";
				i++;
			});
			$("#mainTable").html(linkHtml);
		}
	});
}
function delLink(id) {
	var url = "../../handler/sypaController/deleteLink";
	$.post(url, {
		"linkId" : id
	}, function() {
		if (data.ret) {
			infoNotice("success", "操作成功", "");
		}
	});
}
function getParameters() {
	parameters.sypaName = $("#link-name").val();
	parameters.sypaValue = $("#link-url").val();
	parameters.sypaRemark = $("#link-order").val();
	parameters.sypaType = 10;
}
function modLink(obj) {
	moveto($("#addOrMOd").parent().offset().top + 80);
	parameters.sypaId = obj.id;
	$("#linkTitle").html("修改友情链接");
	$("#link-save").html("<i class='fa fa-check-square-o'></i> 修改");
	$("#link-name").val(obj.name);
	$("#link-url").val(obj.title);
	$("#link-order").val(obj.type);
}
function claenInfo() {
	parameters = new Object();
	/*moveto($("#navibar").parent().offset().top);*/
	$("#linkTitle").html("新增友情链接");
	$("#link-save").html("<i class='fa fa-check-square-o'></i> 添加");
	$("#link-name").val("");
	$("#link-url").val("");
	$("#link-order").val("");
	return false;
}