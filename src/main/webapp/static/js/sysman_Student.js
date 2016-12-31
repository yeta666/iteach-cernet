/**
 * @author 张鑫
 * @version 用户管理-学生管理-主函数
 */
var colIds = "";
var isNull = false;
var ids;
var cookieString = "Search=";
var secondCol = 0;
var firstTime = true;
var params;
$(document).ready(function() {
	/*if($.cookie("fileUploadStu")=="1"){
		alert("");
		onkeydown();
	}*/
	if($.cookie("fileUploadStu")=="1"){
		alert("为避免数据出错，禁止刷新或同时打开两个窗口一起上传数据！");
		$.cookie('fileUploadStu','0');//有数据时关闭当前窗口
		closeit();
	}
	$.cookie('fileUploadStu', '');
	$("#importclose").click(function() {
		$.cookie('fileUploadStu', '');		
	});
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
		var grade = $("#quGrade").val();
		var classId = $("#quClass").val();
		var keyword = $("#user-name").val();
		params = {
			"startTime" : "",
			"endTime" : "",
			"searchWord" : keyword,
			"pageArray" : new Array(),
			"recordPerPage" : 10,
			"deptId" : school,
			"grade" : grade,
			"classId" : classId,
			"type" : 1,
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
					$('#user-name').val(params.searchWord);
					$('#chooseSchool').val(params.deptId);
					if (params.deptId > 0) {
						$("#quGrade").removeAttr("style");
						updatedata(params.deptId);
						$('#quGrade').val(params.grade);
						if (params.grade > 0) {
							$("#quClass").removeAttr("style");
							requestclass(params.grade);
							$('#quClass').val(params.classId);
						}
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
		search_student(params);
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

		if(count==0) {
			isNull = true;
			$("#model-info").html("你还没有勾选需要删除的数据,请先勾选!!");
		} else {
			isNull = false;
			$("#model-info").html("确认删除这 <span style='color:red;'>"+ count +"</span> 项?  &nbsp;  &nbsp;   &nbsp;   &nbsp;   &nbsp;   &nbsp;                删除信息将无法恢复！!");
		}
		//$(this).attr("href", "#delStuModal");
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
var dafadsf = 1;
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
 * 禁止F5
 * @returns {Boolean}
 */
function onkeydown(){
	if    (    event.keyCode==116)         
    {         
            event.keyCode    =    0;         
            event.cancelBubble    =   true;         
             return   false;         
    }    
}
/**
 * 关闭窗口
 */
function closeit(){
	//FF中需要修改配置window.close方法才能有作用，为了不需要用户去手动修改，所以用一个空白页面显示并且让后退按钮失效
	//Opera浏览器旧版本(小于等于12.16版本)内核是Presto，window.close方法有作用，但页面不是关闭只是跳转到空白页面，后退按钮有效，也需要特殊处理
	var userAgent = navigator.userAgent;
	if (userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Presto") != -1) {
	    window.location.replace("about:blank");
	} else {
	    window.opener = null;
	    window.open("", "_self");
	    window.close();
	    location.reload();
	   // $.cookie('fileUploadStu','');
	}
}

/**
 * 搜索过滤后的学生
 * 
 * @param param
 */
function search_student(param) {
	viewaction = '../../handler/user/userInfoManage';
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
			HTML += "<tr>" + "<td><input name='info-data' type='checkbox' value='" + item.userId + "'></td><td>"
					+ startIndex + "</td>" + "<td>" + "<a href='sysman_User_Mod.html?modifyUserId=" + item.userId + "&"
					+ colIds + "'>" + f(item.userNumber) + "</a></td>" + "<td>" + f(item.examNum) + "</td>" + "<td>"
					+ f(item.name) + "</td>" + "<td>" + f(item.className) + "</td>" + "<td>" + f(item.gradeName)
					+ "</td>" + "<td>" + f(item.depaName) + "</td>" + "<td>" + f(item.phoneNum) + "</td>" + "</tr>";
			startIndex++;
		});
		$("#mainTable").html(HTML);
	}

	return false;
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
/**
 * 显示学校
 * 
 * @param data
 */
function showSchoolToSelect(data) {
	var data1 = data.data.departments;
	var Html = "<option value='-1'>=专业=</option>";
	for ( var i = 0; i < data1.length; i++) {
		Html += "<option value=" + data1[i].depaId + ">" + data1[i].depaName + "</option>";
	}
	$("#chooseSchool").html(Html);
	$("#chooseSchool").change(function() {
		var depid = $(this).val();
		// 当专业被点击，请求年级
		if (!(depid == -1)) {
			$("#quGrade").removeAttr("style");
			updatedata(depid);
		} else {
			$("#quGrade").val(-1);
			$("#quClass").val(-1);
			$("#quGrade").attr("style", "display:none;");
			$("#quClass").attr("style", "display:none;");
		}
		$("#search").trigger("click");
	});
}
/**
 * 发送年级请求
 * 
 * @param depid
 */
function updatedata(depid) {
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
	var Html = "<option value='-1'>=年级=</option>";
	for ( var i = 0; i < data.length; i++) {
		Html += "<option value=" + data[i].gradId + ">" + data[i].gradName + "</option>";
	}
	$("#quGrade").html(Html);
	$("#quGrade").change(function() {
		var gradeid = $(this).val();
		// 当学校被点击，请求年级
		if (!(gradeid == -1)) {
			$("#quClass").removeAttr("style");
			requestclass(gradeid);
		} else {
			$("#quClass").val(-1);
			$("#quClass").attr("style", "display:none;");
		}
		$("#search").trigger("click");
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
	var Html = "<option value='-1'>=班级=</option>";
	for ( var i = 0; i < data1.length; i++) {
		Html += "<option value=" + data1[i].clasId + ">" + data1[i].clasName + "</option>";
	}
	$("#quClass").html(Html);
	$("#quClass").change(function() {
		$("#search").trigger("click");
	});
}

function f(target) {
	if (target == undefined)
		return "无记录";
	return target;
}