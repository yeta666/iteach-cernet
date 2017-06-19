/**
 * 集中学习js
 * 
 * @author ZhangXin
 */

var isInAddStudent = false;
var studyId;
var studyName_add;
var conflictIds;
var attendedMassedLearningStuIds;
var attendedStudents;
var pageData;
var isFirstRequest = true;
var mlmaMaxTime;
var attendedStudentIds = new Array();
var chapterList;
var malaMaxStudentNum;
var istriggered = false;
var isModify = false;
var nowCourseId;
var startTime;
var endTime;
$(document).ready(function() {
	paginationPage();
	$("#pagination").removeAttr("style");
	AjaxJson("../../handler/sypaController/attainValueByEnName",{enName:'mlmaMaxTime'},backOfSyspa);
	AjaxJson("../../handler/sypaController/attainValueByEnName",{enName:'malaMaxStudentNum'},backOfSyspaNum);
	clickItemFunc("a#create-centralize",function(){
		$("#pagination").attr("style","display:none;");
	});
	clickItemFunc("a#add-puttogather-back",function(){
		$("#pagination").removeAttr("style");
	});
	clickItem("a#cancle-add");
	timepicker("startTime");
	$("#search-massed").click(function(){
		getMassedInfo();
	});
	$("#status").change(function(){
		$("#search-massed").trigger("click");
	});
	getCourseList();
	getMassedInfo();
	//取消添加集中学习
	$("a#cancle-add").click(function(){
		isInAddStudent = false;
		getMassedInfo();
	});
	//添加集中学习
	$("#add-puttogather-ok").click(function(){
		var param = addPuttogatherCheck();

		if(param!=""&&!istriggered){

			AjaxJson("../../handler/massedLearning/addNewMassedLearning",param,addStudentOk);
		}
	});
	updatedata(departId);
	//监听学生搜索按钮
	$("#search-forstudent").click(function(){
		requestStudent();
	});
	//监听添加用户完成按钮
	$("#add-student-finish").click(function(){
		if($("option[name^='getAllowStu']").size()>malaMaxStudentNum)
		{
			alert("超过最大人数");
			return;
		}
		var studentAddIds = "";
		var studentDeleteIds="";
		$("option[name^='getAllowStu']").each(function(){
			var canAdd = true;
			var nowVal = $(this).attr("value");
			for(var i=0;i<attendedStudentIds.length;i++)
			{
				if(attendedStudentIds[i] == nowVal)
				{
					canAdd = false;
				}
			}
			if(canAdd)
			{
				studentAddIds += nowVal+",";
			}
		});

		for(var i=0;i<attendedStudentIds.length;i++)
		{
			var canDel = true;
			var nowVal = attendedStudentIds[i];
			$("option[name^='getAllowStu']").each(function(){
				if($(this).attr("value")==nowVal)
				{
					canDel = false;
				}
			});
			if(canDel)
				studentDeleteIds +=nowVal+",";
		}
		studentDeleteIds = dropPoit(studentDeleteIds);
		studentAddIds = dropPoit(studentAddIds);
		var params={
				smleMaleId:studyId,
				addStuIds:studentAddIds,
				deleStudent:studentDeleteIds
		};
		//alert(params);
		AjaxJson("../../handler/massedLearning/addStudentToOneMassedLearning",params,buildOk);
	});
	//监听移除用户
	$("#remove-student").click(function(){
		$("option[name^='getAllowStu']").each(function(){
			if(this.selected)
			{
				var val=$(this).val();
				$(this).remove();
				$("#now-student").html("<p>已添加学生总人数："+$("option[name^='getAllowStu']").size()+" 人</p>");
				$("input[class='student-add-together']").each(function(){
					if($(this).val()==val&&$(this).attr("checked")=="checked")
					{
						$(this).removeAttr("checked");
					}
				});
			}
		});
	});
});
/**
 * back of system parameter
 * @param backInfo
 */
function backOfSyspa(backInfo)
{
	mlmaMaxTime = backInfo.data.value;
	$("#time-last1").html("*最大时间限制为："+mlmaMaxTime);
}

function backOfSyspaNum(backInfo)
{
	malaMaxStudentNum = backInfo.data.value;
}
function getMassedInfo()
{
	var status1 = $("#status").val();
	var studyName =$("#study-name").val();
	viewaction = '../../handler/massedLearning/selectMassedLearningInfo';
	params = {
			"startTime" : "",
			"endTime" : "",
			"searchWord" : "",
			"pageArray" : new Array(),
			"recordPerPage" : 10,
			'maleTeacherId':userId,
			'maleState':status1,
			'maleName':studyName
	};
	params.maleState=status1;
	initialBind();// 绑定分页的一些操作响应
	initSearch();
}
/**
 * 查看集中学习列表
 */
function refreshContent(pageRecords) {
	//显示老师开创的集中学习列表
	if(!isInAddStudent){
		$("#waring-nodata").html("");
		$("#study-together-table").empty();
		$.ajaxSettings.async = false;
		var HTML = "";
		if(pageRecords.data.length<=0)
		{
			HTML +="<tr class='alert'><td colspan='9'>"+
			"<button type='button' class='close' data-dismiss='alert'>&times;</button>"+
			"<span style='color:red;font-size:18px;'>提示: 没有符合您所需查询数据...</span>"+
			"</td></tr>" ;
					/*"<div class='alert'>"+
			"<button type='button' class='close' data-dismiss='alert'>&times;</button>"+
			"<strong>提示!</strong> 没有符合您所需查询数据..."+
			"</div>";*/
			
			$("#study-together-table").html(HTML);
		}
		else
		{
			$.each(pageRecords.data,function(itemIndex, item) {
				var d = new Date(item.maleCreateTime);
				var url = 'my_Tutor_Learningvedio.html?courId='+item.maleCourseId+'&resId='+item.maleResouseId+'&courseName='+escape(item.maleCourseName)+'&maleId='+item.maleId+'&'+urlColHtml();
				HTML += "<tr>"+
				"<td><input type='checkbox' name='studytogether' value='"+item.maleId+"'></td>"+
				"<td>"+item.maleName+"</td>"+
				"<td>"+item.maleCourseName+"</td>"+
				"<td>"+item.maleCreateDate+"</td>"+
				"<td>"+(item.maleState==0?'未开始':'已结束')+"</td>"+
				"<td>"+item.maleStartTime+"</td>"+
				"<td>"+item.maleActualStuNum+"</td>"+
				"<td name='"+item.mlmaMaxStudentNum+"'>"+(item.maleState==1?'已经结束':'<a class="'+item.maleCourseId+'" name="study-together'+item.maleId+'" startTime="'+item.maleStartTime+'" endTime="'+item.maleEndTime+'" href="#add-student" >点击调整</a>')+"</td>"+
				"<td>"+(item.maleState==1?'':'<a class=\"btn btn-info\" href=\"'+url+'\">开始</a>')+"</td>"+
				"</tr>";
			});
			$("#study-together-table").html(HTML);
			//删除集中学习监听
			$("#delstudytogether").click(function(){
				var dropIds="";
				var $delSelected = $("input[name='studytogether']");
				var count = 0;
				for(var i=0;i<$delSelected.length;i++) {
					if($($delSelected[i]).is(":checked")) {
						dropIds += ($($delSelected[i]).parents("tr")).attr("value")+",";
						count ++;
					}
				}
				/*$delSelected.each(function(index,item){
						if($($delSelected[i]).is(":checked")) {
							ids += ($($delSelected[i]).parents("tr")).attr("value")+",";
							count ++;
							alert(ids);
						}*/
					/*if($(this).attr("checked")=="checked")
					{
						//alert("删除点击啦");
						dropIds += $(this).attr("value")+",";
					}
				});*/
				dropIds = dropPoit(dropIds);
				if(count == 0)
				{
					alert("请先勾选需要删除的集中学习");
					return;
				}
				/*$(this).attr("href","#myModal");*/
				
				$("#delLabel").html("确认删除这 <span style='color:red;'>"+ count +"</span> 项?");
				$("#myModal").modal({
					'backdrop':true
				});
				$(".closeButton").click(function(){
					$("#myModal").modal('hide');
				});
			/*$("#make-sure").click(function(){
					AjaxJson("../../handler/massedLearning/deleteMassedLearning",{maleIds:dropIds},backOfDelete);
				});*/
			});

			$("a[name^='study-together']").each(function(index,item){
				clickItem(this);
				$(this).click(function(){
					startTime = $(this).attr("startTime");
					endTime = $(this).attr("endTime");
					nowCourseId = parseInt($(this).attr("class"));
					isModify = true;
					$("#add-student-finish").empty().append("<i class='fa fa-check-square-o'></i> 调整完成");
					$("#cancle-add").empty().append("<i class='fa fa-close'></i> 取消调整");
					studyId = $(this).attr('name');
					studyId = studyId.split("study-together")[1];
					mlmaMaxStudentNum = parseInt($(this).parent().attr("name"));
					$("#most-student").html("<p>集中学习人数上限："+malaMaxStudentNum+" 人</p>");

					$("#now-student").html("<p>已添加学生总人数："+0+" 人</p>");
					requestStudent();
				});
			});
		}
	}
	//显示学生列表
	else
	{
		$("#add-student-table").empty();
		$.ajaxSettings.async = false;
		var HTML = "";
		if(pageRecords.data ==null||pageRecords.data.length<=0)
		{
			HTML +="<tr class='alert'><td colspan='9'>"+
			"<button type='button' class='close' data-dismiss='alert'>&times;</button>"+
			"<span style='color:red;font-size:18px;'>提示: 没有符合您所需查询数据...</span>"+
			"</td></tr>";
			$("#waring-nodata").html(HTML);
		}
		else
		{
			$("#all-selected-student").empty();
			$("#waring-nodata").html("");
			attendedMassedLearningStuIds = pageRecords.data.attendedMassedLearningStuIds;
			conflictIds = pageRecords.data.conflictMassed;
			pageData = pageRecords.data.pageData;
			attendedStudents = attendedMassedLearningStuIds.split("@");
			//显示已经在此集中学习的学生
			if(isFirstRequest&&attendedMassedLearningStuIds!=""){
				isFirstRequest = false;
				for(var i = 0;i<attendedStudents.length;i++)
				{
					var foo = attendedStudents[i].split(",");
					attendedStudentIds[i] = parseInt(foo[0]);
					$("#all-selected-student").append("<option name='getAllowStu' value='"+parseInt(foo[0])+"'>"+foo[1]+";</option>");
				}
			}
			//显示已选总人数
			$("#now-student").html("<p>已添加学生总人数："+$("option[name^='getAllowStu']").size()+" 人</p>");

			var conflictStudent = ","+pageRecords.data.conflictMassed;
			//判断重复，如果select框中已经有了该学生，那么就在界面上勾选上
			$.each(pageRecords.data.pageData,function(itemIndex, item) {
				var iscontain = false;
				$("option[name^='getAllowStu']").each(function(){
					if($(this).attr("value")==item.stuId)
						iscontain = true;
				});
				var org = ","+item.stuId+",";
				var patten = new RegExp(org);
				var isConflict = false;
				if(patten.test(conflictStudent)==true)
				{
					isConflict = true;
				}
				HTML += "<tr>"+
				"<td>"+(isConflict==true?"":"<input class='student-add-together' "+(iscontain?'checked="checked"':'')+" name='"+item.stuName+"' type='checkbox' value='"+item.stuId+"'>")+"</td>"+
				"<td>"+item.stuNum+"</td>"+
				"<td>"+item.stuName+"</td>"+
				"<td>"+item.stuGrade+"</td>"+
				"<td>"+item.stuClass+"</td>"+
				"<td>"+(isConflict==true?"时间冲突":"可添加")+"</td>"+
				"</tr>";
			});
			$("#add-student-table").html(HTML);
			$("input[class='student-add-together']").each(function(){
				$(this).bind("click",function(){
					var foo = $(this).attr("value");
					var studentName = $(this).attr("name");
					var isselected = $(this).is(":checked")?true:false;
					//alert("我被点击啦~"+foo+" name:"+studentName);
					if(!isselected){
						$("a[name^='getAllowStu']").each(function(){
							if($(this).prop("value")==foo)
							{
								alert("attr prop");
								$(this).remove();
								$("#now-student").html("<p>已添加学生总人数："+$("option[name^='getAllowStu']").size()+" 人</p>");
							}
						});
					}
					else
					{
						$("#all-selected-student").append("<option name='getAllowStu' value='"+foo+"'>"+studentName+";</option>");
						$("#now-student").html("<p>已添加学生总人数："+$("option[name^='getAllowStu']").size()+" 人</p>");
					}
				});
			});
		}
	}

	return false;
}
/**
 * 删除学习返回信息
 * @param backInfo
 */
function backOfDelete(backInfo)
{
	var status = backInfo.data.status;
	if(status==0)
		{
		alert("请勾选");
		}
		else if(status == 1)
	{
		alert("删除失败");
	}
	else
	{
		alert("删除成功");
		location.reload();
	}
}
/**
 * 请求学生信息
 */
function requestStudent()
{
	isInAddStudent = true;
	var grade = $("#quGrade").val();
	var classid = $("#quClass").val();
	var searchKey = $("#user-name").val();
	viewaction = '../../handler/user/finStudentByIds';
	params = {
			"stuMaleStartTimeStr" : startTime,
			"stuMaleEndTimeStr" : endTime,
			"searchWord" : "",
			"pageArray" : new Array(),
			"recordPerPage" : 10,
			'stuMaleId':studyId,
			'stuSchoolId':departId,
			'stuName':searchKey,
			'stuClasId':classid,
			'stuGradeId':grade,
			'stuCourseId':nowCourseId
	};
	initialBind();// 绑定分页的一些操作响应
	initSearch();
}
/**
 * 校验
 */
function addPuttogatherCheck()
{

	var courseId = $("#show-courses").val();
	var resource = $("#resource-sort").val();
	var studyName = $("#study-name-holder").val();
	var startTime = $("#startTime").val();
	var capter = $("#capter-sort").val();
	if(courseId<=0)
	{
		state = 'error';
		header = "提交失败";
		message = "请选择课程";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return "";
	}
	nowCourseId = courseId;
	if(capter<0)
	{
		state = 'error';
		header = "提交失败";
		message = "请选择章节";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return "";
	}
	if(resource<=0)
	{
		state = 'error';
		header = "提交失败";
		message = "请选择资源";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return "";
	}
	if(startTime ==""){
		state = 'error';
		header = "提交失败";
		message = "请选择日期";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return "";
	}

	var reg = /^[0-9]{4}\-[0-9]{2}\-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$/;
	if(!startTime.match(reg))
	{
		state = 'error';
		header = "提交失败";
		message = "日期格式不正确：例2010-12-1 12:30:00";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return "";
	}
	if(studyName==""){
		state = 'error';
		header = "提交失败";
		message = "请输入学习名称";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return "";
	}

	var ttest = /^\d*$/;
	var lastTime = parseInt($("#time-last").val());
	if(!$("#time-last").val().match(ttest))
	{
		state = 'error';
		header = "提交失败";
		message = "学习时间只能由数字组成";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return "";
	}
	if(!(lastTime<=mlmaMaxTime&&lastTime>0)){
		state = 'error';
		header = "提交失败";
		message = "学习时间没有在规定范围内";
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
		return "";
	}
	var params = {
			maleSchoolId:departId,
			maleStartDate:startTime,
			maleDuration:lastTime,
			maleCourseId:courseId,
			maleResouseId:resource,
			maleName:studyName,
			maleTeacherId:userId
	};
	return params;
}
/**
 * 请求课程列表
 */
function getCourseList()
{
	console.log(departId + " " + userId);
	AjaxJson("../../handler/course/viewCourseList", {
		departId : departId,
		userId : userId
	}, showCourseToSelect);
}

/**
 * 显示列表
 * @param backInfo
 */
function showCourseToSelect(backInfo)
{
	console.log(backInfo);
	if(backInfo.data != null){
		var data = backInfo.data.courses;
		var Html = "<option value=-1>=选择课程=</option>";
		for ( var i = 0; i < data.length; i++) {
			Html += "<option value=" + data[i].courseId + ">" + data[i].courseName
			+ "</option>";
		}
		$("#show-courses").html(Html);
		$("#show-courses").change(function() {
			getChapter($(this).val());
		});
	}else{
		var Html = "<option value=-1>=选择课程=</option>";
		$("#show-courses").html(Html);
		$("#show-courses").change(function() {
			getChapter($(this).val());
		});
	}
}
/**
 * 获取章节
 * @param courseId
 */
function getChapter(courseId)
{
	AjaxJsonGet("../../handler/chapter/viewChapterListByCourse.do", {
		courId : courseId
	}, showChapterToSelect);

}

/**
 * 显示资源
 * @param backInfo
 */
function showChapterToSelect(backInfo)
{
	console.log(backInfo);
	chapterList = backInfo.data.chapterList;
	var data = chapterList;
	var Html = "<option value=-1>=选择章节=</option>";
	for ( var i = 0; i < data.length; i++) {
		if(data[i].resources[0]==undefined)
			Html += "<option value=-1>" + data[i].chapName
			+ "</option>";
		else
			Html += "<option value="+i+">" + data[i].chapName
			+ "</option>";
	}
	$("#capter-sort").html(Html);
	$("#capter-sort").change(function() {
		var index = $(this).val();
		var Html = "";
		if(index == -1)
		{
			Html+="<option value='-1'>没有对应资源</option>";
		}
		else
		{
			Html +="<option value='-1'>=选择资源=</option>";
			var resources = chapterList[index].resources;
			for(var i=0;i<resources.length;i++)
			{
				Html += "<option value='"+resources[i].resoId+"'>"+resources[i].resoTitle+"</option>";
			}
			$("#resource-sort").html(Html);
		}
	});
}
/**
 * 创建完成
 */
function buildOk(backInfo)
{
	var data = backInfo.data.message;
	alert(data);
}


/**
 * 发送年级请求
 * @param depid
 */
function updatedata(depid)
{
	AjaxJson("../../handler/register/gradeInfo", {
		depa_id:depid
	}, showGradeToSelect);
}
/**
 * 显示年级
 * @param data
 */
function showGradeToSelect(data)
{
	var data = data.data.gradeInfo;
	var Html = "<option value='-1'>=年级=</option>";
	for (var i = 0; i < data.length; i++) {
		Html += "<option value=" + data[i].gradId + ">" + data[i].gradName
		+ "</option>";
	}
	$("#quGrade").html(Html);
	$("#quGrade").change(function() {
		var gradeid = $(this).val();
		//当学校被点击，请求年级
		requestclass(gradeid);
		$("#search-forstudent").trigger("click");
	});
}
/**
 * 请求班级
 * @param classid
 */
function requestclass(gradeid)
{
	AjaxJson("../../handler/register/classesInfo", {
		grade_id:gradeid
	}, showClassToSelect);
}
/**
 * 显示班级
 * @param data
 */
function showClassToSelect(data)
{
	var data1 = data.data.classesInfo;
	var Html = "<option value='-1'>=班级=</option>";
	for ( var i = 0; i < data1.length; i++) {
		Html += "<option value=" + data1[i].clasId + ">" + data1[i].clasName
		+ "</option>";
	}
	$("#quClass").html(Html);
	$("#quClass").change(function() {
		$("#search-forstudent").trigger("click");
	});
}

function addStudentOk(data){
	var status = data.data.status;
	if(1==status)
	{
		state = 'error';
		header = "提交失败";
		message = data.data.message;
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
	}
	else if(2==status)
	{
		state = 'error';
		header = "提交失败";
		message = data.data.message;
		infoNotice(state, header, message, $('#infomsg'));
		moveto("table");
	}
	else
	{
		clickItem("a#add-puttogather-ok");
		istriggered = true;
		$("a#add-puttogather-ok").trigger("click");
		$("#pagination").removeAttr("style");
		$("#most-student").html("<p>集中学习人数上限："+malaMaxStudentNum+" 人</p>");
		$("#now-student").html("<p>已添加学生总人数："+0+" 人</p>");
		var maleId = data.data.maleId;
		studyId = maleId;
		isModify = false;
		$("#add-student-finish").empty().append("<i class='fa fa-check-square-o'></i>添加完成");
		$("#cancle-add").empty().append("<i class='fa fa-close'></i>取消添加");
		requestStudent();
	}

}

function dropPoit(target)
{
	if(target.charAt(target.length-1)==",")
	{
		return target.substring(0,target.length-1);
	}
	return target;
}
