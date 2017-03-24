//缓存已选课程信息
var selectedCourse;

$(document).ready(
		function(){

			ShowColumn();
			//获取已选课程信息
			getInfo(userId);
			$(".courseBlockDiv-img").mouseover(function(){
				$(this).find('.beginStudy').addClass("courseBlockDiv-img-hover");
				$(this).find('.beginStudy').find('i').addClass("icon-circle-arrow-right");
				$(this).find('.front-div').css("display","block");
				$(this).find('.img-front-div').css("display","none");
			}).mouseout(function(){
				$(this).find('.beginStudy').removeClass("courseBlockDiv-img-hover");
				$(this).find('.beginStudy').find('i').removeClass("icon-circle-arrow-right");
				$(this).find('.front-div').css("display","none");
				$(this).find('.img-front-div').css("display","block");
			});

			$("div[id^='selectedcourse-block']").click(
					function(){
						var id = $(this).attr("id");
						id = id.split("selectedcourse-block")[1];
						window.location="../html/Learning_courseDetail.html?courseId="+id;
					}
			);
		}
);

function getInfo(id)
{
	$.ajax({
		type:"post",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		url:'../../handler/sc/student',
		async:false,
		data:{
			userId:id
		},
		dataType:'json',
		success:function(ba){
			selectedCourse = ba.data.result;
			selectedCourseShow(selectedCourse);
			/*			for(var i = 0;i < selectedCourse.length;i++)
			{
				ht +="<tr><td>"+(i+1)+"</td><td>"+selectedCourse[i].courName+"</td><td><a name='"+i+"' id='course_info' href='#myModal' class='btn btn-info' " +
				"data-toggle='modal'>课程详情</a></td></tr>";
			}
			ht+="</tbody>";
			$("#table_course").html(ht);
			$("#course_info").click(function(){ 
				var t = parseInt(this.name);
				$("p[class='course']").html(selectedCourse[t].courName);
				$("p[class='teacher']").html(selectedCourse[t].courTeacherIds);
			});*/
		}
	});
}
//选课
function selectCourse(userId,courseId)
{
	$.ajax({
		type:"post",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		url:'../../handler/sc/selectCourse',
		async:false,
		data:{
			userId:userId,
			courseId:courseId
		},
		dataType:'json',
		success:function(ba){
			alert(ba.data.selCourInfo.insertSelCourInfo);
			var status = ba.data.selCourInfo.status;
			switch(status)
			{
			case '1':break;
			case '2':break;
			case '3':break;
			case '4':flushSelectCourse(ba.data.selCourInfo.data);break;
			default :break;
			}
		}
	});
}


//退课
function dropCourse(userId,reSelectId)
{
	$.ajax({
		type:"post",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		url:'../../handler/sc/deleteSelCou',
		async:false,
		data:{
			userId:userId,
			reSelectId:reSelectId
		},
		dataType:'json',
		success:function(ba){
			alert(ba.data.dropCourseInfo.deleteSelCourInfo);
			var status = ba.data.dropCourseInfo.status;
			switch(status)
			{
			case 1:break;
			case 2:flushDropCourse(ba.data.dropCourseInfo.data);break;
			default : break;
			}
		}
	});
}

/**
 * 已经选择好的课程
 */
function selectedCourseShow(selectedCourse) {

	var HTML = "";
	for(var i=0;i<selectedCourse.length;) {
		HTML += "<div id='selectedcourse-block"+selectedCourse[i].rscoId+"' class='courseBlockDiv-img'>" +
		"<div class='front-div'>" +
		"<div class='transparent_class transparent-background'></div>" +
		"<div class='label label-inverse front-div-label'>查看课程</div>" +
		"</div>" +
		"<div class='blockDiv-innerDiv3'>" +
		"<img src='../img/icon/big"+(++i)+".jpg'>" +
		"<div class='img-front-div'>" +
		"<div class='transparent_class transparent-img-background'></div>" +
		"<div class='img-front-div-label'>"+selectedCourse[i-1].courName+"</div>" +
		"</div>" +
		"</div>" +
		"<div class='progress' style='margin:5px;height:10px;'>" +
		"<div class='bar studyProgress' style='width:"+(i*10)+"%;'></div>" +
		"</div>" +
		"<div class='blockDiv-innerDiv4'>" +
		"<span class='beginStudy'>开始学习&nbsp;<i class=''></i></span>" +
		"</div>" +
		"</div>";
	}

	$("#courseDiv").append(HTML);
}