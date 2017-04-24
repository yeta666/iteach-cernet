var result ="";
$(document).ready(function(){
	homeCourselist();	//加载名师讲堂
	recommendCourseslist();	//猜你喜欢
	judgeShowTeacher();//判断是否显示主讲教师	
	
	//过滤效果
	var $container = $('#gallery');
	$container.isotope({
		itemSelector: '.item',
		animationOptions: {
			duration: 750,
			easing: 'linear',
			queue: false
		}
	});

	// filter items when filter link is clicked
	$('#filters a').click(function() {
		var selector = $(this).attr('data-filter');
		$container.isotope({
			filter: selector
		});
		return false;
	});
	
	//开始学习按钮
	$("#start_learning_btn").click(function(){
		//判断是否已经登陆
		if ($.cookie("user") == null || $.cookie("user") == "") {
			//cookie没有保存user
			//关闭当前modal
			$("#courseModal").modal("hide");
			alert("请先登录");
		}else{
			//cookie保存了user
			$("#start_learning_btn").attr("href", "Learning_chooseCourse.html?courId="+$("#courId").html()+"&selectedType=1&firstCol=2&secondCol=16");
		}
	});
});

//判断是否显示主讲教师
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
			if(result1 == "是"){
				homeTeacherList();	//显示主讲教师
			}else{
				
			}
		}
	});
}
/**
 * 名师讲堂数据
 */
function homeCourselist() {
	//获取名师讲堂数据
	$.getJSON("../../handler/course/viewAllCourseList.do", {}, function(data) {
		var courseList = data.data.courseList;
		for(var i = 0; i < courseList.length; i++) {
			$('<div class="famous_teacher_course item" courCateIds="' + courseList[i].courCateIds + '" courCredit="' +
				courseList[i].courCredit + '" courDescribe="' + courseList[i].courDescribe + '" courTeacherIds="' +
				courseList[i].courTeacherIds + '" courseStuCount="' + courseList[i].courseStuCount + '" courId="' +
				courseList[i].courId + '"><a href="#courseModal" data-toggle="modal"> <img src="../../' +
				courseList[i].courImg + courseList[i].fileName + '" alt="' + courseList[i].courName + '" /></a><p>' +
				courseList[i].courName + '</p></div>').click(function() {
				//点击课程出现课程简介
				//为模态框加载数据
				$("#courCateIds").html($(this).attr("courCateIds"));
				$("#courCredit").html($(this).attr("courCredit"));
				$("#courDescribe").html($(this).attr("courDescribe"));
				$("#courTeacherIds").html($(this).attr("courTeacherIds"));
				$("#courseStuCount").html($(this).attr("courseStuCount"));
				$("#courId").html($(this).attr("courId"));
				$("#courName").html(this.childNodes[1].innerHTML);
				document.getElementById("courImg").src = this.childNodes[0].childNodes[1].src;
			}).appendTo($("#gallery"));
		}
	});
}
/**
 * 猜你喜欢栏目
 */
function recommendCourseslist() {
	//获取userId
	//console.log(userId);
	
	//获取猜你喜欢数据
	$.ajax({
		type: "post",
		contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		//url:'../../handler/courseReom',
		url: "http://127.0.0.1:8082/recom/courseRecom",
		dataType: "json",
		data: {
			"userId": userId
		},
		success: function(data) {
			if(!data.ret) {
				console.log("数据加载出错，请联系管理员！");
				return;
			}
			var courseList = data.data.result;
			for(var i = 0; i < courseList.length; i++) {
				$('<div class="guess_you_like item" courCateIds="' + courseList[i].courCateIds + '" courCredit="' +
					courseList[i].courCredit + '" courDescribe="' + courseList[i].courDescribe + '" courTeacherIds="' +
					courseList[i].courTeacherIds + '" courseStuCount="' + courseList[i].courChoosedNum + '" courId="' +
					courseList[i].courId + '"><a href="#courseModal" data-toggle="modal"> <img src="../../static/img/homeImages/recourseimg.jpg" alt="' +
					courseList[i].courName + '" /></a><p>' +
					courseList[i].courName + '</p></div>').click(function() {
					//点击课程出现课程简介
					//为模态框加载数据
					$("#courCateIds").html($(this).attr("courCateIds"));
					$("#courCredit").html($(this).attr("courCredit"));
					$("#courDescribe").html($(this).attr("courDescribe"));
					$("#courTeacherIds").html($(this).attr("courTeacherIds"));
					$("#courseStuCount").html($(this).attr("courseStuCount"));
					$("#courId").html($(this).attr("courId"));
					$("#courName").html(this.childNodes[1].innerHTML);
					document.getElementById("courImg").src = this.childNodes[0].childNodes[1].src;
				}).appendTo($("#gallery"));
			}
		}
	});
}

//这个功能暂时没用
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
