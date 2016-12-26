/*
 *  author：郭海蓉 
 *  time:2013-07
 */
var searchType="",searchWord="",courseCateId="",courseCateList="",departIdtemp=0,departids=0;//搜索变量
var courId="";
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
	// 市级
	if (departmentTypeID ==1) {
		$("#seleteSchoolID").show();
		selceteSchool();
		departids=0;
	} else {
		$("#seleteSchoolID").hide();
		departids=departIdtemp;
	}
	selceteCourseCateNames();
	initialBind();// 绑定分页的一些操作响应
	//加载数据
	showDataFirst();
	// 绑定全选/取消全选按钮
	$('#chooseall').click(function() {
		if($(this).html()=="全选"){
			$('input[name="chooseCourse"]').prop("checked", true);
			$(this).text("反选");
		}
		else{
		$('input[name="chooseCourse"]').prop("checked",false);
		$(this).text("全选");
		}
	});
	$("#search").click( function () {
		if (departmentTypeID ==1) {
			departId = $("#seleteSchoolID").val();
		}else{
			departId = 	departIdtemp;
		}
		courseCateId=$("#courseCateId").val();
		searchType=$("#searchType").val();
		searchWord=$("#searchWord").val();
		viewaction = '../../handler/course/courseList';
		params = {
				"departId":departId,
				"courseCateId":courseCateId,
				"searchType":searchType,
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
function showDataFirst() {
	secondCol = getRequest("secondCol");
	viewaction = '../../handler/course/courseList';
	params = {
			"departId":departId,
			"courseCateId":courseCateId,
			"searchType":searchType,
			"searchWord":searchWord,
			"courseCateId":0,
			"pageArray":new Array(),
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
			if(params.searchWord!=null){
				$('#searchWord').val(params.searchWord);
			}
			$('#seleteSchoolID').val(params.departId);
			$('#courseCateId').val( params.courseCateId);
			$('#searchType').val(params.searchType);
		} else {
			// 删除cookie
			document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		}
	}
	initSearch();
}
//加载数据到数据区域
function refreshContent(pageRecords) {
	$.ajaxSettings.async = false;
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	var tableContent="";
	var time="",flag="";
	if (pageRecords.data.length == 0) {
		tableContent = "<tr><td colspan=\"11\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>";
	} else {
		var strOpentoAll ="";
		$.each(pageRecords.data, function(itemIndex, item) {
			if(item.courOpenToAll == 0)
				strOpentoAll = "校选课";
			else
				strOpentoAll = "公选课";
			tableContent += "<tr>" +
		    "<td class='tdcenter'><input type=\"checkbox\" value="+item.courId+" name=\"chooseCourse\"></td>"+
		    "<td class='tdcenter'>"+startIndex+"</td>"+
			"<td class='tdcenter'>"+item.courCode+"</td>"+
			"<td class='tdcenter'><a href='eduman_Course_Modify.html?id="+item.courId+"&firstCol=9&secondCol=37'>"+item.courName+"</td>"+
			"<td class='tdcenter'>"+item.courType+"</td>"+
			"<td class='tdcenter'>"+item.courCredit+"</td>"+
			"<td class='tdcenter'>"+item.cateNames+"</td>"+
			"<td class='tdcenter'>"+item.departName+"</td>"+
			"<td class='tdcenter'>"+strOpentoAll+"</td>"+
			"</td><tr>";
			startIndex++;
		});
	}
	$('#tableContent tbody:nth-child(2)').empty().append(tableContent);
	return false;
}
//选择领域
function selceteCourseCateNames() {
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/courseCategory/viewAllCates",
		dataType : 'json',
		data:{
			isOpen:1
		},
		success : function(data) {
			if (data.ret) {
				if ($.isEmptyObject(data.data.courseCates)) {
					courseCateList = "<option value='-1'>课程领域为空！！</option>";
				} else {
					courseCateList += "<option value=\"0\">全部领域</option>";
					$.each(data.data.courseCates, function(i, val) {
						courseCateList += "<option value='" + val.cocaId + "'>" + val.cocaName + "</option>";
					});
				}

			} else {
				courseCateList = "<option value='-1'>"+data.errmsg+"</option>";
			}
			$("#courseCateId").empty().append(courseCateList);
		}
	});
}
//删除
function deleteCourse() {
	courId="";
		var count=0;
	$("input[name='chooseCourse']").each(function(index, item) {
		var $current = $(this);
		if ( $(this).is(":checked"))
		{
			courId += $(this).val() + ",";
		count++;
		//alert(classIDs);
		}
	});
	if (courId.charAt(courId.length - 1) == ',')
		courId = courId.substring(0, courId.length - 1);

	if (count==0) {
		isNull = true;
		alert("你还没有勾选需要删除的课程,请先勾选!!");
	} else {
	
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/course/delete",
		dataType : 'json',
		data:{courseIds:courId,
		},
		success : function(data) {
			if (data.ret) {		
				alert("删除成功！");
				showDataFirst();
			} else {
				alert(data.errmsg);
			}

		}
	});
	}
};

//一键更新相应课程的成绩
function updateScore(){
	$("input[name='chooseCourse']").each(function() {
		var $current = $(this);
		if ($(this).is(":checked"))
		{
			if (courId=="") {
				courId+=$current.val();
			}else{
				courId+=","+$current.val();
			}

		}
	});
	if(courId==""){
		alert("请先选择课程，然后再执行更新成绩操作！");
		return;
	}
	$("#updateScoreModal").modal('show');
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/course/updateScore",
		dataType : 'json',
		data:{courseIds:courId,
		},
		success : function(data) {
			if (data.ret) {		
				alert("更新成绩成功！");
			} else {
				alert(data.errmsg);
			}
			$("#updateScoreModal").modal('hide');
		}
	});
}