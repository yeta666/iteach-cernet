var startTime = '';		// 查询的开始时间
var endTime = '';		// 查询的结束时间
var viewaction = '';	// 分页请求的action
var params;		// 请求的参数 
var secondCol = 0;
$(document).ready(function() {
	ShowColumn();		//	添加左侧菜单显示
	$('#resources').attr("class", "active");	//	添加菜单选中样式
	// back To Top
	backToTop();
	paginationPage();	//	添加分页
	bindTimePicker();	//	绑定时间选择插件
	applyCourseSelectData();	//	请求所有课程列表
	applyTableData();	//	请求表格数据
	bindSearchClick();	//	搜索按钮点击事件
	bindOptClick();		//绑定增删改按钮点击事件
	addPopDivClick();	//	添加弹出层按钮事件
	modPopDivClick();	//	修改弹出层按钮事件
	delPopDivClick();	//	删除弹出层按钮事件
});

var modResoId = null;
var ids = null;
var courId = null;
var startTime = null;
var endTime = null;
var keyWord = null;

/**
 * 绑定添加按钮点击事件
 */
function bindOptClick() {
	$("#addButton").click(function(){
		cleanDiv();
		$("#addModal").modal({
			'backdrop':true
		});
	});
	$("#modButton").click(function(){
		var $modSelected = $("input[type=checkbox]");
		var $oneCheckBox = null;
		for(var i=0;i<$modSelected.length;i++) {
			if($($modSelected[i]).is(":checked")) {
				if($oneCheckBox == null) {
					$oneCheckBox = $($modSelected[i]);
				} else {
					alert("请选择一项修改！");
					return;
				}
			}
		}
		if($oneCheckBox == null) {
			alert("请勾选一项修改!");
			return;
		}
		var $selTr = $oneCheckBox.parents("tr");
		$("#resoTitle2").val($selTr.find(".title").html());
		$("#resoLocation2").val($selTr.find(".location").html());
		var cid = $selTr.find(".courName").attr("cid");
		var $opts = $("#resoCourse2 option");
		for(var i=0;i<$opts.length;i++) {
			if($($opts[i]).attr("value") == cid) {
				$($opts[i]).attr("selected","selected");
			}
		}
		$("#resoDescribe2").val($selTr.find(".addtime").attr("value"));
		modResoId = $selTr.attr("value");
		$("#modModal").modal({
			'backdrop':true
		});
	});
	$("#delButton").click(function(){
		var $modSelected = $("input[type=checkbox]");
		ids = "";
		var count = 0;
		for(var i=0;i<$modSelected.length;i++) {
			if($($modSelected[i]).is(":checked")) {
				ids += ($($modSelected[i]).parents("tr")).attr("value")+",";
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
		$("#addModal").modal('hide');
		$("#modModal").modal('hide');
		$("#delModal").modal('hide');
	});
}

/**
 * 清空表格 添加前
 */
function cleanDiv(){
	$("#resoCourse option:selected").attr("-1");
	$("#resoTitle").val("");
	$("#resoDescribe").val("");
	$("#file").val("");
}
/**
 * 刷新数据时，查询表格数据的
 */
function cleanFind(){
	$("#startTime").val("");
	$("#endTime").val("");
	$("#keyword").val("");
//	$("#quCourse option:selected").val("-1");
}


/**
 * 弹出层修改提交按钮事件
 */
function modPopDivClick() {
	$("#modResouceButton").click(function(){
		var upload=$("#file1").val();
		if(upload==""||upload==null){
			var resoType = $("#resType2 option:selected").val();
			var resoCourId = $("#resoCourse2 option:selected").attr("value");
			if(resoCourId==-1) {
				alert("所属课程不为空！");
				return false;
			}
			var resoTitle =$("#resoTitle2").val();
			if(resoTitle=="") {
				alert("文档名称不为空！");
				return false;
			}
			var resoDescribe = $("#resoDescribe2").val();
			if(resoType != -1 && resoCourId != -1 && resoTitle != "") {
				//modResourceByAdminWithoutFile.do
				$.post("../../handler/resource/modResourceByAdminWithoutFile.do",
						{
					resoId:modResoId,
					resoCourId:resoCourId,
					resoType:2,
					resoTitle:resoTitle,
					resoDescribe:resoDescribe
						},function(data){
							if(data.ret) {
								if(data.data.status == 1){
									alert("更改成功！");
									//$('#addReModal').modal('hide');
									window.location.reload();
									cleanFind();
								}else if(data.data.status == 0){
									alert("更改失败！");
									cleanFind();
								}
							}
						},"json");
			}
		}
		else{
			var resoType = $("#resType2 option:selected").val();
			var atArr = $("#file1").val().split(".");
			if(atArr[atArr.length-1]!="doc"||atArr[atArr.length-1]!="txt"){
				alert("文档格式不正确！");
				return false;
			}
			var resoCourId = $("#resoCourse2 option:selected").attr("value");
			if(resoCourId==-1) {
				alert("所属课程不为空！");
				return false;
			}
			var atArr = $("#file1").val().split(".");
			if(atArr[atArr.length-1]!="doc"||atArr[atArr.length-1]!="txt"){
				alert("文档格式不正确！");
				return false;
			}
			var resoTitle =$("#resoTitle2").val();
			if(resoTitle=="") {
				alert("文档名称不为空！");
				return false;
			}
			var resoDescribe = $("#resoDescribe2").val();
			if(resoType != -1 && resoCourId != -1 && resoTitle != "") {
				loading();//动态加载小图标
				$.ajaxFileUpload({
					url :'../../handler/resource/modResourceByAdmin.do',
					secureuri :false,
					fileElementId :'file1',
					dataType : 'json',
					data:{
						resoId:modResoId,
						resoCourId:resoCourId,
						resoType:2,
						resoTitle:resoTitle,
						resoDescribe:resoDescribe
					},
					success : function (data, status){
						if (data.ret) {
							if(data.data.status == 1){
								alert("更改成功！");
								//$('#addReModal').modal('hide');
								window.location.reload();
								cleanFind();
							}else if(data.data.status == 0){
								alert("更改失败！请检查文件格式是否为doc");
								cleanFind();
							}
						}
					},
					error: function (data, status, e){
						alert(e);
					}
				});
			}
		}
	});
}

/**
 * 删除按钮点击事件
 */
function delPopDivClick() {
	$("#delResouceButton").click(function(){
		if(ids != null) {
			$.post("../../handler/resource/delResourceByAdmin.do",{
				ids:ids
			},function(data){
				if(data.ret) {
					if(data.data.status == 1) {
						alert("删除成功！");
						window.location.reload();
						cleanFind();
					}
				}
			},"json");
		}
	});
}


/**
 * 弹出层添加提交按钮事件
 */
function addPopDivClick() {
	$("#addResouceButton").click(function(){
		var upload=$("#file").val();
		if(upload==null||upload==""){
			alert("请先点击“浏览”按钮选择要上传的附件！");
			return false;
		}
		var atArr = $("#file").val().split(".");
		if(atArr.length<2||cont.toUpperCase().match(atArr[atArr.length-1].toUpperCase())==null){
			alert("文档格式不正确！");
			return false;
		}
		var resoType = $("#resType option:selected").val();
		var resoCourId = $("#resoCourse option:selected").attr("value");
		if(resoCourId==-1) {
			alert("所属课程不为空！");
			return false;
		}
		var resoTitle =$("#resoTitle").val();
		if(resoTitle=="") {
			alert("文档名称不为空！");
			return false;
		}
		var resoDescribe = $("#resoDescribe").val();
		if(resoType != -1 && resoCourId != -1 && resoTitle != "") {
			loading();//动态加载小图标
			$.ajaxFileUpload({
				url :'../../handler/resource/addResourceByAdmin.do',
				secureuri :false,
				fileElementId :'file',
				dataType : 'json',
				data:{
					resoCourId:resoCourId,
					resoType:2,
					resoTitle:resoTitle,
					resoDescribe:resoDescribe
				},
				success : function (data, status){
					if (data.ret) {
						if(data.data.status == 1){
							alert("上传成功！");
							$('#addReModal').modal('hide');
							window.location.reload();
							cleanFind();
						}
					}
				},
				error: function (data, status, e){
					alert(e);
				}
			});
		}
	});
}

/**
 * 绑定时间选择插件并并定点击查询事件
 */
function bindTimePicker() {
	timepicker('startTime');
	timepicker('endTime');
}

/**
 * 绑定搜索按钮点击事件
 */
function bindSearchClick() {
	$("#search").click(function(){	
		courId=$("#quCourse").val();
		startTime = $("#startTime").val();
		endTime = $("#endTime").val();
		keyWord = $("#keyword").val();
		/*判断时间值  start*/
		if(startTime==''&&endTime!=''){
			alert("请输入开始日期");
			return ;
		}
		if(endTime==''&&startTime!=''){
			var myDate = new Date();
			var makedate;
			var getmouth = parseInt(myDate.getMonth())+1;
			makedate = myDate.getFullYear()+"-"+getmouth+"-"+myDate.getDate();
			$("#endTime").val(makedate);
			endTime = makedate;
		}
		if(startTime!=''&&startTime>=endTime){
			alert("请输入正确时间值");
			return ;
		}
		params.courId=courId;
		params.startTime=startTime;
		params.endTime=endTime;
		params.keyWord=keyWord;
		params.currentPage=1;
		document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		var cookieString = "Search=";
		cookieString = cookieString + JSON.stringify(params);
		document.cookie = cookieString;
		
		initSearch();
	});
}

/**
 * 通过课程筛选查询
 * @param select框DOM对象
 */
function searchByCourse(dom) {
	$("#search").trigger("click");
}

/**
 * 查询课程下拉列表中的数据
 */
function applyCourseSelectData() {
	$.getJSON("../../handler/course/viewAllCourseNameList.do",{},function(data){
		if(data.ret) {
			var courseList = data.data.viewAllCourse;
			if(courseList != null) {
				var HTML = "<option value='-1'>=选择课程=</option>";
				for(var i=0;i<courseList.length;i++) {
					HTML += "<option value='" + courseList[i].courId + 
					"'>" + courseList[i].courName+"</option>";
				}
				$("#resoCourse").html(HTML);
				$("#quCourse").html(HTML);
				$("#resoCourse2").html(HTML);
			}
		}
	});
}
function download(obj){
	window.open("../../handler/resource/downloadDocumentResource.do?data=asdfjvkxfsf&password=wjlxulax23&rtype=2&rescis=2&rid="+$(obj).attr('value'));
}
/**
 * 查询table里数据，填充表格
 */
function applyTableData() {
	secondCol = getRequest("secondCol");
	viewaction = "../../handler/resource/viewResourceListForAdmin.do";
	params = {
			"pageArray":new Array(),
			"recordPerPage":10,
			"courId":-1,
			"resoType":2,
			"keyWord":'',
			"startTime":'',
			"endTime":'',
			"currentPage" : 1,
			"secondCol" : secondCol
	};
	if ($.cookie("Search") != null && $.cookie("Search") != "") {
		var SearchCookie = $.cookie("Search");
		var SearchPar = JSON.parse(SearchCookie);
		if (SearchPar.secondCol == secondCol) {
			params = SearchPar;
			$('#quCourse').val(params.courId);
			$('#keyword').val(params.keyWord);
			$('#startTime').val(params.startTime);
			$('#endTime').val(params.endTime);
		} else {
			// 删除cookie
			document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		}
	}	
	initialBind();		// 绑定分页的一些操作响应
	initSearch(); 
}

/**
 * 实际表格数据处理方法
 * @param data 所有表格数据
 */
function refreshContent(data) {
	var dataList = data.data;
	if(dataList != null) {
		var HTML = "";
		if(dataList.length == 0)
			HTML += "<tr><td colspan='8' style='text-align:center;color:green;'>暂无</td></tr>";
		for(var i=0;i<dataList.length;i++) {
			if(dataList[i].courName==""||dataList[i].courName==null||dataList[i].courName=="undefined"){
				dataList[i].courName = "暂无关联课程";
			}
			HTML += "<tr value='" + dataList[i].resoId + "' class='dataTr'  data-toggle='tooltip' title='描述:   " +
			(dataList[i].resoDescribe == null?
					'无':dataList[i].resoDescribe)+"'>" +
					"<td><input class='dataCheckbox' type='checkbox'/></td>" +
					"<td>" + ((currentPage-1)*recordPerPage+i+1) + "</td>" +
					"<td class='title' style='cursor:pointer;'><a class='modDoc'  name='"+ dataList[i].resoFilename+"'>" + dataList[i].resoTitle + "</a></td>" +
					"<td class='courName' cid='" + dataList[i].courId + "'>" + dataList[i].courName + "</td>" +
					"<td class='realname'>" + dataList[i].userRealname + "</td>" +
					"<td class='addtime' value='" + (dataList[i].resoDescribe == null?
							'':dataList[i].resoDescribe) + "'>" + dataList[i].resoAddtime + "</td>" +
							"<td onclick='download(this);'  value='"+dataList[i].resoId+"'><i class='fa fa-download'"+"'></i></td>"+
							"</tr>";
		}
		$("#mainTable").html(HTML);
		var $icon = $(".title");
		if($icon != null) {
			for(var i=0;i<$icon.length;i++) {
				$($icon[i]).click(function(){
					var $selTr = $(this).parent("tr");
					$("#resoTitle2").val($selTr.find(".modDoc").html());
					$("#modReoDoc").val($selTr.find(".modDoc").attr('name'));
					var cid = $selTr.find(".courName").attr("cid");
					var $opts = $("#resoCourse2 option");
					for(var i=0;i<$opts.length;i++) {
						if($($opts[i]).attr("value") == cid) {
							$($opts[i]).attr("selected","selected");
						}
					}
					$("#resoDescribe2").val($selTr.find(".addtime").attr("value"));
					modResoId = $selTr.attr("value");
					$("#modModal").modal({
						'backdrop':true
					});
				});
			}
		}

		$('.handshow').mouseover(function(){
			$(this).css("cursor","pointer");
		}).mouseout(function(){
			$(this).css("cursor","default");
		});
		/*$(".dataTr").mouseover(function(){
			$(this).css("cursor","pointer");
		}).mouseout(function(){
			$(this).css("cursor","default");
		});*/
		bindSelectAll();	//	绑定全选事件
		//cleanFind();
	}
}

/**
 * 绑定表单全选按钮事件
 */
function bindSelectAll() {
	$("#chooseall").click(function(){
		if(!isSelectAll())
			for(var i=0;i<$boxes.length;i++) {
				/*$($boxes[i]).attr("checked","checked");*/
				$($boxes[i]).prop("checked",true);
				$(this).html("反选");
			} else {
				for(var i=0;i<$boxes.length;i++)
					/*$($boxes[i]).removeAttr("checked");*/
					$($boxes[i]).prop("checked",false);
				$(this).html("全选");
			}
	});
	var $boxes = $(".dataCheckbox");
	for(var i=0;i<$boxes.length;i++) {
		$($boxes[i]).click(function(){
			if(isSelectAll())
				$("#chooseall").html("反选");
			else
				$("#chooseall").html("全选");
		});
	}
}

function isSelectAll() {
	var $boxes = $(".dataCheckbox");
	for(var i=0;i<$boxes.length;i++)
		if(!$($boxes[i]).is(":checked"))
			return false;
	return true;
}

