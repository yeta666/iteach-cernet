var attaId = -1;
$(document).ready(function() {
	getInfo(userId);
	ShowColumn();
	$("#remark-info").click(changeInfo);
	/**
	 * 保存用户资料修改
	 */
	$("#btn-save").click(function() {
		var gender = $("#2").val();
		var userIdCard = $("#3").val();
		var email = $("#4").val();
		var phone = $("#5").val();
		var address = $("#6").val();
		var mark = $("#7").val();
		if(gender != "男" && gender != "女") {
			alert("请正确填写性别（男\女）");
			return;
		}
		if(userIdCard.length < 15 || userIdCard.length > 18) {
			alert("身份证号必须是15-18位");
			return;
		}

		var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;

		if(!email.match(reg) && email != "") {
			alert("邮箱格式不正确");
			return;
		}
		if(phone.length > 30) {
			alert("电话位数不能超过30位");
			return;
		}
		if(address.legtn > 50) {
			alert("地址位数不能超过50位");
			return;
		}
		if(mark.length > 500) {
			alert("备注过长，不能超过500位");
			return;
		}
		var params = {
			userId: userId,
			userIdCard: userIdCard,
			gender: gender,
			email: email,
			phone: phone,
			address: address,
			mark: mark,
			picId: attaId
		};
		AjaxJson("../../handler/user/modifyPersonalInfo", params, backOfModify);
	});
});
/**
 * 获取学生个人信息并展示
 * 
 * @author ZhangXin
 * @param id
 */
function getInfo(id) {
	$.ajax({
		type: "post",
		contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		url: '../../handler/user/findUserById',
		async: false,
		data: {
			userId: id
		},
		dataType: 'json',
		success: function(ba) {
			showUserInfo(ba);
		}
	});
}
/**
 * 展示用户信息
 * 
 * @author ZhangXin
 * @param data
 */
function showUserInfo(backInfo) {
	var data = backInfo.data.data;
	if(null != data) {
		var arr = new Array(data.userLoginname, data.userRealname, data.userGender, data.userIdNum,
			data.userEmail, data.userPhoneNum, data.userAddress, data.userRemark, data.userCoverPictureId);

		for(var i = 0; i < 7; i++) {
			var id = "#info-item" + i;
			if(arr[i] == undefined)
				arr[i] = "";
			$(id).html("<input id='" + i + "' type='text' class='form-control' readonly='readonly' value='" + arr[i] + "'><span " + (i < 2 ? "class='no-info'" : "class='info'") + "></span>");
		}
		$("#info-item7").html("<textarea class='form-control' id='" + 7 + "' style='width:400px;height:100px;resize:none;' readonly='readonly'>" + arr[7] + "</textarea><span class='info'></span>");

		if(arr[8] != undefined) {
			attaId = arr[8];
			AjaxJson("../../handler/attachment/findAttachmentById", {
				attaId: arr[8]
			}, function(backData) {
				if(backData.data.attachment != null && backData.data.attachment != "") {
					var data = backData.data.attachment;
					userAttaId = data.attaId;
					var imgUrl = "../../" + data.attaLocation + data.attaFilename;
					if(!CheckImgExists(imgUrl)) {
						imgUrl = "../../upload/portrait/user.jpg";
					}
					$("#head-pic").attr("src", imgUrl);
				} else {
					$("#head-pic").attr("src", "../../upload/portrait/user.jpg");
				}
			});
		} else {
			$("#head-pic").attr("src", "../../upload/portrait/user.jpg");
		}
	}
}

function fileUp() {
	var pic = $("#postfile").val();
	pic = pic.slice(pic.lastIndexOf("."));
	if("" != pic && !(".bmp" == pic || ".png" == pic || ".jpg" == pic)) {
		state = 'error';
		header = "添加失败";
		message = "图片格式不正确 .bmp  .png  .jpg";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return;
	}

	if("" != pic) {
		//上传头像
		$.ajaxFileUpload({
			'url': "../../handler/load/upload",
			'secureuri': false,
			'fileElementId': "postfile",
			'dataType': "json",
			'data': {
				fileType: 7,
				location: "upload/portrait/"
			},
			'success': function(data, status) {
				console.log(data);
				//删除原来的图片附件
				if(attaId > 0) {
					AjaxJson("../../handler/attachment/delete", {
						attachId: attaId
					}, function(backData) {
						console.log(backData);
						if(!backData.ret) {
							alert("删除原始头像失败！");
						}
					});
				}

				//显示新上传的图片
				attachmentId = data.data.attachId;
				AjaxJson("../../handler/attachment/findAttachmentById", {
					attaId: attachmentId
				}, function(backData) {
					var data = backData.data.attachment;
					if(data == null) {
						$("#head-pic").attr("src", "../../upload/portrait/user.jpg");
						alert("1111");
					} else {
						userAttaId = data.attaId;
						attaId = userAttaId;
						$("#head-pic").attr("src", "../../" + data.attaLocation + data.attaFilename);
					}
				});

			},
			'error': function(data, status, e) {
				console.log(data);
				state = 'error';
				header = "添加失败";
				message = "头像上传失败!!";
				infoNotice(state, header, message, $('#infomsg'));
				moveto("table");
			}
		});
	}
}

/**
 * 修改用户信息，界面上发生的变化
 */
function changeInfo() {
	$("#remark-info").html("<i class='icon-pencil icon-white'></i>正在编辑");
	$("input").each(function(index) {
		if(index >= 3)
			$(this).removeAttr("readonly");
	});

	$("textarea").each(function(index) {
		$(this).removeAttr("readonly");
	});
	$("#postfile").attr("style", "padding-top:100px;");
	$("#btn-save").removeAttr("style").css("text-align", "center");
	$("span").filter("[class='info']").html("可编辑").css("color", "green").css("font-size", "15px");
}

function backOfModify(backInfo) {
	var data = backInfo.data.data;
	alert(data.info);
}