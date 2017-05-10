var viewaction = ''; // 分页请求的action
var params; // 请求的参数
var veseId = new Array();
var colIds = "";
var secondCol = 0;
/**
 * @author htx
 */
$(document).ready(function() {
	ShowColumn(); // 添加左侧菜单显示
	colIds = urlColHtml();// 栏目ids
	paginationPage(); // 添加分页
	applyTableData(); // 请求表格数据
	bindOptClick(); // 绑定增删改按钮点击事件
	delPopDivClick(); // 删除弹出层按钮事件
	serverTest();//视频测试
});
/**
 * 绑定添加按钮点击事件
 */
function bindOptClick() {
	$("#delButton").click(
			function() {
				var count = 0;
				$("input[name='chooseRecourd']").each(function() {
					var $current = $(this);
					if ($current.is(":checked")) {
						veseId.push($current.val());
						count++;
					}
				});
				if (count == 0) {
					infoNotice("error", "请至少勾选一项修改!", "", $('#data-grid'));
					return;
				}
				$("#delLabel").html(
						"确认删除这 <span style='color:red;'>" + count
						+ "</span> 项?");
				$("#delModal").modal({
					'backdrop' : true
				});
			});

	$("#testServer").click(function(){
		serverTest();
	});
}

/**
 * 删除按钮点击事件
 */
function delPopDivClick() {
	$("#delResouceButton").click(function() {
		var url = "../../handler/videoServer/deleteServer";
		if (veseId != null) {
			$.post(url, {
				veseId : veseId.toString()
			}, function(data) {
				if (data.ret) {
					if (data.data.status == 1) {
						infoNotice("error", "删除成功！!", "", $('#data-grid'));
						initSearch();
					}
				}
			}, "json");
			initSearch();
			$("#delModal").modal('hide');
		}
	});
}
/**
 * 查询table里数据，填充表格
 */
function applyTableData() {
	secondCol = getRequest("secondCol");
	viewaction = "../../handler/videoServer/selectAllServer";
	params = {
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
		} else {
			// 删除cookie
			document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		}
	}
	initialBind(); // 绑定分页的一些操作响应
	initSearch();
}

/**
 * 实际表格数据处理方法
 * 
 * @param data
 *            所有表格数据
 */
function refreshContent(data) {
	var dataList = data.data;
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	if (dataList != null) {
		var HTML = "";
		if (dataList.length == 0)
			HTML += "<tr><td colspan='8' style='text-align:center;color:green;'>暂无</td></tr>";
		for ( var i = 0; i < dataList.length; i++) {
			var type = "";
			if (dataList[i].veseType == 1)
				type = "主服务器";
			else if (dataList[i].veseType == 2)
				type = "从服务器";
			var state = "";
			if (dataList[i].veseState == 0)
				state = "正常";
			else if (dataList[i].veseState == 1)
				state = "故障";
			var enable = "";
			if (dataList[i].veseEnable == 1)
				enable = "是";
			else if (dataList[i].veseEnable == 0)
				enable = "否";
			var IpContentHtml = "<p>服务器IP：" + dataList[i].veseInnerIp
			+ "</p><p>服务器IP子网掩码：" + dataList[i].veseInnerMask
			+ "</p><p>客户端可访问IP：" + dataList[i].veseOuterIp
			+ "</p><p>客户端IP子网掩码：" + dataList[i].veseOuterMask + "</p>";
			HTML += "<tr value='"
				+ dataList[i].veseId
				+ "'>"
				+ "<td><input class='dataCheckbox' type='checkbox' name='chooseRecourd' value='"
				+ dataList[i].veseId
				+ "'/></td><td>"
				+ startIndex
				+ "</td>"
				+ "<td><a href='sysman_Server_mod.html?veseId="
				+ dataList[i].veseId
				+ "&"
				+ colIds
				+ "' id='"
				+ dataList[i].veseId
				+ "' rel='popover' data-original-title='<strong>视频服务器IP信息</strong>' data-content='"
				+ IpContentHtml + "' >" + dataList[i].veseName
				+ "</a></td><td>" + dataList[i].veseDepart + "</td><td>"
				+ type + "</td><td>" + state + "</td><td>" + enable
				+ "</td><td>" + dataList[i].veseLoginName + "/"
				+ dataList[i].vesePassword + "</td><td>"
				+ dataList[i].veseRemark + "</td></tr>";
			startIndex++;
		}
		$("#mainTable").html(HTML);
		// popover工具
		$("[rel=popover]").popover({
			placement : 'top',
			trigger : 'hover',
			html : 'true', // needed to show html of course
		});
		bindSelectAll(); // 绑定全选事件
	}
}
/**
 * 绑定表单全选按钮事件
 */
function bindSelectAll() {
	$("#chooseall").click(function() {
		if (!isSelectAll())
			for ( var i = 0; i < $boxes.length; i++) {
				$($boxes[i]).prop("checked", true);
				$(this).html("反选");
			}
		else {
			for ( var i = 0; i < $boxes.length; i++)
				$($boxes[i]).prop("checked",false);
			$(this).html("全选");
		}
	});
	var $boxes = $(".dataCheckbox");
	for ( var i = 0; i < $boxes.length; i++) {
		$($boxes[i]).click(function() {
			if (isSelectAll())
				$("#chooseall").html("反选");
			else
				$("#chooseall").html("全选");
		});
	}
}

function isSelectAll() {
	var $boxes = $(".dataCheckbox");
	for ( var i = 0; i < $boxes.length; i++)
		if (!$($boxes[i]).is(":checked"))
			return false;
	return true;
}

/**
 * 测试服务器显示
 */
function serverTest(){
	var locations = new Array();
	var Html="";
	$.post("../../handler/resource/viewVideoAddressForAdjustment.do",{
		},function(mydata){
		locations = mydata.data.servers;
		var nowIP =  mydata.data.localIp;
		$("#nowIP").html(nowIP);
		for(var i=0;i<locations.length;i++){
			Html += "<div class='control-group'><div class='controls' id='serverID"+i+"'>"+locations[i]+"</div></div>";	
		}
		$("#allIP").html(Html);
	},"json");
}

