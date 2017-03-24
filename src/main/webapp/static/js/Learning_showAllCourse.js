//缓存已选课程信息
var startTime = '';// 查询的开始时间
var endTime = '';// 查询的结束时间
var courseName='';//输入的关键字
var colIds = "";
var selectedCourse;
var itemOnePage = 6;
var nowCategory = 0;
$(document).ready(function(){
	ShowColumn();	
	timepicker("course-start-time");
	timepicker("course-end-time");
	colIds = urlColHtml();
	recommendCourseslist();//猜你喜欢
	AjaxJson("../../handler/courseCategory/viewAllCates",{
		isOpen:1
	},backOfCategroy);
	//获取所有课程信息
	if (userId <= 0) {
		$('#pagination').hide();
	} else
		paginationPage();
	pageRecord(9);
	loadCourse();// 加载全部课程
	bindSearch();// 绑定搜索按钮
}
);

function searchCourseByName(searchCourseName,cateId)
{
	secondCol = getRequest("secondCol");
	viewaction = '../../handler/sc/course';
	var startTime = $("#course-start-time").val();
	var endTime=$("#course-end-time").val();
	params = {
			"startTime" : "",
			"endTime" : "",
			"searchWord" : searchCourseName,
			"pageArray" : new Array(),
			"recordPerPage" : 6,
			"openTime":startTime,
			"closeTime":endTime,
			"cateId":cateId,
			"userId":userId,
			"currentPage":1,
			"secondCol" : secondCol,
	};
	initialBind();// 绑定分页的一些操作响应

	initSearch();
}
//加载我的课程
function loadCourse() {
	var secondCol = getRequest("secondCol");
	var firstCol = getRequest("firstCol");
	viewaction = '../../handler/sc/course';
	params = {
			"startTime" : "",
			"endTime" : "",
			"searchWord" : courseName,
			"pageArray" : new Array(),
			"recordPerPage" : 6,
			"openTime":startTime,
			"closeTime":endTime,
			"cateId":0,
			"userId":0,
			"currentPage":1,
			"firstCol":firstCol,
			"secondCol" : secondCol
	};
	//判断cookie中是否有查询参数
	if ($.cookie("Search") != null && $.cookie("Search") != "") {
		var SearchCookie = $.cookie("Search");
		var SearchPar = JSON.parse(SearchCookie);
		if (SearchPar != null && SearchPar.secondCol == secondCol) {
			params = SearchPar;
			$('#course-input').val(params.searchWord);
			$('#course-start-time').val(params.startTime);
			$('#course-end-time').val(params.endTime);
		} else {
			// 删除cookie
			document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
		}
	}
	//
	// 绑定分页的一些操作响应
	initialBind();
	// 加载
	initSearch();
}
//绑定搜索
function bindSearch() {
	$('#course-search').click(function() {
		getParams();
		initSearch();
		return false;
	});
}
//设置参数
function getParams() {
	// 删除cookie
	document.cookie = "Search=;expires=" + (new Date(0)).toGMTString();
	courseName = $("#course-input").val();
	startTime = $('#course-start-time').val();
	endTime = $('#course-end-time').val();
	// 点击搜索，一切重新查找
	params.currentPage = 1;
	params.searchWord = courseName;
	params.openTime = startTime;
	params.closeTime = endTime;
	// 添加cookie
	var cookieString = "Search=";
	cookieString = cookieString + JSON.stringify(params);
	document.cookie = cookieString;
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
		list = "<li class=\"activeCate\" name=\"Categorylist\"><a id='0' href='#' onclick='obtainCouseId(this);'>全部领域</a></li>";
		$.each(data, function(i, val) {
			list += "<li name=\"Categorylist\"><a id=" + val.cocaId+" href='#' onclick='obtainCouseId(this);'>" + val.cocaName+"</a></li>";
			list += "";});
		$("#Categorylist").append(list);
		$("li[name='Categorylist']").click(function(){
			$("li[name='Categorylist']").removeClass();
			$(this).addClass('activeCate');
		});
	}

}

function obtainCouseId(obj) {
	var courseName = $("#course-input").prop("value");
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
	//alert("pageRecords:"+pageRecords);
	$.ajaxSettings.async = false;
	var HTML = "";
	if(pageRecords==undefined||pageRecords==null||$.isEmptyObject(pageRecords.data))
	{
		HTML = "<div class=\"alert fade in\" style=\"padding-left: 40px;\">"+
		"<button class='close' data-dismiss=\"alert\" type=\"button\"></button><strong>查询为空</strong>sorry,你所查询的课程不存在"
		+"</div>";
		$("#recommend-course").html(HTML);
		return false;
	}else{
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
			HTML +="<li class=\"col-xs-4\">"+
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
	}
	//bind();
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
}/**
 * 猜你喜欢栏目
 */
function recommendCourseslist() {
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
					if(recommendCourseData.length<6){
						recomle = recommendCourseData.length;
					}else{
						recomle = 6;
					}
					if(recomle==0)
					{
						homehtml = "<div class=\"alert fade in\">"
							+ "<button class='close' data-dismiss=\"alert\" type=\"button\"></button><strong>无推荐课程！</strong>sorry，无法判断你所感兴趣的课程！"
							+ "</div>";

					}else{
						for(var i=0;i<recomle;i++) {
							if(i % 3 ==0 && i==0){//开始新的一行
								homehtml += "<div class=\"row\"><ul style=\"list-style-type:none\">";
							}else if(i % 3 ==0 && i>0){
								homehtml += "</ul></div><div class=\"row\"><ul style=\"list-style-type:none\">";
							}
							//here start 教师信息
							/*
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
							}*/
							//here end
							//替换416行的引号部分//recommendCourseData[i].courImg+recommendCourseData[i].fileName
							homehtml +="<li class=\"col-xs-4\">"+
							"<a href=\"Learning_chooseCourse.html?courId="+recommendCourseData[i].courId+"&selectedType=1&"+colIds+"\" class=\"thumbnail\" title=\"点击查看详情\">"+
							"<img src=\"../../"+"static/img/homeImages/recourseimg.jpg"+"\"  />";	

							var gotcoure = recommendCourseData[i].courName;
							var op = countlen(gotcoure);
							if(op<29){
								homehtml += "<div class=\"coursetile\">"+gotcoure+"</div>";
							}
							else{
								var nString =  buildNewStr(gotcoure);
								homehtml += "<div class=\"coursetile\">"+nString+"</div>";
							}
							if(recommendCourseData[i].courType==""||recommendCourseData[i].courType==null){
								recommendCourseData[i].courType="暂无课程类型";
							}
							homehtml +="</a>"+
							"<div class=\"caption\">"+//here 下一行的teach在传值存在的情况下可以取消屏蔽，并删除"主讲教师测试"
							/*"主讲："+"主讲教师测试"teach+"<br />领域："+recommendCourseData[i].courCates+"<br />"*/"课程类型："+recommendCourseData[i].courType+"<br />学分："+recommendCourseData[i].courCredit+"<br />学习人数："+recommendCourseData[i].courChoosedNum+
							/*"<br />开始时间："+(recommendCourseData[i].opentTime==undefined?'':recommendCourseData[i].opentTime)+"<br/>"+
							"结束时间："+(recommendCourseData[i].closeTime==undefined?'':recommendCourseData[i].closeTime)+*/
							"</div>"+
							"</li>";
						}
						homehtml +="</ul></div>";
					}
				}
				$("#recommendCourses").append(homehtml);
			}else{
				console.log("数据加载出错，请联系管理员！");
				return ;
			}

		}
	});

}