/**
 * @author htx
 * @version 系统管理-系统参数设置-主函数
 */
var paramid = 0;// 修改的用户id
var ex=/^(\+|-)?\d+$/;
$(document).ready(function() {
	// back To Top
	backToTop();
	ShowColumn();
	getAllParameter();
	// 绑定基本信息提交按钮
	$('#modparam').click(function() {
		updateParameter();
	});
	/*// 绑定返回按钮
	$('#cancelmod').click(function() {
		location.href = "sysman_Parameters.html?firstCol=12&secondCol=49";
		return false;
	});*/
});

//获取参数
function getAllParameter() {
	$.ajax({
		type : "post",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		url : '../../handler/sypaController/viewAllSypa',
		async : false,
		dataType : 'json',
		data : {
			"pType" : 0
		},
		success : function(data) {
			$.each(data.data.result, function(i, data) {
				if (data.sypaType < 10) {
					html = "<form class='form-horizontal'><div class='form-group col-sm-12'><label class='control-label col-sm-2' >" + data.sypaName
					+ "</label><div class='col-sm-4'>";
					if (data.sypaEnName == 'sensitiveWords' || data.sypaEnName == 'examInfo'||data.sypaEnName == 'fileFormats'
						|| data.sypaEnName == 'copyright' || data.sypaEnName == 'studyModule'||data.sypaEnName=='welcomeMsg') {
						html += "<textarea class='form-control' style='height: 100px; width: 310px;' name='" + data.sypaEnName + "' id='" + data.sypaId
						+ "' value='" + data.sypaValue + "'>" + data.sypaValue + "</textarea > ";
					} else if (data.sypaEnName == "cheatingControl" || data.sypaEnName == "register"||data.sypaEnName == "isShowTeacher") {
						if(data.sypaValue=="是"){
							html += "<input type='radio' class='span1 "+ data.sypaId+"' name='" + data.sypaEnName + "' value='是'  checked/>是 &nbsp;&nbsp;";
							html += "<input type='radio' class='span1 "+ data.sypaId+"'name='" + data.sypaEnName + "' value='否'/>否 &nbsp;&nbsp;";
						}else
						{
							html += "<input type='radio' class='span1 "+ data.sypaId+"' name='" + data.sypaEnName + "' value='是'/>是 &nbsp;&nbsp;";
							html += "<input type='radio' class='span1 "+ data.sypaId+"'name='" + data.sypaEnName + "' value='否' checked/>否 &nbsp;&nbsp;";
						}
					}else if (data.sypaEnName == "uniqueLogin") {
						if(data.sypaValue=="1"){
							html += "<input type='radio' class='span1 "+ data.sypaId+"' name='" + data.sypaEnName + "' value='1'  checked/>是 &nbsp;&nbsp;";
							html += "<input type='radio' class='span1 "+ data.sypaId+"'name='" + data.sypaEnName + "' value='0'/>否 &nbsp;";
						}else
						{
							html += "<input type='radio' class='span1 "+ data.sypaId+"' name='" + data.sypaEnName + "' value='1'/>是 &nbsp;&nbsp;";
							html += "<input type='radio' class='span1 "+ data.sypaId+"'name='" + data.sypaEnName + "' value='0' checked/>否 &nbsp;";
						}
					} else {
						html += "<input type='text' class='form-control' name='" + data.sypaEnName + "' id='"
						+ data.sypaId + "' value='" + data.sypaValue + "'></input> ";
					}
					html += "</div><div class='col-sm-4'><span class='help-inline'>" + data.sypaRemark + "</span></div></div>";
					if (data.sypaType == 1) {
						$('#study_center').append(html);
					} else if (data.sypaType == 2) {
						$('#bbs_center').append(html);
					} else {
						$('#system_center').append(html);
					}
				}
			});
		}
	});
}


//更新参数
function updateParameter() {
	var val = "";
	var flag = 0;
	$.each($("input[class='form-control']"), function(index, item) {
		var value=$(item).val().replace(/,/g,'，');
		if($(item).attr('name')!='systemName'&&!ex.test(value)){
			//$(item).attr("for","inputError");
			flag=1;
		}else if($(item).attr('name')=='systemName'&&value.length>20){
			flag=2;
		}
		else{
			val = val + $(item).attr('id')+ "～" + value + ",";	 
		}
	});
	$.each($('textarea'),function(index,item){
		val = val +$(this).attr('id') + "～" +$(this).val().replace(/；/g,';').replace(/,/g,'，').replace(/～/g,'~') + ",";
	});
	$.each($('input:radio:checked'),function(index,item){
		val = val +$(item).attr('class').split(' ')[1]+"～"+$(item).val().replace(/,/g,'，').replace(/～/g,'~')+",";
	});
	if(flag==0){
		$.ajax({
			type : "post",
			contentType : "application/x-www-form-urlencoded;charset=UTF-8",
			url : '../../handler/sypaController/updateSypa',
			async : false,
			data : {
				arrayList : val
			},
			dataType : 'json',
			success : function(data) {
				infoNotice("success", "", data.data.state);
			}
		})
	}else if(flag==1){
		infoNotice("error", "", "数字格式错误!");
	}else if(flag==2){
		infoNotice("error", "", "系统名长度过长!");
	}else{
		infoNotice("error", "", "格式错误！");
	}	 
	moveto("column");
	return;
}
