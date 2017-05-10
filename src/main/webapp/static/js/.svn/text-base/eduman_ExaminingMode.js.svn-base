var viewaction = '';	// 分页请求的action
var params;		// 请求的参数 
var keyWord=null;
var secondCol = 0;
$("#emkeyword").val("");
/**
 * @author anny 
 * 主函数
 */
$(document).ready(function() {
	// Show Column
	ShowColumn();
	$('#eduman').attr("class", "active");
	paginationPage();	//	添加分页
	applyTableData('');	//	请求表格数据
	bindSearchClick();	//	搜索按钮点击事件
	// back To Top
	backToTop();
	bindOptClick();		//绑定增删改按钮点击事件
	addPopDivClick();	//	添加弹出层按钮事件
	modPopDivClick();
	delPopDivClick();   //	删除弹出层按钮事件

});


/**
 * 绑定添加按钮点击事件
 */
function bindOptClick() {
	$("#addButton").click(function(){
		$("#addModal").modal({
			'backdrop':true
		});
		//重置空
		$("#examModeName").val("");
		$("#examModeScale1").val("");
		$("#examModeScale2").val("");
		$("#examModeScale3").val("");
		$("#examModeScale4").val("");
		$("#examModeScale5").val("");
		//$("#examModeScale6").val("");
		$("#examModeScores1").val("");
		$("#examModeScores2").val("");
		$("#examModeScores3").val("");
		$("#examModeScores4").val("");
		$("#examModeScores5").val("");
		//$("#examModeScores6").val("");
		$("#resoDescribe").val("");

	});
	$("#delButton").click(function(){
		var $delSelected = $("input[type=checkbox]");
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
			return null;
		}
		$("#delLabel").html("确认删除这 <span style='color:red;'>"+ count +"</span> 项?");
		$("#delModal").modal({
			'backdrop':true
		});
	});
	/*$("#closeButton").click(function(){
		$("#addModal").modal('hide');
		$("#modModal").modal('hide');
		$("#delModal").modal('hide');
	});*/
}

/**
 * 修改弹出页面显示
 */
var modExamModeId;
function modClick(){
	var $icon = $(".title");
	if($icon != null) {
		for(var i=0;i<$icon.length;i++) {
			var replace = new RegExp("^[1-9]\d*|0$");
			$($icon[i]).click(function(){
				var $selTr = $(this).parent("tr");
				$("#examModeNameMod").val($selTr.find("#modExamModeName").html());
				$("#examModeScaleMod1").val(($selTr.find("#Pattern1").html()).replace(/[^[0-9]\d*|0$]/g, ""));
				$("#examModeScaleMod2").val(($selTr.find("#Pattern2").html()).replace(/[^[0-9]\d*|0$]/g, ""));
				$("#examModeScaleMod3").val(($selTr.find("#Pattern3").html()).replace(/[^[0-9]\d*|0$]/g, ""));
				$("#examModeScaleMod4").val(($selTr.find("#Pattern4").html()).replace(/[^[0-9]\d*|0$]/g, ""));
				//$("#examModeScaleMod5").val(($selTr.find("#Pattern5").html()).replace(/[^[0-9]\d*|0$]/g, ""));
				//$("#examModeScaleMod6").val(($selTr.find("#Pattern6").html()).replace(/[^[0-9]\d*|0$]/g, ""));
				$("#examModeScoresMod1").val(($selTr.find("#Threhold1").html()).replace(/[^[0-9]\d*|0$]/g, ""));
				$("#examModeScoresMod2").val(($selTr.find("#Threhold2").html()).replace(/[^[0-9]\d*|0$]/g, ""));
				$("#examModeScoresMod3").val(($selTr.find("#Threhold3").html()).replace(/[^[0-9]\d*|0$]/g, ""));
				$("#examModeScoresMod4").val(($selTr.find("#Threhold4").html()).replace(/[^[0-9]\d*|0$]/g, ""));
				//$("#examModeScoresMod5").val(($selTr.find("#Threhold5").html()).replace(/[^[0-9]\d*|0$]/g, ""));
				//$("#examModeScoresMod6").val(($selTr.find("#Threhold6").html()).replace(/[^[0-9]\d*|0$]/g, ""));
				$("#resoDescribeMod").val($selTr.attr("title"));
				modExamModeId = $selTr.attr("value");
				$("#modModal").modal({
					'backdrop':true
				});
			});
		}
	}
}

/**
 * 弹出层添加提交按钮事件
 */
function addPopDivClick() {
	$("#addExamModeButton").click(function(){
		var name = $("#examModeName").val();
		var pattern = getEMSL_add();
		if(pattern==null || pattern==""){
			return;
		}
		var threhold = getEMSO_add();
		if(threhold==null || threhold==""){
			return;
		}
		var describe = $("#resoDescribe").val();
		var addParam = {
				name:name,
				pattern:pattern,
				threhold:threhold,
				describe:describe,
		};
		if(name!=""&&name!=null&&describe!=null&&describe!=""&&pattern!=-1){
			$.post("../../handler/evaluateMethod/addEvaluateMethod.do",
					addParam,function(data){
				if(data.ret) {
					if(data.data.status == 1) {
						alert("添加成功！");
						window.location.reload();
					}
				}
			},"json");
		}else{
			alert("请填写完整");
		}
	});
}

/**
 * 添加传值组合-分值划分
 */
function getEMSL_add(){
	//var re = new RegExp("^[1-9]\d*|0$");
	var re = new RegExp("^\\d+$");
	if(re.test($("#examModeScale1").val())&&re.test($("#examModeScale2").val())&&re.test($("#examModeScale3").val())&&re.test($("#examModeScale4").val())){
		if($("#examModeScale1").val()== '')
			$("#examModeScale1").val(0);
		if($("#examModeScale2").val()== '')
			$("#examModeScale2").val(0);
		if($("#examModeScale3").val()== '')
			$("#examModeScale3").val(0);
		if($("#examModeScale4").val()== '')
			$("#examModeScale4").val(0);
		/*if($("#examModeScale5").val()== '')
			$("#examModeScale5").val(0);*/
		/*if($("#examModeScale6").val()== null)
			$("#examModeScale6").val(0);*/
		var emsltotal = parseInt($("#examModeScale1").val())+parseInt($("#examModeScale2").val())+parseInt($("#examModeScale3").val())+parseInt($("#examModeScale4").val());
		if(emsltotal==100){
			var eMSL = $("#examModeScale1").val()+","+$("#examModeScale2").val()+","+$("#examModeScale3").val()+","+$("#examModeScale4").val()+","+$("#examModeScale5").val();
			return eMSL;
		}
		else if(emsltotal>100){
			alert("分值划分值超出100%，请重新划定");
		}
		else if(emsltotal<100){
			alert("分值划分值不足100%，请重新划定");
		}
	}
	else{
		alert("请输入正确的划分值，要求是正整数！");
	}
}
/**
 *修改传值组合-分值划分
 */
function getEMSL_mod(){
	var re = new RegExp("^\\d+$");
	if(re.test($("#examModeScaleMod1").val())&&re.test($("#examModeScaleMod2").val())&&re.test($("#examModeScaleMod3").val())&&re.test($("#examModeScaleMod4").val())){
		var emsltotal = parseInt($("#examModeScaleMod1").val())+parseInt($("#examModeScaleMod2").val())+parseInt($("#examModeScaleMod3").val())+parseInt($("#examModeScaleMod4").val());
		if(emsltotal==100){
			var eMSL = $("#examModeScaleMod1").val()+","+$("#examModeScaleMod2").val()+","+$("#examModeScaleMod3").val()+","+$("#examModeScaleMod4").val();
			return eMSL;
		}
		else if(emsltotal>100){
			alert("分值划分值超出100%，请重新划定");
		}
		else if(emsltotal<100){
			alert("分值划分值不足100%，请重新划定");
		}
	}
	else{
		alert("请输入正确的划分值，要求为非负整数！");
	}
}
/**
 * 添加传值组合-考核方式阀值
 */
function getEMSO_add(){
	var re = new RegExp("^\\d+$");
	if(re.test($("#examModeScores1").val())&&re.test($("#examModeScores2").val())&&re.test($("#examModeScores3").val())&&re.test($("#examModeScores4").val())){
		var eMSO = $("#examModeScores1").val()+","+$("#examModeScores2").val()+","+$("#examModeScores3").val()+","+$("#examModeScores4").val();
		return eMSO;
	}else{
		alert("请填写正确的非负整数阈值");
	}
}
/**
 * 修改传值组合-考核方式阈值
 */
function getEMSO_mod(){
	var re = new RegExp("^\\d+$");
	if(re.test($("#examModeScoresMod1").val())&&re.test($("#examModeScoresMod2").val())&&re.test($("#examModeScoresMod3").val())&&re.test($("#examModeScoresMod4").val())){
		var eMSO = $("#examModeScoresMod1").val()+","+$("#examModeScoresMod2").val()+","+$("#examModeScoresMod3").val()+","+$("#examModeScoresMod4").val();
		return eMSO;
	}
	else{
		alert("请填写正确的非负整数阈值");
	}
}
/**
 * 弹出层修改提交按钮事件
 */
function modPopDivClick() {
	$("#modExamModeButton").click(function(){
		var name = $("#examModeNameMod").val();
		var pattern = getEMSL_mod();
		if(pattern==null || pattern==""){
			return;
		}
		var threhold = getEMSO_mod();
		if(threhold==null || threhold==""){
			return;
		}
		var describe = $("#resoDescribeMod").val();
		var addParam = {
				id:modExamModeId,
				name:name,
				pattern:pattern,
				threhold:threhold,
				describe:describe,
		};
		if(name!=""&&name!=null&&describe!=null&&describe!=""&&pattern!=-1){
			$.post("../../handler/evaluateMethod/addEvaluateMethod.do",
					addParam,function(data){
				if(data.ret) {
					if(data.data.status == 1) {
						//获取该考核方式的相关课程
						$.post("../../handler/course/viewCoursesByEM",
								{evaMetId:modExamModeId},function(data){
							alert("修改成功！");
							
							if(data.ret){
								if(data.data.courses!=null
										&&data.data.courses!=undefined
										&&data.data.courses.length>0){
									var courNames=data.data.courses[0].courName;
									for(var i=1;i<data.data.courses.length;i++){
										courNames+="，"+data.data.courses[i].courName;
									}
									alert("请尽快到课程管理页面更新相关课程的成绩！具体课程包括："+courNames);
								}
							}else{
								alert(data.data.errmsg);
							}							
							window.location.reload();
						});
					}
				}
			},"json");
		}else{
			alert("请填写完整再提交修改！");
		}
	});
}


/**
 * 删除按钮点击事件
 */
function delPopDivClick() {
	$("#delExamModeButton").click(function(){
		if(ids != null) {
			$.post("../../handler/evaluateMethod/delEvaluateMethod.do",{
				ids:ids
			},function(data){
				if(data.ret) {
					if(data.data.status == 1) {
						alert("删除成功！");
						window.location.reload();
					}else{
						alert("删除失败！很可能存课程正在使用该考核方式！");
						$("#delModal").modal('hide');
					}
				}
			},"json");
		}
	});
}

/**
 * 查询table里数据，填充表格
 */
function applyTableData(name) {
	viewaction = "../../handler/evaluateMethod/viewEvaluateMethodList.do";
	if(keyWord != null) { 
		params = {
				"pageArray":new Array(),
				"recordPerPage":10,
				"name":name,
				"currentPage" : 1,
				"secondCol" : secondCol
		};
	} else {
		params = {
				"pageArray":new Array(),
				"recordPerPage":10,
				"currentPage" : 1,
				"secondCol" : secondCol
		};
	}
	if ($.cookie("Search") != null && $.cookie("Search") != "") {
		var SearchCookie = $.cookie("Search");
		var SearchPar = JSON.parse(SearchCookie);
		if (SearchPar.secondCol == secondCol) {
			params = SearchPar;
			if(params.name!= null){
				$('#emkeyword').val(params.name);
			}
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
var cuo = new Array();
var cu = new Array();
function refreshContent(data) {
	var dataList = data.data;
	if(dataList != null) {
		var HTML = "";
		if(dataList.length == 0)
			HTML += "<tr><td colspan='16' style='text-align:center;color:green;'>暂无数据，请添加</td></tr>";
		for(var i=0;i<dataList.length;i++) {
			//取到划分比例的值
			cuo = dataList[i].evmePattern.split(",");
			cu = dataList[i].evmeThrehold.split(",");
			//alert(cuo);
			var ct = cutTable("Pattern","Threhold",cuo,cu);
			//取到考核方式的值

			//var kh = cutTable1("Threhold");
			HTML += "<tr id='modDescribe' class='resovediotime' value='" + dataList[i].evmeId + "'  data-toggle='tooltip'	title='"+dataList[i].evmeDescribe+"' >" +
			"<td><input class='dataCheckbox' type='checkbox'/></td>" +
			"<td>" +((currentPage-1)*recordPerPage+i+1) + "</td>" +
			"<td class='title' style='cursor:pointer;'><a id='modExamModeName' onclick='modClick();'>" + dataList[i].evmeName + "</a></td>" +
			ct+
			/*"<td id='modDescribe' class='resovediotime'>" +  + "</td>"+*/
			"</tr>";
		}

		$("#mainTable").html(HTML);
		bindSelectAll();	//	绑定全选事件
	}
}


/**
 * 分割表格函数 封装 - 比例部分
 */
function cutTable(sign,sign1,list,list1){
	var ct = "";
	var s = sign;
	var s1= sign1;
	ct += "<td id='"+s+1+"'class='courName getcenter'>" + list[0] + "%</td>";
	ct += "<td id='"+s1+1+"'class='courName getcenter'>" + list1[0] + "次</td>";
	ct += "<td id='"+s+2+"'class='courName getcenter'>" + list[1] + "%</td>";
	ct += "<td id='"+s1+2+"'class='courName getcenter'>" + list1[1] + "分钟</td>";
	ct += "<td id='"+s+3+"'class='courName getcenter'>" + list[2] + "%</td>";
	ct += "<td id='"+s1+3+"'class='courName getcenter'>" + list1[2] + "次</td>";
	ct += "<td id='"+s+4+"'class='courName getcenter'>" + list[3] + "%</td>";
	ct += "<td id='"+s1+4+"'class='courName getcenter'>" + list1[3] + "分</td>";
	/*ct += "<td id='"+s+5+"'class='courName getcenter'>" + list[4] + "%</td>";
	ct += "<td id='"+s1+5+"'class='courName getcenter'>" + list1[4] + "分钟</td>";*/
/*	ct += "<td id='"+s+6+"'class='courName getcenter'>" + list[5] + "%</td>";
	ct += "<td id='"+s1+6+"'class='courName getcenter'>" + list1[5] + "分</td>";*/
	return ct;
}

/**
 * 分割表格函数 封装 -阈值部分
 */
/*function cutTable1(sign,list){
	var ct = "";
	var s = sign;
	for(var i=0;i<list.length;i++){
		ct += "<td id='"+s+i+"'class='courName getcenter'>" + list[i] + "</td>";
	}
	return ct;
}
 */
/**
 * 绑定搜索按钮点击事件
 */
function bindSearchClick() {
	$("#search").click(function(){
		keyWord = $("#emkeyword").val();
		if(keyWord == '')
			keyWord == null;
		applyTableData(keyWord);
		document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		var cookieString = "Search=";
		cookieString = cookieString + JSON.stringify(params);
		document.cookie = cookieString;
	});
}


/**
 * 绑定表单全选按钮事件
 */
function bindSelectAll() {
	$("#chooseall").click(function(){
		if(!isSelectAll())
			for(var i=0;i<$boxes.length;i++) {
				$($boxes[i]).attr("checked","checked");
				$(this).html("反选");
			} else {
				for(var i=0;i<$boxes.length;i++)
					$($boxes[i]).removeAttr("checked");
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