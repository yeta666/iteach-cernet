/**
 * @author 庞俊涛
 * @version 系统管理-日志浏览-主函数
 */
var viewaction = '';// 分页请求的action
var params;// 请求的参数
var recordPerPage = 20;
$(document).ready(function() {
	// back To Top
	backToTop();
	//列表显示
	ShowColumn();
	$('#sysman').attr("class", "active");
	// 新的时间插件显示示例
	timepicker("startTime");
	timepicker("endTime");
	// 加载分页 
	if (userId <= 0) {
		$('#pagination').hide();
	} else {
		paginationPage();
	}
	initialBind(); //绑定分页
	secondCol = getRequest("secondCol");
	viewaction = "../../handler/log/queryLogs";
	var currentTime = (new Date()).format("yyyy-MM-dd hh:mm:ss");
	params = {
			"searchWord":'',
			"startTime":'',
			"endTime":currentTime,
			"pageArray":new Array(),
			"recordPerPage":10,
			"currentPage" : 1,
			"secondCol" : secondCol
		};
	// /判断cookie中是否有查询参数
	if ($.cookie("Search") != null && $.cookie("Search") != "") {
		var SearchCookie = $.cookie("Search");
		var SearchPar = JSON.parse(SearchCookie);
		if (SearchPar.secondCol == secondCol) {
			params = SearchPar;
			$('#queryusername').val(params.searchWord);
			$('#startTime').val(params.startTime);
			$('#endTime').val(params.endTime);
		} else {
			// 删除cookie
			document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		}
	}
	initSearch();
	//搜索
	$("#search").click(function() {
		var userName = $("#queryusername").val();
		params.endTime = $("#endTime").val();
		params.startTime = $("#startTime").val();
		params.searchWord = userName;
		document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		var cookieString = "Search=";
		cookieString = cookieString + JSON.stringify(params);
		document.cookie = cookieString;
		initSearch();
	});
	//deleteLogs
	$("#deleteLogs").click(function() {
		var userName = $("#queryusername").val();
		params.endTime = $("#endTime").val();
		params.startTime = $("#startTime").val();
		params.searchWord = userName;
		if(userName==""&& $("#endTime").val()==""&&$("#startTime").val()==""){
			if(!confirm("警告！你什么条件都没有选择，这将清空数据库所有的日志！你确实要删除所有日志嘛")){
				return;
			}
		}
		var r = confirm("亲:你确实要删除这些条件 下的日志么？");
		if(r){
			$.post("../../handler/log/deleteLogs",params,function(data){
				if(data.errcode==1){
					alert(data.errmsg);
				}else{
					alert("删除成功！");
					window.location.reload(); 
				}
			});
		}else{
			return;
		}
	});
});

//加载数据到数据区域
function refreshContent(pageRecords) {
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	var tableContent = "";
	if (pageRecords.data.length == 0) {
		tableContent = "<tr><td colspan=\"7\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>";
	} else {
		departs = pageRecords.data;
		var i = 0;
		$.each(pageRecords.data, function(itemIndex, item) {
			//预处理
			if (item.depaAbbreviation == undefined || item.depaAbbreviation == null) {
				item.depaAbbreviation = "";
			}
			if (item.depaCode == undefined || item.depaCode == null) {
				item.depaCode = "";
			}
			i++;
			var time = item.logTime;
			var currentTime = getLocalTime(time / 1000);
			tableContent += "<tr><td class=‘tdcenter’>"+((currentPage-1)*(recordPerPage-10)+i)+"</td><td class=‘tdcenter’>" + item.userName + "</td><td class=‘tdcenter’>"
					+ item.logOpt + "</td>" + "<td class=‘tdcenter’>" + currentTime + "</td><td class=‘tdcenter’>" + item.logIp + "</td></tr>";
			startIndex++;
		});
	}
	$('#mainTable').empty().append(tableContent);
	return false;
}

function exportLog() {
	var maxCount = $("#maxLogCount").val();
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	var userName = encodeURIComponent(encodeURIComponent($("#queryusername").val()));
	if (startTime != null && endTime != null) {
		var s = new Date(startTime.replace(/\-/g, "\/"));
		var e = new Date(endTime.replace(/\-/g, "\/"));
		var t = new Date();
		if (s >= e) {
			alert("亲，你选择的开始时间比结束时间大了！请输入不同的时间区间！");
			return;
		}
	}

	var url = "../../handler/log/exportLog?searchType=1" + "&searchWord=" + userName + "&startTime=" + startTime
			+ "&endTime=" + endTime + "&maxCount=" + maxCount;
	window.location.href = url;
}
/**
 * 时间对象的格式化;
 */
Date.prototype.format = function(format) {
	/*
	 * eg:format="YYYY-MM-dd hh:mm:ss";
	 */
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	}

	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	}

	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
}
/**
 * 将时间戳转换为时间格式
 */
function getLocalTime(nS) {
	return new Date(parseInt(nS) * 1000).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
}