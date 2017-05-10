/**
 *  author：郭海蓉 
 *  time:2013-07
 */
var viewaction ="",searchKeyd="";
var params;
var courseId = 0;
var startTime="",endTime="";
var orignalDepartID=0;//记录保留登陆用户的departID
var departIdtemp=0,userIdtemp=0;
var departIdtempS=0,userIdtempS=0;
$(document).ready(function() {
	orignalDepartID=departId;
	ShowColumn();
	backToTop();
	if (userId <= 0) {
		$('#pagination').hide();
	} else{
		paginationPage();
	}
	// 新的时间插件显示示例
	timepicker("startTime");
	timepicker("endTime");

	$('#bbs_InfoManage').attr("class", "active");
	initialBind();// 绑定分页的一些操作响应
	// 市级管理员具有选择学校筛选的功能
	//默认显示（区分教师和教务管理员）
	if (departmentTypeID ==1) {//市级管理员
		$("#seleteSchoolID").show();
		selceteSchool();
		departId=0;
		selceteCourse();
		$("#seleteSchoolID").change(function(){ //学校改变时显示该学校下的可选课程
			$("#selecteCourserID").empty();
			departId = $("#seleteSchoolID").val();
			selceteCourse();
		});
		userIdtemp=0;
		departIdtemp=orignalDepartID;
	} else if(departmentTypeID !=1 && userType!=1 && userType!=2){//校级管理员
		$("#seleteSchoolID").hide();
		selceteCourse();
		userIdtemp=0;
		departIdtemp=orignalDepartID;
	}else if(departmentTypeID !=1 && userType==1 ){//学生
		$("#seleteSchoolID").hide();
		selceteCourseByuserID();
		departIdtemp=-2;
		userIdtemp=userId;
	}else if(departmentTypeID !=1 && userType==2 ){//教师
		$("#seleteSchoolID").hide();
		selceteCourseByuserID();
		departIdtemp=-1;
		userIdtemp=userId;
	}
	secondCol = getRequest("secondCol");
	firstShowBBSData();
	bindSearch();
	// 绑定全选/取消全选按钮
	$('#chooseallPost').click(function() {
		if($(this).html()=="全选"){
			$('input[name="choosePost"]').prop("checked", true);
			$(this).text("反选");
		}
		else{
		$('input[name="choosePost"]').prop("checked",false);
		$(this).text("全选");
		}
	});
	//搜索
	//通过选定的筛选条件和输入的搜索词，获取搜素结果，显示帖子信息

});

/**
 * 根据departid获取课程
 */
function bindSearch() {
	$('#search').click(function() {
		getParams();
		initSearch();
		return false;
	});
}

function getParams() {
	// 删除cookie
	document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
	courseId = $("#selecteCourserID").val();
	searchKeyd=$("#searchKeyd").val();
	startTime = $('#startTime').val();
	endTime = $('#endTime').val();
	// 点击搜索，一切重新查找
	params.currentPage = 1;
	params.courseId = courseId;
	params.searchWord=searchKeyd;
	params.startTime = startTime;
	params.endTime = endTime;
	// 添加cookie
	var cookieString = "Search=";
	cookieString = cookieString + JSON.stringify(params);
	document.cookie = cookieString;
}
function selceteCourse1() {
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/course/viewCourseListByDepart",
		dataType : 'json',
		data:{departId:departId,
			type:1},
		success : function(data) {
			var bbscourseList;
			if (data.ret) {			
				if ($.isEmptyObject(data.data.courses)) {
					bbscourseList+= "<option value='-1'>课程数据为空！！</option>";
				} else {
					bbscourseList+='<option value="0">所有课程</option>';
					$.each(data.data.courses, function(i, val) {
						bbscourseList+="<option value='" + val.courseId + "'>" + val.courseName + "</option>";
					});
				}
			} else {
				bbscourseList = "<option>"+data.errmsg+"</option>";
			}
			$("#selecteCourserID").empty().append(bbscourseList);
		}
	});
}
function firstShowBBSData(){
	//默认显示登陆用户管辖范围的帖子信息列表（市级为全部，校级为本校）
	viewaction = '../../handler/bbsPost/viewBbsPostList';
	params = {
			"searchWord":'',
			"startTime":'',
			"endTime":'',
			"departId":departIdtemp,
			"courseId" :courseId,
			"userId":userIdtemp,
			"queryType":0,
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
			if (departmentTypeID == 1&&params.departId!=orignalDepartID){
				$('#seleteSchoolID').val(params.departId);
			}
			$('#selecteCourserID').val(params.courseId);
			$('#startTime').val(params.startTime);
			$('#endTime').val(params.endTime);
			$('#searchKeyd').val(params.searchWord);
		} else {
			// 删除cookie
			document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		}
	}
	initialBind();
	initSearch();
}
//加载数据到数据区域（显示分页的帖子列表信息，鼠标经过显示全部时间）
function refreshContent(pageRecords) {
	var time="";
	$.ajaxSettings.async = false;
	var startIndex = (currentPage - 1) * 10 + 1;
	var tableContent="";
	if (pageRecords.data.length == 0) {
		tableContent = "<tr><td colspan=\"6\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>";
	} else {
		$.each(pageRecords.data, function(itemIndex, item) {
			var flag="";
			if (item.bbpoIstop) {
				flag+="<img src=\"../img/bbs_top.png\">";
			}
			if(item.bbpoIsbest){
				flag+="<img src=\"../img/bbs_star_bronze.png\">";
			}
			time=item.realTime.split(" ")[0];
			tableContent += "<tr>"+
			"<td><input type=\"checkbox\" value="+item.bbpoId+" name=\"choosePost\"></td><td>"+startIndex+"</td>"+
			"<td><a  href='javascript:onclick=visitPostDetail("+item.bbpoId+");'  id='\"+item.bbpoId+\"' title=\""+item.bbpoTitle+"\" >"+titleFormat(item.bbpoTitle,16)+flag+"</td>"+
			"<td>"+item.courseName+"</td>"+
			"<td>"+item.userName+"</td>"+
			"<td title='"+item.realTime+"'>"+time+ "</td>"+
			"<td>" + item.bbpoVisitnum + "/" + item.bbpoReplynum + "</td>"+
			"</tr>";
			startIndex++;
		});
	}
	$('#viewPostListTable tbody:nth-child(2)').empty().append(tableContent);
	return false;
}
//点击帖子名称，让帖子浏览数加1，再跳转到帖子信息显示页面
function visitPostDetail(id){
	$.ajaxSettings.async = false;
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/bbsPost/addVisitNum",
		dataType : 'json',
		data : {
			bbsPostId : id
		},
		success : function(data) {
			if(data.ret){
				window.location.href = "bbs_viewPost.html?id="+ id+"&page=2"+"&firstCol=7&secondCol=30";
			}else{
				alert(data.errmsg);
			}
		}
	});
	return false;
}
//精品和置顶，取消精品，取消置顶（批量）
function setPost(setType,setValue){
	 classIDs="";
		var count=0;
		$("input[name='choosePost']").each(function(index, item) {
			if ( $(this).is(':checked'))
				{
				classIDs += $(this).val() + ",";
				count++;
				//alert(classIDs);
				}
				
		});
		if (classIDs.charAt(classIDs.length - 1) == ',')
			classIDs = classIDs.substring(0, classIDs.length - 1);

		if (count==0) {
			isNull = true;
			alert("请先选中!!");
		
	//postId="";
//	var setRecords = new Array();
//	// 获取选择的用户选择的帖子id序列
//	$("input[name='choosePost']").each(function() {
//		var $current = $(this);
//		if ($(this).attr('checked')!=undefined)
//			setRecords.push($(this).val());
//	});
//	if (setRecords.length==0) {
//		alert("请选中要操作的记录！");
//		return false;
	}else{
		$.ajax({
			type : 'post',
			contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
			url : "../../handler/bbsPost/setPostType",
			dataType : 'json',
			data:{bbsPostIds:classIDs,
				setType:setType,
				setValue:setValue},
				success : function(data) {
					if (data.ret) {		
						firstShowBBSData();
					} else {
						alert(data.message);
					}
				}
		});
	}
}


//删帖（批量）
function deletePost(){
	postId="";
	// 获取选择的用户选择的帖子id序列
	$("input[name='choosePost']").each(function() {
		var $current = $(this);
		if ($current.is(":checked") )
		{
			if (postId=="") {
				postId+=$current.val();
			}else{
				postId+=","+$current.val();
			}

		}
	});
	if (postId=="") {
		alert("请选中要操作的记录！");
		return false;
	}else{
		$.ajax({
			type : 'post', 
			contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
			url : "../../handler/bbsPost/deletePost",
			dataType : 'json',
			data:{bbsPostIds:postId,
			},
			success : function(data) {
				if (data.ret) {
					alert("删除成功！");
					firstShowBBSData();
				} else {
					alert(data.message);
				}

			}
		});
	}
}