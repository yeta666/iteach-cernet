$(document).ready(function(){
//	$("#upload").click(function(){
//		importUserExcle();
//	});
});

function importUserExcel() {
	var fileName = $("#file").val();
	var suffix = fileName.slice(fileName.lastIndexOf(".") + 1);

	if (fileName == "") {
		msg = "上传文件不能为空";
		alert(msg);
		return false;
	}
	
	if (!(suffix == "xls" || suffix == "xlsx")) {
		msg = "文件格式错误，只能上传excel文件";
		alert(msg);
		return false;
	} else {
		var type = 1;
		$.ajaxFileUpload({
			url : "../../handler/adminUserInfo/importUserInfo",
			secureuri : false,
			fileElementId : "file",
			dataType : "json",
			success : function(data, status) {
				alert(data.data);
			},
			error : function(data, status, e) {
				alert("上传失败!!");
			}
		});
	}
}