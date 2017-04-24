/**
 * @author 张鑫
 * @version 用户管理-教务员管理-主函数
 */
var colIds = "";
var isNull = false;
var ids;
var cookieString = "Search=";
var secondCol = 0;
var firstTime = true;
var params;
$(document).ready(function() {
	backToTop();// back To Top
	ShowColumn(); // 列表显示
	colIds = urlColHtml();
	paginationPage();
	$('#sysmanUser').attr("class", "active");
	// 获取二级栏目id
	secondCol = getRequest("secondCol");
	AjaxJson("../../handler/department/viewDepartments", {
		type : 4
	}, showDepToSelect);

	$("#search").click(function() {
		var depart = $("#quUnit").val();
		var keyword = $("#admin-name").val();
		params = {
			"startTime" : "",
			"endTime" : "",
			"searchWord" : keyword,
			"pageArray" : new Array(),
			"recordPerPage" : 10,
			"deptId" : depart,
			"type" : 3,
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
					$('#admin-name').val(params.searchWord);
					$('#quUnit').val(params.deptId);
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
		search_admin(params);
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
			$("#model-info").html("确认删除这 <span style='color:red;'>"+ count +"</span> 项?  &nbsp;  &nbsp;   &nbsp;   &nbsp;   &nbsp;   &nbsp;                删除信息将无法恢复！!");
		}
		//$(this).attr("href", "#delEduModel");
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
	}
}

/**
 * 显示单位
 * 
 * @param data
 */
function showDepToSelect(data) {
	var data1 = data.data.departments;
	var Html = "<option value='-1'>=单位=</option>";
	for ( var i = 0; i < data1.length; i++) {
		Html += "<option value=" + data1[i].depaId + ">" + data1[i].depaName + "</option>";
	}
	$("#quUnit").html(Html);
	$("#quUnit").change(function() {
		var depid = $(this).val();
		// 当学校被点击，请求年级
		// updatedata(depid);
		$("#search").trigger("click");
	});
}

/**
 * 搜索过滤后的管理员
 * 
 * @param param
 */
function search_admin(param) {
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
			HTML += "<tr>" + "<td><input name='info-data' type='checkbox' value='" + item.userId + "'></td><td>"
					+ startIndex + "</td>" + "<td>" + "<a href='sysman_User_Mod.html?modifyUserId=" + item.userId + "&"
					+ colIds + "'>" + f(item.userNumber) + "</a></td>" + "<td>" + f(item.name) + "</td>" + "<td>"
					+ f(item.phoneNum) + "</td>" + "<td>" + (item.email == undefined ? "" : item.email) + "</td>"
					+ "<td>" + f(item.depaName) + "</td>" + "</tr>";
			startIndex++;
		});
		$("#mainTable").html(HTML);
	}
	return false;
}

function f(target) {
	if (undefined == target)
		return "无记录";
	return target;
}