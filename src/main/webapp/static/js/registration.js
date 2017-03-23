var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
var title1="";
$(document).ready(function() {
	showWelcomeMsg();
	backToTop();// back To Top
	// 新的时间插件显示示例
	$('#startYear').datetimepicker({
		language : "zh-CN",
		autoclose : 0,// 时间选择完成后，不自动关闭datetimepicker
		todayBtn : 1,
		weekStart : 1,// 一周日期以周一为第一天
		todayHighlight : 1,
		startView : 3,// 日期时间选择器打开之后首先显示的视图为：一年的12个月
		minView : 3,// 时间选择精确到：年月
		forceParse : 1// 如果用户输入不正确的日期，强行解析用户输入的日期
	});

	/*timepicker("startYear");*/
	$('#role').change(function(){
		if($('#role').find('option:selected').val()==2){
			$('#ID1').hide();
			$('#grade').hide();
			$('#class').hide();
			$('#ID2').hide();
		}
		else{
			$('#ID1').show();
			$('#grade').show();
			$('#class').show();
			$('#ID2').show();
		}
	});
	$('#register').click(function() {
		submit();
	});
	$.getJSON("../../handler/register/department", function(data) {
		$.each(data.data.depatement, function(i, depatement) {
			$('#school').append("<option value=" + depatement.depaId + ">" + depatement.depaName + "</option>");
		});
	});
});

//下拉列表框中选择一个就请求相应的数据
$('#school').change(function() {
	var depa_id = $(this).val();
	$.post("../../handler/register/gradeInfo", {
		depa_id : depa_id
	}, function(data) {
		$('#grade').children().remove();
		$('#grade').append("<option value='0'>请选择年级</option>");
		$.each(data.data.gradeInfo, function(i, data) {
			$('#grade').append("<option value=" + data.gradId + ">" + data.gradName + "</option>");
		});
	});
});

$('#grade').change(function() {
	var grade_id = $(this).val();
	$.post("../../handler/register/classesInfo", {
		grade_id : grade_id
	}, function(data) {
		$('#class').children().remove();
		$('#class').append("<option  value='0'>请选择班级</option>");
		$.each(data.data.classesInfo, function(i, data) {
			$('#class').append("<option value=" + data.clasId + ">" + data.clasName + "</option>");
		});
	});
});

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
			if ($.cookie("user") == null || $.cookie("user") == "") {
				unLoginData = {
						"userName" : "登录",
						"userLoginname" : ""
				};
				$("#getnav").html("<div class=\"setTitle nav navbar-nav \">"+title1+"</div><ul class=\"nav navbar-nav navbar-right \">");
			} else {
				$("#getnav").html("<div class=\"setTitle nav navbar-nav \">"+title1+"</div><ul class=\"nav navbar-nav navbar-right \">"
						+"<li><a class='changeTitleright' href=\"userCenter.html?firstCol=1&secondCol=14\">进入系统</a></li></ul>");
			}
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

//提交注册
function submit() {
	var userName = $('#userName').val().replace(/\s/g,"");
	var realName = $('#realName').val().replace(/\s/g,"");
	var sex = $('input:radio:checked').val();
	var password = $('#password').val().replace(/\s/g,"");
	var confirmPassword = $('#confirmPassword').val().replace(/\s/g,"");
	var school = $('#school').val();
	var grade = $('#grade').val();
	var stuClass = $('#class').val();
	var role = $('#role').find('option:selected').val();
	var startYear = $('#startYear').val().replace(/\s/g,"");
	var stuNum = $('#stuNum').val().replace(/\s/g,"");
	var idNum = $('#ID').val();
	var email = $('#email').val().replace(/\s/g,"");
	var telNum = $('#telNum').val().replace(/\s/g,"");
	var other = $('#other').val().replace(/\s/g,"");
	var address = $('#address').val().replace(/\s/g,"");
	var filter = /[^\s]*[A-Za-z0-9]{4,15}[^\s]*$/;
	//alert(school);
	if (userName==""||!filter.test(userName)) {
		infoNotice("error", "输入错误", "用户名为空或不符合格式要求！");
		moveto("infomsg");
		return false;
	}
	else if (realName=="") {
		infoNotice("error", "输入错误", "真实姓名为空！");
		moveto("infomsg");
		return false;
	} 
	else if (password==""||!filter.test(password)) {
		infoNotice("error", "输入错误", "密码不符合格式要求！");
		moveto("infomsg");
		return false;
	}
	else if (password != confirmPassword) {
		infoNotice("error", "输入错误", "两次输入的密码不一致！");
		return false;
	}	
	else if (school<=0) {
		infoNotice("error", "输入错误", "请选择正确的学校！");
		return false;
	} 
	else if(email!="" && !myreg.test(email)){
		infoNotice("error", "输入错误", "邮箱格式不对！");
		return false;
	}
	else if(idNum!="" && !isIdCardNo(idNum)){
		infoNotice("error", "输入错误", "身份证信息错误！");
		return false;
	}
	
	else if (role == "1") {
		if(stuClass==0||grade ==0||confirmPassword.replace(/\s/g,"") == "" || startYear.replace(/\s/g,"") == ""){
			infoNotice("error", "输入错误", "请选择正确的学校、年级、班级！");
			return false;
		}
		else{
			idNum = idNum.replace(/\s/g,"");
			password = hex_md5(password);
			$.ajax({
				type : "post",
				contentType : "application/x-www-form-urlencoded;charset=UTF-8",
				url : "../../handler/register/registerUser",
				data : {
					userLoginname : userName,
					userRealname : realName,
					userGender : sex,
					userPwd : password,
					userDepaId : school,
					userClasId : stuClass,
					userIdNum : idNum,
					userYearOfEntrance : startYear,
					userCadasExamNum : stuNum,
					userEmail : email,
					userPhoneNum : telNum,
					userType : role,
					userAddress : address,
					userRemark : other
				},
				dataType : 'json',
				success : function(data) { // 请求成功时执行的回调函数
					var authenticationInfo = data.data.authenticationInfo;
					var insertStatus = data.data.status;
					if (authenticationInfo != null && authenticationInfo != "")
						alert(authenticationInfo);
					if (insertStatus == 1)
						infoNotice("success", "", "注册成功！");
					else if (insertStatus == 2)
						infoNotice("error", "", "注册失败，用户名已存在！");
					else if (insertStatus == 3)
						infoNotice("success", "", "注册失败!");
				}
			});
		}
	} else {
		password = hex_md5(password);
		$.ajax({
			type : "post",
			contentType : "application/x-www-form-urlencoded;charset=UTF-8",
			url : "../../handler/register/registerUser",
			data : {
				userLoginname : userName,
				userRealname : realName,
				userGender : sex,
				userPwd : password,
				userDepaId : school,
				userYearOfEntrance : startYear,
				userIdNum : idNum,
				userEmail : email,
				userPhoneNum : telNum,
				userType : role,
				userAddress : address,
				userRemark : other
			},
			dataType : 'json',
			success : function(data) { // 请求成功时执行的回调函数
				var authenticationInfo = data.data.authenticationInfo;
				var insertStatus = data.data.status;
				if (authenticationInfo != null && authenticationInfo != "")
					alert(authenticationInfo);
				if (insertStatus == 1)
					infoNotice("success", "", "注册成功！");
				else if (insertStatus == 2)
					infoNotice("error", "", "注册失败，用户名已存在！");
				else if (insertStatus == 3)
					infoNotice("error", "", "注册失败，后台报错！");
			}
		});
	}
	// 身份证验证
	function isIdCardNo(num) 
	{
		//var factorArr = new Array(7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2,1);

		//var varArray = new Array();

		//var lngProduct = 0;
		//var intCheckDigit;
		var intStrLen = num.length;
		//var idNumber = num;    
		// initialize
		if ((intStrLen != 15) && (intStrLen != 18)) {
			 error = "输入身份证号码长度不对！";
			alert(error);
			// frmAddUser.txtIDCard.focus();
			return false;
		}    
		/*// check and set value
		for(i=0;i<intStrLen;i++) {
			varArray[i] = idNumber.charAt(i);
			if ((varArray[i] < '0' || varArray[i] > '9') && (i != 17)) {
				// error = "错误的身份证号码！.";
				// alert(error);
				// frmAddUser.txtIDCard.focus();
				return false;
			} else if (i < 17) {
				varArray[i] = varArray[i]*factorArr[i];
			}
		}
		if (intStrLen == 18) {
			// check date
			var date8 = idNumber.substring(6,14);
			if (checkDate(date8) == false) {
				// error = "身份证中日期信息不正确！.";
				// alert(error);
				return false;
			}        
			// calculate the sum of the products
			for(i=0;i<17;i++) {
				lngProduct = lngProduct + varArray[i];
			}        
			// calculate the check digit
			intCheckDigit = 12 - lngProduct % 11;
			switch (intCheckDigit) {
			case 10:
				intCheckDigit = 'X';
				break;
			case 11:
				intCheckDigit = 0;
				break;
			case 12:
				intCheckDigit = 1;
				break;
			}        
			// check last digit
			if (varArray[17].toUpperCase() != intCheckDigit) {
				// error = "身份证效验位错误!...正确为： " + intCheckDigit + ".";
				// alert(error);
				return false;
			}
		} */
		return true;
	}
}