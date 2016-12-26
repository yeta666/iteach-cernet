/*
 *  author：吴岘辉 
 *  time:2013-07
 */
var courses=null;
var selectDepartId=0;
var currentId;
var secondCol = 0;
$(document).ready(function() {
	backToTop();
	ShowColumn();
	if (userId <= 0) {
		$('#pagination').hide();
	} else{
		paginationPage();
	}
	initialBind();
	selectDepartId=departId;
	//学校筛选
	if (departmentTypeID ==1) {// 市级
		selectDepartId=0;
		$("#seleteSchool").show();
		$.post("../../handler/department/viewDepartments", {
			"type" : 3
		}, function(data) {
			if (data.ret) {
				selectDeparts=data.data.departments;
				var departments=data.data.departments;
				if(departments.length>0){
					var departStrs="";
					for(var i=0;i<departments.length;i++){
						departStrs+="<option value='"+departments[i].depaId+"'>"+departments[i].depaName+"</option>";
					}
					$("#seleteDepartID").append(departStrs);
					$("#departs").append(departStrs);
				}
			} else {
				infoNotice("error", "", data.errmsg);
			}
		}, "json");
	} else {
		$("#seleteSchool").hide();
	}

	//触发改变
	$("#departs").change(function(){
		getTeachers();
	});
	secondCol = getRequest("secondCol");
	//数据显示
	viewaction = '../../handler/course/viewCourseAndTeahcerInfo';
	params = {
			"departType":departmentTypeID,
			"departId":selectDepartId,
			"searchType":0,
			"searchWord":"",
			"pageArray" : new Array(),
			"recordPerPage" : 10,
			"currentPage" : 1,
			"secondCol" : secondCol
	};	

	//搜索
	$("#search").click(function(){
		currentId=$("#seleteDepartID").val();
		if(currentId==""||currentId<=0){
			currentId=selectDepartId;
		}
		params.departId=currentId;
		params.searchType=$("#searchType").val();
		params.searchWord=$("#searchWord").val();
		document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		var cookieString = "Search=";
		cookieString = cookieString + JSON.stringify(params);
		document.cookie = cookieString;
		initSearch();
	}); 
	if ($.cookie("Search") != null && $.cookie("Search") != "") {
		var SearchCookie = $.cookie("Search");
		var SearchPar = JSON.parse(SearchCookie);
		if (SearchPar.secondCol == secondCol) {
			params = SearchPar;
			$('#seleteDepartID').val(params.departId);
			$('#searchType').val(params.searchType);
			if(params.searchWord!=null){
				$('#searchWord').val(params.searchWord);
			}
		} else {
			// 删除cookie
			document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		}
	}
	//获取数据
	initSearch();
});

//获取教师
function getTeachers(){
	//根据部门获取主讲教师
	var depaId=0;
	depaId=$("#departs").val();
	if(depaId<0){
		depaId=selectDepartId;
	}
	$.post("../../handler/user/viewUsersByTypeAndDepa", {
		"userType" : 2,"depaId":depaId
	}, function(data) {
		if (data.ret) {
			if(data.data.users!=undefined||data.data.users.length>0){
				var teachertStrs="";
				for(var i=0;i<data.data.users.length;i++){
					teachertStrs+="<option value='"
						+data.data.users[i].userId+","+data.data.users[i].userDepaId+"'>"
						+data.data.users[i].userRealname
						+"</option>";
				}
				$("#mentroTeacher").empty().append(teachertStrs);
			}
		} else {
			infoNotice("error", "", data.errmsg);
		}
	}, "json");
}

//加载数据到数据区域
function refreshContent(pageRecords) {
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	var tableContent="";
	if (pageRecords.data.length == 0) {
		tableContent = "<tr><td colspan=\"6\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>";
	} else {
		courses=pageRecords.data;
		$.each(pageRecords.data, function(itemIndex, item) {
			//预处理
			if(item.courCode==undefined||item.courCode==null){
				item.courCode="";
			}
			var mainTeaName=stringTruncate(item.courTeacherName,",",2);
			var mentroTeaName=stringTruncate(item.courMentroTeaName,",",2);
			tableContent+="<tr>" +
			"<td>"+startIndex+"</td>" +
			"<td><a href='#mod' data-toggle='modal' onclick='setCourseInfo("+item.courId+")'>"+item.courName+"</a></td>" +
			"<td>"+item.courCode+"</td>" +
			"<td>"+item.departName+"</td>" +
			"<td title='"+item.courTeacherName+"'>"+mainTeaName+"</td>" +
			"<td title='"+item.courMentroTeaName+"'>"+mentroTeaName+"</td>" +
			"</tr>";
			startIndex++;
		});
	}
	$('#courseList tbody:nth-child(2)').empty().append(tableContent);
	return false;
}

//修改教师信息的准备工作
function setCourseInfo(courId){	
	$("#courId").val(courId);
	//获取已有的辅导教师
	var mentroTeaIds="";
	var mentroTeaNames="";
	var mentroTeaDepaIds="";
	$.each(courses, function(itemIndex, item) {
		if(item.courId==courId){
			mentroTeaIds+=item.courMentroTeaids;
			mentroTeaNames+=item.courMentroTeaName;
			mentroTeaDepaIds+=item.courMentroTeaDepaIds;
			return false;
		}
	});
	//显示已有辅导教师
	var mentroTeaStrs="";
	if(mentroTeaIds.length>0&&mentroTeaNames.length>0
			&&mentroTeaDepaIds.length>0){
		mentroTeaIds=mentroTeaIds.substring(1,
				mentroTeaIds.length-1);
		var ids=mentroTeaIds.split(",");
		var names=mentroTeaNames.split(",");
		var depas=mentroTeaDepaIds.split(",");
		if(ids.length>0&&names.length>0&&depas.length>0){
			$.each(ids, function(index, id) {
				mentroTeaStrs+="<span id='mentro"+id+"' class='"+depas[index]+"'>";				
				mentroTeaStrs+="&nbsp;&nbsp;&nbsp;&nbsp;";
				mentroTeaStrs+=names[index]+
				"<a onclick='delTeacher(\"mentro"+id+"\");' "+ 
				"class='btn btn-warning btn-sm'><i class='fa fa-close'></i></a></span>";
			});
		}		
	}
	$("#mentroTeachers").html(mentroTeaStrs);
	//为新增教师做准备
	if (departmentTypeID ==1) {// 市级
		$("#departs").val(-1);
		$("#departs").show();
		
	}else{
		//$("#departs").val(departId);
		$("#departs").hide();
	}
	getTeachers();
}

//增加辅导教师
function addTeacher(){
	var newTeaId=$("#mentroTeacher").val();
	if(newTeaId==null||newTeaId==""||newTeaId<=0){
		alert("请先选择老师!");
		return;
	}
	var oriVal=newTeaId;
	var newTeaDepaId=newTeaId.split(",")[1];
	newTeaId=newTeaId.split(",")[0];
	//判重
	var temp=$("#mentro"+newTeaId).get();
	if(temp!=null&&temp.length>0){
		alert("教师不能重复添加!");
	}else{
		var str="<span id='mentro"+newTeaId+"' class='"+newTeaDepaId+"' >";
		str+="&nbsp;&nbsp;&nbsp;&nbsp;";
		str+=$("#mentroTeacher  option[value='"+oriVal+"']").text()+
		"<a onclick='delTeacher(\"mentro"+newTeaId+"\");' "+ 
		"class='btn btn-warning btn-small'><i class='icon-remove icon-white'></i></a></span>"
		$("#mentroTeachers").append(str);
	}
}

//删除教师
function delTeacher(id){
	var temp=$("#"+id).get();
	if(temp==null||temp==undefined){
		return;
	}
		
	if(departmentTypeID ==3){
		var teaDepaId=$(temp).attr("class");
		if(departId!=teaDepaId){
			alert("对不起，你只能删除该课程本学校的辅导老师！");
			return;
		}
	}
	$("#"+id).remove();
}

//修改教师
function modifyTeachers(){
	var mentroTeaIds=",";
	spans=$("span[id^='mentro']");
	if(spans!=null&&spans.length>0){
		$.each(spans,function(index,item){
			mentroTeaIds+=item.id.substr(6)+",";
		});
	}
	if(mentroTeaIds.length<3){
		mentroTeaIds="";
	}
	//提交
	$.post("../../handler/course/setTeacherToCourse", {
		"courseId" : $("#courId").val(),
		"addType":2,
		"teacherIds":mentroTeaIds,
	}, function(data) {
		if (data.ret) {
			infoNotice("success", "", "修改辅导教师成功!");
			$('#mod').modal('hide');
			var currentId=$("#seleteDepartID").val();
			if(currentId==""||currentId<=0){
				currentId=selectDepartId;
			}
			params = {
					"departType":departmentTypeID,
					"departId":currentId,
					"searchType":0,
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

