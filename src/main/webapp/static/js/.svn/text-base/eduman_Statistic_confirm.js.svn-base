var viewaction = '';// 分页请求的action
var search ='',confirmRemark='', postfile='';
var departmentT=0,userT=0,confirmOrNot =0, exportDepaId = 0, confirmDepaId = 0,getattachesId=0, exportId=0;
var params;// 请求的参数
$(document).ready(function() {
	$("#postfile").val("");
	$("#queryInfo").val("");
	ShowColumn();// 显示功能栏
	backToTop();// 回顶部
	userT = userType;
	departmentT = departmentTypeID;
	selceteOrg();
	if (userId <= 0) {
		/** 加载分页 */
		$('#pagination').hide();
	} else {
		paginationPage();
	}
	// 显示数据
	var secondCol = getRequest("secondCol");
	viewaction = '../../handler/scoreExportConfirm/viewList';
	params = {
			"query":"",
			"confirmOrNot":0,
			"exportDepaId":0,
			"confirmDepaId":0,
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
			$('#keyword').val(params.confirmOrNot);
			$('#isConfirmed').val(params.search);
			$('#exportDepart').val(params.exportDepaId);
			$('#confirmDepart').val(params.confirmDepaId);
		} else {
			// 删除cookie
			document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		}
	}
	// 绑定分页的一些操作响应
	initialBind();
	initSearch();
    
	$("#search").click(function() {
		search = $('#keyword').val();
		confirmOrNot = $('#isConfirmed').val();
		exportDepaId = $('#exportDepart').val();
		confirmDepaId = $('#confirmDepart').val();
		/*$('#keyword').val(params.confirmOrNot);
		$('#isConfirmed').val(params.search);
		$('#exportDepart').val(params.exportDepaId);
		$('#confirmDepart').val(params.confirmDepaId);*/
		$('#scoreInquiryTable tbody:nth-child(2)').empty().append(
		"<tr><td colspan=\"5\" class=\"tdcenter\"><img src=\"../img/octocat-spinner.gif\" />正在加载数据...</td></tr>");
		viewaction = '../../handler/scoreExportConfirm/viewList';
		params = {
				"query":search,
				"confirmOrNot":confirmOrNot,
				"exportDepaId":exportDepaId,
				"confirmDepaId":confirmDepaId,
				"pageArray" : new Array(),
				"recordPerPage" : 10,
				"currentPage" : 1,
				"secondCol" : secondCol
		};
		document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		var cookieString = "Search=";
		cookieString = cookieString + JSON.stringify(params);
		document.cookie = cookieString;
		initSearch();
		
	});

	$('.waitupload').bind("click",function(){
		exportId = $(this).attr("value");
		$("#addAttachment").click(function(){
			postfile1 = $("#postfile").val();
			if(postfile != postfile1){
				postfile = postfile1;
			}
			else{
				alert("不能上传多个文件！");
				return ;
			}
			var getpostfile = new Array();
			getpostfile = postfile.split(".");
			if (postfile == null || postfile == "") {
				alert("请先点击“浏览”按钮选择要上传的附件！");
				return ;
			}
			else if(postfile!=""&&getpostfile[getpostfile.length-1]!="xls"){
				alert("请删除错误格式的文件，上传正确格式的文件！");
				$("#postfile").val("");
				return ;
			}
			else{
				$("#loading").show();// 动态加载小图标
				$.ajaxFileUpload({
					url : '../../handler/load/upload',
					secureuri : false,
					fileElementId : 'postfile',
					dataType : 'json',
					data : {
						fileType : 7,
						location : 'upload/score/'
					},
					success : function(data, status) {
						$("#loading").hide();
						if (data.ret) {
							if ($.isEmptyObject(data.data.attachId)) {
								getattachesId = data.data.attachId;
								//alert(getattachesId);
								alert("文件完成上传！");
							}
						}else{
							alert(data.errmsg);
						}
					},
					error : function(data, status, e) {
						$("#loading").hide();
						alert(e);
					}
				});
			}
		});
	}); 

	$("#getQueryInfo").click(function (){
		confirmRemark = $("#queryInfo").val();
		if(getattachesId!=""&&confirmRemark!=""){
			$.ajax({
				type : 'post',
				contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
				url : "../../handler/scoreExportConfirm/confirm",
				dataType : 'json',
				data:{
					exportId:exportId,
					confirmAttaId:getattachesId,
					confirmRemark:confirmRemark
				},
				success : function(data){
					if(data.ret){
						alert(data.data.info);
						$("#postfile").val("");
						location.reload();
					}else{
						alert(data.errmsg);
					}
				}
			});
		}
		else{
			alert("请上传附件并填写备注！");
		}

	});
});

//选择机构
function selceteOrg() {
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/department/viewDepartments",
		dataType : 'json',
		data:{type:3},
		success : function(data) {
			var schoolList;
			if (data.ret) {
				if ($.isEmptyObject(data.data.departments)) {
					schoolList = "<option value='-1'>学校数据为空！！</option>";
				} else {
					schoolList += "<option value=\"0\">所有机构</option>";
					$.each(data.data.departments, function(i, val) {
						schoolList += "<option value='" + val.depaId + "'>" + val.depaName + "</option>";
					});
				}

			} else {
				schoolList = "<option value='-1'>"+data.errmsg+"</option>";
			}
			$(".seleteSchoolID").empty().append(schoolList);
		}
	});
}
//加载数据到数据区域
function refreshContent(pageRecords) {
	$.ajaxSettings.async = false;
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	var tableContent = "";
	if (pageRecords.data.length == 0) {
		tableContent = "<tr><td colspan=\"10\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>";
	} else {
		selecteCourseData = pageRecords.data;
		$.each(pageRecords.data, function(itemIndex, item) {
			tableContent += "<tr><td>" + startIndex + "</td>" + "<td>" + item.exportUserName + "</td>" + "<td>" + item.exportDepartName
			+ "</td><td>" + item.exportTimeStr + "</td>" + "<td align='center'>" 
			+"<a class='fa fa-download'  href='../../handler/load/download?attaId="+item.exportAttachId+"'><i class='icon-download-alt icon-white'></i></a></td>";
			if(item.confirmUserName==undefined||item.confirmUserName==""){
				//无结果等待上传
				tableContent +="<td>暂无</td><td>暂无</td><td>暂无</td>" 
					+ "<td><button class='btn btn-info waitupload' href='#waitUpload' data-toggle='modal'  value='" + item.secoId +"'>等待确认</button></td>";
			}
			else if(item.resultAttachId==undefined){
				//确认成功，无结果可下载
				tableContent += "<td>"+ item.confirmUserName + "</td>" + "<td>" + item.confirmDepartName + "</td>"+"<td>"+item.exportTimeStr+"</td>"
				+"<td>确认成功</td>";
			}
			else{
				//确认未成功，有结果可下载
				tableContent += "<td>"+ item.confirmUserName + "</td>" + "<td>" + item.confirmDepartName + "</td>"+"<td>"+item.exportTimeStr+"</td>"
				+"<td><a class='btn btn-success' href='../../handler/load/download?attaId="+item.resultAttachId+"'><i class='icon-download-alt icon-white'></i></a></td>";
			}

			if(item.confirmUserName==undefined||item.confirmUserName==""){
				tableContent += "<td>暂无</td>";
			}
			else{
				tableContent += "<td>" + item.confirmRemark +"</td>";
			}

			tableContent+="</tr>";
			startIndex++;
		});
	}
	$('#scoreInquiryTable tbody:nth-child(2)').empty().append(tableContent);
	return false;
}