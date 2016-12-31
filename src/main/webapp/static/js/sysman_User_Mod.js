/**
 * 用户信息修改
 * 
 * @author ZhangXin
 */
var data;// 用户接收和发送用户信息
var oldRole;
var oldRoleIds = new Array();
var modifyUserId = getRequest("modifyUserId");
var isClickMethod = false;
var userDep;
var isExit;
var studentUser;
var gradeId;
var classId;
var isFirstView = true;
var userAttaId;
$(document).ready(
		function() {

			/*
			 * window.onbeforeunload = function(e){ e = e || window.event; var
			 * msg = "您确定要离开此页面吗？"; // IE e.cancelBubble = true; e.returnValue =
			 * msg; // Firefox if(e.stopPropagation) { e.stopPropagation();
			 * e.preventDefault(); } // Chrome / Safari return msg; };
			 */

			ShowColumn();// 显示功能栏
			$("#dropdownToggle").click(function(){
				$("#content").show();
			});
			$("#close").click(function(){
				document.getElementById("content").style.display='none';
			});
			timepicker("time-inschool");
			/* 编辑器内容清空 start */
			//var temp = $("#remarkNew").cleditor()[0];
			$("#remarkNew").val("");
			//temp.updateFrame();
			/* 编辑器内容清空 end */

			AjaxJson("../../handler/role/findRoleById", {
				userId : modifyUserId
			}, backOfUserRoles);
			AjaxJson("../../handler/user/findUserById", {
				userId : modifyUserId
			}, backOfUserInfo);
			if (departmentTypeID != 3) {
				$("#departments").removeAttr("style");
				AjaxJson("../../handler/department/viewDepartments", {
					type : 4
				}, backOfDepart);
			} else {
				requestGrade(departId);
			}
			AjaxJson("../../handler/role/findRoleByUserType", {
				userType : userType
			}, backRoles);

			$("#userNameNew").blur(function() {

				checkStudentNumber($(this));
			});
			$("#add-role").click(function() {
				if (!isClickMethod) {
					isClickMethod = true;
					$("#show-button").attr("class", "icon-chevron-up");
					showFoo($(this), $("#content"));
				} else {
					$("#show-button").attr("class", "icon-chevron-down");
					$("#content").removeAttr("style");
					$("#content").attr("style", "position: absolute;display: none;");
					isClickMethod = false;
				}
			});
			$("#submit-modify").click(
					function() {
						if ($("#userNameNew").val() == "" || $("#userNameNew").val().length < 4
								|| $("#userNameNew").val().length > 20) {
							state = 'error';
							header = "提交失败";
							message = "用户名不能为空,且位数在4-20位";
							infoNotice(state, header, message, $('#infomsg'));
							moveto("table");
							return;
						}

						AjaxJson("../../handler/adminUserInfo/isUserExist", {
							userLoginname : $("#userNameNew").val()
						}, function(backInfo) {

							if (backInfo.data.status == 1 && (data.userLoginname != $("#userNameNew").val())) {
								isExit = 1;
								state = 'error';
								header = "提交失败";
								message = "用户名已经存在,请重新输入";
								infoNotice(state, header, message, $('#infomsg'));
								moveto("table");
								return;
							} else {
								isExist = 2;
								data.userLoginname = $("#userNameNew").val();
								if ($("#userRealNameNew").val() == "") {
									state = 'error';
									header = "提交失败";
									message = "用户真实姓名不能为空";
									infoNotice(state, header, message, $('#infomsg'));
									moveto("table");
									return;
								}
								// 学生信息验证
								if (studentUser == 1) {

									// 年级
									var gradeid = $("#modify-grade").val();
									if (gradeid == undefined || gradeid <= 0) {
										state = 'error';
										header = "提交失败";
										message = "请选择年级";
										infoNotice(state, header, message, $('#infomsg'));
										moveto("table");
										return;
									}
									gradeId = gradeid;
									// 班级
									var classid = $("#modify-class").val();
									if (classid == undefined || classid <= 0) {
										state = 'error';
										header = "提交失败";
										message = "请选择班级";
										infoNotice(state, header, message, $('#infomsg'));
										moveto("table");
										return;
									}
									data.userClasId = classid;
									// 入学时间
									var timeIn = $("#time-inschool").val();
									if ("" == timeIn) {
										state = 'error';
										header = "提交失败";
										message = "请填写入学时间";
										infoNotice(state, header, message, $('#infomsg'));
										moveto("table");
										return;
									}
									data.userYearOfEntrance = timeIn;
									// 学籍号
									var studyNum = $("#study-id-new").val();
									if (studyNum != data.userCadasExamNum) {
										data.userCadasExamNum = studyNum;
									}

								}
								data.userRealname = $("#userRealNameNew").val();
								data.userGender = $("#sex").val();
								if ($("#passwordNew").val() == "") {
									state = 'error';
									header = "提交失败";
									message = "密码不能为空";
									infoNotice(state, header, message, $('#infomsg'));
									moveto("table");
									return;
								}
								if (data.userPwd != $("#passwordNew").val() && $("#passwordNew").val().length >= 4
										&& $("#passwordNew").val().length <= 20) {
									data.userPwd = hex_md5($("#passwordNew").val());
								} else if (data.userPwd == $("#passwordNew").val()) {
								} else {
									state = 'error';
									header = "提交失败";
									message = "密码位数为：4-20位";
									infoNotice(state, header, message, $('#infomsg'));
									moveto("table");
									return;
								}
								if ($("#userIdNew").val().length != 0
										&& ($("#userIdNew").val().length != 15 && $("#userIdNew").val().length != 18)) {
									state = 'error';
									header = "提交失败";
									message = "用户身份证号码为15位或者18位";
									infoNotice(state, header, message, $('#infomsg'));
									moveto("table");
									return;
								}
								var email = $("#emailNew").val();
								var e = /^[a-zA-Z0-9_+.-]+\@([a-zA-Z0-9-]+\.)+[a-zA-Z0-9]{2,4}$/;
								if (email != "" && !e.test(email)) {
									state = 'error';
									header = "提交失败";
									message = "邮箱验证不通过";
									infoNotice(state, header, message, $('#infomsg'));
									moveto("table");
									return;
								}
								// 电话验证
								var p1 = /^\d*$/;
								if (!p1.test($("#phoneNumNew").val())) {
									state = 'error';
									header = "提交失败";
									message = "电话号码不正确";
									infoNotice(state, header, message, $('#infomsg'));
									moveto("table");
									return;
								}

								if (isExit == 1) {
									state = 'error';
									header = "提交失败";
									message = "用户名已经存在";
									infoNotice(state, header, message, $('#infomsg'));
									moveto("table");
									return;
								}
								// 判定用户角色
								var roleAdd = "";
								var roleDel = "";
								$("input[name='role-method']").each(function() {
									var canAdd = true;
									var nowVal = $(this).val();
									var check = $(this).attr("checked");
									for ( var i = 0; i < oldRoleIds.length; i++) {
										if (oldRoleIds[i] == nowVal) {
											canAdd = false;
										}
									}
									if (canAdd && check == "checked") {
										roleAdd += nowVal + ",";
									}
								});

								for ( var i = 0; i < oldRoleIds.length; i++) {
									var canDel = true;
									var nowVal = oldRoleIds[i];
									$("input[name='role-method']").each(function() {
										if ($(this).val() == nowVal && $(this).attr("checked") == "checked") {
											canDel = false;
										}
									});
									if (canDel)
										roleDel += nowVal + ",";
								}
								roleDel = dropPoit(roleDel);
								roleAdd = dropPoit(roleAdd);
								data.userIdNum = $("#userIdNew").val();
								data.userEmail = $("#emailNew").val();
								data.userPhoneNum = $("#phoneNumNew").val();
								data.userAddress = $("#addressNew").val();
								data.userRemark = $("#remarkNew").val();
								if ($("#modify-depart").val() > 0)
									data.userDepaId = $("#modify-depart").val();
								data.roleDel = roleDel;
								data.roleAdd = roleAdd;
								data.userCoverPictureId = userAttaId;
								AjaxJson("../../handler/adminUserInfo/modefiedUserInfo", data, backOfModify);
							}

						});
					});

			$("#cancle-modify").click(function() {
				window.location = document.referrer;
			});
		});

/**
 * 检查用户名是否可用
 * 
 * @param target
 */
function checkStudentNumber(target) {
	var number = target.val();

	if ($("#userNameNew").val() == "" || $("#userNameNew").val().length < 4 || $("#userNameNew").val().length > 20) {
		$("#user-add-check").html("4-20位,工号可作为登录名");
		return;
	}
	if (number == "" || number == data.userLoginname) {
		isExit = 2;
		$("#user-add-check").html("4-20位,工号可作为登录名");
		return;
	}
	AjaxJson("../../handler/adminUserInfo/isUserExist", {
		userLoginname : number
	}, checkUsernameback);
}
function checkUsernameback(backInfo) {
	if (backInfo.data.status == 1 && (data.userLoginname != $("#userNameNew").val())) {
		isExit = 1;
		$("#user-add-check").html("不可用").css({
			"font-size" : "15px",
			"color" : "red"
		});
	} else {
		isExist = 2;
		$("#user-add-check").html("可用").css({
			"font-size" : "15px",
			"color" : "green"
		});
	}
}
/**
 * 返回用户信息
 * 
 * @param backInfo
 */
function backOfUserInfo(backInfo) {
	data = backInfo.data.data;
	$("#userNameNew").val(data.userLoginname);
	$("#userRealNameNew").val(data.userRealname);
	if (data.userGender == "男") {
		$("#sex option[value='男']").attr("selected", "selected");
	} else {
		$("#sex option[value='女']").attr("selected", "selected");
	}
	userAttaId = data.userCoverPictureId;
	$("#passwordNew").val();
	$("#userIdNew").val(data.userIdNum);
	$("#emailNew").val(data.userEmail);
	$("#phoneNumNew").val(data.userPhoneNum);
	$("#addressNew").val(data.userAddress);

	//var o = $("#remarkNew").cleditor()[0];
	$("#remarkNew").val(data.userRemark);
	//o.updateFrame();

	$("#passwordNew").val(data.userPwd);
	$("#time-inschool").val(data.userYearOfEntrance);
	$("#study-id-new").val(data.userCadasExamNum);
	userDep = data.userDepaId;
	studentUser = data.userType;
	//alert(studentUser);
	classId = data.userClasId;
	$("#userNameCanChange").text("用户名（学籍号）");
	//if (studentUser == 2 || studentUser == 1) {
		if (undefined == userAttaId) {
			$("#head-pic-mod").attr("src", "../../upload/portrait/user.jpg");
		} else {
			AjaxJson("../../handler/attachment/findAttachmentById", {
				attaId : userAttaId
			}, function(backData) {
				var data = backData.data.attachment;
				if (data != null)
					$("#head-pic-mod").attr("src", "../../" + data.attaLocation + data.attaFilename);
				else {
					$("#head-pic-mod").attr("src", "../../upload/portrait/user.jpg");
				}
			});
		}
	//}

	if (studentUser == 1) {
		$("#student-grade").removeAttr("style");
		$("#student-class").removeAttr("style");
		$("#time-in").removeAttr("style");
		$("#study-id").removeAttr("style");

	}
	if (studentUser == 2 || studentUser == 1) {
		$("#head-pic").removeAttr("style");
	}
}
/**
 * 修改用户信息后返回的信息
 * 
 * @param backInfo
 */
function backOfModify(backInfo) {
	if (backInfo.errcode != 0) {
		state = 'error';
		header = "提交失败";
		message = "用户信息修改失败";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
	} else {
		state = 'success';
		header = "提交成功";
		message = "用户信息修改成功";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		history.back();
	}

}

function backOfUserRoles(backInfo) {
	oldRole = backInfo.data.roles;
	var Html = "";
	for ( var i = 0; i < oldRole.length; i++) {
		Html += oldRole[i].roleName + " ";
		oldRoleIds[i] = oldRole[i].roleId;
	}
	//alert(Html);
	$("#role-input").val(Html);
}
/**
 * 返回单位信息
 */
function backOfDepart(backInfo) {
	var data = backInfo.data.departments;
	// var Html="<option value='-1'>无单位</option>";
	var Html = "";
	for ( var i = 0; i < data.length; i++) {
		if (userDep == data[i].depaId)
			Html += "<option value='" + data[i].depaId + "' selected='selected'>" + data[i].depaName + "</option>";
		else
			Html += "<option value='" + data[i].depaId + "'>" + data[i].depaName + "</option>";
	}
	$("#modify-depart").html(Html);
	if (isFirstView && studentUser == 1)
		requestGrade(userDep);
	if (studentUser == 1) {
		$("#modify-depart").change(function() {
			var depid = $(this).val();
			var oriGradeId = $("#modify-grade").val();
			$("#modify-grade").html("");
			$("#modify-class").html("");
			// 当学校被点击，请求年级
			requestGrade(depid);
			requestclass($("#modify-grade").val());

		});
	}

}
/**
 * 发送年级请求
 * 
 * @param depid
 */
function requestGrade(depid) {
	AjaxJson("../../handler/register/gradeInfo", {
		depa_id : depid
	}, showGradeToSelect);

}
/**
 * 显示年级
 * 
 * @param data
 */
function showGradeToSelect(data) {

	var data = data.data.gradeInfo;
	var Html = "";
	for ( var i = 0; i < data.length; i++) {
		Html += "<option value=" + data[i].gradId + ">" + data[i].gradName + "</option>";
	}
	$("#modify-grade").html(Html);
	if (isFirstView && classId != undefined) {
		AjaxJson("../../handler/gradeClass/selectOneClass", {
			classId : classId
		}, function(backInfo) {
			var data = backInfo.data.data;
			gradeId = data.gradeName;
			$("#modify-grade").val(gradeId);
		});
	}
	if (isFirstView && studentUser == 1 && classId != undefined) {
		requestclass(gradeId);
	}
	$("#modify-grade").change(function() {
		var gradeid = $(this).val();
		// 当年级被点击，请求班级
		requestclass(gradeid);
	});
}
/**
 * 请求班级
 * 
 * @param classid
 */
function requestclass(gradeid) {
	AjaxJson("../../handler/register/classesInfo", {
		grade_id : gradeid
	}, showClassToSelect);
}
/**
 * 显示班级
 * 
 * @param data
 */
function showClassToSelect(data) {
	var data1 = data.data.classesInfo;
	var Html = "";
	for ( var i = 0; i < data1.length; i++) {
		Html += "<option value=" + data1[i].clasId + ">" + data1[i].clasName + "</option>";
	}
	$("#modify-class").html(Html);
	if (isFirstView) {
		isFirstView = false;
		$("#modify-class").val(classId);
	}
}
/**
 * 显示角色
 * 
 * @param backinfo
 */
function backRoles(backinfo) {
	var data = backinfo.data.roles;
	var Html = "<table class='table'><tbody>";
	for ( var i = 0; i < data.length; i += 1) {
		if (i % 3 == 0)
			Html += "<tr>";
		var isCheck = false;
		for ( var j = 0; j < oldRoleIds.length; j++) {
			if (data[i].roleId == oldRoleIds[j]) {
				isCheck = true;
			}
		}
		Html += "<td><input type='checkbox' " + (isCheck ? 'checked="checked"' : '') + "name='role-method' value='"
				+ data[i].roleId + "'/><p>" + data[i].roleName + "</p></td>";
		if (i % 3 == 2 || i == data.length - 1)
			Html += "</tr>";
	}
	Html += "</tbody></table>";
	$("#roles").html(Html);

	$("input[name='role-method']").each(function() {
		$(this).bind("click", function() {
			var info = "";
			$("input[name='role-method']").each(function() {
				if($(this).is(":checked"))
					info += $(this).next().html() + "  ";
			});
			$("#role-input").prop("value",info);

		});
	});
}
/**
 * 弹出框
 * 
 * @param This
 * @param target
 */
function showFoo(This, target) {
	// 获取源控件位置
	var xThis = This.offset().left;
	var yThis = This.offset().top;
	var heightThis = This.height();
	var widthThis = This.width();
	// 获取目标控件位置
	var widthTarget = target.width();
	var xTarget = xThis + widthThis / 2 - widthTarget / 2 + 10;
	var yTarget = yThis + heightThis + 10;
	// 修改样式
	$("#content").removeAttr("style");
	var style = "position:absolute;top:" + yTarget + "px; left:" + xTarget + "px; display:block;";

	target.attr("style", style);
	target.show();
};
function dropPoit(target) {
	if (target.charAt(target.length - 1) == ",") {
		return target.substring(0, target.length - 1);
	}
	return target;
}

function fileUp() {
	var pic = $("#postfile").val();
	pic = pic.slice(pic.lastIndexOf("."));
	if ("" != pic && !(".bmp" == pic || ".png" == pic || ".jpg" == pic)) {
		state = 'error';
		header = "添加失败";
		message = "图片格式不正确 .bmp  .png  .jpg";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return;
	}

	if ("" != pic) {
		// 上传头像
		$.ajaxFileUpload({
			'url' : "../../handler/load/upload",
			'secureuri' : false,
			'fileElementId' : "postfile",
			'dataType' : "json",
			'data' : {
				fileType : 7,
				location : "upload/portrait/"
			},
			'success' : function(data, status) {
				attachmentId = data.data.attachId;
				AjaxJson("../../handler/attachment/findAttachmentById", {
					attaId : attachmentId
				}, function(backData) {
					var data = backData.data.attachment;
					userAttaId = data.attaId;
					$("#head-pic-mod").attr("src", "../../" + data.attaLocation + data.attaFilename);
				});

			},
			'error' : function(data, status, e) {
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