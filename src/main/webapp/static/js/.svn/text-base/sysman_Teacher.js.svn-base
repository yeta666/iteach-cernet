/**
 * @author ZhangXin
 * @version 用户管理-教师管理-主函数
 */

var isContentShow = false;
var colIds = "";
var isNull = false;
var ids;
var cookieString = "Search=";
var secondCol = 0;
var firstTime = true;
var params;
$(document).ready(function() {
	backToTop();// back To Top
	ShowColumn();// 列表显示
	colIds = urlColHtml();
	paginationPage();
	$('#sysmanUser').attr("class", "active");
	// 获取二级栏目id
	secondCol = getRequest("secondCol");
	if (departmentTypeID != 3) {
		$("#chooseSchool").removeAttr("style");
		AjaxJson("../../handler/department/viewDepartments", {
			type : 4
		}, showSchoolToSelect);
	}

	$("#search").click(function() {
		var school = $("#chooseSchool").val();
		if (departmentTypeID == 3) {
			school = departId;
		}
		var courseId = $("#quCourse").val();
		var classId = $("#quClass").val();
		var keyword = $("#course-name").val();
		params = {
			"startTime" : "",
			"endTime" : "",
			"searchWord" : keyword,
			"pageArray" : new Array(),
			"recordPerPage" : 10,
			"deptId" : school,
			"courseId" : courseId,
			"type" : 2,
			"currentPage" : 1,
			"secondCol" : secondCol
		};
		if (firstTime) {
			// /判断cookie中是否有查询参数
			if ($.cookie("Search") != null && $.cookie("Search") != "") {
				var SearchCookie = $.cookie("Search");
				var SearchPar = JSON.parse(SearchCookie);
				if (SearchPar != null && SearchPar.secondCol == secondCol) {
					params = SearchPar;
					$('#course-name').val(params.searchWord);
					$('#chooseSchool').val(params.deptId);
					if (params.deptId > 0) {
						$("#quCourse").removeAttr("style");
						updatedata(params.deptId);
						$('#quCourse').val(params.courseId);
					}
				} else {
					// 删除cookie
					document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
				}
			}
			//
			firstTime = false;
		} else {
			// 删除cookie
			document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
			// 添加cookie
			var cookieString = "Search=";
			cookieString = cookieString + JSON.stringify(params);
			document.cookie = cookieString;
		}
		search_teacher(params);
	});

	$("#search").trigger("click");
	$("#info-warning").click(function() {
		ids = "";
		var count=0;
		$("input[name='info-data']").each(function(index, item) {
			if ( $(this).is(":checked"))
				{
				ids += $(this).val() + ",";
				count++;
				
				}
				
		});
		if (ids.charAt(ids.length - 1) == ',')
			ids = ids.substring(0, ids.length - 1);

		if (count==0) {
			isNull = true;
			$("#model-info").html("你还没有勾选需要删除的数据,请先勾选!!");
		} else {
			isNull = false;
			$("#model-info").html("确认删除这 <span style='color:red;'>"+ count +"</span> 项?  &nbsp;  &nbsp;   &nbsp;   &nbsp;   &nbsp;   &nbsp;                删除信息将无法恢复！");
		}
		//$(this).attr("href", "#delTeaModel");
	});
	$("#make-sure").click(function() {
		if (isNull == false) {
			AjaxJson("../../handler/adminUserInfo/deleteUserInfos", {
				userIds : ids
			}, deleteBack);
		}
	});

	// 绑定全选/取消全选按钮
	$('#chooseall').click(function() {
		if($(this).html()=="全选"){
			$('input[name="info-data"]').prop("checked", true);
			$(this).text("反选");
		}
		else{
		$('input[name="info-data"]').prop("checked",false);
		$(this).text("全选");
		}
	});

});
function deleteBack(data) {
	alert(data.data.deleteUserInfo);
	if (data.data.deleteUserInfo.indexOf("删除成功") != -1) {
		$("#model-info").html("用户删除成功");
		$("#myModal").trigger("click");
		$("#search").trigger("click");
		return;
	}
}
/**
 * 显示学校
 * 
 * @param data
 */
function showSchoolToSelect(data) {
	var data1 = data.data.departments;
	var Html = "<option value='-1'>=单位=</option>";
	for ( var i = 0; i < data1.length; i++) {
		Html += "<option value=" + data1[i].depaId + ">" + data1[i].depaName + "</option>";
	}
	$("#chooseSchool").html(Html);
	$("#chooseSchool").change(function() {
		var depid = $(this).val();
		// 当学校被点击，请求年级

		if (!(depid == -1)) {
			$("#quCourse").removeAttr("style");
			updatedata(depid);
		} else {
			$("#quCourse").val(-1);
			$("#quCourse").attr("style", "display:none;");
		}
		$("#search").trigger("click");
	});
}
/**
 * 获取课程信息
 * 
 * @param depid
 */
function updatedata(depid) {
	AjaxJson("../../handler/course/viewCourseListByDepart", {
		departId : depid,
		type : 0
	}, showCourseToSelect);

}
function showCourseToSelect(data) {
	var data = data.data.courses;
	var Html = "<option value=-1>=课程=</option>";
	for ( var i = 0; i < data.length; i++) {
		Html += "<option value=" + data[i].courseId + ">" + data[i].courseName + "</option>";
	}
	$("#quCourse").html(Html);
	$("#quCourse").change(function() {
		$("#search").trigger("click");
	});
}
/**
 * 搜索过滤后的老师
 * 
 * @param param
 */
function search_teacher(param) {
	viewaction = '../../handler/user/userInfoManage';
	params = param;
	initialBind();// 绑定分页的一些操作响应
	initSearch();
}
/**
 * 处理返回数据
 * 
 * @param pageRecords
 * @returns {Boolean}
 */
function refreshContent(pageRecords) {

	$("#mainTable").empty();
	$.ajaxSettings.async = false;
	var HTML = "";
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	if (pageRecords.data.length <= 0) {
		HTML += "<div class='alert'>" + "<button type='button' class='close' data-dismiss='alert'>&times;</button>"
				+ "<strong>提示!</strong> 没有符合您所需查询数据..." + "</div>";
		$("#waring-nodata").html(HTML);
	} else {
		$("#waring-nodata").html("");
		$.each(pageRecords.data, function(itemIndex, item) {
			var attId;
			var isPic = false;
			if (item.userCoverPictureId != undefined || item.userCoverPictureId != null) {
				isPic = true;
				var attId = item.userCoverPictureId;
			}
			var info = (isPic ? "<div><img src='../img/1.jpg'/></div>" : "<p>该老师还没有头像</p>");
			HTML += "<tr>" + "<td><input  name='info-data' type='checkbox' value='" + item.userId + "'></td><td>"
					+ startIndex + "</td>" + "<td>" + "<a id='" + (isPic ? attId : 0)
					+ "' onmouseout='outMouse()' name='../../" + item.attaLocation + item.attaFilename
					+ "' href='sysman_User_Mod.html?modifyUserId=" + item.userId + "&" + colIds + "'>"
					+ item.userNumber + "</a></td>" + "<td>" + item.name + "</td>" + "<td>"
					+ (item.depaName == undefined ? "无记录" : item.depaName) + "</td>" + "<td>" + item.phoneNum + "</td>"
					+ "<td>" + (item.email == undefined ? "" : item.email) + "</td>"
					+ "<td><a style='cursor:pointer;' name='" + item.userId + "'>点击查看</a></td>" + "</tr>";
			startIndex++;
		});
		$("#mainTable").html(HTML);

		$("td a[name^='../']").each(function() {
			$(this).bind('mouseover', function() {
				if ($(this).attr("id") != 0) {
					var loca = $(this).attr("name");
					$("#teacher-pic").html("<img src='" + loca + "'/>");
					showFoo($(this), $("#content"));
				} else {
					$("#teacher-pic").html("<img src='../../upload/portrait/user.jpg'/>");
					showFoo($(this), $("#content"));
				}

			});

		});
		$("td a").bind("click", function() {
			if (isContentShow) {
				isContentShow = false;
				$("#contentTeacher").removeAttr("style");
				$("#contentTeacher").attr("style", "position: absolute;display: none;");
			} else {
				isContentShow = true;
				var t = $(this).attr("name");
				AjaxJson("../../handler/selectCourse/queryTeacherCourses", {
					userId : t
				}, deal);
				setTimeout("1000");
				showFoo($(this), $("#contentTeacher"));
			}
		});
	}
	return false;
}

function outMouse() {
	$("#content").removeAttr("style");
	$("#content").attr("style", "position: absolute;display: none;");
}
/**
 * 老师头像信息
 * 
 * @param backInfo
 */
function resultOfAtta(backInfo) {
	var data = backInfo.data.attachment;
	if (data == undefined || data == null) {

	} else {

	}
}
/**
 * 查询老师所有课程
 * 
 * @param backInfo
 */
function deal(backInfo) {
	var data = backInfo.data.data;
	var Html = "";
	if (data.length > 0) {
		Html = "<table class='table'><tbody>";
		for ( var i = 0; i < data.length; i += 1) {
			if (i % 3 == 0)
				Html += "<tr>";

			Html += "<td><span>" + data[i].courName + "</span></td>";
			if (i % 3 == 2 || i == data.length - 1)
				Html += "</tr>";
		}
		Html += "</tbody></table>";
	} else {
		Html += "<div class='alert fade in'><strong>该老师还没有所授课程哦！</strong></div>";
	}
	$("#show-courses").html(Html);
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
	target.removeAttr("style");
	var style = "position:absolute;top:" + yTarget + "px; left:" + xTarget + "px; display:block;";

	target.attr("style", style);
	target.show();
};