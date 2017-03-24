$(document).ready(function() {
	$("#impEducational").attr("href","#importModal");
	$("#impEducational").click(function(){
		$("#waring-data").attr("style","display:none;");
		$("#file").val("");
		if($.cookie("fileUploadStu") == null || $.cookie("fileUploadStu") == ""){
			$.cookie('fileUploadStu', '1'); //控制上传学生信息 唯一开启!!没有数据时，无需关闭。
		}
	});
	$("#upload").click(function(){
		importUserExcel($(this).attr("name"));
		
		//下面的id都找不到
		$("#waitFileUpload").modal({
			'backdrop':true
		});
		var $records = $('#waitTable');
		$records.empty().append( "<tr><td colspan=\"5\" class=\"tdcenter\"><img src=\"../img/octocat-spinner.gif\" />正在加载数据...</td></tr>");
		$('#waitFileUpload').modal('show');
	});
});



function importUserExcel(userType) {
	//要上传的文件名
	var fileName = $("#file").val();
	//要上传的文件名的后缀
	var suffix = fileName.slice(fileName.lastIndexOf(".") + 1);
	//如果没有选择要上传的文件，则出现错误提示
	if (fileName == "") {
		var Html = "<div class=\"alert alert-block alert-error fade in\">"+
		"<button data-dismiss=\"alert\" class=\"close\" type=\"button\">×</button>"+
		"<h4 class=\"alert-heading\">导入信息出现错误</h4>"+
		"<p id='error-info'>文件不能为空</p></div>";
		$("#waring-data").html(Html);
		$("#waring-data").removeAttr("style");
		return false;
	}
	//如果要上传的文件名的后缀不为xls，则出现错误提示
	if (!(suffix == "xls")) {
		var Html = "<div class=\"alert alert-block alert-error fade in\">"+
		"<button data-dismiss=\"alert\" class=\"close\" type=\"button\">×</button>"+
		"<h4 class=\"alert-heading\">导入信息出现错误</h4>"+
		"<p id='error-info'>文件格式不正确,以  .xls  结尾的Excel文件</p></div>";
		$("#waring-data").html(Html);
		$("#waring-data").removeAttr("style");
		return false;
	} else {
		var type = 1;
		$.ajaxFileUpload({
			url : "../../handler/adminUserInfo/importUserInfo",
			secureuri : false,
			fileElementId : "file",
			dataType : "json",
			data:{
				userType:userType
			},
			success : function(data, status) {
				var status = data.status;
				if(1==status)
				{
					$('#waitFileUpload').modal('hide');
					var Html = "<div class=\"alert alert-block alert-error fade in\">"+
					"<button data-dismiss=\"alert\" class=\"close\" type=\"button\">×</button>"+
					"<h4 class=\"alert-heading\">导入信息出现错误</h4>"+
					"<p id='error-info'>文件格式不正确,以  .xls  结尾的Excel文件</p></div>";
					$("#waring-data").html(Html);
					$("#waring-data").removeAttr("style");
					$.cookie("fileUploadStu", null, {
						path : '/'  //上传学生信息判定是否重复打开浏览器
					});
				}
				else if(2==status)
				{
					$("#waring-data").attr("style","display:none;");
					$('#waitFileUpload').modal('hide');
					$.cookie("fileUploadStu", null, {
						path : '/'  //上传学生信息判定是否重复打开浏览器
					});
					alert("导入数据成功");
					$("#search").trigger("click");
				}
				else if(3==status)
				{
					$('#waitFileUpload').modal('hide');
					var Html = "<div class=\"alert alert-block alert-error fade in\">"+
					"<button data-dismiss=\"alert\" class=\"close\" type=\"button\">×</button>"+
					"<h4 class=\"alert-heading\">导入信息出现错误</h4>"+
					"<p id='error-info'>导入的数据中存在错误的用户信息，我们已经为你返回所有错误数据，<a style='cursor: pointer;' " +
					"href='../../handler/adminUserInfo/downloadErrorUserInfo?fileName="+data.errorData+"'>点击这里下载</a>，修改之后重新导入即可。</p></div>";
					$("#waring-data").html(Html);
					$("#waring-data").removeAttr("style");
					$.cookie("fileUploadStu", null, {
						path : '/'  //上传学生信息判定是否重复打开浏览器
					});
				}
				else if(4 == status)
				{
					$('#waitFileUpload').modal('hide');
					var Html = "<div class=\"alert alert-block alert-error fade in\">"+
					"<button data-dismiss=\"alert\" class=\"close\" type=\"button\">×</button>"+
					"<h4 class=\"alert-heading\">导入信息出现错误</h4>"+
					"<p id='error-info'>请检查所上传的数据格式，<a style='cusor:pointer;' href='../../handler/adminUserInfo/downloadUserInfoModel?fileName=userInfoMode.zip&userType="+userType+"'>点击下载模板</a></p></div>";
					$("#waring-data").html(Html);
					$("#waring-data").removeAttr("style");
					$.cookie("fileUploadStu", null, {
						path : '/'  //上传学生信息判定是否重复打开浏览器
					});
				}
			},
			error : function(data, status, e) {
				$('#waitFileUpload').modal('hide');
				$.cookie("fileUploadStu", null, {
					path : '/'  //上传学生信息判定是否重复打开浏览器
				});
				alert("上传失败!!");
			}
		});
	}
}