var colIds="";
$(document).ready(function(){
	colIds = urlColHtml();
	//alert("colIds:"+colIds);
	//添加course
	ShowColumn();
	showmycourse();
	$('#learning').attr("class", "active");
});


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
 * 字符串超长后重新构建字符串
 */
function buildNewStr(str){
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

//显示我的课程   @author:Mryang
function showmycourse(){
	$.getJSON("../../handler/course/viewCourseList.do",{},function(data){
		var mycourse = data.data.courseList;
		var mycoursehtml = "";
		if(mycourse != null && mycourse.length>0){
			var coursenum = mycourse.length;
			for(var i=0;i<=coursenum;i++) {
				if(i % 3 ==0 && i==0){//开始新的一行
					mycoursehtml += "<div class=\"row\" style=\"margin-bottom: 20px;margin-right: 0px;\"><ul style=\"list-style-type:none\">";
				}else if(i % 3 ==0 && i>0){
					mycoursehtml += "</ul></div><div class=\"row\" style=\"margin-bottom: 20px;margin-right: 0px;\"><ul style=\"list-style-type:none\">";
				}
				if(i==0){
					mycoursehtml +="<li class=\"col-xs-4\">"+
					"<div class=\"thumbnail\">"+
					"<a href=\"Learning_showAllCourse.html?firstCol=2&secondCol=16\"><img src=\"../../upload/eduman/addcourse.jpg\"  title=\"点击开始选择课程学习\" id=\"addcourseimg\" /></a>"+
					"</div>"+
					"<div class=\"caption  text-center\">"+
					"	<a href=\"Learning_showAllCourse.html?firstCol=2&secondCol=16\" class=\"btn btn-info\"><i class=\"fa fa-caret-square-o-right\"></i> 添加课程</a>"+
					"</div>"+
					"</li>";
				}else if(i>0){
					mycoursehtml +="<li class=\"col-xs-4\">"+
					"<div class=\"thumbnail\">"+//
					"<a href=\"Learning_chooseCourse.html?"+colIds+"&courId="+mycourse[i-1].courId+"&selectedType=1"+"\"><img src=\"../../"+mycourse[i-1].courImg+mycourse[i-1].fileName+"\"  class=\"pop\" courid=\""+mycourse[i-1].courId+"\" coursedes=\""+mycourse[i-1].courDescribe+"\""
					+ " coursetitle=\""+mycourse[i-1].courName+"\" data-toogle=\"popover\" data-placement=\"top\" data-content=\""
					+"主讲："+mycourse[i-1].courTeacherIds+"\""
					+ " title data-original-title=\""+mycourse[i-1].courName+"\" data-trigger=\"hover\" data-html=\"true\" /></a>";
					//+ "<div class=\"coursetile\">"+mycourse[i-1].courName+"</div>"
					
					var gotcoure = mycourse[i-1].courName;
					var op = countlen(gotcoure);
					if(op<29){
						mycoursehtml += "<div class=\"coursetile text-center\"><font color=\"#00BFFF\">"+gotcoure+"</div>";
					}
					else{
						var nString =  buildNewStr(gotcoure);
						mycoursehtml += "<div class=\"coursetile text-center\"><font color=\"#00BFFF\">"+nString+"</div>";
					}				
					mycoursehtml+="</div>"+
					"<div class=\"caption  text-center\">"+	
					"	<a href=\"Learning_chooseCourse.html?"+colIds+"&courId="+mycourse[i-1].courId+"&selectedType=1"+"\" class=\"btn btn-info\"><i class=\"fa fa-caret-square-o-right\"></i> 开始课程学习</a>"+
					"</div>"+
					"</li>";
				}
			}
			mycoursehtml +="</ul></div>";
		}else{
			mycoursehtml += "<div class=\"row\"><ul style=\"list-style-type:none\">";
			mycoursehtml +="<li class=\"col-xs-4\">"+
			"<div class=\"thumbnail\">"+
			"<a href=\"Learning_showAllCourse.html?firstCol=2&secondCol=16\"><img src=\"../../upload/eduman/addcourse.jpg\"  title=\"点击开始选择课程学习\" /></a>"+
			"</div>"+
			"<div class=\"caption  text-center\">"+
			"	<a href=\"Learning_showAllCourse.html?firstCol=2&secondCol=16\" class=\"btn btn-info\"><i class=\"fa fa-caret-square-o-right\"></i> 添加课程</a>"+
			"</div>"+
			"</li></ul></div>";
		}
		
		$("#myCourse").append(mycoursehtml);
		$(".pop ").popover();
		$("#addcourseimg").click(function(){
			window.location.href = 'Learning_showAllCourse.html?firstCol=2&secondCol=16';
		});
		
	});
}



function addCourse() {
	$.getJSON("../../handler/course/viewCourseList.do",{},function(data){
		var blockDivData = data.data.courseList;
		var HTML = "";
		if(blockDivData.length>0){
			for(var i=0;i<blockDivData.length;i++) {
				//alert(blockDivData[i].courImg);
				HTML += "<div class='courseBlockDiv-img' value='"+blockDivData[i].courId+"'>" +
				"<div class='front-div'>" +
				"<div class='transparent_class transparent-background'></div>" +
				"<div class='label label-inverse front-div-label' id='viewcourse'>查看课程</div>" +
				"</div>" +
				"<div class='blockDiv-innerDiv3'>" +
				"<img class='all-Img' src='../../"+blockDivData[i].courImg+blockDivData[i].fileName+"'>" +
				"<div class='img-front-div'>" +
				"<div class='transparent_class transparent-img-background'></div>" +
				"<div class='img-front-div-label'>"+blockDivData[i].courName+"</div>" +
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
		}
		$("#courseDiv").append(HTML);

		//给‘添加课程’添加鼠标事件
		$(".courseBlockDiv-font").mouseover(function(){
			$(this).addClass("courseBlockDiv-font-hover");
		}).mouseout(function(){
			$(this).removeClass("courseBlockDiv-font-hover");
		});

		//	给‘开始学习’添加鼠标事件
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
		}).click(function(){
			window.location.href = 'Learning_chooseCourse.html?courId='+$(this).attr("value")+'&selectedType=1';
		});

		$("#control-add").click(function(){
			window.location.href = 'Learning_showAllCourse.html';//加备注id，控制栏目active
		});

	});
}



