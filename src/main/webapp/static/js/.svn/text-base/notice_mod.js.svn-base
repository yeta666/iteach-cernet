var params;// 请求的参数
var userT;//用户种类
var modNoticeId;//修改Id
var firstCol;
var secondCol;

/**
 * @author anny
 * 主函数
 */
$(document).ready(function() {
	$('.summernote').summernote({
        lang: 'zh-CN'
    });
	//编辑器启动
	modNoticeId = getRequest("modNotice");
	firstCol = getRequest("firstCol");
	secondCol = getRequest("secondCol");
	ShowColumn();// Show Column
	backToTop();// back To Top
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
		getModNoticeData();
		modPopDivClick(3);
	}
	else if(userT==4){
		$("#noticeAddSys").css("display","block");//系统管理员，发送角色
		//alert("系统管理员");
		getModNoticeData();
		modPopDivClick(4);
	}
});


/**
 * 获取需要修改的通知的具体内容
 * 
 */
function getModNoticeData(){
	//var noanIdMod;
	var noanTitleMod;
	var noanRoleidsMod;
	var noanTopdaysMod;
	//var noanCreatoridMod;
	var noanContentMod;
	$.ajax({
		type : 'POST',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',// 发送信息至服务器时内容编码类型
		url : '../../handler/noticeAnnouncement/viewOneNoticeAnnouncement.do',
		async : false, // 需要同步请求数据
		dataType : 'json',
		data:{noanId:modNoticeId,},
		success : function(data, status) {
			noanIdMod = data.data.oneNotice.noanId;
			noanTitleMod = data.data.oneNotice.noanTitle;
			noanRoleidsMod = data.data.oneNotice.noanRoleids;
			noanTopdaysMod = data.data.oneNotice.noanTopdays;
			noanCreatoridMod = data.data.oneNotice.noanCreatorid;
			noanContentMod = data.data.oneNotice.noanContent;
			
		}
	});
    $("#titleMod").val(noanTitleMod);
   
    $('.summernote').code(noanContentMod);
   
    if(noanTopdaysMod==''||noanTopdaysMod==null){
    	  $("#topDaysMod").val('');
    }
    else{
    	  $("#topDaysMod").val(noanTopdaysMod);
    }
    
    var getSendRoleIds=noanRoleidsMod.split(',');
   // noanRoleidsMod = null;
    for(var i=0;i<getSendRoleIds.length;i++){
       if(getSendRoleIds[i]!=''&&getSendRoleIds[i]!=null){
    	   if(getSendRoleIds[i]==1){
    		   $('input[id="notAddSys1Mod"]').prop("checked", true);
    	   }
    	   else if(getSendRoleIds[i]==2){
    		   $('input[id="notAddSys2Mod"]').prop("checked", true);
    	   }
    	   else if(getSendRoleIds[i]==3){
    		   $('input[id="notAddSys3Mod"]').prop("checked", true);
    	   }
       }
    }
    
}




/**
 * send-更改通知   提交按钮事件
 */
function modPopDivClick(reoType) {
	$("#modNoticeButton").click(function(){
		var modNotID = modNoticeId;
		var addTitle = $("#titleMod").val();
		var addContent = $('.summernote').code();
		var addTopDays = $("#topDaysMod").val();
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
					noanId:modNotID,
					noanTitle:addTitle,
					noanContent:addContent,
					noanTopdays:addTopDays,
			};
			if(reoType==2){
				addParam.roleIds=',1,';
			}
			if(reoType==3){
				var selerole = "," ;
				$("input[class='sendObj1']").each(function() {
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
					if ($current.is(':checked') )
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
							alert("修改成功！");
							//window.location.reload();
							window.location.href = 'notice.html?firstCol='+firstCol+'&secondCol='+secondCol;
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



