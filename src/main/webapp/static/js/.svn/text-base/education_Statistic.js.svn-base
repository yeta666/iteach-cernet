var announcementInfo;
$(document).ready(function() {
	// getCookie();
	ShowColumn();
	$('#edumanStatistic').attr("class", "active");
	// getReply(userId);
	// backTotop();
	
	timepicker("newstartTime");
	timepicker("newendTime");
	clickItem("#student-course a");
	clickItem("#course-time a");
	clickItem("#student-grade a");
	clickItem("#course-select a");
	clickItem("#centralize a");
	clickItem("#addQu",findAllSchool);
	clickItem("a[id^='select-info']");
	clickItem("a[name='return']");
	clickItem("a[id^='look-up-detail']");
	clickItem("#teacher-tutor a");
	$("#submit").click(submit);
	
	//添加或删除课程
	clickItem("#addButton",findAllSchool);
//	clickItem("#all-school",findAllSchool);
//	showStudyprogressInfo();
//	showStudySta();
});
/**
 * 导航栏点击事件监听
 * 
 * @author ZhangXin
 */
function clickItem(location,callback) {
	$(location).click(function(e) {
		e.preventDefault();
		$(this).tab('show');
		callback();
	});
}
/**
 * 获取所有的学校
 */
function findAllSchool()
{
	var param = {type:3};
	var url = "../../handler/department/viewDepartments";
	AjaxJson(url, param, showSchool);
}
/**
 * 展示所有学校
 * @param data
 */
function showSchool(data)
{
	var data = data.data.departments;
	var Html = "<option value='-1'>选择学校</option>";
	for(var i = 0;i<data.length;i++)
	{
		Html +="<option value="+data[i].depaId+">"+data[i].depaName+"</option>";
	}
	$("#all-school").html(Html);
	$("#all-school").change(function(){
		var param = {departId:$(this).val()};
		//获取所有课程
		AjaxJson("../../handler/course/viewCourseList",param,showCourse);
	});
}
/**
 * 显示所有课程（具体学校的）
 * @param data
 * @returns
 */
function showCourse(data)
{
	var data = data.data.courses;
	var HTML="<option value='-1'>选择课程</option>";
	for(var i = 0;i<data.length;i++)
	{
		HTML+="<option value="+data[i].courseId+">"+data[i].courseName+"</option>";
	}
	$("#all-course").html(HTML);
}
/**
 * 提交时间
 */
function submit()
{
	if($("#all-school").val()<0)
	{
		alert("请选择学校");
		return;
	}
	if($("#all-course").val()<0)
	{
		alert("请选择课程");
		return;
	}
	var timeStart = new Date($("#newstartTime").val());
	var timeEnd = new Date($("#newendTime").val());
	var param={
			csinCourId:$("#all-school").val(),
			csinDepaId:$("#all-course").val(),
			csinOpentime:timeStart,
			csinClosetime:timeEnd,
			csinRemark:$("#remark").val()
			};
	AjaxJson("../../handler/course/setTimeOfSelectCourse", param, timeSubmited);
}
/**
 * 提交完成
 * @param data
 */
function timeSubmited(data)
{
	alert("ok");
}



/**
 * 简化ajax
 */
function AjaxJson(url,param,success)
{
	$.ajax({
		type : "post",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		url : url,
		async : false,
		data : param,
		dataType : 'json',
		success : success
	});
}