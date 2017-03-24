var params;// 请求的参数
var userT;//用户种类
/**
 * @author anny
 * 主函数
 */
$(document).ready(function() {
	ShowColumn();// Show Column
	backToTop();// back To Top

	 $('.summernote').summernote({
         lang: 'zh-CN'
     });
	
	$("#title").val("");
	$("#topDays").val("");
	/*编辑器内容清空  end*/
	userT = userType;
	if(userT==1){
		//alert("学生");
	}
	else if(userT==2){
		//alert("老师");
	}
	else if(userT==3){
		$("#noticeAddTeaSys").css("display","block");//教务管理员，发送角色
		//alert("教务管理员");
		addPopDivClick(3);
	}
	else if(userT==4){
		$("#noticeAddSys").css("display","block");//系统管理员，发送角色
		//alert("系统管理员");
		addPopDivClick(4);
	}
});

/**
 * send-发送通知   提交按钮事件
 */
function addPopDivClick(reoType) {
	$("#sendNoticeButton").click(function(){
		var addTitle = $("#title").val();
		var addContent = $(".summernote").code();
		var addTopDays = $("#topDays").val();
		if(addTopDays==''){
			addTopDays=0;
		}
		if (!/^\d+$/.test(addTopDays)) {
		       alert("请填写整天数！");
			}
		if(addTitle!=""&&addContent!=""){
			if(addTopDays==''){
				addTopDays = 0;
			}
			var addParam = {
					noanTitle:addTitle,
					noanContent:addContent,
					noanTopdays:addTopDays,
					noanDeparid:departId,
					noanCourid:departmentTypeID
			};
			if(reoType==2){
				addParam.roleIds=',1,';
			}
			if(reoType==3){
				var selerole = "," ;
				$("input[class='sendObj']").each(function() {
					var $current = $(this);
					if ($current.is(':checked'))
						selerole += $current.val()+",";
				});
				addParam.roleIds=selerole;
				//alert(selerole);
			}
			if(reoType==4){
				var selerole = "," ;
				$("input[class='sendObj']").each(function() {
					var $current = $(this);
					if ($current.is(':checked'))
						selerole += $current.val()+",";
				});
				addParam.roleIds=selerole;
				//alert(selerole);
			}
			if(addTitle != null && addContent != null && addTopDays!=null) {
				$.post("../../handler/noticeAnnouncement/addNoticeAnnouncementList.do",
						addParam,function(data){
					if(data.ret) {
						if(data.data.status == 1) {
							alert("添加成功！");
							//window.location.reload();
							window.location.href = 'notice.html?firstCol=1&secondCol=15';
						}else{
							alert("添加失败！请联系管理员。");
						}
					}
				},"json");		
			}
		}
		else{
			alert("请将数据填写完整！");
		}
	});
}



