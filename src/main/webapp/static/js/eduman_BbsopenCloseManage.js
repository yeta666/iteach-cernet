/*
 *  author：郭海蓉 
 *  time:2013-07
 */
var flag=0,departIdFirset=0,departIdtemp=0;
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
	
	$('#bbs_InfoManage').attr("class", "active");
	
	
	if (departmentTypeID==1) {// 市级
		departIdFirset=0;
		$("#seleteSchoolID").show();
		selceteSchool();
	} else {
		$("#seleteSchoolID").hide();
		departIdFirset=departIdtemp;
	}
	
	
	$("#searchWord").show();
	initialBind();// 绑定分页的一些操作响应
	$("#searchWord").val("");
	secondCol = getRequest("secondCol");
	firstShowData();
	// 绑定全选/取消全选按钮
	$('#choosePostStatus').click(function() {
		if($(this).html()=="全选"){
			$('input[name="choosePostSta"]').prop("checked", true);
			$(this).text("反选");
		}
		else{
		$('input[name="choosePostSta"]').prop("checked",false);
		$(this).text("全选");
		}
	});
//	$('#choosePostStatus').toggle(function() {
//		$('input[name="choosePostSta"]').attr("checked", "true");
//		$(this).text("反选");
//	}, function() {
//		$('input[name="choosePostSta"]').each(function() {
//			if ($(this).attr("checked")) {
//				$(this).removeAttr("checked");
//			} else
//				$(this).attr("checked", "true");
//		});
//		$(this).text("全选");
//	});

	$("#searchType").change(function(){ 
		searchType=$("#searchType").val();
		if (searchType==3) {
			$("#searchWord").hide();
			$("#searchTypeTwo").show();
			flag=1;
		}else{
			$("#searchTypeTwo").hide();
			$("#searchWord").show();
		}
	});
	searchPost();//搜索
});
//显示用户所有课程的帖子（按时间排序）
function firstShowData(){
	viewaction = '../../handler/course/viewCoursesBbsList';
	params = {
			"departId":departIdFirset,
			"searchWord":'',
			"searchType" :1,
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
			if (departmentTypeID == 1){
				$('#seleteSchoolID').val(params.departId);
			}
			$('#searchType').val(params.searchType);
			if(params.searchType==3&&params.searchWord!=''){
				$('#searchTypeTwo').val(params.searchWord);
			}else{
				$('#searchWord').val(params.searchWord);
			}
		} else {
			// 删除cookie
			document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		}
	}
	initSearch();
}


//加载数据到数据区域（）
function refreshContent(pageRecords) {
	$.ajaxSettings.async = false;
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	var tableContent="";
	var flag2="";
	var openOrClose="";
	var	tdclass="";
	if (pageRecords.data.length == 0) {
		tableContent = "<tr><td colspan=\"6\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>";
	} else {
		$.each(pageRecords.data, function(itemIndex, item) {
			if (item.bbsStatus) {
				flag2="开";
				tdclass="";
			}else{
				flag2="闭";
				tdclass="info";
			}
			tableContent += "<tr class='"+tdclass+"'><td class='tdcenter'><input type=\"checkbox\" value="+item.courseId+" name=\"choosePostSta\"></td><td  class='tdcenter'>"+startIndex+"</td><td class='tdcenter'>"+ 
			item.courseCode+"</td><td class='tdcenter'>"+item.courseName+"</td><td class='tdcenter'>"+item.moderator+"</td><td class='tdcenter'>"+flag2+"</td><tr>";
			startIndex++;
		})
	}
	$('#openCloseBBS tbody:nth-child(2)').empty().append(tableContent);
	return false;
}
var departId="";
var searchType="";
var searchWord="";
//通过关键词，搜索帖子
function searchPost(){
	$("#searchPostInOC").click(function(){
		var departIdfor=0;
		if (departmentTypeID==1) {
			departIdfor = $("#seleteSchoolID").val();
		}else{
			departIdfor=departIdtemp;
		}
		searchType = $("#searchType").val();
		if (flag==1) {
			searchWord=$("#searchTypeTwo").val();
		}else{
			searchWord=$("#searchWord").val();
		}
		viewaction = '../../handler/course/viewCoursesBbsList';
		params = {
				"searchWord":searchWord,
				"departId":departIdfor,
				"searchType" :searchType,
				"pageArray" : new Array(),
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

}
//设置论坛版块开闭功能
function bbsOpenClose(statype){
	var courseIds="";//记录选择版块的ids
	$("input[name='choosePostSta']").each(function() {
		var $current = $(this);
		if ($current.is(":checked") )
		{
			if (courseIds=="") {
				courseIds+=$current.val();
			}else{
				courseIds+=","+$current.val();
			}

		}
	});
	if (courseIds=="") {
		alert("请选中要操作的记录！");
		return false;
	}else{
		$.ajax({
			type : 'post',
			contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
			url : "../../handler/course/setCourseBbsStatus",
			dataType : 'json',
			data:{courseIds:courseIds,
				newStatus:statype},
				success : function(data) {
					if (data.ret) {
						alert("操作成功！");
						firstShowData();
					} else {
						alert(data.message);
					}
				}
		});
	}
}