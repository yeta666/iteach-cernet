var result ="";
$(document).ready(function(){
	//addCourse();
	judgeShowTeacher();
	homeCourselist();
	homeTeacherList();
	recommendCourseslist();
	/*
	$("#DesTClose").click(function(){
		$("#DesteachModal").modal('hide');
	});*/
	
});


function getVal(dom) {
	var qw = $(dom).attr('name');
	$("#homeLearningDes").html(qw);
	$('#DesModal').on('shown.bs.modal', function () {
		$('#myInput').focus();
	});
}

function judgeShowTeacher(){
	$.ajax({
		type:"post",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		url:'../../handler/sypaController/attainValueByEnName',
		data:{
			enName:"isShowTeacher"
		},
		dataType:'json',
		success:function(result){
			result1 = result.data.value;
			if(result1=="是"){
				$("#ShowTeacher").show();
			}else{
				$("#ShowTeacher").hide();
			}
		}
	});
}
/*function addCourse() {
	$.getJSON("../../handler/course/viewAllCourseList.do",{},function(data){
		var blockDivData = data.data.courseList;
		var HTML = "";
		if(blockDivData != null)
		for(var i=0;i<blockDivData.length;i++) {
			HTML += "<div class='courseBlockDiv-img1' name='"+blockDivData[i].courDescribe+"' value='"+blockDivData[i].courId+"' onclick='getVal(this);'>" +
				"<div class='front-div1'>"+
					"<div class='transparent_class transparent-background1'></div>" +
					"<div class='label label-inverse front-div-label1' id='viewcourse'>查看课程介绍</div>" +
				"</div>" +
				"<div class='blockDiv-innerDiv31'>" +
					"<img class='all-Img1' src='../../"+blockDivData[i].courImg+blockDivData[i].fileName+"'>" +
					"<div class='img-front-div' >" +
						"<div class='transparent_class transparent-img-background1'></div>" +
						"<div class='img-front-div-label1'>"+blockDivData[i].courName+"</div>" +
					"</div>" +
				"</div>"  +
				"<div class='blockDiv-innerDiv41'>" +
					"<span class='beginStudy'>点击查看&nbsp;<i class=''></i></span>" +
				"</div>" +
			"</div>";
		}
		$("#courseDiv").append(HTML);

		//	给‘开始学习’添加鼠标事件
		$(".courseBlockDiv-img1").mouseover(function(){
			$(this).find('.beginStudy').addClass("courseBlockDiv-img-hover");
			$(this).find('.beginStudy').find('i').addClass("icon-circle-arrow-right");
			$(this).find('.front-div1').css("display","block");
			$(this).find('.img-front-div').css("display","none");
		}).mouseout(function(){
			$(this).find('.beginStudy').removeClass("courseBlockDiv-img-hover");
			$(this).find('.beginStudy').find('i').removeClass("icon-circle-arrow-right");
			$(this).find('.front-div1').css("display","none");
			$(this).find('.img-front-div').css("display","block");
		});


	});
}*/

/**
 * 计算传入字符串长度
 */
function countlen(str){
	var len = 0;
	for(var i=0;i<str.length;i++){
		var c = str.charCodeAt(i);
		if((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)){
			len++;
		}
		else{
			len+=2;
		}
	}
	return len;
}

/**
 * 字符串超长后重新构建字标题符串
 */
function buildNewTitle(str){
	var nStr = "";
	var count = 0;
	for(var i=0;i<str.length;i++){
		var c = str.charCodeAt(i);                                          
		if((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)){
			if(count<27){
				nStr += str[i];
				count ++;
			}
		}
		else{
			if(count<13){
				nStr += str[i];
				count ++;
			}
		}
	}
	nStr = nStr+"...";
	return nStr;
}
/**
 * 字符串超长后重新构建字首页课程内容符串
 */
function buildNewStr(str){
	var nStr = "";
	var count = 0;
	for(var i=0;i<str.length;i++){
		var c = str.charCodeAt(i);                                          
		if((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)){
			if(count<140){
				nStr += str[i];
				count ++;
			}
		}
		else{
			if(count<70){
				nStr += str[i];
				count ++;
			}
		}
	}
	nStr = nStr+"...";
	return nStr;
}
/**
 * 最新课程介绍
 */
function homeCourselist() {//onclick='getVal(this);'
	var turnPage ="";//关于登录课程跳转的判断条件
	$.getJSON("../../handler/course/viewAllCourseList.do",{},function(data){
		var homeCourseData = data.data.courseList;
		var homehtml = "";
		if(homeCourseData != null){
			var coursenum = homeCourseData.length;
			for(var i=0;i<coursenum;i++) {
				if ($.cookie("user") == null || $.cookie("user") == "") {
					turnPage = "";
				}else{
					turnPage =" href=\"Learning_chooseCourse.html?courId="+homeCourseData[i].courId+"&selectedType=1&"+"&firstCol=2&secondCol=16\"";
				}
				homehtml+="<div class=\"portfolio1\" data-cat=\"logo\" style=\"display: inline-block; opacity: 1;\"><div  class=\"portfolio-wrapper\" name='"+homeCourseData[i].courDescribe+"' onclick='getVal(this);' data-toggle=\"modal\" data-target=\"#DesModal\" >"
				+"<a "+turnPage+" class=\"b-link-stripe b-animate-go  thickbox\"> <img class=\"sethome_pic\" src=\"../../"+homeCourseData[i].courImg+homeCourseData[i].fileName+"\" /><div class=\"b-wrapper\">"
				+"<h2 class=\"b-animate b-from-left  b-delay03\"><img src=\"../img/homeImages/set.png\" alt=\"\" /></h2></div></a>";
				var gotcoure = homeCourseData[i].courName;
				var op = countlen(gotcoure);
				if(op<29){
					homehtml += "<div class=\"blog-right-l\"><h5><a href=\"#\">"+gotcoure+"</a></h5><p class=\"setWord\">";
				}
				else{
					var nString =  buildNewTitle(gotcoure);
					homehtml += "<div class=\"blog-right-l\"><h5><a href=\"#\">"+nString+"</a></h5><p class=\"setWord\">";
				}
				var gotDescribe = homeCourseData[i].courDescribe;
				var gd = countlen(gotDescribe);
				if(gd<200){
					homehtml += gotDescribe+"</p></div></div></div>";
				}
				else{
					var ndString =  buildNewStr(gotDescribe);
					homehtml += ndString+"</p></div></div></div>";
				}


			}
			/*if(i % 3 ==0 && i==0){//开始新的一行
					homehtml += "<div class=\"row-fluid\">";
				}else if(i % 3 ==0 && i>0){
					homehtml += "</div><div class=\"row-fluid\">";
				}*/
			/*homehtml += "<div class=\"span4 well\"><img src=\"../../"+homeCourseData[i].courImg+homeCourseData[i].fileName+"\" class=\"pop\" coursedes=\""+homeCourseData[i].courDescribe+"\""
				+ " coursetitle=\""+homeCourseData[i].courName+"\" data-toogle=\"popover\" data-placement=\"top\" data-content=\"领域："+homeCourseData[i].courCateIds+"<br />学分："+homeCourseData[i].courCredit+"<br />"
				+"主讲教师："+homeCourseData[i].courTeacherIds+"<br />学习人数："+homeCourseData[i].courseStuCount+"\""
				+ " title data-original-title=\""+homeCourseData[i].courName+"\" data-trigger=\"hover\" data-html=\"true\" />";
				var gotcoure = homeCourseData[i].courName;
				var op = countlen(gotcoure);
				if(op<29){
					homehtml += "<div class=\"coursetile\">"+gotcoure+"</div>";
				}
				else{
					var nString =  buildNewStr(gotcoure);
					homehtml += "<div class=\"coursetile\">"+nString+"</div>";
				}
				homehtml += "</div>";*/
			/*}
			homehtml += "</div>";*/
		}
		$("#showTeacher").append(homehtml);
	});
}


/**
 * 猜你喜欢栏目
 */
function recommendCourseslist() {
	var turnPage = "";//登录与否的课程跳转
	$.ajax({
		type:"post",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		url:'../../handler/courseReom',
		data:{
		},
		dataType:'json',
		success:function(result){
			if(result.ret){
				var recommendCourseData= result.data.result;
				var homehtml = "";
				if(recommendCourseData != null){
					var recomle = "";
					if(recommendCourseData.length<8){
						recomle = recommendCourseData.length;
					}else{
						recomle = 8;
					}
					for(var i=0;i<recomle;i++) {//替换234行的引号部分//recommendCourseData[i].courImg+recommendCourseData[i].fileName
						if ($.cookie("user") == null || $.cookie("user") == "") {
							turnPage = "";
						}else{
							turnPage =" href=\"Learning_chooseCourse.html?courId="+recommendCourseData[i].courId+"&selectedType=1&"+"&firstCol=2&secondCol=16\"";
						}
						homehtml+="<div class=\"portfolio1\" data-cat=\"logo\" style=\"display: inline-block; opacity: 1;\"><div  class=\"portfolio-wrapper\" name='"+recommendCourseData[i].courDescribe+"' onclick='getVal(this);' data-toggle=\"modal\" data-target=\"#DesModal\" >"
						+"<a "+turnPage+" class=\"b-link-stripe b-animate-go  thickbox\"> <img class=\"sethome_pic\" src=\"../../"+"static/img/homeImages/recourseimg.jpg"+"\" /><div class=\"b-wrapper\">"
						+"<h2 class=\"b-animate b-from-left  b-delay03\"><img src=\"../img/homeImages/set.png\" alt=\"\" /></h2></div></a>";
						var gotcoure = recommendCourseData[i].courName;
						var op = countlen(gotcoure);
						if(op<29){
							homehtml += "<div class=\"blog-right-l\"><h5><a href=\"#\">"+gotcoure+"</a></h5><p class=\"setWord\">";
						}
						else{
							var nString =  buildNewTitle(gotcoure);
							homehtml += "<div class=\"blog-right-l\"><h5><a href=\"#\">"+nString+"</a></h5><p class=\"setWord\">";
						}
						var gotDescribe = recommendCourseData[i].courDescribe;
						var gd = countlen(gotDescribe);
						if(gd<200){
							homehtml += gotDescribe+"</p></div></div></div>";
						}
						else{
							var ndString =  buildNewStr(gotDescribe);
							homehtml += ndString+"</p></div></div></div>";
						}


					}
				}
				$("#recommendCourses").append(homehtml);
			}else{
				alert("数据加载出错，请联系管理员！");
				return ;
			}
		
		}
	});
	
	
	
/*	$.getJSON("../../handler/indexCourseReom",{},function(data){
		alert("!!@");
		var homeCourseData = data.data.courseList;
		var homehtml = "";
		if(homeCourseData != null){
			var coursenum = homeCourseData.length;
			for(var i=0;i<coursenum;i++) {
				homehtml+="<div class=\"portfolio1\" data-cat=\"logo\" style=\"display: inline-block; opacity: 1;\"><div  class=\"portfolio-wrapper\" name='"+homeCourseData[i].courDescribe+"' onclick='getVal(this);' data-toggle=\"modal\" data-target=\"#DesModal\" >"
				+"<a  class=\"b-link-stripe b-animate-go  thickbox\"> <img class=\"sethome_pic\" src=\"../../"+homeCourseData[i].courImg+homeCourseData[i].fileName+"\" /><div class=\"b-wrapper\">"
				+"<h2 class=\"b-animate b-from-left  b-delay03\"><img src=\"../img/homeImages/set.png\" alt=\"\" /></h2></div></a>";
				var gotcoure = homeCourseData[i].courName;
				var op = countlen(gotcoure);
				if(op<29){
					homehtml += "<div class=\"blog-right-l\"><h5><a href=\"#\">"+gotcoure+"</a></h5><p>";
				}
				else{
					var nString =  buildNewTitle(gotcoure);
					homehtml += "<div class=\"blog-right-l\"><h5><a href=\"#\">"+nString+"</a></h5><p>";
				}
				var gotDescribe = homeCourseData[i].courDescribe;
				var gd = countlen(gotDescribe);
				if(gd<200){
					homehtml += gotDescribe+"</p></div></div></div>";
				}
				else{
					var ndString =  buildNewStr(gotDescribe);
					homehtml += ndString+"</p></div></div></div>";
				}


			}
		}
		$("#recommendCourses").append(homehtml);
	});*/
}


/**
 * 名师简介 
 */
function homeTeacherList(){
	$.ajax({
		type:"post",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		url:'../../handler/user/viewMajorTeachers.do',
		data:{
			teacherId:-1
		},
		dataType:'json',
		success:function(result){
			var homeTeacherData = result.data.users;
			var homehtml = "";
			if(homeTeacherData != null){
				//var coursenum = homeTeacherData.length;
				for(var i=0;i<8&&i<homeTeacherData.length;i++) {
					if(i % 4 ==0 && i==0){//开始新的一行
						homehtml += "<div class=\"row-fluid\">";
					}else if(i % 4 ==0 && i>0){
						homehtml += "</div><div class=\"row-fluid\">";
					}
					homehtml += "<div class=\"span3 well\">" +

					//"<img src=\"../../static/img/course.png"+"\" " +
					"<img src=\"../../"+homeTeacherData[i].teacherCoverLocation+homeTeacherData[i].teacherCoverFileName+"\" " +
					"class=\"pops\" coursedes=\""+homeTeacherData[i].teacherRemark+"\""
					+ " teachcourse=\""+homeTeacherData[i].courseNames+"\"coursetitle=\""+homeTeacherData[i].teacherName+"\" " +
					"data-toogle=\"popover\" data-placement=\"top\" data-content=\"教师名称："+homeTeacherData[i].teacherName+"<br />所属学校："+homeTeacherData[i].schoolName+"<br />"+"\""
					+ " title data-original-title=\""+homeTeacherData[i].teacherName+"\" data-trigger=\"hover\" data-html=\"true\" />";
					var gotcoure = homeTeacherData[i].teacherName;
					homehtml += "<div class=\"coursetile\">"+gotcoure+"</div>";
					homehtml += "</div>";
				}
				homehtml += "</div>";
			}
			$("#teachertest").append(homehtml);
			$(".pops").popover().click(function(){
				
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
