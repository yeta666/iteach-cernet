/**
 * @author
 * @version 系统管理-banner
 */
$(document).ready(function() {
	backToTop(); // back To Top
	ShowColumn(); // 列表显示
	// 绑定全选/取消全选按钮
	$('#chooseall').click(function() {
		if($(this).html() == "全选") {
			$('input[name="choosebanner"]').prop("checked", "true");
			$(this).text("反选");
		} else {
			$('input[name="choosebanner"]').each(function() {
				if($(this).is(":checked")) {
					$(this).prop("checked", false);
				} else
					$(this).prop("checked", "true");
			});
			$(this).text("全选");
		}
	});
	showBanner(); // 显示顶部图片
	bindDelBanner(); // 绑定删除按钮
	bindUploadBanners(); // 绑定用户文件上传操作
});
// 绑定上传文件的相关操作，包括上传文件和下载模板信息
function bindUploadBanners() {
	$('#upload').click(function() {
		var filename = $('#fileInput').val();
		var suffix = filename.slice(filename.indexOf('.') + 1);
		var msg = '';
		if(filename == '') {
			msg = '上传文件不能为空';
		}
		if(filename != '') {
			if(suffix.replace(/jpg/, '') == '' || suffix.replace(/png/, '') == '') {
				msg = '';
			} else {
				msg = '文件格式错误，只能上传jpg 、 png图片';
			}
		}
		if(msg != '')
			infoNotice("error", "", msg);
		else {
			$('#upload').text("上传中...");
			$('#upload').attr("disabled", true);
			$.ajaxFileUpload({
				url: "../../handler/banner/addBanner",
				secureuri: false,
				fileElementId: "fileInput",
				dataType: "json",
				success: function(data, status) {
					console.log(data);
					if(data.ret) {
						$('#upload').text("上传");
						$('#upload').attr("disabled", false);
						var result = "success";
						var msg = data.data.message;
						if(data.data.state != "success" || msg == '')
							result = "error";
						infoNotice(result, "", msg);
						$('#batchimportmodal').modal('hide')
					} else
						infoNotice("error", "", "上传图片失败！");
					$("#batchimportmodal").modal("hide");
					showBanner();
				},
				error: function(data, status, e) {
					$('#upload').text("上传");
					$('#upload').attr("disabled", false);
					infoNotice("error", "", "文件大小超出限制");
					$("#batchimportmodal").modal("hide");
				}
			});
		}
		return false;
	});
}

function bindDelBanner() {
	$('#delbanner').click(function() {
		var url = "../../handler/banner/delBanner";
		// 获取选择的用户id序列
		var delbanners = new Array();
		$("input[name='choosebanner']").each(function() {
			var $current = $(this);
			if($current.is(':checked'))
				delbanners.push($current.val());
		});
		var state;
		var message;
		var header;
		// 如果选择的id为空，则提示错误信息
		if(delbanners.length == 0) {
			state = 'error';
			header = "删除失败";
			message = "未选择任何图片进行删除";
			delInfoNotice(state, header, message);
		}
		// 否则，向后台发送删除请求并显示结果信息
		else {
			$.post(url, {
				"delbanners": delbanners.toString()
			}, function(data) {
				if(data.ret) {
					state = 'success';
					header = "删除成功";
					if(data.data.state == 'fail') {
						state = "error";
						header = "删除失败";
					}
					message = data.data.message;
					delInfoNotice(state, header, message);
					// 删除记录后，重新请求
					if(state == 'success') {
						showBanner();
					}
				}
			}, "json");
		}
		return false;
	});
}
// 加载数据到数据区域
function showBanner() {
	var url = "../../handler/banner/viewAllBanner";
	var $records = $('#banners');
	var startIndex = 1;
	$records.empty();
	$.get(url, function(data, status) {
		$.each(data.data.result, function(entryIndex, entry) {
			var picture_msg = "<img style='width:100%; height:auto;' src='../img/banner/" + entry.filename + "' alt='" + entry.filename + "'>";
			var rowhtml = "<tr><td class=\"tdcenter\"><input type='checkbox' name='choosebanner' value='" +
				entry.filename + "'></td><td class=\"tdcenter\">" + startIndex + "</td>" + "<td width='300px'>" +
				picture_msg + "</td><td class=\"tdcenter\">" + entry.filetype + "</td><td class=\"tdcenter\">" +
				entry.filesize + "</td>";
			rowhtml += "</tr>";
			$records.append(rowhtml);
			startIndex = startIndex + 1;
		});
	}, "json");
	$('#chooseall').text("全选");
}

function delInfoNotice(state, header, message) {
	var classes = 'alert alert-' + state;
	var infoHtml = "";
	// 消息框被关闭后，存在，需要重新创建
	infoHtml = "<div id='infomsg' class='" + classes + "'>" + "<a class='close' data-dismiss='alert'>×</a>" +
		"<h4 class='alert-heading'>" + header + "</h4><p>" + message + "</p></div>";
	$('#infomsg').html(infoHtml);
}

function cleanInfo() {
	$("#infomsg").empty();
	$("#fileInput").val("");
	return false;
}