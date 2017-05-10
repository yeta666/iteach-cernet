/*
 *  author：郭海蓉 
 *  time:2013-07
 */
var courId="",searchWord="",seleteSchoolID="",courseCateId="",courseCateList="",evaMethods="";//搜索变量
var oriEvaMetId=0;
var courTeaacherId='';
var userDepaId='';
$(document).ready(function() {
	colIds = urlColHtml();
	ShowColumn();//显示功能栏
	backToTop();//回顶部
	$("#addSubmit").click(function(){
		addTeacher1();
	});
	if (userId <= 0) {
		$('#pagination').hide();
	} else{
		paginationPage();
	}
	$('#eduman').attr("class", "active");
	courseId=location.search.split("?")[1].split("&")[0].split("=")[1];//获取课程id
	selceteCourseCateNames();//显示可选课程分类
	selceteEvaluate();//显示可选考核方式
	viewInfoBeforeModify();//显示修改前的课程信息
	//更改任课教师
	if (departmentTypeID == 1) {//市级管理员
		$("#seleteSchoolID").show();
		departId=0;
		getTeachers(2);
		$("#seleteSchoolID").change(function(){ //学校改变时显示该学校下的可选课程
			$("#CourseTeacher").empty();
			departId = $("#seleteSchoolID").val();
			getTeachers(2);
		});
		selceteSchool();
	}else{
		$("#seleteSchoolID").hide();
		getTeachers(2);
	}
	
});
//获取全部课程领域
function selceteCourseCateNames() {
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/courseCategory/viewAllCates",
		dataType : 'json',
		data:{isOpen:1},
		success : function(data) {
			if (data.ret) {
				if ($.isEmptyObject(data.data.courseCates)) {
					courseCateList = "<label class='checkbox-inline'> "+
					"<input name='checkCourseClass' value='-1' type='checkbox'>当前没有可选课程领域！"+
					"</label>";
				} else {
					$.each(data.data.courseCates, function(i, val) {
						courseCateList += courseCateList = "<label class='checkbox-inline'> "+
						"<input name='checkCourseClass' value='"+val.cocaId+"' type='checkbox'>"+val.cocaName+
						"</label>";
					});
				}

			} else {
				courseCateList = "<label class='checkbox-inline'> "+
				"<input name='checkCourseClass' value='-1' type='checkbox'>"+data.errmsg+
				"</label>";
			}
			$("#Cateid").empty().append(courseCateList);
		}
	});
}
//获取考核方式
function selceteEvaluate() {
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/evaluateMethod/viewAllEvaMethods",
		dataType : 'json',
		data:{},
		success : function(data) {
			if (data.ret) {
				if ($.isEmptyObject(data.data.evaMethods)) {
					evaMethods = "<option value='-1'>考核方式数据为空！！</option>";
				} else {
					$.each(data.data.evaMethods, function(i, val) {
						evaMethods += "<option value='" + val.evmeId + "'>" + val.evmeName + "</option>";
					});
				}

			} else {
				evaMethods = "<option value='-1'>"+data.errmsg+"</option>";
			}
			$("#evalMode").empty().append(evaMethods);
		}
	});
}
//显示修改前的课程详细信息
function viewInfoBeforeModify(){
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/course/viewDetail",
		dataType : 'json',
		data:{courseId:courseId},
		success : function(data) {
			if (data.ret) {
				if ($.isEmptyObject(data.data)) {
					alert("获取详细信息失败！！");
				} else {
					courId=data.data.courId;
					oriEvaMetId=data.data.courTepaId;
					$("#courCode").val(data.data.courCode);
					$("#courName").val(data.data.courName);
					$("#courCredit").val(data.data.courCredit);
					$("#courType").attr("value",data.data.courType);
					$("#searchCourseYear").attr("value",data.data.courYear);
					$("#searchCourTerm").attr("value",data.data.courTerm);
					$("#searchCourTermPhase").attr("value",data.data.courStudyPhase);
					$("#searchArtScience").attr("value",data.data.courArtScience);
					$("#courCreatorName").val(data.data.courCreatorName);
					$("#courCreateTime").val(data.data.courCreateTime);
					$("#courTimeSchedule").val(data.data.courTimeSchedule);
					$("#departID").val(data.data.departName);
					$("#courState").val(data.data.courState);
					$("#scoreLimit").val(data.data.courTestLimitScore);
					//主讲教师
					
					//获取id
					if(data.data.courTeaIds!=null && data.data.teaNames!=null){				
						courTeaacherId=data.data.courTeaIds;
						var courTeaIds=data.data.courTeaIds.substring(1,data.data.courTeaIds.length-1);
						var aCourTeaIds=new Array();
						aCourTeaIds=courTeaIds.split(",");
						//获取name
						var aTeaNames=new Array();
						aTeaNames=data.data.teaNames.split(",");
						for ( var y = 0; y < aCourTeaIds.length; y++) {
							var str="<span id='main"+aCourTeaIds[y]+"' >";			
							str+=aTeaNames[y]+"&nbsp;"+
							"<a onclick='delTeacher(\"main"+aCourTeaIds[y]+"\");' "+ 
							"class='btn btn-sm btn-danger'><i class='fa fa-minus'></i></a></span>";
							str+="&nbsp;&nbsp;&nbsp;&nbsp;";	
							$("#mainTeachers").append(str);
						}
					}
					
					//辅导教师
					if (data.data.mentroTeaNames==undefined || data.data.mentroTeaNames == "" ) {
						$("#mentroTeaNames").val("未指定");
					}else{
						$("#mentroTeaNames").val(data.data.mentroTeaNames);
					}
					$("#descript").val(data.data.courDescribe);
					//考核方式
					$("#evalMode").val(data.data.courTepaId);
					//公开状态
					if (data.data.courOpenToAll==false) {
						$("#publicState").val(0);
					}else{
						$("#publicState").val(1);
					}
/*					if(data.data.departType==1){
						$("#publicState").val(1);
						$("#publicState").attr("disabled","disabled");
					}*/
					//课程领域
					var str=data.data.courCateids.substring(1,data.data.courCateids.length-1);
					var aCourseCate=new Array();
					aCourseCate=str.split(",");
					for ( var int = 0; int < aCourseCate.length; int++) {
						$('input[value="' + aCourseCate[int] + '"]').attr("checked","true");
					}
					//附件显示
					if(data.data.courCoverPictureId==undefined||data.data.courCoverPictureId==""){
						$("#uploadPicOrNot").append("<p>没有上传图片！</p>");
					}else{
						prePicId=data.data.courCoverPictureId;
						pictureState=1;
						$("#attachment")
						.append(
								"<img id='"+attachesIds+"1' alt='"+data.data.coverPicAttachName+"' src='../../"+data.data.coverPicLocation+data.data.coverPicRealName+"'>");
					}}
			} else {
				alert(data.errmsg);
			}
			$("#courCateid").empty().append(courseCateList);
		}
	});
}
var courCode="",courName="",courCredit="",evalMode="",courTimeSchedule="";courtype="";
var courseyear = 0,courseterm = 0,coursetermphase = 0,courseartscience = 0;
var descript="",courCateids="",seleteSchoolID="",publicState="";
//提交修改后的的课程
function modifyCourseM() {
	courCode=$("#courCode").val();
	courName=$("#courName").val();
	courtype=$("#courType").val();
	courCredit=$("#courCredit").val();
	evalMode=$("#evalMode").val();
	seleteSchoolID=$("#seleteSchoolID").val();
	courTimeSchedule=$("#courTimeSchedule").val();
	courseyear = $("#searchCourseYear").val();
	var CourseTeacher = $("#CourseTeacher").val();
	courseterm = $("#searchCourTerm").val();
	coursetermphase = $("#searchCourTermPhase").val();
	courseartscience = $("#searchArtScience").val();
	descript=$("#descript").val();
	var scoreLimit=$("#scoreLimit").val();
	var courCateids="";
	$("input[name='checkCourseClass']").each(function() {
		var $current = $(this);
		if ($current.attr('checked') != undefined)
		{
			if (courCateids=="") {
				courCateids+=","+$current.val()+",";
			}else{
				courCateids+=$current.val()+",";
			}
		}
	});
	//获取主讲教师
	var teacherId=",";
	var spans=$("span[id^='main']");
	if(spans!=null&&spans.length>0){
		$.each(spans,function(index,item){
			teacherId+=item.id.substr(4)+",";
		});
	}
	if(teacherId==","){
		teacherId="";
	}
	publicState = $("#publicState").val();
	moveto("pictures");//页面滚动到顶部，便于用户查看错误提示信息
	if(courCode=="" || isNaN(courCode)){
		infoNotice("error", "输入错误！", "课程代码必填且必须是数字！",$('#data-grid'));
		$("#courCode").focus();
	}else if(courName==""){
		infoNotice("error", "输入错误！", "课程名称不能为空！",$('#data-grid'));
		$("#courName").focus();
	}else if(courCredit=="" || isNaN(courCredit)||courCredit<=0){
		infoNotice("error", "输入错误！", "学分必填且必须是非负的数字！",$('#data-grid'));
		$("#courCredit").focus();
	}else if(courCateids=="" || courCateids=="," ){
		infoNotice("error", "选择错误！", "请选择课程领域！",$('#data-grid'));
	}else if(descript==""){
		infoNotice("error", "输入错误！", "课程描述不能为空！",$('#data-grid'));
		$("#descript").focus();
	}else if(CourseTeacher == ""){
		infoNotice("error", "选择错误！", "请设置主讲教师！",$('#data-grid'));
		return false;
	}else if(isNaN(scoreLimit)||scoreLimit>100||scoreLimit<0){
		infoNotice("error", "输入错误！", "测试限制需为0到100的数值！",$('#data-grid'));
		return false;
	}else{
		$("#Mod").modal('show');
		$.ajax({
			type : 'post',
			contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
			url : "../../handler/course/modify",
			dataType : 'json',
			data:{
				courId:courId,
				courCateids:courCateids,
				courTepaId:evalMode,
				courCode:courCode,
				courName:courName,
				courType:courtype,
				courYear:courseyear,
				courTerm:courseterm,
				courStudyPhase:coursetermphase,
				courArtScience:courseartscience,
				courCredit:courCredit,
				courTimeSchedule:courTimeSchedule,
				courDescribe:descript,
				courCoverpictureid:prePicId,
				seleteSchoolID:seleteSchoolID,
				courTeacherIds:teacherId,//CourseTeacher,
				courOpenToAll:publicState,
				courState:$("#courState").val(),
				courTestLimitScore:scoreLimit
				},
				success : function(data){
					$("#Mod").modal('hide');
					if(data.ret){	
						alert("修改课程成功！");
						if(evalMode!=oriEvaMetId){
							alert("考核方式已变动，请尽快更新该课程的成绩！");
						}
						window.location.href="eduman_Courses_Manage.html?firstCol=9&secondCol=37";
					}else{
						alert(data.errmsg);
					}
				}
		});
	}
}


/**
 * 根据departId获取教师列表，用于设置主讲教师和辅导教师
 * userType为1表示学生
 * userType为2表示老师
 */
function getTeachers(userType){
	$.post("../../handler/user/viewUsersByTypeAndDepa", {
		"userType" :userType,"depaId":departId
	}, function(data) {
		if (data.ret) {
			if(data.data.users!=undefined||data.data.users.length>0){
				var teachertStrs="";
				for(var i=0;i<data.data.users.length;i++){
					if(courTeaacherId==data.data.users[i].userId){
						userDepaId=data.data.users[i].userDepaId;
						teachertStrs+="<option value='"
							+data.data.users[i].userId+"' selected=selected>"
							+data.data.users[i].userRealname
							+"</option>";
					}
					else {
						teachertStrs+="<option value='"
						+data.data.users[i].userId+"'>"
						+data.data.users[i].userRealname
						+"</option>";
						}
				}
				$("#CourseTeacher").empty().append(teachertStrs);
			}
		} else {
			infoNotice("error", "", data.errmsg);
		}
	}, "json");
}
//选择学校
function selceteSchool() {
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/department/viewDepartments",
		dataType : 'json',
		data:{type:3},
		success : function(data) {
			var schoolList;
			if (data.ret) {
				if ($.isEmptyObject(data.data.departments)) {
					schoolList = "<option value='-1'>学校数据为空！！</option>";
				} else {
					schoolList += "<option value=\"0\">所有机构</option>";
					$.each(data.data.departments, function(i, val) {
						if(userDepaId==val.depaId){
							schoolList += "<option value='" + val.depaId + "' selected=selected>" + val.depaName + "</option>";
						}
						else {
							schoolList += "<option value='" + val.depaId + "'>" + val.depaName + "</option>";
						}
					});
				}

			} else {
				schoolList = "<option value='-1'>"+data.errmsg+"</option>";
			}
			$("#seleteSchoolID").empty().append(schoolList);
		}
	});
}
