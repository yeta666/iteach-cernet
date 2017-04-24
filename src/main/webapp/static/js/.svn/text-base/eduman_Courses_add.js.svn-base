/*
 *  author：郭海蓉 
 *  time:2013-07
 */
var searchType = "", searchWord = "", seleteSchoolID = "";
var courseCateId = "", courseCateList = "", evaMethods = "";// 搜索变量
var courId = "";
$(document).ready(function() {
	ShowColumn();// 显示功能栏
	backToTop();// 回顶部
	if (userId <= 0) {
		/** 加载分页 */
		$('#pagination').hide();
	} else {
		paginationPage();
	}
	$('#eduman').attr("class", "active");
	//获取课程领域
	selceteCourseCateNames();
	//获取课程考核方式
	selceteEvaluate();
	//选择设置主讲老师
	if (departmentTypeID ==1) {//市级管理员
		//$("#publicState").attr("disabled","disabled");
		$("#seleteSchoolID").show();
		selceteSchool();
		departId=0;
		getTeachers(2);
		$("#seleteSchoolID").change(function(){ //学校改变时显示该学校下的可选课程
			$("#CourseTeacher").empty();
			departId = $("#seleteSchoolID").val();
			getTeachers(2);
		});
	}else{
		$("#seleteSchoolID").hide();
		getTeachers(2);
	}

});
//获取领域
function selceteCourseCateNames() {
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/courseCategory/viewAllCates",
		dataType : 'json',
		data : {
			isOpen:1
		},
		success : function(data) {
			if (data.ret) {
				if ($.isEmptyObject(data.data.courseCates)) {
					courseCateList = "<label class='checkbox-inline'> "
						+ "<input name='checkCourseClass' value='-1' type='checkbox'>当前没有可选课程领域！" + "</label>";
				} else {
					$.each(data.data.courseCates, function(i, val) {
						courseCateList += courseCateList = "<label class='checkbox-inline'> "
							+ "<input name='checkCourseClass' value='" + val.cocaId + "' type='checkbox'>"
							+ val.cocaName + "</label>";
					});
				}

			} else {
				courseCateList = "<label class='checkbox-inline'> "
					+ "<input name='checkCourseClass' value='-1' type='checkbox'>" + data.errmsg + "</label>";
			}
			$("#courCateid").empty().append(courseCateList);
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
		data : {},
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
				evaMethods = "<option value='-1'>" + data.errmsg + "</option>";
			}
			$("#evaluationMode").empty().append(evaMethods);
		}
	});
}
var code = "", name = "", courCredit = "", evaluationMode = "", courCateids = "";
var description = "", courTimeSchedule = "",publicState="";
//提交新增的课程
function addNewCourseM() {
	$("#infomsg").empty();
	code = $("#code").val();
	courTimeSchedule = $("#courTimeSchedule").val();
	name = $("#name").val();
	courCredit = $("#courCredit").val();
	courCateids = "";
	$("input[name='checkCourseClass']").each(function() {
		var $current = $(this);
		if ($(this).is(":checked")) {
			if (courCateids == "") {
				courCateids += "," + $current.val() + ",";
			} else {
				courCateids += $current.val() + ",";
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
	publicState = $("#publicState").val();
	//获取考核方式
	evaluationMode = $("#evaluationMode").val();
	var scoreLimit=$("#scoreLimit").val();
	description = $("#description").val();
	var courtype = $("#courType").val();
//?

	var courseyear = $("#searchCourseYear").val();
	var courseterm = $("#searchCourTerm").val();
	var coursetermphase = $("#searchCourTermPhase").val();
	var courseartscience = $("#searchArtScience").val();
//?
	moveto("pictures");//页面滚动到顶部，便于用户查看错误提示信息
	if (code == "" || isNaN(code)){
		infoNotice("error", "输入错误！", "课程代码必填且必须是数字！",$('#data-grid'));
		return false;
	} else if (name == "") {
		infoNotice("error", "输入错误！", "课程名称不能为空！",$('#data-grid'));
		return false;
	} else if (courCredit == "" || isNaN(courCredit)) {
		infoNotice("error", "输入错误！", "学分必填且必须是数字！",$('#data-grid'));
		return false;
	} else if (description == "") {
		infoNotice("error", "输入错误！", "课程描述不能为空！",$('#data-grid'));
		return false;
	} else if (courCateids == "") {
		infoNotice("error", "输入错误！", "请选择课程所属领域！",$('#data-grid'));
		return false;
	}else if(teacherId == ""){
		infoNotice("error", "选择错误！", "请设置主讲教师！",$('#data-grid'));
		return false;
	}else if(isNaN(scoreLimit)||scoreLimit>100||scoreLimit<0){
		infoNotice("error", "输入错误！", "测试限制需为0到100的数值！",$('#data-grid'));
		return false;
	} else {
		$.ajax({
			type : 'post',
			contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
			url : "../../handler/course/create",
			dataType : 'json',
			data : {
				courYear:courseyear,
				courTerm:courseterm,
				courStudyPhase:coursetermphase,
				courArtScience:courseartscience,
				courCateids : courCateids,
				courTepaId : evaluationMode,
				courCode : code,
				courType: courtype,
				courName : name,
				courCredit : courCredit,
				courCreateUserid : userId,
				courDescribe : description,
				courVerify :1,
				courTimeSchedule : courTimeSchedule,
				courCoverpictureid : prePicId,
				courTeacherIds:teacherId,
				courState:$("#courState").val(),
				courOpenToAll:publicState,
				courTestLimitScore:scoreLimit
			},
			success : function(data) {
				if (data.ret) {
					alert("新增课程成功！");
					window.location.href = "eduman_Courses_Manage.html?firstCol=9&secondCol=37";
				} else {
					alert(data.errmsg);
				}
			}
		});
	}
}
function resetContent() {
	$("#code").val("");
	$("#name").val("");
	$("#courCredit").val("");
	$("#description").val("");
	$("#courTimeSchedule").val("");
	$('input[name="checkCourseClass"]').each(function() {
		var $current = $(this);
		$(this).removeAttr("checked");
	});
}