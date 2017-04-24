var departs = null;
var selectDeparts = null;
editParams = new Object();
var isAdd = true;
$(document).ready(function() {
	backToTop();// 回顶部
	ShowColumn();// 列表显示
	paginationPage(); // 加载分页
	var depaId = departId;
	if (departmentTypeID != 3) {
		depaId = -1;
	}
	if (departmentTypeID != 3) {
		$("#schoolId").removeAttr("style");
		showSchool();// 显示学习列表
	} else {
		updatedata(departId);
	}
	secondCol = getRequest("secondCol");
	params = {
		"depaId" : depaId,
		"gradId" : -1,
		"searchType" : 1,
		"searchWord" : "",
		"pageArray" : new Array(),
		"recordPerPage" : 10,
		"currentPage" : 1,
		"secondCol" : secondCol
	};
	// /判断cookie中是否有查询参数
	if ($.cookie("Search") != null && $.cookie("Search") != "") {
		var SearchCookie = $.cookie("Search");
		var SearchPar = JSON.parse(SearchCookie);
		if (SearchPar.secondCol == secondCol) {
			params = SearchPar;
			if (departmentTypeID == 1) {
				$('#schoolId').val(params.depaId);
				// 当学校被点击，请求年级
				updatedata(params.depaId);
			}
			$('#gradeId').val(params.gradId);
			$('#searchType').val(params.searchType);
			$('#searchWord').val(params.searchWord);
		} else {
			// 删除cookie
			document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		}
	}
	// 搜索
	$("#search").click(function() {
		getparams();
		document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		var cookieString = "Search=";
		cookieString = cookieString + JSON.stringify(params);
		document.cookie = cookieString;
		initSearch();
	});
	// 全选
	$("#chooseall").click(function() {
		var $checkboxes = $("#mainTable input[type=checkbox]");
		if ($(this).html() == '全选') {
			for ( var i = 0; i < $checkboxes.length; i++) {
				/*$($checkboxes[i]).attr("checked", "checked");*/
				$($checkboxes[i]).prop("checked",true);
			}
			$(this).html('反选');
		} else {
			for ( var i = 0; i < $checkboxes.length; i++) {
				/*$($checkboxes[i]).removeAttr("checked");*/
				$($checkboxes[i]).prop("checked",false);
			}
			$(this).html('全选');
		}
	});
	// 获取数据
	viewaction = '../../handler/gradeClass/selectAllClass';
	initialBind();
	initSearch();
	// 绑定删除按钮
	$("#delButton").click(function() {
		deleteDeparts();
	});
	if (depaId > 0) {
		$("#school").hide();
	}
});

// 加载数据到数据区域
function refreshContent(pageRecords) {
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	var tableContent = "";
	if (pageRecords.data.length == 0) {
		tableContent = "<tr><td colspan=\"7\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>";
	} else {
		$.each(pageRecords.data, function(itemIndex, item) {
			var clasRemark = "无备注";
			if (item.clasRemark != null && item.clasRemark != "") {
				clasRemark = item.clasRemark;
			}
			tableContent += "<tr>" + "<td><input type='checkbox'/ value='" + item.clasId + "'></td>" + "<td>"
					+ startIndex + "</td>" + "<td><a href='#addOrMod' data-toggle='modal' onclick='setDepartInfo("
					+ item.clasId + ")'>" + item.clasName + "</a></td>" + "<td>" + item.gradeName + "</td><td>"
					+ item.schoolName + "</td><td>" + clasRemark + "</td></tr>";
			startIndex++;
		});
	}
	$('#mainTable').empty().append(tableContent);
	$("#addGread").empty();
	return false;
}
function getparams() {
	var depaId = $("#schoolId").val();
	if (departmentTypeID == 3) {
		depaId = departId;
	}
	var gradId = $("#gradeId").val();
	params.depaId = depaId;
	params.gradId = gradId;
	params.searchType = $("#searchType").val();
	params.searchWord = $("#searchWord").val();
	params.currentPage = 1;
}
function getEditParams() {
	var depaId = $("#schoolType").val();
	if (departId > 0) {
		depaId = departId;
	}
	var gradId = $("#greadType").val();
	editParams.depaId = depaId;
	editParams.gradId = gradId;
	if ($("#gradeName").val() == "" || $("#gradeName").val() == undefined)
		editParams.gradName = "";
	else
		editParams.gradName = $("#gradeName").val();
	editParams.clasName = $("#className").val();
	editParams.clasRemark = $("#classRemark").val();
}
// 修改机构信息的准备工作
function setDepartInfo(departId) {
	$.ajaxSettings.async = false;
	if (departId > 0) {
		isAdd = false;
		// 修改
		$("#boxTitle").html("修改年级、班级");
		$("#sub").html("<i class='fa fa-check-square-o'></i> 修改");
		var url = "../../handler/gradeClass/selectOneClass";
		$.post(url, {
			"classId" : departId
		}, function(data) {
			if (data.ret) {
				var resultData = data.data.data;
				$("#className").val(resultData.clasName);
				$("#classRemark").val(resultData.clasRemark);
				$("#schoolType option[value='" + resultData.schoolName + "']").attr("selected", "selected");
				$("#schoolType").attr("disabled", "disabled");
				updatedata(resultData.schoolName);
				$("#greadType option[value='" + resultData.gradeName + "']").attr("selected", "selected");
			}
		}, "json");
		editParams.clasId = departId;
		return false;
	} else {
		isAdd = true;
		$("#schoolType").removeAttr("disabled");
		$("#boxTitle").html("新增年级、班级");
		$("#sub").html("<i class='fa fa-check-square-o'></i> 添加");
		cleanInfo();
	}
}

// 新增，或者修改
function addClass() {
	getEditParams();
	if (editParams.depaId <= 0) {
		infoEditNotice("error", "", "请选择正确的学校！");
		return false;
	}
	if (!(editParams.gradId > 0 || editParams.gradeName != "")) {
		infoEditNotice("error", "", "请选择正确的年级！");
		return false;
	}
	if (editParams.clasName == "" || editParams.clasName.length < 0) {
		infoEditNotice("error", "", "班级名称不能为空！");
		return false;
	}
	if (isAdd) {
		// 新增
		url = "../../handler/gradeClass/insertClass";
		$.post(url, editParams, function(data) {
			if (data.ret) {
				infoNotice(data.data.state, "", data.data.message);
				$('#addOrMod').modal('hide');
				initSearch();
			} else {
				infoNotice("error", "", data.errmsg);
			}
		}, "json");
		cleanInfo();
	} else {
		// 修改
		$("#sub").html("正在修改，请稍后……");
		url = "../../handler/gradeClass/updateClass";
		$.post(url, editParams, function(data) {
			$("#sub").html("修改");
			if (data.ret) {
				infoNotice(data.data.state, "", data.data.message);
				$('#addOrMod').modal('hide');
				initSearch();
			} else {
				infoNotice("error", "", data.errmsg);
			}
		}, "json");
		cleanInfo();
	}
	return;
}

// 删除
function deleteDeparts() {
	if (confirm("删除后可能会影响系统其他功能，请确认是否删除！")) {
		// 获取选择的用户id序列
		var classIds = new Array();
		$("input[type=checkbox]").each(function() {
			var $current = $(this);
			if ($current.is(':checked') )
				classIds.push($current.val());
		});
		// 如果选择的id为空，则提示错误信息
		if (classIds.length == 0) {
			infoNotice("error", "", "请先选中要删除机构！");
		}
		// 否则，向后台发送删除请求并显示结果信息
		else {
			$.post("../../handler/gradeClass/deleteClass", {
				"clasIdArray" : classIds.toString()
			}, function(data) {
				if (data.ret) {
					infoNotice(data.data.state, "", data.data.message);
					initSearch();
				} else {
					infoNotice("error", "", data.errmsg);
				}
			}, "json");
		}
	}
}

/**
 * 显示学校
 * 
 * @param data
 */
function showSchool() {
	AjaxJson("../../handler/department/viewDepartments", {
		type : 3
	}, showSchoolToSelect);
}
function showSchoolToSelect(data) {
	var data1 = data.data.departments;
	var Html = "<option value='-1'>选择学校</option>";
	for ( var i = 0; i < data1.length; i++) {
		Html += "<option value=" + data1[i].depaId + ">" + data1[i].depaName + "</option>";
	}
	$("#schoolId").html(Html);
	$("#schoolType").html(Html);
	$("#schoolId").change(function() {
		var schoolId = $(this).val();
		// 当学校被点击，请求年级
		updatedata(schoolId);
		$("#search").trigger("click");
	});
	$("#schoolType").change(function() {
		var schoolId = $(this).val();
		// 当学校被点击，请求年级
		updatedata(schoolId);
	});
}
/**
 * 发送年级请求
 * 
 * @param depid
 */
function updatedata(schoolId) {
	AjaxJson("../../handler/register/gradeInfo", {
		depa_id : schoolId
	}, showGradeToSelect);
}
/**
 * 显示年级
 * 
 * @param data
 */
function showGradeToSelect(data) {
	var data = data.data.gradeInfo;
	var Html = "<option value='-1'>选择年级</option>";
	for ( var i = 0; i < data.length; i++) {
		Html += "<option value=" + data[i].gradId + ">" + data[i].gradName + "</option>";
	}
	$("#gradeId").html(Html);
	Html += "<option value='-10000'>点击增加年级</option>";
	$("#greadType").html(Html);
	$("#gradeId").change(function() {
		$("#search").trigger("click");
	});
	$("#greadType")
			.change(
					function() {
						var gradeId = $(this).val();
						$("#addGread").empty();
						if (gradeId == -10000) {
							/*var greadHtml = "<label class='control-label'>新增年级：</label>"
									+ "<div class='controls'><input id='gradeName' class='input' type='text' name='gradeName'> *(例如“2014级”)</div>";*/
							var greadHtml="<div class=''><label class='control-label col-sm-3' for='gradeName'>新增年级：</label>"
										+"<div class='col-sm-6'><input id='gradeName' class='input form-control' type='text' name='gradeName'></div>"
										+"<div class='col-sm-3'><span class='help-block m-b-none'>*(例如'2014级')</span></div></div>";
							$("#addGread").append(greadHtml);
						}
					});
}
function cleanInfo() {
	$("#schoolType").val(-1);
	$("#greadType").val(-1);
	$("#gradeName").val("");
	$("#className").val("");
	$("#classRemark").val("");
}