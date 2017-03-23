$(document).ready(function(){
	$("#course-teacher").attr("value",userName);
	$("#course-teacher").attr('disabled','disabled');
	$("#course-register").click(function(){
		registerCourse();
	});
});

function registerCourse()
{
	var courseName = $("#course-name").attr("value");
	if(""==courseName||courseName.length>16)
	{
		alert("请输入正确的课程名");
		return;
	}
	var courseCore = $("#course-core").attr("value");
	if(courseCore==""||courseName.length>16)
	{
		alert("请输入正确的课程代码");
		return;
	}
	
	alert(courseName+"---"+courseCore+"---"+userId);
	$.ajax({
		type : "POST",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",// 发送信息至服务器时内容编码类型
		url : '../../handler/teacher/registerCourse',
		async : false, // 需要同步请求数据
		data : {
			courseName:courseName,
			courseCore:courseCore,
			userId:userId
		},
		dataType : 'json',
		success : function(data) {
			alert(data.data.insertResult.info);
		}
	});
}