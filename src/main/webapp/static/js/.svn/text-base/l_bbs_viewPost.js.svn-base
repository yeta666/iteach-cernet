/**
 *  author：郭海蓉 
 *  time:2013-08
 */
var viewaction="",actionUrls="",isdelete=0,isbest=0,deleteReply=0,ISnewReply=0;
var bbstop=false;//置顶 标志
var best=false;//精品 标志
var role=0;//记录登陆者为学生还是老师
var actionUrlsArray=null;//记录action分割出来的数组
var pageid=-1;//标识页面是从“我的讨论”还是“论坛管理”跳转过来的，用于【删帖】后的页面跳转
var canNotDelete=0;
$(document).ready(function(){	
	//获取postid
	pageid=0;
	postId=location.search.split("?")[1].split("&")[0].split("=")[1];
	pageid=location.search.split("?")[1].split("&")[1].split("=")[1];
	//绑定编辑器插件
	$("#replayText").cleditor();
	//全局设置
	chacheCheck=false;
	ShowColumn();
	backToTop();
	if (userId <= 0) {
		$('#pagination').hide();
	} else{
		paginationPage();
	}
	$('#bbsIndex').attr("class", "active");
	$("#replayText").val("");//清空回复框
	//绑定分页的一些操作
	initialBind();
	//显示主帖信息
	viewPostInfo();
	//显示回复信息
	viewReplyInfo();
	$("#postfile").val("");
	//获取权限
	$.get("../../handler/authority/uAcWhiteList", function(data){
		actionUrls=data.data.acWhiteList;
		actionUrlsArray=actionUrls.split(",");
	});
	//是否有删帖权限
	if (canNotDelete==0) {
		for ( var i = 0; i < actionUrlsArray.length; i++) {
			if (actionUrlsArray[i]=="/bbsPost/deletePost") {
				isdelete=1;
				break;
			}else{
				isdelete=0;
			}
		}
	}
	//是否有精品置顶权限
	for ( var i = 0; i < actionUrlsArray.length; i++) {
		if (actionUrlsArray[i]=="/bbsPost/setPostType") {
			isbest=1;
			break;
		}else{
			isbest=0;
		}
	}
	//是否有删除回复权限
	for ( var i = 0; i < actionUrlsArray.length; i++) {
		if (actionUrlsArray[i]=="/bbsReply/deleteBbsReply") {
			deleteReply=1;
			break;
		}else{
			deleteReply=0;
		}
	}
	//是否可以进行回复操作
	for ( var i = 0; i < actionUrlsArray.length; i++) {
		if (actionUrlsArray[i]=="/bbsReply/createBbsReply") {
			ISnewReply=1;
			break;
		}else{
			ISnewReply=0;
		}
	}
	//显示回复信息
	viewReplyInfo();
	//显示回复区域
	if (ISnewReply==1) {
		$("#newReply").show();
	}
/**
*绑定鼠标经过事件
*$('#addAttachment').popover({
*	content:cont,
*	trigger:'hover',
*});
*/
	//显示删除自己的帖子的按钮
	if (canNotDelete==1) {
		$("#bbs_deleteMy").show();
	}else{
		$("#bbs_deleteMy").hide();
	}
	//显示删帖按钮
	if (isdelete==1 || canNotDelete==1) {
		$("#bbs_delete").show();
	}else{
		$("#bbs_delete").hide();
	}
	//置顶，取消置顶，精品，取消精品按钮的显示与隐藏
	if (isbest==1) {
		if (bbstop) {
			$("#bbs_canceltop").show();
			$("#bbs_top").hide();

		}else{
			$("#bbs_canceltop").hide();
			$("#bbs_top").show();
		}
		if(best){
			$("#bbs_canclestarte").show();
			$("#bbs_starte").hide();
		}else{
			$("#bbs_canclestarte").hide();
			$("#bbs_starte").show();
		}
	}
});
/**
 * 显示主帖信息
 */
function viewPostInfo(){
	$.ajaxSettings.async = false;
	var postContentInfo="";
	var flag="";
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/bbsPost/viewBbsPostDetail",
		dataType : 'json',
		data : {
			bbsPostId :postId
		},
		success : function(data) {
			if(data.ret){
				var flag="";
				if (data.data.isTop) {
					bbstop=true;
					flag+="<img src=\"../img/bbs_top.png\">";
				}
				if(data.data.isBest){
					best=true;
					flag+="<img src=\"../img/bbs_star_bronze.png\">";
				}
				postContentInfo+="<tr class=\"success\">"+
				"<td ><strong><i class=\"icon-comment\"></i> "+data.data.postTitle+flag+"</strong></td>"+
				"</tr>";
				if (userName==data.data.userName) {
					canNotDelete=1;
				}else{
					canNotDelete=0;
				}
				postContentInfo+=
					"<tr >"+
					"<td><small>作者："+data.data.userName+
					"&nbsp;&nbsp;"+
					"课程："+data.data.courseName+
					"&nbsp;&nbsp;"+
					"浏览："+data.data.visitNum+"&nbsp;&nbsp;回复："+data.data.replyNum+
					"&nbsp;&nbsp;"+
					"时间："+data.data.pubTime+"</small></td>"+
					"</tr>";
				postContentInfo+="<tr><td><p>"+data.data.postContent+"</p></td></tr>";
				if(data.data.attachs!=undefined){
					$.each(data.data.attachs, function(i, val) {
						postContentInfo+="<tr class='warning'>";

						postContentInfo+="<td>" +
						"<strong>附件:</strong>"+
						"&nbsp;&nbsp;&nbsp;&nbsp;"+
						"<a href='../../handler/load/download?attaId="+val.attaId+
						"&attaFilename="+val.attaFilename+"&attaLocation="+val.attaLocation+
						"&attaOriFilename="+val.attaOriFilename+"'>"
						+val.attaOriFilename+"</a></td>";

						postContentInfo+="</tr>";
					});
				}
			}else{
				postContentInfo+="<tr class=\"error\"><td><b>"+data.message+"</b></td></tr>";
			}
			$('#viewPostinfoTable tbody:nth-child(2)').empty().append(postContentInfo);
		}
	});
}
/**
 * 显示回复信息（分页）
 */
function viewReplyInfo(){
	viewaction = "../../handler/bbsReply/viewReplyList",
	params = {
			"bbsPostId":postId,
			"pageArray" : new Array(),
			"recordPerPage" : 10
	};	
	initSearch();
}
/**
 * 加载数据到数据区域
 * @param pageRecords
 */
function refreshContent(pageRecords) {
	$.ajaxSettings.async = false;
	var postReplyInfo="";
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	if (pageRecords.data.length == 0) {
		$('#pagination').hide();
		postReplyInfo = "<tr class=\"info\" ><td class=\"tdcenter\"><i class=\"icon-heart\"></i><strong>帖子还没有回复，快来抢沙发吧~~~</strong></td></tr>";
	} else {
		$.each(pageRecords.data, function(itemIndex, item) {
			postReplyInfo+="<tr class=\"info\"><td><small><strong>回复："+item.replyUserName+
			"&nbsp;&nbsp;" +
			"时间："+item.realReplyTime+"</strong></small>&nbsp;&nbsp;&nbsp;";
			if (deleteReply==1) {
				postReplyInfo+="<button type='button' class='btn btn-small btn-primary'"+
				"id='"+item.replyId+"'	name='"+item.replyId+"' "+
				"onclick='javascript:deleteBbsReply("+item.replyId+");'>"+
				"删除</button>";
			}
			postReplyInfo+="</td></tr>";
			postReplyInfo+="<tr><td><p>"+item.replyContent+"</p></td></tr>";
			if(item.attas!=undefined){
				$.each(item.attas, function(i, val) {
					postReplyInfo+="<tr class='warning'>";
					postReplyInfo+="<td>" +
					"附件:<a href='../../handler/load/download?attaId="
					+val.attaId+"&attaFilename="+val.attaFilename+
					"&attaLocation="+val.attaLocation+
					"&attaOriFilename="+val.attaOriFilename+"'>"
					+val.attaOriFilename+"</a></td>";
					postReplyInfo+="</tr>";
				});
			}

		});
	}
	$('#viewReplyInfoTable tbody:nth-child(2)').empty().append(postReplyInfo);
}

function deleteBbsReply(replyId){
	var bbsReplyId=replyId
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/bbsReply/deleteBbsReply",
		dataType : 'json',
		data:{bbsReplyId:bbsReplyId},
		success : function(data){
			if(data.ret){
				alert("删除回复成功！");
				//显示回复信息
				viewReplyInfo();
			}else{
				alert(data.errmsg);
			}
		}
	});
}
/**
 * 提交回复内容到后台
 */
function submitNewReplyContent() {
	var replayText=$("#replayText").val();
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/bbsReply/createBbsReply",
		dataType : 'json',
		data:{bbreBbpoId:postId,
			bbreUserId:userId,
			bbreContent:replayText,
			attaches:attachesIds},
			success : function(data){
				if(data.ret){
					alert("回复成功！");
					//显示回复信息
					viewReplyInfo();
					var temp=$("#replayText").cleditor()[0];
					$("#replayText").val("");
					temp.updateFrame();
					$("#attachment").empty();
					attachesIds="";
					$("#postfile").val("");
				}else{
					alert(data.errmsg);
				}
			}
	});
}

/**
 * 精品和置顶，取消精品，取消置顶（批量/单个）
 * @param setType
 * @param setValue
 */
function setPost(setType,setValue){
	// 获取选择的用户选择的帖子id序列
	var content="";
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/bbsPost/setPostType",
		dataType : 'json',
		data:{bbsPostIds:postId,
			setType:setType,
			setValue:setValue},
			success : function(data) {
				if (data.ret) {		
					content="操作成功，查看效果~~";
					$("#annocement").attr({ class: "alert alert-success"});
				} else {
					content=data.errmsg;
					$("#annocement").attr({ class: "alert alert-error"});
				}
				$("#annocement").empty().append(content);
				$("#annocement").show();
			}
	});
}

/**
 * 删帖（批量/单个）
 */
function deletePost(){
	var content="";
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/bbsPost/deletePost",
		dataType : 'json',
		data:{bbsPostIds:postId,
		},
		success : function(data) {
			if (data.ret) {	
				if(pageid==1){
					window.location.href="bbs_index.html?firstCol=3&secondCol=17";
				}else if(pageid==2){
					window.location.href="eduman_BbsInfoManage.html?firstCol=7&secondCol=30";
				}
			} else {
				alert(data.errmsg);
			}

		}
	});
}
/**
 * 删帖（批量/单个）
 */
function deletePostMy(){
	var content="";
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/bbsPost/deletePost",
		dataType : 'json',
		data:{bbsPostIds:postId,
		},
		success : function(data) {
			if (data.ret) {	
				alert("删帖成功！");
				if(pageid==1){
					window.location.href="bbs_index.html?firstCol=3&secondCol=17";
				}else if(pageid==2){
					window.location.href="eduman_BbsInfoManage.html?firstCol=7&secondCol=30";
				}
			} else {
				alert(data.errmsg);
			}
			
		}
	});
}
