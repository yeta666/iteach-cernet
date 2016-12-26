/**
 * 主函数
 * @author anny
 * 
 */
var school;
var grade;
//var stuClass;
//var yourname;
var yourCID;
var youerrealname;
$(document).ready(function() {
	//$("#yourname").val("");

	showWelcomeMsg();
	showLinks();
	$("#yourCID").val("");
	$("#youerrealname").val("");
	$.getJSON("../../handler/register/department", function(data) {
		$.each(data.data.depatement, function(i, depatement) {
			$('#school').append("<option value=" + depatement.depaId + ">" + depatement.depaName + "</option>");
		});
	});
	//下拉列表框中选择一个就请求相应的数据
	$("#upmes").click(function() {
	//	yourname = $("#yourname").val();
		yourCID = $("#yourCID").val();
		youerrealname = $("#youerrealname").val();
		school = $('#school').val();
		grade = $('#grade').val();
		//stuClass = $('#class').val();
		if(school=='0'){
			alert("请选择学校信息！");
		}
		else if(grade=='0'){
			alert("请选择年级信息！");
		}
		else if(youerrealname==""||yourCID==""){
			alert("请填写完整个人信息！");
		}
		else{
			$.post("../../handler/user/confirmUserInfo.do",{
				//userLoginname:yourname,
				userIdNum:yourCID,
				userRealname:youerrealname,
				depaId:school,
				gradId:grade,
				//clasId:stuClass
				},function(data){
				if(data.data.uLoginname!=null) {
					var a=data.data.uLoginname;
					alert("用户名："+a+";"+"密码："+a);
					window.location.href='home.html';
				}else {
					alert("信息有误，请重新填写！");
				}
			},"json");
		}
	});
});


$('#school').change(function() {
	var depa_id = $(this).val();
	$.post("../../handler/register/gradeInfo", {
		depa_id : depa_id
	}, function(data) {
		$('#grade').children().remove();
		$('#grade').append("<option value='0'>--请选择年级--</option>");
		$.each(data.data.gradeInfo, function(i, data) {
			$('#grade').append("<option value=" + data.gradId + ">" + data.gradName + "</option>");
		});
	});
});

/*$('#grade').change(function() {
	var grade_id = $(this).val();
	$.post("../../handler/register/classesInfo", {
		grade_id : grade_id
	}, function(data) {
		$('#class').children().remove();
		$('#class').append("<option  value='0'>--请选择班级--</option>");
		$.each(data.data.classesInfo, function(i, data) {
			$('#class').append("<option value=" + data.clasId + ">" + data.clasName + "</option>");
		});
	});
});*/
//获取首页栏目条
//首页欢迎信息
function showWelcomeMsg(){
	$.ajax({
		type : "post",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		url : '../../handler/sypaController/viewAllSypa',
		async : true,
		data : {
			"pType" : 3
		},
		dataType : 'json',
		success : function(data) {
			if(data==""&&data==null){
				alert("获取数据失败，请联系数据库管理员！");
				return;
			}else{
			var resultData = data.data.result;
			var welcomeHtml = "";
			
			$.each(resultData, function(DataIndex, index) {
				if(index.sypaEnName=="welcomeMsg")
					welcomeHtml += index.sypaValue;
				if(index.sypaEnName=="systemName")
					title1 = index.sypaValue;
			});
			$("#getnav").html("<div class=\"setTitle nav navbar-nav \">"+title1+"&nbsp;<span class='titleSmalll'>赛尔网络下一代互联网技术创新项目</span>"+"</div><ul class=\"nav navbar-nav navbar-right \">"
					+"<li><a href=\"home.html\">返回首页</a></li></ul>");
			$("#welcomemsg").html(welcomeHtml);
		   }
	    }
	});


	if ($.cookie("parameters") != null && $.cookie("parameters") != "") {
		var parCookie = $.cookie("parameters");
		var parameters = eval(parCookie);
		var welcomeHtml = "ttt";
		for ( var i = 0; i < parameters.length; i++) {
			if (parameters[i].sypaEnName == "welcomeMsg") {
				welcomeString = parameters[i].sypaValue;
				welcomeHtml += welcomeString;
				$("#welcomemsg").empty().append(welcomeHtml);
			}
		}
	}
}
function showLinks() {
	$.ajax({
		type : "post",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		url : '../../handler/sypaController/viewAllSypa',
		async : false,
		data : {
			"pType" : 10
		},
		dataType : 'json',
		success : function(data) {
			if(data==""&&data==null){
				alert("获取数据失败，请联系数据库管理员！");
				return;
			}else{
				var resultData = data.data.result;
				var linkHtml = "";
				$.each(resultData, function(DataIndex, index) {
					linkHtml += "<li class='liststyle-doc'><a target='_blank' id='" + index.sypaId + "' name='" + index.sypaName
					+ "' href='http://" + index.sypaValue + "'>" + index.sypaName + "</a></li>";
				});
				$("#link").html(linkHtml);
			}
			
		}
	});
}