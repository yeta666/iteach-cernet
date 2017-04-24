//缓存已选课程信息
var selectedCourse;

$(document).ready(
		function(){
			ShowColumn();
			//获取已选课程信息
			$('#Learning').attr("class", "active");
			getInfo(userId);
			//获取所有课程信息
			getCourse();
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
			
			$("#addcourse-block").click(
					function(){
						
						window.location="../html/Learning_showAllCourse.html";
					}
			);
			
			$("div[id^='selectedcourse-link']").click(
					function()
					{
						var id = $(this).attr("id");
						id = id.split("selectedcourse-link")[1];
						window.location="../html/Learning_courseDetail.html?courseId="+id;
					}
			);
			
			$("div[id^='newcourse-block']").click(
					function()
					{
						var id = $(this).attr("id");
						id = id.split("newcourse-block")[1];
						window.location="../html/Learning_selectCourseDetail.html?courseId="+id;
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
 * @author ZhangXin
 */
function selectedCourseShow(selectedCourse) {
	if(null == selectedCourse)
		return;
	var HTML = "";
	var length = 0;
	if(selectedCourse.length>5)
		length = 5;
	else
		length = selectedCourse.length;
	for(var i=0;i<length;) {
		HTML += "<div id='selectedcourse-link"+selectedCourse[i].rscoId+"' class='courseBlockDiv-img'>" +
					"<div class='front-div'>" +
						"<div class='transparent_class transparent-background'></div>" +
						"<div class='label label-inverse front-div-label'>查看课程</div>" +
					"</div>" +
					"<div class='blockDiv-innerDiv3'>" +
						"<img src='../img/icon/big"+(++i)+".jpg'>" +
//					"<img src='"+selectedCourse[i-1].courImg+"'>"+
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

/**
 * 显示所有课程信息
 * @param databack
 */
function showAllCourse(allCourse)
{
	if(null == allCourse)
		return;
	var length = 0;
	if(allCourse.length>6)
		length = 6;
	else
		length = allCourse.length;
	var HTML = "";
	for(var i=0;i<length;i++) {
		HTML += "<div id='newcourse-block"+allCourse[i].courId+"' class='courseBlockDiv-img' style='height: 125px;margin-top:50px;'>" +
					"<div class='front-div'>" +
						"<div class='transparent_class transparent-background'></div>" +
						"<div class='label label-inverse front-div-label'>查看课程</div>" +
					"</div>" +
					"<div class='blockDiv-innerDiv3'>" +
//						"<img src='../img/icon/big"+(++i)+".jpg'>" +
					"<img src='../img/icon/"+allCourse[i].courImg+"'>" +
						"<div class='img-front-div'>" +
							"<div class='transparent_class transparent-img-background'></div>" +
							"<div class='img-front-div-label'>"+allCourse[i].courName+"</div>" +
						"</div>" +
					"</div>" +
				"</div>";
	}
	
	$("#recommend-course").append(HTML);
}
//刷新选课后的课程目录
function flushSelectCourse(databack)
{
	selectedCourse = databack;
	var ht = "<thead><tr><th>编号</th><th>课程名</th><th></th></tr></thead><tbody>";
	for(var i = 0;i < selectedCourse.length;i++)
	{
		ht +="<tr><td>"+(i+1)+"</td><td>"+selectedCourse[i].courName+"</td><td><a name='"+i+"' id='course_info' href='#myModal' class='btn btn-info' " +
		"data-toggle='modal'>课程详情</a></td></tr>";
	}
	ht+="</tbody>";
	$("#table_course").html(ht);
	$("#table_course tr td a").click(function(){ 
		t = parseInt(this.name);
		alert(t);
		$("p[class='course']").html(selectedCourse[t].courName);
		$("p[class='teacher']").html(selectedCourse[t].courTeacherIds);
	});
}

function flushDropCourse(selectedCoruses)
{
	flushSelectCourse(selectedCoruses);
}

//获取课程信息
function getCourse()
{
	params = {
			"startTime" : "",
			"endTime" : "",
			"searchWord" : "",
			"recordPerPage" : 6
	};
	$.ajax({
		type:"post",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		url:'../../handler/sc/course',
		async:false,   
		data:{query:params},
		dataType:'json',
		success:function(result){
			var m = result.data.pageData;
			showAllCourse(m[0].data);
			/*for(var i = 0;i<m.length;i++)
			{
				if(i==0)
					ht += "<li class='active'><a data-toggle='tab' href='#l"+i+"'>"+m[i].courName+"</a></li>";
				else
					ht += "<li><a data-toggle='tab' href='#l"+i+"'>"+m[i].courName+"</a></li>";
				if(i==0)
					courdet += "<div id='l"+i+"' class='tab-pane active left-tab-div'><div class='page-header'>"
					+"<h2>"
					+"课程信息 <small>course info</small>"
					+"</h2>"
					+"</div>"
					+"<p id='courseinfo' class='text-info'>课程名称："+m[i].courName+"</p>"
					+"<p id='coursedis' class='text-info'>课程简介："+m[i].courDescribe+"</p>"
					+"<p id='coursecre' class='text-info'>课程学分："+m[i].courCredit+"</p>"
					+"</div>";
				else
					courdet += "<div id='l"+i+"' class='tab-pane left-tab-div'><div class='page-header'>"
					+"<h2>"
					+"课程信息 <small>course info</small>"
					+"</h2>"
					+"</div>"
					+"<p id='courseinfo' class='text-info'>课程名称："+m[i].courName+"</p>"
					+"<p id='coursedis' class='text-info'>课程简介："+m[i].courDescribe+"</p>"
					+"<p id='coursecre' class='text-info'>课程学分："+m[i].courCredit+"</p>"
					+"</div>";
			}
			$("ul#courses").html(ht);
			$("div#coursedet").html(courdet);*/
		}
	});
}