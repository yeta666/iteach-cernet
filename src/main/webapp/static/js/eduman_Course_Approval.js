/*
 *  author：郭海蓉 
 *  time:2013-07
 */
var searchType="",startTime="",endTime="",searchWord="",flag=0,departIdtemp=0;//搜索变量
var secondCol = 0;
$(document).ready(function() {
	departIdtemp=departId;
	ShowColumn();//显示功能栏
	backToTop();//回顶部
	if (userId <= 0) {
		/** 加载分页 */
		$('#pagination').hide();
	} else{
		paginationPage();
	}
	$('#eduman').attr("class", "active");
	// 新的时间插件显示示例
	timepicker("startTime");
	timepicker("endTime");

	selceteSchool();
	$("#searchWord").show();
	$("#searchWord").val("");
	initialBind();// 绑定分页的一些操作响应
	// 市级
	if (departmentTypeID ==1) {
		$("#seleteSchoolID").show();
		selceteSchool();
		departId=0;
	} else {
		$("#seleteSchoolID").hide();
	}
	secondCol = getRequest("secondCol");
	//显示审核课程列表
	viewaction = '../../handler/course/queryCourseOfCheck';
	params = {
			"departmentId":departId,
			"searchType":3,
			"searchWord":searchWord,
			"startTime":startTime,
			"endTime":endTime,
			"pageArray":new Array(),
			"recordPerPage" : 10,
			"currentPage" : 1,
			"secondCol" : secondCol
	};
	if ($.cookie("Search") != null && $.cookie("Search") != "") {
		var SearchCookie = $.cookie("Search");
		var SearchPar = JSON.parse(SearchCookie);
		if (SearchPar.secondCol == secondCol) {
			params = SearchPar;
			$('#seleteSchoolID').val(params.departmentId);
			$('#searchType').val(params.searchType);
			if(params.searchWord!=null){
				$('#searchWord').val(params.searchWord);
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
	initSearch();
	$("#searchType").change(function(){ 
		searchType=$("#searchType").val();
		if (searchType==3) {
			$("#searchWord").hide();
			$("#searchTypeTwo").show();
			flag=1;
		}else{
			$("#searchTypeTwo").hide();
			$("#searchWord").show();
			flag=0;
		}
	});
	//搜索
	$("#search").click( function () {
		if (departmentTypeID ==1) {
			departId = $("#seleteSchoolID").val();
		} else{
			departId = departIdtemp;
		}
		searchType=$("#searchType").val();
		startTime=$("#startTime").val();
		endTime=$("#endTime").val();
		if (flag==1) {
			searchWord=$("#searchTypeTwo").val();
		}else{
			searchWord=$("#searchWord").val();
		}
		viewaction = '../../handler/course/queryCourseOfCheck';
		params = {
				"departmentId":departId,
				"searchType":searchType,
				"startTime":startTime,
				"endTime":endTime,
				"searchWord":searchWord,
				"pageArray":new Array(),
				"recordPerPage" : 10,
				"currentPage" : 1,
				"secondCol" : secondCol
		};	
		document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		var cookieString = "Search=";
		cookieString = cookieString + JSON.stringify(params);
		document.cookie = cookieString;
		initSearch();
	});
});

//加载数据到数据区域
function refreshContent(pageRecords) {
	$.ajaxSettings.async = false;
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	var tableContent="";
	var time="",flag="";
	if (pageRecords.data.length == 0) {
		tableContent = "<tr><td colspan=\"7\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>";
	} else {
		$.each(pageRecords.data, function(itemIndex, item) {
			if (item.courVerify==0) {
				flag="未审核";
			}else if(item.courVerify==1){
				flag="通过";
			}else{
				flag="拒绝";
			}

			if(item.courCateName==""||item.courCateName==null||item.courCateName=="undefined"){
				item.courCateName = "暂无相关领域";
			}
			time=item.courCreateTime.split(" ")[0];
			tableContent += "<tr><td class='tdcenter'>"+startIndex+"</td>"+
			"<td class='tdcenter'>"+item.courCode+"</td>"+
			"<td class='tdcenter'>"+item.courCateName+"</td>"+
			"<td class='tdcenter'><a href='#addOrMod' data-toggle='modal' onclick='updataInfo("+item.courId+")'>"+item.courName+"</a></td>"+
			"<td class='tdcenter'>"+item.courCreateUserName+"</td>"+
			"<td class='tdcenter' title='"+item.courCreateTime+"'>"+time+"</td>"+
			"<td class='tdcenter'>"+flag+"</td><tr>";
			startIndex++;
		});
	}
	$('#tableContent tbody:nth-child(2)').empty().append(tableContent);
	return false;
}
//搜素

//查看课程详情，执行审核操作
function updataInfo(courId){
	if (courId>0) {
		var tableContent="";
		//显示课程详情
		$.ajax({
			type : 'post',
			contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
			url : "../../handler/course/queryOneCheckCourse",
			dataType : 'json',
			data:{courseId:courId},
			success : function(data) {
				if (data.ret) {
					if ($.isEmptyObject(data.data.courseInfo)) {
						tableContent="<tr><td>数据为空！！</td></tr>";
					}else{
						var schelue="暂无安排";
						if(data.data.courseInfo.timeSchelue!=null||
								data.data.courseInfo.timeSchelue!=undefined){
							schelue=data.data.courseInfo.timeSchelue;
						}						
						tableContent="<tr  class=\"success\"><td width='100px'>课程代码:</td><td>"+data.data.courseInfo.courCode+"</td></tr>"+
						"<tr><td >课程分类:</td><td>"+data.data.courseInfo.courCateName+"</td></tr>"+
						"<tr><td>课程名称:</td><td>"+data.data.courseInfo.courName+"</td></tr>"+
						"<tr><td >课程描述:</td><td>"+data.data.courseInfo.description+"</td></tr>"+
						"<tr><td >课程安排:</td><td>"+schelue+"</td></tr>"+
						"<tr><td >申请者:</td><td>"+data.data.courseInfo.courCreateUserName+"</td></tr>"+
						"<tr><td >申请时间:</td><td>"+data.data.courseInfo.courCreateTime+"</td></tr>";
						if(data.data.courseInfo.courVerify>0){
							$("#buttonFunction").empty().append("<button class=\"btn btn-warning\" id=\"undoAl\" type=\"button\">返回</button>");
							if(data.data.courseInfo.courVerify==1){
								tableContent+="<tr><td '>审核状态:</td><td>通过</td></tr>";
							}else{
								tableContent+="<tr><td >审核状态:</td><td>拒绝</td></tr>";
							}
						}else{
							$("#buttonFunction").empty().append("<button class='btn btn-success' id='pass' type='button'><i class='fa fa-check'></i>&nbsp;通过</button> <button class='btn btn-danger' id='refuse' type='button'><i class='fa fa-close'></i>&nbsp;拒绝</button><button class='btn btn-warning' id='undoAl' type='button'><i class='fa fa-question'></i>&nbsp;暂不审核</button>");
						}
						if (data.data.courseInfo.courOpenToAll==false) {
							$("#publicState").val(0);
						}else{
							$("#publicState").val(1);
						}
						if(data.data.courseInfo.courCreatorDepaType==1){
							$("#publicState").val(1);
							$("#publicState").attr("disabled","disabled");
						}
						$("#courState").val(data.data.courseInfo.courState);
					}
				} else {
					alert(data.errmsg);
				}
			}
		});
		$('#modalTable tbody:nth-child(2)').empty().append(tableContent);
		var openToAlltemp="";
		//审核通过
		$("#pass").click( function () { 
			openToAlltemp=$("#publicState").val();
			$.ajax({
				type : 'post',
				contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
				url : "../../handler/course/checkCouse",
				dataType : 'json',
				data:{courseId:courId,
					status:1,
					openToAll:openToAlltemp,
					courState:$("#courState").val()},
					success : function(data) {
						if (data.ret) {
							alert("审核操作成功！");
							$('#addOrMod').modal('hide');
							window.location.href="eduman_Courses_Approval.html?firstCol=9&secondCol=38";
						} else {
							alert(data.errmsg);
						}
					}
			});
		});
		//审核拒绝
		$("#refuse").click( function () { 
			$.ajax({
				type : 'post',
				contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
				url : "../../handler/course/checkCouse",
				dataType : 'json',
				data:{courseId:courId,
					status:2,
					openToAll:openToAlltemp,
					courState:$("#courState").val()},
					success : function(data) {
						if (data.ret) {
							alert("审核操作成功！");
							$('#addOrMod').modal('hide');
							window.location.href="eduman_Courses_Approval.html?&firstCol=9&secondCol=38";
						} else {
							alert(data.errmsg);
						}
					}
			});
		});
		//暂不审核
		$("#undoAl").click( function () { 
			$('#addOrMod').modal('hide');
		});
	}

}
