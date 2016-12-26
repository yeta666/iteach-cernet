$(document).ready(function(){
	AjaxJson("../../handler/teacher/getTeacherCourseInfo",{userId:userId},BackOfCourseInfo);
});
/**
 * 显示老师授课信息
 * @param backinfo
 */
function BackOfCourseInfo(backinfo)
{
	var data = backinfo.data.data;
	var Html="";
	alert(data.length);
	if(data.length<=0)
	{
		HTML +="<div class='alert'>"+
		"<button type='button' class='close' data-dismiss='alert'>&times;</button>"+
		"<strong>提示!</strong> 没有符合您所需查询数据..."+
		"</div>";
		$("#waring-nodata").html(HTML);
	}
	else
	{
		for(var i = 0 ;i<data.length;i++)
		{
			Html += "<tr><td>"+data[i].cour_name+"</td><td>"+data[i].cour_code+"</td><td>"+data[i].count+"</td></tr>";
		}
		$("#teachcourse-info").html(Html);
	}

}