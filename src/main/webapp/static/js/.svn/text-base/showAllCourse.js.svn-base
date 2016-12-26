//缓存已选课程信息

var colIds = "";
var selectedCourse;
var itemOnePage = 6;
var nowCategory = 0;
$(document).ready(function(){
		
	ShowColumn();	
	timepicker("course-start-time");
	timepicker("course-end-time");
	colIds = urlColHtml();
	
	AjaxJson("../../handler/courseCategory/viewAllCates",{
		isOpen:1
	},backOfCategroy);
	//获取所有课程信息
	if (userId <= 0) {
		$('#pagination').hide();
	} else
		paginationPage();
	searchCourseByName("",0);
	$("#course-search").click(
			function(){
				var courseName = $("#course-input").attr("value");
				searchCourseByName(courseName,nowCategory);
				//获取所有课程信息
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
			}
	);
}
);

/**
 * 课程搜索功能
 * 
 * @author ZhangXin
 * @param searchCourseName
 */
function searchCourseByName(searchCourseName,cateId)
{
	viewaction = '../../handler/sc/course';
	var reg = /^[0-9]{4}\-[0-9]{2}\-[0-9]{2} [0-9]{2}$/;
	var startTime = $("#course-start-time").val();
	var endTime=$("#course-end-time").val();
	if(startTime !=""&&!startTime.match(reg))
	{
		alert("请输入正确的日期格式,例如:1991-7-8 12");
		return ;
	}
	if(endTime !=""&&!endTime.match(reg))
	{
		alert("请输入正确的日期格式,例如:1991-7-8 12");
		return ;
	}
	params = {
			"startTime" : "",
			"endTime" : "",
			"searchWord" : searchCourseName,
			"pageArray" : new Array(),
			"recordPerPage" : 6,
			"openTime":startTime,
			"closeTime":endTime,
			"cateId":cateId,
			"userId":userId
	};
	initialBind();// 绑定分页的一些操作响应
	pageRecord(6);
	initSearch();
}
function backOfCategroy(backInfo)
{
	var data = backInfo.data.courseCates;
	if (data == null) {
		return;
	}
	var list;
	if ($.isEmptyObject(data)) {
		list = "<p>没有关联任何领域！</p>";

	} else {
		list = "<li class=\"active1\" name=\"Categorylist\"><a id='0' href='#' onclick='obtainCouseId(this);'>全部领域</a></li>";
		$.each(data, function(i, val) {
			list += "<li name=\"Categorylist\"><a id=" + val.cocaId+" href='#' onclick='obtainCouseId(this);'>" + val.cocaName+"</a></li>";
			list += "";});

		$("#Categorylist").append(list);
		$("li[name='Categorylist']").click(function(){
			$("li[name='Categorylist']").removeClass();
			$(this).addClass('active1');
		});
	}

}

function obtainCouseId(obj) {
	var courseName = $("#course-input").attr("value");
	var categoryId = obj.id;
	nowCategory = categoryId;
	searchCourseByName(courseName,nowCategory);
}


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


function refreshContent(pageRecords) {
	$.ajaxSettings.async = false;
	var HTML = "";
	if(pageRecords==undefined||pageRecords==null||$.isEmptyObject(pageRecords.data))
	{
		HTML = "<div class=\"alert fade in\">"+
		"<button class='close' data-dismiss=\"alert\" type=\"button\"></button><strong>查询为空</strong>sorry,你所查询的课程不存在"
		+"</div>";
		$("#recommend-course").html(HTML);
		return false;
	}
	$.each(pageRecords.data,function(itemIndex, item){
		if(itemIndex % 3 ==0 && itemIndex==0){//开始新的一行
			HTML += "<div class=\"row\"><ul style=\"list-style-type:none\">";
		}else if(itemIndex % 3 ==0 && itemIndex>0){
			HTML += "</ul></div><div class=\"row\"><ul style=\"list-style-type:none\">";
		}

		var teacherInfo = item.courTeachers.split("$$$");
		var teach = "";
		for(var i=0;i<teacherInfo.length-1;i++)
		{
			var te = teacherInfo[i].split("$&$");
			if(te.length==3)
			{
				var m = formatString(te[1]);
				teach +="<a href=\"#myModal\" data-toggle=\"modal\" style='cursor:pointer;' onclick='showTeacherInfo(\""+m+"\",null,\""+te[0]+"\")'>"+te[0]+"</a>&nbsp;&nbsp;&nbsp;";
			}
			else if(te.length == 4)
			{
				var m = formatString(te[2]);
				teach+="<a href='#myModal' data-toggle=\"modal\" style='cursor:pointer;' onclick='showTeacherInfo(\""+m+"\",\""+te[0]+"\",\""+te[1]+"\")'>"+te[1]+"</a>&nbsp;&nbsp;&nbsp;";
			}			
		}
		HTML +="<li class=\"col-lg-4\">"+
		"<a href=\"Learning_chooseCourse.html?courId="+item.courId+"&selectedType=1&"+colIds+"\" class=\"thumbnail\" title=\"点击查看详情\">"+
		"<img src=\"../../"+item.courImg+item.fileName+"\"  />";	


		var gotcoure = item.courName;
		var op = countlen(gotcoure);
		if(op<29){
			HTML += "<div class=\"coursetile\">"+gotcoure+"</div>";
		}
		else{
			var nString =  buildNewStr(gotcoure);
			HTML += "<div class=\"coursetile\">"+nString+"</div>";
		}
		if(item.courType==""||item.courType==null){
			item.courType="暂无课程类型";
		}
		HTML +="</a>"+
		"<div class=\"caption\">"+
		"主讲："+teach+"<br />领域："+item.courCates+"<br />课程类型："+item.courType+"<br />学分："+item.courCredit+"&nbsp;&nbsp;学习人数："+item.courChoosedNum+
		"<br />开始时间："+(item.opentTime==undefined?'':item.opentTime)+"<br />"+
		"结束时间："+(item.closeTime==undefined?'':item.closeTime)+
		"</div>"+
		"</li>";
	});
	HTML +="</ul></div>";
	$("#recommend-course").html(HTML);

	bind();
	return false;
}

function showTeacherInfo(remark,location,name)
{
	if(null!=location)
	{
		$("#teacher-pic").attr("src",location);
		$("#teacher-name").html("老师姓名："+name);
		$("#teacher-remark").html("老师简介："+remark);

	}
	else 
	{
		$("#teacher-pic").attr("src","../../upload/portrait/user.jpg");
		$("#teacher-name").html("老师姓名："+name);
		$("#teacher-remark").html("老师简介："+remark);
	}
}
function bind()
{
	$("div[id^='newcourse-link']").click(
			function(){
				var id = $(this).attr("id");
				//alert(id);
				id = id.split("newcourse-link")[1];
				window.location="../html/Learning_chooseCourse.html?courId="+id+"&selectedType=0&"+colIds;
			}
	);


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

/**
 * 显示搜索到的课程
 * 
 * @author ZhangXin
 * @param data
 */
function showSearchCourse(data)
{
	showAllCourse(data);
	showPaginationPage();
	$("div[id^='newcourse-link']").click(
			function(){
				var id = $(this).attr("id");
				id = id.split("newcourse-link")[1];
				window.location="../html/Learning_selectCourseDetail.html?courseId="+id+"&"+colIds;
			}
	);
}
/**
 * 显示所有课程信息
 * @param databack
 */
function showAllCourse(data)
{
	var allCourse = data.values;
	var HTML = "";
	for(var i=0;i<allCourse.length;i++) {
		HTML += "<div id='newcourse-link"+allCourse[i].courId+"' class='courseBlockDiv-img'>" +
		"<div class='front-div'>" +
		"<div class='transparent_class transparent-background'></div>" +
		"<div class='label label-inverse front-div-label'>查看课程介绍</div>" +
		"</div>" +
		"<div class='blockDiv-innerDiv3'>" +
		"<img src='../img/icon/"+allCourse[i].courImg+"'>" +
		"<div class='img-front-div'>" +
		"<div class='transparent_class transparent-img-background'></div>" +
		"<div class='img-front-div-label'>"+allCourse[i].courName+"</div>" +
		"</div>" +
		"</div>" +
		"</div>";
	}

	$("#recommend-course").html(HTML);
	bind();
}
//获取课程信息
function getCourse()
{
	alert(1);
	$.ajax({
		type:"post",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		url:'../../handler/sc/course',
		async:false,   
		data:{
			courseOnePage:itemOnePage
		},
		dataType:'json',
		success:function(result){
			var m = result.data.result;
			var ht = "";
			var courdet="";
			showAllCourse(m);
		}
	});
}

function formatString(target)
{
	/*target = formatTool(target,"\"");
	target = formatTool(target,"\<");
	target = formatTool(target,"\>");
	target = formatTool(target,"\'");*/
	var reg=new RegExp("<.+?>","g");
	target=target.replace(reg,"");
	return target;
}


function formatTool(target,form)
{
	var foo = target.split(form);
	var item = "";
	for(var i=0;i<foo.length-1;i++)
	{
		item += foo[i]+"\\"+form;
	}
	item+= foo[foo.length-1];
	return item;
}