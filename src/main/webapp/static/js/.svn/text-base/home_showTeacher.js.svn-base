$(document).ready(function(){
	showTeacher();
	$("#DesClose").click(function(){
		$("#DesteachModal").modal('hide');
	});
});
function showTeacher(){
	$.ajax({
		type:"post",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		url:'../../handler/user/viewMajorTeachers.do',
		data:{teacherId:-1
			},
		dataType:'json',
		success:function(result){
			var homeTeacherData = result.data.users;
			var homehtml = "";
			if(homeTeacherData != null){
				//var coursenum = homeTeacherData.length;
			
				for(var i=0;i<homeTeacherData.length;i++) {
					if(i % 4 ==0 && i==0){//开始新的一行
						homehtml += "<div class=\"row-fluid\">";
					}else if(i % 4 ==0 && i>0){
						homehtml += "</div><div class=\"row-fluid\">";
					}
					homehtml += "<div class=\"span3 well\">" +
					//"<img src=\"../../static/img/course.png"+"\" " +
					"<img src=\"../../"+homeTeacherData[i].teacherCoverLocation+homeTeacherData[i].teacherCoverFileName+"\" " +
									"class=\"pop\" coursedes=\""+homeTeacherData[i].teacherRemark+"\""
									+ " teachcourse=\""+homeTeacherData[i].courseNames+"\"coursetitle=\""+homeTeacherData[i].teacherName+"\" " +
							"data-toogle=\"popover\" data-placement=\"top\" data-content=\"主讲教师："+homeTeacherData[i].teacherName+"<br />所属学校："+homeTeacherData[i].schoolName+"<br />"+"\""
					+ " title data-original-title=\""+homeTeacherData[i].teacherName+"\" data-trigger=\"hover\" data-html=\"true\" />";
					var gotcoure = homeTeacherData[i].teacherName;
					homehtml += "<div class=\"coursetile\">"+gotcoure+"</div>";
					homehtml += "</div>";
					
				}
				homehtml += "</div>";
			}
			$("#showALLTeacher").append(homehtml);
			$(".pop").popover().click(function(){
				var courseTitle = $(this).attr("coursetitle");
				var courseDes = $(this).attr('coursedes');
				var teacherCourse = $(this).attr('teachcourse');
				$("#teacherTitle").html(courseTitle);
				$("#homeLearningTeacherDes").html(courseDes);
				$("#homeLearningTeacherCourse").html(teacherCourse);
				$("#DesteachModal").modal({				
					'backdrop':true
				});
			});

		}
	});
}
