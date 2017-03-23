/*
 *  author：郭海蓉 
 *  time:2013-07
 */
var classData=null;//存储分类数据，用于修改功能
var code="",name="",description="",state="";//新增，修改参数
var tempcocaId=0;//记录cocaid
var secondCol = 0;
var classIDs="";
$(document).ready(function() {
	ShowColumn();
	backToTop();
	if (userId <= 0) {
		$('#pagination').hide();
	} else
		paginationPage();
	$('#eduman').attr("class", "active");
	initialBind();
	// 加载数据
	showDatafirst();
	//全选	
	$('#chooseallClass').click(function() {
		if($(this).html()=="全选"){
			$('input[name="chooseID"]').prop("checked", true);
			$(this).text("反选");
		}
		else{
		$('input[name="chooseID"]').prop("checked",false);
		$(this).text("全选");
		}
	});
	

	//新增课程分类
	$("#add").click(function(){//提交
		code=$("#code").val();
		name=$("#name").val();
		description=$("#description").val();
		state=$("#state").val();
		if (code=="" || name=="" || description==""){
			$("#infoNotForCouseClass").html("<div class=\"alert alert-error\">"+
					"<button type=\"button\" class=\"close\" data-dismiss=\"alert\">&times;</button>"+
					"<h4>请输入必填信息！</h4></div>"
			);
		}else{
			$.ajax({
				type : 'post',
				contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
				url : "../../handler/courseCategory/create",
				dataType : 'json',
				data:{cocaName:name,
					cocaCode:code,
					cocaDescirbe:description,
					cocaCreateUserid:userId,
					cocaState:state,
				},
				success : function(data) {
					if (data.ret) {
						alert("新增操作成功！");
						$('#addOrMod').modal('hide');
						showDatafirst();
					} else {
						alert(data.errmsg);
					}
				}
			});
		}
	});
	//修改课程分类
	$("#updata").click( function () { 
		code=$("#code").val();
		name=$("#name").val();
		description=$("#description").val();
		state=$("#state").val();
		if (code=="" || name=="" || description==""){
			$("#infoNotForCouseClass").html("<div class=\"alert alert-error\">"+
					"<button type=\"button\" class=\"close\" data-dismiss=\"alert\">&times;</button>"+
					"<h4>请输入必填信息！</h4></div>"
			);
		}else{
			$.ajax({
				type : 'post',
				contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
				url : "../../handler/courseCategory/modify",
				dataType : 'json',
				data:{cocaName:name,
					cocaId:tempcocaId,
					cocaCode:code,
					cocaDescirbe:description,
					cocaState:state},
					success : function(data) {
						if (data.ret) {
							alert("修改操作成功！");
							$('#addOrMod').modal('hide');
							showDatafirst();
						} else {
							alert("失败！！"+data.errmsg);
						}
					}
			});
		}
	});


});
function showDatafirst(){
	secondCol = getRequest("secondCol");
	viewaction = '../../handler/courseCategory/viewList';
	params = {
			"pageArray":new Array(),
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
		} else {
			// 删除cookie
			document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		}
	}
	
	initSearch();
}
var code="",name="",description="",state="";
//加载数据到数据区域
function refreshContent(pageRecords) {
	$.ajaxSettings.async = false;
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	var tableContent="";
	var time="",flag="";
	if (pageRecords.data.length == 0) {
		tableContent = "<tr><td colspan=\"7\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>";
	} else {
		classData=pageRecords.data;
		$.each(pageRecords.data, function(itemIndex, item) {
			flag="";
			if (item.cocaState==0) {
				flag="闭";
			}else if(item.cocaState==1){
				flag="开";
			}
			time=item.formatCreateTime.split(" ")[0];
			tableContent += "<tr><td class='tdcenter'><input type=\"checkbox\" value="+item.cocaId+" name=\"chooseID\"></td>"+
			"<td >"+startIndex+"</td>"+
			"<td >"+item.cocaCode+"</td>"+
			"<td ><a href='#addOrMod' data-toggle='modal' onclick='operateCourseClass("+item.cocaId+")'>"+item.cocaName+"</a></td>";
			if (item.cocaDescirbe!=undefined) {
				tableContent +="<td >"+titleFormat(item.cocaDescirbe,10)+"</td>";
			}else{
				tableContent +="<td > — </td>";
			}
			tableContent +="<td >"+item.creatorName+"</td>"+
			"<td  title='"+item.formatCreateTime+"'>"+time+"</td>"+
			"<td >"+flag+"</td><tr>";
			startIndex++;
		});
	}
	$('#tableContent tbody:nth-child(2)').empty().append(tableContent);
	return false;
}
function operateCourseClass(cocaId){
	tempcocaId=cocaId;
	//新增
	if(cocaId==0){
		$("#myModalLabel").html("新增课程领域");
		//清空输入框
		$("#code").val("");
		$("#name").val("");
		$("#description").val("");
		//隐藏修改按钮和创建人，创建时间输入框
		$("#reset").show();
		$("#add").show();
		$("#updata").hide();
		$("#cancle").hide();
		$("#createTime1").hide();
		$("#createUser1").hide();
		$("#reset").click(function(){//重置
			$("#code").val("");
			$("#name").val("");
			$("#description").val("");
		});

	}else if (cocaId>0) {     //修改
		$("#myModalLabel").html("课程领域修改");
		$("#reset").hide();
		$("#add").hide();
		$("#updata").show();
		$("#cancle").show();
		$("#createTime1").show();
		$("#createUser1").show();
		$("#state1").show();
		$.each(classData, function(itemIndex, item) {
			if(item.cocaId==tempcocaId){
				$("#code").val(item.cocaCode);
				$("#name").val(item.cocaName);
				$("#description").val(item.cocaDescirbe);
				$("#createUser").val(item.creatorName);
				$("#createTime").val(item.formatCreateTime);
				$('option[value="' + item.cocaState + '"]').attr("selected","selected");
				return false;
			}
		});

	}
}
//删除
function deleteCourseClass() {
	 classIDs="";
	var count=0;
	$("input[name='chooseID']").each(function(index, item) {
		if ( $(this).is(":checked"))
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
		alert("你还没有勾选需要删除的数据,请先勾选!!");
	} else {
		//alert(count);
		$.ajax({
			type : 'post',
			contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
			url : "../../handler/courseCategory/delete",
			dataType : 'json',
			data:{courseCateIds:classIDs
			},
			success : function(data) {
				if (data.ret) {
					alert("删除操作成功！");
					showDatafirst();
				} else {
					alert(data.errmsg);
				}
			}
		});
	}
	//$(this).attr("href", "#delTeaModel");
};



/*function deleteCourseClass(){
	var classIDs="";
	
	$("input[name='chooseID']").each(function() {
		var $current = $(this);
		if ($current.attr('checked') != undefined)
		{
			if (classIDs=="") {
				classIDs+=$current.val();
			}else{
				classIDs+=","+$current.val();
			}

		}
	});
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/courseCategory/delete",
		dataType : 'json',
		data:{courseCateIds:classIDs
		},
		success : function(data) {
			if (data.ret) {
				alert("删除操作成功！");
				showDatafirst();
			} else {
				alert(data.errmsg);
			}
		}
	});
}*/


