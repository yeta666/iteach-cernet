var pictureState=0;//记录是否已上传了图片
var prePicId=0;
//控制图片上传
function sunbmitNewBbsAttachNew(type, location) {
	var upload = $("#postfile").val();
	if (upload == null || upload == "") {
		alert("请先点击“浏览”按钮选择要上传的附件！");
		return false;
	}
	$("#loading").show();// 动态加载小图标
	$.ajaxFileUpload({
		url : '../../handler/load/upload',
		secureuri : false,
		fileElementId : 'postfile',
		dataType : 'json',
		data : {
			fileType : type,
			location : location
		},
		success : function(data, status) {
			$("#loading").hide();
			if (data.ret) {
				if ($.isEmptyObject(data.data.attachId)) {
					if (pictureState==0) {//上传该图片之前没有上传封面图片
						pictureState=1;
						$("#uploadPicOrNot").remove();
					}else if(pictureState==1){//上传该图片之前有图片（以前的/最近一次上传的）
						deleteAttachmentNew(prePicId);
					}
					prePicId=data.data.attachId;
				}
				$("#attachment")
				.html(
						"<img id='"+attachesIds+"1' alt='"+data.data.fileName+"' src='../../upload/eduman/"+data.data.realName+"' />");
			}
	},
	error : function(data, status, e) {
		$("#loading").hide();
		alert(e);
	}
});
	return false;
}

//控制附件删除
function deleteAttachmentNew(id) {
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/attachment/delete",
		dataType : 'json',
		data : {
			attachId : id
		},
		success : function(data) {
			if (data.ret) {
				pictureState=0;
				$("#" + id + "1").remove();
			} else {
				alert("更改图片失败！");
			}
		}
	});
}