var isClickMethod = false;
var attachmentId = -1;
var coursename;
var coursecode;
var grade;
var userId;
var brief;
var plan;
var forum;
var methods;
var coursesort;
$(document).ready(function(){
	$("#coursename").val("");
	$("#course-sort").val("");
	$("#coursecode").val("");
	$("#creator").val("");
	$("#grade").val("");
	$("#brief").val("");
	$("#forum").val("");
	$("#plan").val("");
	$("#eva-method-input").val("");
	AjaxJson("../../handler/course/categorysort",{}, backEvaMethod);
	AjaxJson("../../handler/course/evaluateMethod",{},backCourseSort);
	$("#register").click(function(){
		check();
	});
	$("#add-method").click(function(){
		if(!isClickMethod){
			isClickMethod = true;
			$("#show-button").attr("class","icon-chevron-up");
			showFoo($(this),$("#content"));
		}
		else
		{
			$("#show-button").attr("class","icon-chevron-down");
			$("#content").removeAttr("style");
			$("#content").attr("style","position: absolute;display: none;");
			isClickMethod = false;
		}
	});
	//$("#creator").value=userName;
	$("#creator").prop("value",userName);
$("#dropdownToggle").click(function(){
	$("#content").show();
});
$("#close").click(function(){
	document.getElementById("content").style.display='none';
});
});
function registerBack(backInfo)
{
	state = 'success';
	header = "完成";
	message = "创建成功";
	infoNotice(state, header, message, $('#infomsg'));
	moveto("table");
	window.location.href = "my_Tutor.html?firstCol=5&secondCol=19";
}
function check()
{
	coursename = $("#coursename").val();
	coursesort = $("#course-sort").val();
	coursecode = $("#coursecode").val();
	creator = $("#creator").val();
	grade = $("#grade").val();
	brief = $("#brief").val();
	forum = $("#forum").val();
	plan = $("#plan").val();

	methods = "";
	$("input[name='eva-method']").each(function(){
		if($(this).is(":checked"))
			methods +=","+$(this).val();
	});

	if(coursename=="" || coursename.length>16)
	{
		state = 'error';
		header = "添加失败";
		message = "请输入课程名,不超过30位";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return "";
	}
	if(coursesort<=0)
	{
		state = 'error';
		header = "添加失败";
		message = "请选择课程考核方式";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return "";
	}
	
	if(methods=="")
	{
		state = 'error';
		header = "添加失败";
		message = "请勾选课程领域";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return "";
	}
	if(coursecode=="")
	{
		state = 'error';
		header = "添加失败";
		message = "请输入课程代码";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return "";
	}
	var flo = /^(?!0+(\.0+)?$)\d+(\.\d+)?$/;
	if(grade==""||null==grade.match(flo))
	{
		state = 'error';
		header = "添加失败";
		message = "请输入正确的学分，整数或者浮点数";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return "";
	}

	
	if(brief=="")
	{
		state = 'error';
		header = "添加失败";
		message = "请输入所创建的课程简介";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return;
	}
	methods +=",";
	var picId;

	var pic = $("#postfile").val();
	pic = pic.slice(pic.lastIndexOf("."));
	if("" == pic)
	{		
		attachmentId = -1;
		var params = registerNew();
		if(""!=params && params != null)
		{
			AjaxJson("../../handler/course/create",params,registerBack);
		}
	}else if(!(".bmp" == pic || ".png" == pic || ".jpg" == pic))
	{
		state = 'error';
		header = "添加失败";
		message = "图片格式不正确 仅支持bmp、png和jpg格式";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return "";
	}else{
		$.ajaxFileUpload({
			'url' : "../../handler/load/upload",
			'secureuri' : false,
			'fileElementId' : "postfile",
			'dataType' : "json",
			'data':{
				fileType:6
			},
			'success' : function(data, status) {
				attachmentId = data.data.attachId;
				var params = registerNew();
				if(""!=params && params != null)
				{
					AjaxJson("../../handler/course/create",params,registerBack);
				}
			},
			'error' : function(data, status, e) {
				state = 'error';
				header = "添加失败";
				message = "图片上传失败导致课程注册失败!!";
				infoNotice(state, header, message, $('#infomsg'));
				moveto("table");
			}
		});
	}	
}
function registerNew()
{
	if(attachmentId > 0)
	{
		var params = {
				courName:coursename,
				courCode:coursecode,
				courCredit:grade,
				courCreateUserid:userId,
				courDescribe:brief,
				courTimeSchedule:plan,
				courForumname:forum,
				courCateids:methods,
				courTepaId:coursesort,
				courVerify:0,
				courOpenToAll:0,
				courCoverpictureid:attachmentId,
				courTestLimitScore:0
		};
		return params;
	}
	else
	{
		var params = {
				courName:coursename,
				courCode:coursecode,
				courCredit:grade,
				courCreateUserid:userId,
				courDescribe:brief,
				courTimeSchedule:plan,
				courForumname:forum,
				courCateids:methods,
				courTepaId:coursesort,
				courVerify:0,
				courOpenToAll:0,
				courTestLimitScore:0
		};
		return params;
	}
}
function backCourseSort(backInfo)
{
	var data = backInfo.data.data.result;
	var Html = "<option value='-1'>--选择课程考核方式--</option>";
	for(var i = 0 ;i< data.length;i++)
	{
		Html += "<option value="+data[i].evmeId+">"+data[i].evmeName+"</option>";
	}
	$("#course-sort").html(Html);
}


/**
 * 获取考核方式后的处理
 * @param data
 */
function backEvaMethod(backinfo)
{
	var data = backinfo.data.data;
	var Html = "<table class='table'><tbody>";
	for(var i = 0;i< data.length;i+=1)
	{
		if(i%3==0)
			Html +="<tr>";

		Html += "<td><input type='checkbox' name='eva-method' value='"+data[i].cocaId+"'/><p>"+data[i].cocaName+"</p></td>";
		if(i%3==2 || i == data.length-1)
			Html += "</tr>";
	}
	Html +="</tbody></table>";
	$("#methods").html(Html);
	$("input[name='eva-method']").each(function(){
		var info="";
		$(this).bind("click",function(){
			$("input[name='eva-method']").each(function(){
				if($(this).is(":checked")){
					info+=$(this).next().html()+" ";
				}
			});
			$("#eva-method-input").prop("value",info);
		});
	});
	
	
}
/**
 * 弹出框
 * @param This
 * @param target
 */
function showFoo(This,target)
{
	//获取源控件位置
	var xThis = This.offset().left;
	var yThis = This.offset().top;
	var heightThis = This.height();
	var widthThis = This.width();
	//获取目标控件位置
	var widthTarget = target.width();
	var xTarget = xThis + widthThis/2 - widthTarget/2 + 10;
	var yTarget = yThis + heightThis + 10;
	//修改样式
	$("#content").removeAttr("style");
	var style = "position:absolute;top:"+yTarget+"px; left:"+xTarget+"px; display:block;";

	target.attr("style",style);
	target.show();
};