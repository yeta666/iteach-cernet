var departs=null;
var selectDeparts=null;
var type = 0;//机构类型
$(document).ready(function() {
	//列表显示
	ShowColumn();
	// 加载分页 
	if (userId <= 0) {
		$('#pagination').hide();
	} else{
		paginationPage();
	}
	initialBind();
	//加载上级部门
	$.post("../../handler/department/viewDepartments", {
		"type" : type
	}, function(data) {
		if (data.ret) {
			selectDeparts=data.data.departments;
			var departments=data.data.departments;
			if(departments.length>0){
				var departStrs="";
				for(var i=0;i<departments.length;i++){
					departStrs+="<option value='"+departments[i].depaId+"'>"+departments[i].depaName+"</option>";
				}
				$("#depaParentId").append(departStrs);
			}
		} else {
			infoNotice("error", "", data.errmsg);
		}
	}, "json");
	//触发改变
	$("#area").hide();
	$("#school").hide();
	//数据显示
	secondCol = getRequest("secondCol");
	viewaction = '../../handler/department/searchByMutiTerm';
	params = {
			"screenType":0,
			"parentId":-1,
			"departId":-1,
			"searchType":1,
			"searchWord":"",
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
			$('#type').val(params.screenType);
			if(params.parentId>0){
				$("#area").show();
			}
			if(params.departId>0){
				if(params.screenType==1){
					selectShow(params.screenType,1,selectDeparts);	
				}
				else if(params.screenType==3){
					selectShow(params.screenType,3,selectDeparts);
				}
				$("#school").val(params.departId);
				$("#school").show();	
				
			}
			$('#searchType').val(params.searchType);
			$('#searchWord').val(params.searchWord);
		} else {
			// 删除cookie
			document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		}
	}
	
	$("#type").change(function(){
		var type=$("#type").val();
		if(type==3) {
			selectShow(type,3,selectDeparts);
			}
		else if(type==1){		
			selectShow(type,1,selectDeparts);
		}
		
	});
	/*$("#area").change(function(){
		var type=$("#type").val();
		var area=$("#area").val();
		if(area==-1||type<3){
			$("#school").html("<option value='-1'>全部</option>");
			$("#school").hide();
		}else{
			var schoolOptions="<option value='-1'>全部</option>";
			if(selectDeparts.length>0){
				for(var i=0;i<selectDeparts.length;i++){
					if(selectDeparts[i].depaType==3){
						schoolOptions+="<option value='"
							+selectDeparts[i].depaId+"'>"
							+selectDeparts[i].depaName+"</option>";
					}
				}
			}
			$("#school").html(schoolOptions);
			$("#school").show();
		}		
	});*/
	
	//修改和增加机构时，用户选择机构类型
	$("#depaType").change(function(){
		var depaTypeSel=$("#depaType").val();
		$.post("../../handler/department/viewParentDepartments", {
			"type" : depaTypeSel
		}, function(data) {
			if (data.ret) {
				selectDeparts=data.data.departments;
				var departments=data.data.departments;
				var departStrs="";
				if(departments.length>0){
					for(var i=0;i<departments.length;i++){
						departStrs+="<option value='"+departments[i].depaId+"'>"+departments[i].depaName+"</option>";
					}
				}
				else
					{
					departStrs+="<option value='"+"-1"+"'>"+"无"+"</option>";
					}
				$("#depaParentId").html(departStrs);
			} else {
				infoNotice("error", "", data.errmsg);
			}
		}, "json");
	});

	//搜索
	$("#search").click(function(){
		var type=$("#type").val();
		var area=$("#area").val();
		var school=$("#school").val();
		
		var parentId=-1;
		var departId=-1;
		if(type==2){
			if(area>0){
				departId=area;
			}
		}else if(type==3){
			if(school>0){
				departId=school;
			}
			else if(school==-1&&area>0){
				parentId=area;
			}
		}
		else if(type==1){
			departId=school;
		}
		params.departId=departId;
		params.parentId=parentId;
		params.screenType=type;
		params.searchType=$("#searchType").val();
		params.searchWord=$("#searchWord").val();
		params.currentPage=1;
		document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		var cookieString = "Search=";
		cookieString = cookieString + JSON.stringify(params);
		document.cookie = cookieString;
		initSearch();
	});
	//全选	
	$("#chooseall").click(function(){
		var $checkboxes = $("#mainTable input[type=checkbox]");
		if($(this).html() == '全选') {
			for(var i=0;i<$checkboxes.length;i++) {
				/*$($checkboxes[i]).attr("checked","checked");*/
				$($checkboxes[i]).prop("checked",true);
			}
			$(this).html('反选');
		} else {
			for(var i=0;i<$checkboxes.length;i++) {
			/*	$($checkboxes[i]).removeAttr("checked");*/
				$($checkboxes[i]).prop("checked",false);
			}
			$(this).html('全选');
		}
	});
	//重置
	$("#reset").click(function(){
		$("#depaType").val(1);
		$("#depaParentId").val(-1);
		$("#depaName").val("");
		$("#depaAbbreviation").val("");
		$("#depaCode").val("");
	});
	//获取数据
	initSearch();
	$("#sub").click(function(){		
		submitDepart();
	});
});

function selectShow(type,num,selectDeparts){
	if(type==num){
		var areaOptions="<option value='-1'>全部</option>";
		if(selectDeparts.length>0){
			for(var i=0;i<selectDeparts.length;i++){
				if(selectDeparts[i].depaType==num){
					areaOptions+="<option value='"
						+selectDeparts[i].depaId+"'>"
						+selectDeparts[i].depaName+"</option>";
				}
			}
		}
		$("#school").html(areaOptions);
		$("#area").html("<option value='-1'>全部</option>");			
		$("#area").hide();
		$("#school").show();
  }
}

//加载数据到数据区域
function refreshContent(pageRecords) {
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	var tableContent="";
	if (pageRecords.data.length == 0) {
		tableContent = "<tr><td colspan=\"7\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>";
	} else {
		departs=pageRecords.data;
		$.each(pageRecords.data, function(itemIndex, item) {
			//预处理
			if(item.depaAbbreviation==undefined||item.depaAbbreviation==null){
				item.depaAbbreviation="";
			}
			if(item.depaCode==undefined||item.depaCode==null){
				item.depaCode="";
			}			
			tableContent+="<tr>" +
			"<td><input type='checkbox'/ value='"+item.depaId+"'></td>" +
			"<td>"+startIndex+"</td>" +
			"<td><a href='#addOrMod' data-toggle='modal' onclick='setDepartInfo("+item.depaId+")'>"+item.depaName+"</a></td>" +
			"<td>"+item.depaTypeName+"</td>" +
			"<td>"+item.depaAbbreviation+"</td>" +
			"<td>"+item.depaCode+"</td>" +
			"<td>"+item.parentDepaName+"</td>" +
			"</tr>";
			startIndex++;
		});
	}
	$('#departList tbody:nth-child(2)').empty().append(tableContent);
	return false;
}

//修改机构信息的准备工作
function setDepartInfo(departId){
	if(departId>0){
		$("#boxTitle").html("修改机构");
		$("#sub").html("修改");
		$("#reset").hide();
		var saveDepartId = departId;		
		departId = saveDepartId;
		$.each(departs, function(itemIndex, item) {
			if(item.depaId==departId){
				$("#depaId").val(departId);
				$("#depaType").val(item.depaType);
				$("#depaType").change();
				if(item.depaParentId==null||item.depaParentId<=0){
					item.depaParentId=-1;
				}
				$("#depaParentId").val(item.depaParentId);
				$("#depaName").val(item.depaName);
				$("#depaAbbreviation").val(item.depaAbbreviation);
				$("#depaCode").val(item.depaCode);
				return false;
			}
		});
	}else{
		$("#boxTitle").html("新增机构");
		$("#sub").html("添加");
		$("#reset").show();
		$("#depaId").val(-1);
		$("#depaParentId").val(-1);
		$("#depaType").val(1);
		$("#depaName").val("");
		$("#depaAbbreviation").val("");
		$("#depaCode").val("");
	}

}

//新增，或者修改
function submitDepart(){
	var depaId=$("#depaId").val();
	var depaName=$("#depaName").val();
	if(depaName==""){
		alert("机构名称不能为空！");
		return;
	}
	var depaParentId=$("#depaParentId").val();
	if(depaParentId==-1){
		depaParentId=null;
	}
	if(depaId<=0){		
		//新增
		$.post("../../handler/department/addDepartment", {
			"depaType" : $("#depaType").val(),
			"depaParentId":depaParentId,
			"depaName":depaName,
			"depaAbbreviation":$("#depaAbbreviation").val(),
			"depaCode":$("#depaCode").val(),
		}, function(data) {
			if (data.ret) {
				infoNotice("success", "", "新增成功!");
				$('#addOrMod').modal('hide');
				params = {
						"screenType":-1,
						"parentId":-1,
						"departId":-1,
						"searchType":-1,
						"searchWord":"",
						"pageArray" : new Array(),
						"recordPerPage" : 10
				};
				initSearch();
			} else {
				infoNotice("error", "", data.errmsg);
			}
		}, "json");
	}else{
		//修改
		$("#sub").html("正在修改，请稍等……");
		$.post("../../handler/department/updateDepartment", {
			"depaId" : depaId,
			"depaType" : $("#depaType").val(),
			"depaParentId":depaParentId,
			"depaName":depaName,
			"depaAbbreviation":$("#depaAbbreviation").val(),
			"depaCode":$("#depaCode").val(),
		}, function(data) {
			$("#sub").html("修改");
			if (data.ret) {
				infoNotice("success", "", "修改成功!");
				$('#addOrMod').modal('hide');
				params = {
						"screenType":-1,
						"parentId":-1,
						"departId":-1,
						"searchType":-1,
						"searchWord":"",
						"pageArray" : new Array(),
						"recordPerPage" : 10
				};
				initSearch();
			} else {
				infoNotice("error", "", data.errmsg);
			}
		}, "json");
	}
}

//删除
function deleteDeparts(){
	if (confirm("删除后可能会影响系统其他功能，请确认是否删除！")) {
		// 获取选择的用户id序列
		var departIds = new Array();
		$("input[type=checkbox]").each(function() {
			var $current = $(this);
			if ($current.is(':checked'))
				departIds.push($current.val());
		});

		// 如果选择的id为空，则提示错误信息
		if (departIds.length == 0) {
			infoNotice("warning", "", "请先选中要删除机构！");
		}
		// 否则，向后台发送删除请求并显示结果信息
		else {
			$.post("../../handler/department/deleteDepartments", {
				"departmentIds" : departIds.toString()
			}, function(data) {
				if (data.ret) {
					infoNotice("success", "", "删除成功!");
					params = {
							"screenType":-1,
							"parentId":-1,
							"departId":-1,
							"searchType":-1,
							"searchWord":"",
							"pageArray" : new Array(),
							"recordPerPage" : 10
					};
					initSearch();
				} else {
					infoNotice("error", "", data.errmsg);
				}
			}, "json");
		}
	}
}

//批量导入
function importDeparts(){
	var file=$("#postfile").val();
	if(file==""||file==null){
		alert("请先选择导入文件！");
		return;
	}else{
		var str=file.split(".");
		if(str.length<2||str[1]!="xls"){
			alert("导入文件格式不正确！");
			return;
		}
	}
	$("#loading").show();
	$.ajaxFileUpload({
		url :'../../handler/department/importExcel',
		secureuri :false,
		fileElementId :'postfile',
		dataType : 'json',
		success : function (data, status){
			$("#loading").hide();
			if (data.ret) {
				infoNotice("success", "", "批量导入机构成功！");
				$('#batchImportDeparts').modal('hide');
			}else{
				infoNotice("error", "", data.errmsg);
			}
		},
		error: function (data, status, e){
			$("#loading").hide();
			infoNotice("error", "", e);
		}
	});
}

//批量导出
function exportDeparts(){
	var type=$("#type").val();
	var searchType=$("#searchType").val();
	var searchWord=$("#searchWord").val();
	window.location.href="../../handler/department/exportExcel?screenType="
		+type+"&searchType="+searchType+"&searchWord="+searchWord;
}


