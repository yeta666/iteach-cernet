/**
 * @author 张鑫
 * 
 * 选课时间管理相关js
 */

var limits = true;
var announcementInfo;
var isAllSelected = false;
var secondCol = 0;
var searchKey;
var startTime ;
var endTime ;
var school;
$(document).ready(function() {
	ShowColumn();
	paginationPage();
	//获取用户角色
	AjaxJson("../../handler/role/findRoleById",{userId:userId},backOfUserRoles);
//	searchCourseByName();
	timepicker("newstartTime");
	timepicker("newendTime");
	timepicker("startTime");
	timepicker("endTime");
	clickItem("a[name='return']");
	// 显示学校列表
	AjaxJson("../../handler/department/viewDepartments", {
		type : 3
	}, showSchoolToSelect);
	$("#submit").click(submit);
	$("#make-sure").click(deleteInfo);
	$("#delButton").click(function(){
		var csinIds = "";
		$("input[type='checkbox']").each(function(){
			if(this.checked)
			{
				csinIds += $(this).val()+",";
			}
		});
		if(csinIds.charAt(csinIds.length-1)==",")
			csinIds = csinIds.substring(0, csinIds.length-1);
		if(csinIds=="")
		{
			$("#model-info").html("请先勾选所需要的删除的选课时间");
		}
		else
		{
			$("#model-info").html("选课时间删除将无法恢复,是否继续删除");
		}
	});
	clickItem("#time-manager a");
	// 批量修改课程
	$("#addButton").click(function(){
		var csinIds = "";
		$("input[type='checkbox']").each(function(){
		/*	if($(this).is(":checked"))
			{
				csinIds += $(this).val()+",";
			}*/
			if ( $(this).is(":checked"))
			{
				csinIds += $(this).val() + ",";
			}
		});
		//alert(csinIds);
		if(csinIds.charAt(csinIds.length-1)==",")
			csinIds = csinIds.substring(0, csinIds.length-1);
		/*if(csinIds=="")
		{
			alert("请先勾选所需要修改的选课时间");
			$("a[name='return']").trigger("click");
			return;
		}*/
		if(csinIds=="")
		{
			$("#model-infos").html("请先勾选所需要修改的选课时间");
		}
		else{
			var Html="";
		                                                                                                                                                                                                                                                                                                                 
			Html="<div class=\"form-group col-sm-12\"><div class=\"span4\"><label class=\"control-label col-sm-3\" >开始时间：</label></div><div class=\"col-sm-3\"><input id=\"newstartTime\" class=\"input span4\" type=\"text\"placeholder=\"开始时间\" data-date-format=\"yyyy-mm-dd\"></div></div>"
				   +"<div class=\"form-group col-sm-12\">	<div class=\"span4\"><label class=\"control-label col-sm-3\" >结束时间：</label></div><div class=\"col-sm-3\"><input id=\"newendTime\" class=\"input span4\" type=\"text\"placeholder=\"结束时间\" data-date-format=\"yyyy-mm-dd\"></div></div>"
				   +"<div class=\"form-group col-sm-12\"><div class=\"span4\"><label class=\"control-label col-sm-3\" >备注：</label></div><div class=\"col-sm-3\"><textarea id=\"remark\"style=\"width: 400px;height: 150px;resize: none;\"></textarea></div></div>";
			$("#model-infos").html(Html);
		}
	});
	$("#search").click(searchCourseByName);
	
	
	$("#selectAll").click(function(){
		if(isAllSelected)
		{
			isAllSelected = false;
			$(this).html("全选");
			$("input[name='info-data']").each(function(index,item){
				$(this).removeAttr("checked");
			});
		}
		else
		{
			isAllSelected = true;
			$(this).html("取消");
			$("input[name='info-data']").each(function(index,item){
				$(this).attr("checked","checked");
			});
		}
	});
});
//返回用户角色
function backOfUserRoles(backInfo)
{
	var data = backInfo.data.roles;
	for(var i=0;i<data.length;i++)
	{
		var j = data[i].roleId;
		if(j==4||j==5||j==6||j==9)
		{
			limits = false;
		}
	}
	if(limits)
	{
		$("#quCourse").attr("style","display:none;");
	}
	firstView();
}

/**
 * 导航栏点击事件监听
 * 
 * @author ZhangXin
 */
function clickItem(location, callback) {
	$(location).click(function(e) {
		e.preventDefault();
		$(this).tab('show');
		callback();
	});
}
/**
 * 获取所有的学校
 */
function findAllSchool() {
	var param = {
			type : 3
	};
	var url = "../../handler/department/viewDepartments";
	AjaxJson(url, param, showSchool);
}
function showSchoolToSelect(data) {
	var data = data.data.departments;
	var Html = "<option value='-1'>选择学校</option>";
	for ( var i = 0; i < data.length; i++) {
		Html += "<option value=" + data[i].depaId + ">" + data[i].depaName
		+ "</option>";
	}
	$("#quCourse").html(Html);
	$("#quCourse").change(function() {
		/*
		 * var param = {departId:$(this).val()}; //获取所有课程
		 * AjaxJson("../../handler/course/viewCourseList",param,showCourse);
		 */
		var depid = $(this).val();
		updatedata(depid);
	});
}
/**
 * 点击select框中的数据时候直接请求后天返回相应结果
 */
function updatedata(school)
{
	viewaction = '../../handler/course/courseTime';
	params = {
			"startTime" : "",
			"endTime" : "",
			"searchWord" : "",
			"pageArray" : new Array(),
			"recordPerPage" : 10,
			"school" : school,
	};
	
	initialBind();// 绑定分页的一些操作响应
	initSearch();
}
/**
 * 展示所有学校
 * 
 * @param data
 */
function showSchool(data) {
	var data = data.data.departments;
	var Html = "<option value='-1'>选择学校</option>";
	for ( var i = 0; i < data.length; i++) {
		Html += "<option value=" + data[i].depaId + ">" + data[i].depaName
		+ "</option>";
	}
	$("#all-school").html(Html);
	$("#all-school").change(function() {
		var param = {
				departId : $(this).val()
		};
		// 获取所有课程
		AjaxJson("../../handler/course/viewCourseList", param, showCourse);
	});
}
/**
 * 显示所有课程（具体学校的）
 * 
 * @param data
 * @returns
 */
function showCourse(data) {
	var data = data.data.courses;
	var HTML = "<option value='-1'>选择课程</option>";
	for ( var i = 0; i < data.length; i++) {
		HTML += "<option value=" + data[i].courseId + ">" + data[i].courseName
		+ "</option>";
	}
	$("#all-course").html(HTML);
}
/**
 * 提交时间
 */
function submit() {
	/*if ($("#all-school").val() < 0) {
		alert("请选择学校");
		return;
	}
	if ($("#all-course").val() < 0) {
		alert("请选择课程");
		return;
	}*/
	var ids ="";
	$("input[name='info-data']").each(function(index,item){
		/*if(item.checked)
			ids+=item.value+",";*/
		if ( $(this).is(":checked"))
		{
		ids += $(this).val() + ",";
	
		}
	});
	if(ids.charAt(ids.length-1)==',')
		ids = ids.substring(0, ids.length-1);
	//alert(ids);
	if("" == ids)
	{
		alert("还没有选择需要修改的课程时间");
		return;
	}
	var timeStart = new Date($("#newstartTime").val());
	var timeEnd = new Date($("#newendTime").val());
	var param = {
			csinId:ids,
			openDate : timeStart,
			closeDate : timeEnd,
			csinRemark : $("#remark").val()
	};
	AjaxJson("../../handler/course/setTimeOfSelectCourse", param,
			timeSubmited);
}
/**
 * 提交完成
 * 
 * @param data
 */
function timeSubmited(data) {
	if(data.data.setCourseSelectTime=="时间错误!")
	{
		alert("开始时间不能晚于结束时间！");
		return;
	}
	searchCourseByName();
	$("a[name='return']").trigger("click");
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
function firstView()
{
	secondCol = getRequest("secondCol");
	viewaction = '../../handler/course/courseTime';
	var searchKey = $("#keyword").val();
	if (null == searchKey)
		searchKey = "";
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	var school = $("#quCourse").val();
	if(limits)
		params = {
			"startTime" : startTime,
			"endTime" : endTime,
			"searchWord" : searchKey,
			"pageArray" : new Array(),
			"recordPerPage" : 10,
			"school":departId,
			"currentPage" : 1,
			"secondCol" : secondCol
	};
	else
		params = {
			"startTime" : startTime,
			"endTime" : endTime,
			"searchWord" : searchKey,
			"pageArray" : new Array(),
			"recordPerPage" : 10,
			"currentPage" : 1,
			"secondCol" : secondCol
	};
	if ($.cookie("Search") != null && $.cookie("Search") != "") {
		var SearchCookie = $.cookie("Search");
		var SearchPar = JSON.parse(SearchCookie);
		if (SearchPar.secondCol == secondCol) {
			params = SearchPar;
			if(params.searchWord!=null){
				$('#keyword').val(params.searchWord);
			}
			if(params.startTime!=null||params.endTime!=null){
				$('#startTime').val(params.startTime);
				$('#endTime').val(params.endTime);
			}
		} else {
			// 删除cookie
			document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		}
	}
	initialBind();// 绑定分页的一些操作响应
	initSearch();
}
/**
 * 分页显示选课时间信息
 * 
 * @author ZhangXin
 * @param searchCourseName
 */
function searchCourseByName() {
	viewaction = '../../handler/course/courseTime';
	searchKey = $("#keyword").val();
	if (null == searchKey)
		searchKey = "";
    startTime = $("#startTime").val();
	endTime = $("#endTime").val();
	school = $("#quCourse").val();
	if(limits)
		school = departId;
	params = {
			"startTime" : startTime,
			"endTime" : endTime,
			"searchWord" : searchKey,
			"pageArray" : new Array(),
			"recordPerPage" : 10,
			"school" : school,
			"currentPage" : 1,
			"secondCol" : secondCol
	};
	document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
	var cookieString = "Search=";
	cookieString = cookieString + JSON.stringify(params);
	document.cookie = cookieString;
	initialBind();// 绑定分页的一些操作响应
	initSearch();
}
function refreshContent(pageRecords) {
	$("#content-table").empty();
	$.ajaxSettings.async = false;
	var HTML = "";
	if(pageRecords.data.length<=0)
	{
		HTML +="<div class='alert'>"+
		"<button type='button' class='close' data-dismiss='alert'>&times;</button>"+
		"<strong>提示!</strong> 没有符合您所需查询数据..."+
		"</div>";
		$("#waring-nodata").html(HTML);
	}
	else
	{
		$.each(pageRecords.data,function(itemIndex, item) {
			var intime = new Date(item.openTime);
			var outtime = new Date(item.closeTime);
			HTML += "<tr>"+
			"<td><input name='info-data' type='checkbox' value='"+item.id+"'></td>"+
			/*"<td>"+item.departName+"</td>"+*/
			"<td>"+(itemIndex+1)+"</td>"+
			"<td>"+item.courseName+"</td>"+
			"<td>"+item.courseCode+"</td>"+
			"<td>"+(item.openTimeStr==null?'无上限时间':item.openTimeStr)+"</td>"+
			"<td>"+(item.closeTimeStr==null?'无下限时间':item.closeTimeStr)+"</td>"+
			"<td>"+(item.courseOpenToAll==0?'<p style=\'color:green;\'>校选课</p>':'<p style=\'color:red;\'>公选课</p>')+"</td>"+
			"</tr>";
		});
		$("#content-table").html(HTML);
	}

	return false;
}
/**
 * 删除数据
 */
function deleteInfo()
{
	var csinIds = "";
	$("input[type='checkbox']").each(function(){
		if(this.checked)
		{
			csinIds += $(this).val()+",";
		}
	});
	if(csinIds.charAt(csinIds.length-1)==",")
		csinIds = csinIds.substring(0, csinIds.length-1);
	if(csinIds=="")
		return;
	else
	{
		AjaxJson("../../handler/course/deleteTimeOfSelectCourse", {csinIds:csinIds}, backDelete);
	}
}
/**
 * 删除数据返回信息
 * @param data
 */
function backDelete(data)
{
	alert(data.data.deleteInfo);
	//重新展示信息
	searchCourseByName();
}
/**
 * 处理控件位置函数
 * @param This
 * @param target
 */
function showFoo(This,target)
{
	//获取源控件位置
	var xThis = This.offset().left;
	var yThis = This.offset().top;
	var heightThis = This.height();
	var widthThis = This.width();
	//获取目标控件位置
	var widthTarget = target.width();
	var xTarget = xThis + widthThis/2 - widthTarget/2 + 10;
	var yTarget = yThis + heightThis + 10;
	//修改样式
	$("#content").removeAttr("style");
	var style = "position:absolute;top:"+yTarget+"px; left:"+xTarget+"px; display:block;";

	target.attr("style",style);
	target.show();
};