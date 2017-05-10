var startTime = '';		// 查询的开始时间
var endTime = '';		// 查询的结束时间
var keyword=''; //查询关键字
var params;// 请求的参数
var viewaction = '';// 分页请求的action
var departmentT;//部门类别
var userT;//用户种类
var ids = null;//删除ID集合
var viewAllData;
/**
 * @author anny
 * 主函数
 */
$(document).ready(function() {
	ShowColumn();// Show Column
	backToTop();// back To Top
	timepicker("startTime");
	timepicker("endTime");
	showAllNotices();// 显示所有消息
	//bindSearch();// 绑定搜索按钮
	userT = userType;
	departmentT = departmentTypeID;
	if(userT==1||userT==2){		
		$("#noticeadddel").css("display","none");//学生，隐藏添加、删除按钮
	}
	
	else if(userT==3&&departmentT==1){		
		delNotice();
		bindDelClick();
		//modNotice();
	}
	else if(userT==3&&departmentT==3){
		delNotice();
		bindDelClick();
		//modNotice();
	}
	else if(userT==4){
		delNotice();
		bindDelClick();
		//modNotice();
	}
	bindSearchClick();
});


//设置参数
function getParams() {
	/*var searchWord = "";
	var startTime = "";
	var endTime = "";*/
	// 点击搜索，一切重新查找
	params.keyword = keyword;
	params.startTime = startTime;
	params.endTime = endTime;
}

/**
 * 刷新数据时，查询表格数据的
 */
function cleanFind(){
	$("#startTime").val("");
	$("#endTime").val("");
	$("#keyword").val("");
}

/**
 * 绑定搜索按钮点击事件
 */
function bindSearchClick() {
	$("#search").click(function(){
		startTime = $("#startTime").val();
		endTime = $("#endTime").val();
		keyword = $("#keyword").val();
		/*判断时间值  start*/
		if(startTime!=''||keyword != ''){
			showAllNotices();
		}
		else if(startTime==''&&endTime!=''){
			alert("请输入开始日期");
			return ;
		}
		else if(endTime==''&&startTime!=''){
			var myDate = new Date();
			var makedate;
			var getmouth = parseInt(myDate.getMonth())+1;
			makedate = myDate.getFullYear()+"-"+getmouth+"-"+myDate.getDate();
			$("#endTime").val(makedate);
			endTime = makedate;
			showAllNotices();
		}
		else if(startTime!=''&&startTime>=endTime){
			alert("请输入正确时间值");
			return ;
		}
		else if(startTime==''&&endTime==''&&keyword==''){
			showAllNotices();
		}
		/*判断时间值   end*/
		/*if(startTime == '')
			startTime = null;
		if(endTime == '')
			endTime = null;*/
		if(keyword == '')
			keyword == null;
		
	});
}

//加载指定ID的试题信息
function showAllNotices() {
	viewaction = '../../handler/noticeAnnouncement/viewNoticeAnnouncementList.do';
	params = {
			"startTime": startTime,
			"endTime" : endTime,
			"keyWord" : keyword,
			"pageArray" : new Array(),
			"recordPerPage" : 10
	};
	// 获取页面查询参数
	getParams();
	// 绑定分页的一些操作响应
	initialBind();
	initSearch();
}

/**
 * 删除功能展示之前，提供数据的函数
 */
function delNotice(){
	$("#delButton").click(function(){
		var $delSelected = $("input[type='checkbox']");
		ids = "";
		var count = 0;
		for(var i=0;i<$delSelected.length;i++) {
			if($($delSelected[i]).is(":checked")) {
				ids += ($($delSelected[i]).parents("tr")).attr("value")+",";
				count ++;
			}
		}
		if(count == 0) {
			alert("请至少勾选一项修改!");
			return;
		}
		$("#delLabel").html("确认删除这 <span style='color:red;'>"+ count +"</span> 项?");
		$("#delModal").modal({
			'backdrop':true
		});
	});
	$(".closeButton").click(function(){
		$("#delModal").modal('hide');
	});
}
/**
 * 绑定删除按钮点击事件
 */
function bindDelClick() {
	$("#delNoticButton").click(function(){
		$.post("../../handler/noticeAnnouncement/delNoticeAnnouncement.do",{
			ids:ids
		},function(data){
			if(data.ret) {
				if(data.data.status == 1) {
					alert("删除成功！");
					window.location.reload();
					 cleanFind();
				}
			}
		});
	});
}

/**
 * 修改通知信息
 */
function modNotice(id){
	$("#eidNotice").click(function(){
		var modId = "?modNotice="+id+"&"+urlColHtml();
		window.location.href = 'notice_mod.html'+modId;
	});

}

/**
 * 在通知中心里显示通知具体内容
 * 
 */
function showAnnounce(data,index,edit) {
	var announ =data[index];
	$("#announce-title").html(announ.noanTitle);
	$("#announce-content").html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + announ.noanContent);
	//var time = new Date(announ.noanPubtime);
	//$("#announce-time").html(time.toLocaleString());
	$("#announce-time").html(announ.userName+" 于 "+announ.noanPubtime+" 发布");
	if(edit==1){
		$("#eidNoticecol").css("display","display");
	}else{
		$("#eidNoticecol").css("display","none");
	}
	
}

//加载数据到数据区域
function refreshContent(pageRecords) {
	$.ajaxSettings.async = false;
	$("#mainTable").empty();
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	var paperHtml = "";
	if (pageRecords.data.length == 0) {
		$('#mainTable')
		.append(
		"<tr><td colspan=\"10\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>");
	} else {
		viewAllData = pageRecords.data;
		$.each(pageRecords.data, function(index, item) {
			if(item.noanCreatorid==1){
				paperHtml += "<tr  value='" + item.noanId+"'><td><input type='checkbox' "+ 
				"/></td><td class='tdcenter'>" + startIndex + "</td>" + "<td class='tdcenter' id='" + item.noanId
				+ "'><a class='noticeShow' style='cursor:pointer;' onclick='showAnnounce(viewAllData,"+index+","+item.noanCreatorid+");modNotice("+item.noanId+");' href='#myModal' data-toggle='modal' >" + item.noanTitle + "</a></td>"
				+ "<td class='tdcenter'>"
				+ item.userName + "</td>" + "<td class='tdcenter'>" + item.noanPubtime + "</td></tr>";
				
			}
			else if(item.noanCreatorid==0){
				paperHtml += "<tr  value='" + item.noanId+"'><td>-</td><td class='tdcenter'>" + startIndex + "</td>" + "<td class='tdcenter' id='" + item.noanId
				+ "'><a class='noticeShow' style='cursor:pointer;' onclick='showAnnounce(viewAllData,"+index+","+item.noanCreatorid+");modNotice("+item.noanId+");' href=\"#myModal\" data-toggle=\"modal\" name='"+ item.noanId + "'>" + item.noanTitle + "</a></td>"
				+ "<td class='tdcenter'>"
				+ item.userName + "</td>" + "<td class='tdcenter'>" + item.noanPubtime + "</td></tr>";
			}
			startIndex++;
		});
		$('#mainTable').append(paperHtml);
	}
//	全选事件绑定
	var $checkboxes = $("#mainTable input[type=checkbox]");
	$("#chooseall").click(function(){
		if($(this).html() == '全选') {
			for(var i=0;i<$checkboxes.length;i++) {
				/*$($checkboxes[i]).attr("checked","checked");*/
				$($checkboxes[i]).prop("checked",true);
			}
			$(this).html('反选');
		} else {
			for(var i=0;i<$checkboxes.length;i++) {
				/*$($checkboxes[i]).removeAttr("checked");*/
				$($checkboxes[i]).prop("checked",false);
			}
			$(this).html('全选');
		}
	});
	return false;
}
/*//查询
function bindSearch() {
	$('#search').click(function() {
		getParams();
		initSearch();
		return false;
	});
}*/