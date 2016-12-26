var selectCourseNum;
$(document).ready(function(){
	var headerHtml="<div class='navbar navbar-inverse navbar-fixed-top'>"+
	"<div class='navbar-inner'>"+
	"<div class='container'>"+
	"<a data-target='.navbar-responsive-collapse' data-toggle='collapse' class='btn btn-navbar'>" +
	"<span class='icon-bar'></span>" +
	"<span class='icon-bar'></span>" +
	"<span class='icon-bar'></span></a>" +
	"<a href='#' onfocus='this.blur();' class='brand'>我教你 — 学习平台</a>" +
	"<div class='nav-collapse collapse navbar-responsive-collapse'>" +
	"<ul class='nav'><li class='active'><a href='#' onfocus='this.blur();'>主页</a></li>"+
	"<li><a href='#' onfocus='this.blur();'>链接</a></li>"+
	"<li><a href='#' onfocus='this.blur();'>链接</a></li>"+
	"<li class='dropdown'>" +
	"<a data-toggle='dropdown' class='dropdown-toggle' href='#' onfocus='this.blur();'>下拉菜单<strong class='caret'></strong></a>" +
	"<ul class='dropdown-menu'>"+
	"<li><a href='#' onfocus='this.blur();'>下拉导航1</a></li>"+
	"<li><a href='#' onfocus='this.blur();'>下拉导航2</a></li>"+
	"<li><a href='#' onfocus='this.blur();'>其他</a></li>"+
	"<li class='divider'></li>"+
	"<li class='nav-header'>标签</li>"+
	"<li><a href='#' onfocus='this.blur();'>链接1</a></li>"+
	"<li><a href='#' onfocus='this.blur();'>链接2</a></li>"+
	"</ul></li></ul><ul class='nav pull-right'>"+
	"<li><a href='#' onfocus='this.blur();'>右边链接</a></li><li class='divider-vertical'></li>"+
	"</ul></div></div></div></div>";
	$("#navibar").html(headerHtml);
	getInfoo(2);
	getReply(2);
	getAnnounce();
	$("button#comfirmCourse").click(
			function()
			{
				var id = parseInt($("ul#courses li[class='active']").attr("name"));
				selectCourse(2,id);
			}
	);
	$("ul#course li[class='active']").click(
			function()
			{
				alert("find");
				selectCourseNum = $(this).attr("name");
				alert(selectCourseNum);
			}
	);
	$("li#gr a").click(
			function(e)
			{
				getInfoo(2);
				getReply(2);
				getAnnounce();
				e.preventDefault();
				$(this).tab('show');

			}
	);

	$("li#xk a").click(
			function(e)
			{
				getInfoo1(2);
				getCourse();
				e.preventDefault();	
				$(this).tab('show');
			}
	);

	$("li#xx a").click(
			function(e)
			{
				e.preventDefault();
				$(this).tab('show');

			}
	);

	$("li#cs a").click(
			function(e)
			{
				e.preventDefault();
				$(this).tab('show');

			}
	);

	$("li#kc a").click(
			function(e)
			{
				e.preventDefault();
				$(this).tab('show');

			}
	);
	
	$("#dropCourse").click(
			function()
			{
				alert("drop"+t);
				var courId = selectedCourse[t].rscoId;
				alert(courId);
				dropCourse(2,courId);
			}
	);
});
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
	/*$("#table_course tr td a").click(function(){
		t = parseInt(this.name);
		alert(t);
		$("p[class='course']").html(selectedCourse[t].courName);
		$("p[class='teacher']").html(selectedCourse[t].courTeacherIds);
	});*/
}

function flushDropCourse(selectedCoruses)
{
	flushSelectCourse(selectedCoruses);
}
function ssss()
{
	$msg=$("<div id='infomsg' class='"+classes+"'>"
			+"<a class='close' data-dismiss='alert'>×</a>" 
			+"<h4 class='alert-heading'>"+header+"</h4><p>"+message
			+"</p></div>'");
	$msg.insertBefore($afterdom);
}