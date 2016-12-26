var resoType = null;
var resoTitle = null; 
var courId = null;

$(document).ready(function() {
	// back To Top
	backToTop();
	//列表显示
	ShowColumn();
	$('#sysmanResources').attr("class", "active");
	//查询Table数据
	applyTableData();
	//展示全部课程
	viewAllSysResources();
	//	课程检索按钮点击
	$("#courseSel").click(function(){
		var cid = $("#Course option:selected").attr("value");
		if(cid == -1)
			courId = null;
		else
			courId = cid;
		applyTableData();
	});
	$("#resoSearch").click(function(){
		var tempType = $("#quResType option:selected").attr("value");
		var tempTitle = $("#quResTitle").val();
		if(tempType == -1)
			resoType = null;
		else
			resoType = tempType;
		if(tempTitle == "")
			resoTitle = null;
		else 
			resoTitle = tempTitle;
		applyTableData();
	});
	$("#delRe1").click(function(){
		var ids = "";
		var $checked = $("tr input[type=checkbox]:checked");
		if($checked != null) {
			for(var i=0;i<$checked.length;i++) {
				ids += $($checked[i]).attr("value")+",";
			}
		}
		if(ids == "") {
			alert("请至少选择一项删除");
			return;
		}
		$.post("../../handler/resource/delResourceByAdmin.do",{
			ids:ids.substring(0, ids.length-1)
		},function(data){
			try {
				if(data.data.status == 1) {
					alert("删除成功！");
				}
			} catch(e) {
				alert('删除失败！');
			}
		},"json");
	});
});

function selRes(){
	var selResId = $('#resType').val();
	var selResVal = $('#resType').find('option:selected').text();
	var uploadDocVideo = document.getElementById("addUploadRes");
	var uploadLink = document.getElementById("addLinkRes");
	if(selResId==-1){
		alert("请选择资源类型");
		uploadDocVideo.style.display='none';
		uploadLink.style.display='none';
	}
	else if(selResId==1||selResId==2){
		uploadLink.style.display='none';
		uploadDocVideo.style.display='block';
	}
	else if(selResId==3){
		uploadDocVideo.style.display='none';
		uploadLink.style.display='block';
	}
}
//	请求数据创建表格
function applyTableData() {
	var url = "../../handler/resource/viewResourceListForAdmin.do?data=3450asdf66as4459";
	if(courId != null) 
		url += "&courId="+courId;
	if(resoType != null)
		url += "&resoType="+resoType;
	if(resoTitle != null)
		url += "&resoTitle"+resoTitle;
	$.getJSON(url,{
			page:1,
			rows:10
		},function(data){
		if(data.data.status == 1) {
			var list = data.data.resourceList;
			//	合成添加的html字符串  信安专业综合实验结构化评分表
			var html = "";
			if(list != null) {
				for(var i=0;i<list.length;i++) {
					var resoType = null;
					switch(list[i].resoType) {
					case 1 : resoType = "链接";break;
					case 2 : resoType = "文档";break;
					case 3 : resoType = "视频";break;
					default : break;
					}
					html += ("<tr>" +
								"<td><input type='checkbox' value='"+list[i].resoId+"'/></td>" +
								"<td>"+list[i].resoTitle+"</td>" +
								"<td>"+resoType+"</td>" +
								"<td>"+list[i].resoAddtime+"</td>" +
								"<td>"+list[i].userRealname+"</td>" +
								"<td>"+list[i].courName+"</td>" +
								"<td><i class='icon-download-alt' rtype='"+
								list[i].resoType +"' value='"+list[i].resoId+"'></i></td>" +
							"</tr>");
				}
				//	添加到页面上
				$("#mainTable").html(html);
				//	下载按钮点击事件绑定
				$("#mainTable i").each(function(index,dom){
					$(dom).click(function(){
//						alert("rid="+$(dom).attr("value"));
						window.open("../../handler/resource/downloadDocumentResource.do?rid="
								+$(dom).attr("value")+"&rtype="+$(dom).attr("rtype"));
					}).mouseover(function(){
						$(this).css("cursor","pointer");
					}).mouseout(function(){
						$(this).css("cursor","default");
					});
				});
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
							$($checkboxes[i]).attr("checked","checked");
						}
						$(this).html('反选');
					} else {
						for(var i=0;i<$checkboxes.length;i++) {
							$($checkboxes[i]).removeAttr("checked");
						}
						$(this).html('全选');
					}
				});
			}
		}
	});
}

//	判断checkbox是否全选
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
//查询所有课程列表
function viewAllSysResources(){
	var HTML;
	var $records=$('.resCourse');
	$.getJSON("../../handler/course/viewAllCourseNameList.do",{
	},function(data){
		var course = data.data.viewAllCourse;
		for(var i=0;i<course.length;i++){
			 HTML =	"<option value="+course[i].courId+">"+course[i].courName+"</option>";
			 $records.append(HTML);
		}
	   
	});
}
//添加教学资源
function addRes(){
	 
}



//控制附件上传
function uploadRes(){
	var upload=$("#file").val();
	if(upload==null||upload==""){
		alert("请先点击“浏览”按钮选择要上传的附件！");
		return false;
	}
	var resoType = $("#resType option:selected").val();
	var resoCourId = $("#addResCoures option:selected").val();
	var resoTitle = $("#resName").val();
	if(resoType != -1 && resoCourId != -1 && resoTitle != "") {
		loading();//动态加载小图标
		$.ajaxFileUpload({
			url :'../../handler/resource/addResourceByAdmin.do',
			secureuri :false,
			fileElementId :'file',
			dataType : 'json',
			data:{
				resoCourId:resoCourId,
	        	resoType:resoType,
	        	resoTitle:resoTitle,
	        	resoDescribe:$("#resDescribe").val()
			},
			success : function (data, status){
				if (data.ret) {
					if(data.data.status == 1){
						alert("上传成功！");
						$('#addReModal').modal('hide');
					}
				}
			},
			error: function (data, status, e){
				alert(e);
			}
		});
	}
}
