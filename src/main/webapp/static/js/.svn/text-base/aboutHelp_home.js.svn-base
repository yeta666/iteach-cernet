/**
 * @author anny
 */
var userT;//用户种类

$(document).ready(function() {
	ShowColumn();
	userT = userType;
	if ($.cookie("user") == null || $.cookie("user") == "") {
		$("#publicplace").hide();
	}else{
		$("#publicplace").show();
	}
	
	if(userT==1){
		$("#studentPlace").show();
	}
	else if(userT==2){
		$("#teacherPlace").show();
	}
	else if(userT==3){
		$("#ManagerPlace").show();
	}
	else if(userT==4){
		$("#SystemPlace").show();
	}
	
	
});
