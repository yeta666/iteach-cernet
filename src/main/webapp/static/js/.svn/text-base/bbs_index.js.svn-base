/**
 *  author：郭海蓉 
 *  time:2013-08 
 */
var viewaction = "",params="";
var courseId = 0,recordPerPage = 10;
$(document).ready(function() {
	colIds = urlColHtml();
	ShowColumn();
	backToTop();
	if (userId <= 0) {
		$('#pagination').hide();
	} else {
		paginationPage();
	}
	$('#bbsIndex').attr("class", "active");
	bbs_viewCourse();
	initialBind();

	// 默认显示用户关联的所有课程的帖子（按时间排序）
	secondCol = getRequest("secondCol");
	viewaction = '../../handler/bbsPost/viewBbsPostList';
	params = {
			"departId" : -1,
			"courseId" : courseId,
			"userId" : userId,
			"queryType" : 1,
			"searchWord" : '',
			"pageArray" : new Array(),
			"recordPerPage" : recordPerPage,
			"currentPage" : 1,
			"secondCol" : secondCol
	};
	$('#searchKeyWord').val(params.searchWord);
	// /判断cookie中是否有查询参数
	if ($.cookie("Search") != null && $.cookie("Search") != "") {
		var SearchCookie = $.cookie("Search");
		var SearchPar = JSON.parse(SearchCookie);
		if (SearchPar.secondCol == secondCol) {
			params = SearchPar;
			$('#searchKeyWord').val(params.searchWord);
			if(params.courseId>0){
				$("li[name='bbslist']").removeClass();
				$("li[id='l"+params.courseId+"']").attr("class","active");
			}
		} else {
			// 删除cookie
			document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		}
	}
	initSearch();

	searchPost();
});
/**
 * 根据讨论主题搜索
 */
function searchPost() {
	$("#searchPost").click(function() {
		var searchWord = $("#searchKeyWord").val();
		viewaction = '../../handler/bbsPost/viewBbsPostList';
		params = {
				"searchWord" : searchWord,
				"queryType" : 1,
				"departId" : -1,
				"courseId" : courseId,
				"userId" : userId,
				"pageArray" : new Array(),
				"recordPerPage" : recordPerPage,
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

/**
 * 获取显示用户关联的课程
 * @author MrYang
 */
function bbs_viewCourse() {
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/course/viewCoursesByUser",
		dataType : 'json',
		data : {
			userId : userId,
			type:1
		},
		success : function(data) {
			var bbscourseList;
			var count = 1;
			if (data.data == null) {
				return;
			}
			if (data.ret) {
				if ($.isEmptyObject(data.data.courses)) {
					bbscourseList = "<p>您还没有选择学习课程，请到学习中心选择课程开始学习！</p>";

				} else {
					bbscourseList = "<li class=\"active12 \" id=\"l0\" name=\"bbslist\"><a id='0'  href='#' onclick='obtainCouseId(this);'>全部课程</a></li>";
					$.each(data.data.courses, function(i, val) {
						bbscourseList += "<li name=\"bbslist\" id=\"l"+val.courseId+"\"><a id=" + val.courseId+" href='#' onclick='obtainCouseId(this);'>" + val.courseName+"</a></li>";
					});
				} 
			}else{
				alert(data.errmsg);
			}
			$("#bbscourselist").append(bbscourseList);
			$("li[name='bbslist']").click(function(){
				$("li[name='bbslist']").removeClass();
				 $(this).addClass('active12');
			});
		}
	});
}

/**
 * 根据选定的课程名，获取课程id，显示该门课程下的帖子列表
 * @param obj
 * @returns {Boolean}
 */
function obtainCouseId(obj) {
	courseId = obj.id;
	var searchWord = $("#searchKeyWord").val();
	viewaction = '../../handler/bbsPost/viewBbsPostList';
	params = {
			"searchWord" : searchWord,
			"queryType" : 1,
			"departId" : -1,
			"courseId" : courseId,
			"userId" : userId,
			"pageArray" : new Array(),
			"recordPerPage" : recordPerPage,
			"currentPage" : 1,
			"secondCol" : secondCol
	};
	document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
	var cookieString = "Search=";
	cookieString = cookieString + JSON.stringify(params);
	document.cookie = cookieString;
	initSearch();
	return false;
}
/**
 * 我的讨论
 */
function myDiscss() {
	viewaction = '../../handler/bbsPost/viewBbsPostList';
	params = {
			"queryType" : 2,
			"departId" : -1,
			"courseId" : -1,
			"userId" : userId,
			"pageArray" : new Array(),
			"recordPerPage" : recordPerPage,
			"currentPage" : 1,
			"secondCol" : secondCol
	};
	document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
	var cookieString = "Search=";
	cookieString = cookieString + JSON.stringify(params);
	document.cookie = cookieString;
	initSearch();

}
/**
 * 我的回复
 */
function myReply() {
	viewaction = '../../handler/bbsPost/viewBbsPostList';

	params = {
			"queryType" : 3,
			"departId" : -1,
			"courseId" : -1,
			"userId" : userId,
			"pageArray" : new Array(),
			"recordPerPage" : recordPerPage
	};
	document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
	var cookieString = "Search=";
	cookieString = cookieString + JSON.stringify(params);
	document.cookie = cookieString;
	initSearch();

}
/**
 * 加载数据到数据区域（显示分页的帖子列表信息，鼠标经过显示全部时间）
 * @param pageRecords
 * @returns {Boolean}
 */
function refreshContent(pageRecords) {
	var time = "";
	$.ajaxSettings.async = false;
	var startIndex = (currentPage - 1) * 10 + 1;
	var tableContent = "";
	if (pageRecords.data.length == 0) {
		tableContent = "<tr><td colspan=\"6\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>";
	} else {
		$.each(pageRecords.data, function(itemIndex, item) {
			var flag = "";
			if (item.bbpoIstop) {
				flag += "<img src=\"../img/bbs_top.png\">";
			}
			if (item.bbpoIsbest) {
				flag += "<img src=\"../img/bbs_star_bronze.png\">";
			}
			time = item.realTime.split(" ")[0];
			tableContent +=
				"<tr>"+
				"<td>" + startIndex + "</td>"+
				"<td><a  href='javascript:onclick=visitPostDetail(" + item.bbpoId+ ");'  id='"+item.bbpoId+"'"+
				" title='" + item.bbpoTitle + "' >"+ titleFormat(item.bbpoTitle, 16) + flag + "</td>"+
				"<td>" + item.courseName + "</td>"+
				"<td>"+item.userName + "</td>"+
				"<td title='" + item.realTime + "'>" + time + "</td>"+
				"<td>"+item.bbpoVisitnum + "/" + item.bbpoReplynum + "</td>"+
				"</tr>";
			startIndex++;
		});
	}
	$('#viewPostListTable tbody:nth-child(2)').empty().append(tableContent);
	return false;
}
/**
 * 点击帖子名称，让帖子浏览数加1，再跳转到帖子信息显示页面
 * @param id
 * @returns {Boolean}
 */
function visitPostDetail(id) {
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
			if (data.ret) {
				window.location.href = "bbs_viewPost.html?id="+ id+"&page=1"+"&firstCol=3&secondCol=17";
			} else {
				alert("data.errmsg");
			}
		}
	});
	return false;
}