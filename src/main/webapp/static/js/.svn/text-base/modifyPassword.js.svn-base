var passCode = "";
var newCode = "";
var reNewCode = "";
$(document).ready(function() {
	ShowColumn();
	// back To Top
//	backToTop();
	$("#saveModify").click(function() {
		passCode = $("#passcode").val();
		newCode = $("#newcode").val();
		reNewCode = $("#renewcode").val();
	//	alert(passCode+"!"+newCode+"!"+reNewCode);
		if(passCode == null || passCode == ""){
			alert("请输入原密码!");
			clean();
		}
		else if(newCode == null || newCode == ""){
			alert("请输入新密码!");
			clean();
		}
		else if(reNewCode == null || reNewCode == ""){
			alert("请再次输入新密码!");
			clean();
		}
		else{
			if(newCode==reNewCode){
				passCode = hex_md5(passCode);
				newCode = hex_md5(newCode);
				$.post("../../handler/user/modifyUserPwd.html",{ 
					"oldPwd":passCode,
					"newPwd":newCode,
					"userId":userId
				},function(data){
					if(data.data.status==1){
						alert("修改密码成功！");
						clean();
					}
					else if(data.data.status==0){
						alert("旧密码错误，修改密码未成功！");
						clean();
					}
				},"json");
			}else{
				alert("密码不一致，请重新输入！");
				clean();
			}
		}
	});
});
/*重置页面*/
function clean(){
	 $("#passcode").val("");
	 $("#newcode").val("") ;
	 $("#renewcode").val("");
}