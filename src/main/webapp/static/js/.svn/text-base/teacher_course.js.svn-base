$(document).ready(function() {
	AjaxJson("../../handler/teacher/getTeacherCourseInfo",{userId:userId},BackOfCourseInfo);
});

var courseId=0,isMainorTour=0;
function BackOfCourseInfo(backinfo)
{
	var data = backinfo.data.data;
	var Html="";
	if(data.length<=0)
	{
		Html +="<tr class='alert'><td colspan='8'>"+
		"<button type='button' class='close' data-dismiss='alert'>&times;</button>"+
		"<span style='color:red;font-size:18px;'>提示: 您还没有所教授的课程...</span>"+
		"</td></tr>";
		$("#teachcourse-info").html(Html);
	}
	else
	{
		var index = 1;
		for(var i = 0 ;i<data.length;i++)
		{
			isMainorTour=0;
			if(data[i].deleteInfo===false)
			{
				var isCreator = false;
				var verify;
				if(data[i].cour_create_userid==userId)
				{
					isCreator = true;
				}
				var isVerify = false;
				if(data[i].cour_verify==0)
				{
					verify = "审核中";
				}else if(data[i].cour_verify==1)
				{
					verify = "审核通过";
					isVerify = true;
				}
				else{
					verify="未通过审核";
				}
				var te = new RegExp("^.*,"+userId+",.*");
				var isMain = false;
				var isMent = false;
				var des = "";
				if(data[i].cour_mentro_teaids!=undefined&&data[i].cour_mentro_teaids.match(te))
				{
					isMent = true;
					if(isMain)
						des+="   辅导";
					else
						des+="辅导";
					isMainorTour=2;
				}
				if(data[i].cour_teacher_ids!=undefined&&te.test(data[i].cour_teacher_ids))
				{
					isMain = true;
					des+="主讲";
					isMainorTour=1;
				}
				Html += "<tr><td>"+index+"</td><td>"+data[i].cour_name+"</td>" +
				"<td>"+data[i].cour_code+"</td>" +
				"<td>"+des+"</td>"+
				"<td>"+(isCreator?'是':'否')+"</td>"+
				"<td>"+verify+"</td>"+
				"<td>"+(isVerify?data[i].count:'')+"</td>";
				var countp=isVerify?data[i].count:0;
				var strdsa=urlColHtml();
				if (isMainorTour==0 ||  countp==0) {
					Html +="<td> — </td>";
				}else{
					Html +="<td><button type='button' " +
					"onclick=\"location.href='my_Tutor_selectCourseSta.html?courseId="+data[i].courseId+"&isMainorTour="+isMainorTour+"&"+strdsa+"'\"" +
					"class='btn btn-mini btn-info');>查看</button></td>";
				}
				Html +="</tr>";
				index = index +1;
			}
		}
		$("#teachcourse-info").html(Html);
	}

}