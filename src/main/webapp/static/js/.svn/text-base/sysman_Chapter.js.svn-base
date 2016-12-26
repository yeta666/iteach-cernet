/**
 * @author EasonLian
 */

var courId = null;
var chapId = null;
var chapName = null;
var ids = null;

//分页相关参数
var viewaction = '';	// 分页请求的action
var params ;		// 请求的参数 
/*var cookieString = "Search=";*/
var secondCol = 0;
/**confirmAddOrModButton
 * Main 方法
 * 页面加载是执行
 */
$(document).ready(function() {
	paginationPage();		//	添加分页
	backToTop();			//	返回顶部
	ShowColumn();			//	添加左侧菜单显示
	$('#myTutor').attr("class", "active");		//	添加菜单选中样式
	viewAllSysResources();	//	展示全部课程
	applyTableData();		//	查询章节信息
	bindSearchClick();		//	课程点击事件
	bindAddClick();			//	添加确认按钮点击事件
	bindDelClick();			//	删除确认按钮点击事件
	bindReturnTableClick(); //	添加修改处点击 x 和 返回 返回到表格数据
	bindAddOrModPageButtonClick();		//	绑定资源模糊查询按钮点击事件
	$("#addButton").click(function(){
		showAddModPart(1,null);
	});
});

var defaultResoArray = new Array();

/**
 * 绑定添加修改页面的按钮事件
 */
function bindAddOrModPageButtonClick() {
	$("#chosenType1").change(function(){
		var resoType = $("#chosenType1 option:selected").attr("value");
		if(resoType != 0)
			resoParamType = resoType;
		else
			resoParamType = null;
		resoParamTitle = null;
		searchChapterResource();
	});
	$("#addResoButton").click(function(){
		var $rightSelected = $("#freeResoSelect option:selected");
		for(var i=0;i<$rightSelected.length;i++) {
			$("#chosenResoSelect").append($($rightSelected[i]).clone());
			$($rightSelected[i]).remove();
		}
	});
	$("#delResoButton").click(function(){
		var $leftSelected = $("#chosenResoSelect option:selected");
		for(var i=0;i<$leftSelected.length;i++) {
			$("#freeResoSelect").append($($leftSelected[i]).clone());
			$($leftSelected[i]).remove();
		}
	});
}

/**
 * 关闭添加修改页面，并将参数重置
 */
function closeAddOrModPage() {
	$("#addModPartDiv").css("display","none");
	$("#tabContent1").css("display","block");
	resoParamTitle = null;
	resoParamType = null;
	resoChapId = null;
}

/**
 * 添加修改处点击 x 和 返回 返回到表格数据
 */
function bindReturnTableClick() {
	$("#returnTableButton").click(function(){		//	返回表格页面
		closeAddOrModPage();
	});
	$("#confirmAddOrModButton").click(function(){	//	添加、修改按钮提交
		//	获取添加的参数
		var chapOridinal = $("#chapOrdinal").val();
		var chapCourId = $("#course option:selected").attr("value");
		var chapName = $("#chapName").val();
		var chapDescribe = $("#chapDescribe").val();
		var isNum = /^[0-9]+$/;
		if(chapOridinal.match(isNum) == null) {
			alert('章节序号不正确！请输入数字：');
			return;
		}
		if(chapCourId == 0) {
			alert('数据不完整！请选择 \'课程\'');
			return;
		}
		if(chapName == '') {
			alert('数据不完整！请填写 \'章节名\'');
			return;
		}
		//	判断那些resoId是需要添加、那些需要删除
		var $chosenReso = $("#chosenResoSelect option");
		var addResoIds = "";		//	待添加的资源id
		var delResoIds = "";		//	待删除的资源id
		var stillExistResoId = new Array();
		//	遍历所有的默认含有的资源id
		for(var i=0;i<$chosenReso.length;i++) {
			var flag = false;
			var eachResoId = $($chosenReso[i]).attr('value');
			for(var j=0;j<defaultResoArray.length;j++) {
				if(defaultResoArray[j] == eachResoId)
					flag = true;
			}
			if(!flag)
				addResoIds += eachResoId + ",";
			else
				stillExistResoId.push(eachResoId);
		}
		for(var i=0;i<defaultResoArray.length;i++) {
			var flag = true;
			for(var j=0;j<stillExistResoId.length;j++) {
				if(defaultResoArray[i] == stillExistResoId[j])
					flag = false;
			}
			if(flag)
				delResoIds += defaultResoArray[i] + ",";
		}
		//	设置请求参数
		var addOrModChapParams = {
				chapOrdinal:chapOridinal,
				chapCourId:chapCourId,
				chapName:chapName,
				chapDescribe:chapDescribe,
				addResoIds:addResoIds,
				delResoIds:delResoIds
		};
		if(resoChapId != null)
			addOrModChapParams.chapId = resoChapId;
		$.post("../../handler/chapter/addOrModChapterWithResource.do",addOrModChapParams,function(data){
			if(data.ret) {
				if(data.data.status == 1) {
					alert("操作成功！");
					window.location.reload();
				}
			}
		},'json');
		/*alert("chapOridinal:" + chapOridinal +
	"   chapCourId:" + chapCourId +
	"   chapName:" + chapName +
	"   chapDescribe:" + chapDescribe +
	"   addResoIds:" + addResoIds +
	"   delResoIds:" + delResoIds + "   chapId:" + resoChapId);*/
	});
	$("#returnTableX").click(function(){			//	返回表格页面
		closeAddOrModPage();
	});
	$("#delButton").click(function() {				//	删除按钮点击事件
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
		$("#delModal2").modal({
			'backdrop':true
		});
	});
}

/**
 * 绑定删除按钮点击事件
 */
function bindDelClick() {
	$("#delChapterButton1").click(function(){
		$.post("../../handler/chapter/delChapterByAdmin.do",{
			ids:ids
		},function(data){
			if(data.ret) {
				if(data.data.status == 1) {
					alert("删除成功！");
					window.location.reload();
				}
			}
		});
	});
}

/**
 * 绑定添加提交按钮点击事件
 */
function bindAddClick() {
	$("#addsewo").click(function(){
		var addParam = {
				chapOrdinal : $("#addOridinal").val(),
				chapName : $("#addName").val(),
				courId : $("#addCourse option:selected").attr("value"),
				chapDescribe : $("#addDescribe").val()
		};
		$.post("../../handler/chapter/addChapterByAdmin.do",addParam,function(data){
			if(data.ret) {
				if(data.data.status == 1) {
					alert("添加成功!");
					window.location.reload();
				}
			}
		},"json");
	});
}

/**
 * 模糊查询按钮事件
 */
function bindSearchClick() {
	$("#courseSel").click(function(){
		document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		var cookieString = "Search=";
		var cid = $("#Course option:selected").attr("value");
		var cname = $("#searchChapName").val();
		chapName = (cname == '') ? null : cname;
		courId = (cid == -1) ? null : cid;
		applyTableData();
		cookieString = cookieString + JSON.stringify(params);
		document.cookie = cookieString;
	});
}

/**
 * 请求数据创建表格
 */ 
function applyTableData() {
	secondCol = getRequest("secondCol");
	viewaction = "../../handler/chapter/viewAllChapterList.do?data=8asjlkfj281lavcl";
	params = {
			"pageArray":new Array(),
			"recordPerPage":10,
			"currentPage" : 1,
			"secondCol" : secondCol
	};
	if(courId != null)
		params.courId = courId;
	if(chapId != null)
		params.chapId = chapId;
	if(chapName != null) 
		params.chapName = chapName;
	if ($.cookie("Search") != null && $.cookie("Search") != "") {
		var SearchCookie = $.cookie("Search");
		var SearchPar = JSON.parse(SearchCookie);
		if (SearchPar.secondCol == secondCol) {
			params = SearchPar;
			$('#Course').val(params.courId);
			$('#searchChapName').val(params.chapName);
		} else {
			// 删除cookie
			document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		}
	}
	initialBind();		// 绑定分页的一些操作响应
	initSearch(); 
}

/**
 * 创建表格
 * @param data 表格数据
 */
function refreshContent(data) {
	var dataList = data.data;
	var HTML = "";
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	if(dataList.length == 0)
		HTML += "<tr><td colspan='7' style='text-align:center;color:green;'>暂无</td><tr>";
	for(var i=0;i<dataList.length;i++) {
		HTML += "<tr value='" + dataList[i].chapId + "' value2='" + dataList[i].courId + "'>"
		+ "<td><input type='checkbox'/></td><td>"+startIndex+"</td>"
		+ "<td value='"+dataList[i].chapOrdinal+"'>"+"第" + dataList[i].chapOrdinal + "章"+"</td>"
		+ "<td onclick='modTdClick(this);' class='chapNameTd' style='cursor:pointer' title=\""+dataList[i].chapName+"\"><a>" + titleFormat(dataList[i].chapName,10) + "</a></td>"
		+ "<td>" + dataList[i].courName + "</td>"
		+ "<td title=\""+dataList[i].chapDescribe+"\">" +titleFormat( dataList[i].chapDescribe,10) + "</td>"
		+ "<td>" +"<select class='chapterResSet form-control'><option value='-1'>=章节资源=</option>";
		var chapterRes = dataList[i].resources;
		if(chapterRes != null) {
			for(var j=0;j<chapterRes.length;j++){
				var rType = "资源";
				switch(chapterRes[j].resoType) {
				case 1 : rType = '链接'; break;
				case 2 : rType = '文档'; break;
				case 3 : rType = '视频'; break;
				default :
					break;
				}
				HTML += "<option value="+chapterRes[j].resoId+">"
				+ rType + "-"
				+ chapterRes[j].resoTitle+"</option>";
			}
		}
		HTML += "</select>"+"</td>"+"</tr>";
		startIndex++;
	}
	$("#mytbody1").html(HTML);
	//	全选事件绑定
	var $checkboxes = $("#mainTable input[type=checkbox]");
	for(var i=0;i<$checkboxes.length;i++) {
		$($checkboxes[i]).click(function(){
			if(isAllChecked()) {
				$("#chooseall").html("反选");
			} else {
				$("#chooseall").html("全选");
			}
		});
	}
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
}

/**
 * 判断checkbox是否全选
 * @return boolean 是否全选
 */
function isAllChecked() {
	var flag = true;
	var $checkboxes = $("#mainTable input[type=checkbox]");
	for(var i=0;i<$checkboxes.length;i++) {
		if(!$($checkboxes[i]).is(":checked")) { 
			flag = false;
			break;
		}
	}
	return flag;
}

/**
 * 查询所有课程列表
 */
function viewAllSysResources(){
	$.getJSON("../../handler/course/viewAllCourseNameList.do",{
	},function(data){
		var HTML = "";
		var course = data.data.viewAllCourse;
		for(var i=0;i<course.length;i++)
			HTML +=	"<option value="+course[i].courId+">"+course[i].courName+"</option>";
		//	查询所有课程，填写到模糊查询下拉列表中
		$('.resCourse').html($('.resCourse').html() + HTML);
		//	查询所有课程，填写到添加修改下拉列表中
		$("#course").html($("#course").attr('defaultname') + HTML);
	});
}

/**
 * 修改点击事件事件
 */
function modTdClick(dom) {
	var $myTr = $(dom).parents('tr');
	var $myTdArray = $myTr.find('td');
	$("#chapOrdinal").val($($myTdArray[2]).attr('value'));
	$("#chapName").val($($myTdArray[3]).find('a').html());
	$("#chapDescribe").val($($myTdArray[4]).html());
	$("#course option[value='" + $myTr.attr('value2') + "']").attr("selected","selected");
	showAddModPart(2,$myTr.attr('value'),$myTr.attr('value2'));
}

var resoParamTitle = null;
var resoParamType = null;
var resoChapId = null;
var rseoCourId = null;

/**
 * 添加、修改点击事件
 */
function showAddModPart(isAddOrMod,chapId1,courId1) {
	$("#tabContent1").css("display","none");
	$("#addModPartDiv").css("display","block");
	//	清除添加 修改框中的值
	if(isAddOrMod == 1) {
		$("#chapOrdinal").val('');
		$("#course option[value='0']").attr("selected","selected");
		$("#chapName").val('');
		$("#chapDescribe").val('');
		$("#freeResoSelect").html('');
		$("#chosenResoSelect").html('');
	} else {
		//	查询章节资源信息
		resoChapId = chapId1;
		rseoCourId = courId1;
		searchChapterResource();
	}
	//	跳转到添加和修改页面时能动画移动到添加的最顶端
	$("html:not(:animated),body:not(:animated)").animate({
		scrollTop : 0
	}, 600);
	//	判断是添加还是修改，并修改显示标题
	$("#addModTitle").html((isAddOrMod == 1)?'添加章节':'修改章节');
}

/**
 * 查询章节资源信息
 */
function searchChapterResource() {
	var resoParams = new Object();
	if(resoChapId > 0)
		resoParams.chapId = resoChapId;
	if(resoParamTitle != null)
		resoParams.resoName = resoParamTitle;
	if(resoParamType != null)
		resoParams.resoType = resoParamType;
	if(rseoCourId != null) 
		resoParams.courId = rseoCourId;
	//	发送请求查询资源信息数据
	$.getJSON("../../handler/resource/viewResourceListByChapterPage.do",resoParams,function(data){
		if(data.ret) {
			if(data.data.chosenResoList != null) {
				makeMultiSelect_left(data.data.chosenResoList);
			}
			if(data.data.freeResoList != null) {
				makeMultiSelect_right(data.data.freeResoList);
			}
		}
	});
}

/**
 * 构造多选框 左侧
 */
function makeMultiSelect_left(list) {
	$("#chosenResoSelect").html('');
	defaultResoArray.length = 0;
	var LHTML = "";
	var leftList = list;
	for(var i=0;i<leftList.length;i++) {
		var type1 = null;
		switch(leftList[i].resoType) {
		case 1 : type1 = "链接"; break;
		case 2 : type1 = "文档"; break;
		case 3 : type1 = "视频"; break;
		default : break;
		}
		LHTML += "<option value='" + leftList[i].resoId + "'>" + type1 + " - " + leftList[i].resoTitle + "</option>";
		defaultResoArray.push(leftList[i].resoId);
	}
	$("#chosenResoSelect").html(LHTML);
}

/**
 * 构造多选框 右侧
 */
function makeMultiSelect_right(list) {
	$("#freeResoSelect").html('');
	var RHTML = "";
	var rightList = list;
	for(var i=0;i<rightList.length;i++) {
		var type2 = null;
		switch(rightList[i].resoType) {
		case 1 : type2 = "链接"; break;
		case 2 : type2 = "文档"; break;
		case 3 : type2 = "视频"; break;
		default : break;
		}					
		RHTML += "<option value='" + rightList[i].resoId + "'>" + type2 + " - " + rightList[i].resoTitle + "</option>";
	}
	$("#freeResoSelect").html(RHTML);
}

/**
 * 添加页面选择课程时改变右侧栏目
 */
function changeSelectRigth(dom) {
	rseoCourId = $(dom).find('option:selected').attr('value');
	if(rseoCourId == 0)
		rseoCourId = null;
	searchChapterResource();
}






