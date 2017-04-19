var chapId;
var courId;
var rid;
var coursename;
var colIds = "";
var ClosesWindow = true;
function CheckWindowClosed()
{
	$.cookie('colVideo','0');
}
$(document).ready(function(){
	// back To Top
	ShowColumn();
	colIds = urlColHtml();
	backToTop();
	courId = getRequest("courId");
	//alert(courId);
	chapId = getRequest("chapId");
	rid = getRequest("rid");
	$.getJSON("../../handler/chapter/viewChapterListByCourse.do",{
		"courId":courId,
	},function(data){
		var blockDivData = data.data.chapterList;
		coursename= data.data.course.courName;
		createChapter(blockDivData);
		//显示课程图片
		var courseimtstr = "<img src=\"../../"+data.data.course.courImg+data.data.course.fileName+"\" /  style='width: 100%; padding-left: 30px;'>";
		$("#courseimg").append(courseimtstr);
	});
	$.getJSON("../../handler/resource/viewResourceListByChapter.do",{
		"chapId":chapId,
		"page":-1,
		"rows":-1,
	},function(data){
		var blockDivDatadoc = data.data.documents;
		var blockDivDatalink = data.data.links;
		doc(blockDivDatadoc);
		link(blockDivDatalink);
	});
	//切换右侧栏 start
	$('#mytab a[href="#bbs"]').click(function(e){
		e.preventDefault();
		$('#mytab a:last').tab('show');

	});
	$('#mytab a[href="#chaper"]').click(function(e){
		e.preventDefault();
		$('#mytab a:first').tab('show');
	});
	//切换右侧栏 end
	//getchapterID();
	ShowSubColumn1();
	/*JS控制F5键刷新cookie清除*/
	document.onkeydown = function(e){
		e=e||window.event;
		  if(e.keyCode==116){//116 是f5按键代码
			  $.cookie('colVideo','0');
		  }
		};
});
/**
 * 获取课程名称-生成代码
 */
/*function getCouTitel(courTitel){
	var title = "<h4>"+courTitel+"</h4>";
	$("#videoTitel").append(title);
}*/



function createChapter(blockDivData){
	var $records=$('#courseresolist');
	$records.empty();
	var data = blockDivData;
	var resoliststr = "";
	var currvediostr = "";
	$.each(data,function(entryIndex,entry){
		var res = null;
		if(entry.resources.length!=0){
			var resocount = 1;
			for(var j=0;j<entry.resources.length;j++){
				if(entry.resources[j].resoType==3){
					res = entry.resources[j].resoId;
					var activestr ="";
					var resoDescribe ="";
					if(res==rid){
						activestr = " class=\"active1\"";				
						currvediostr ="<i class=\"fa fa-video-camera\"></i>&nbsp;"+entry.chapOrdinal+"."+resocount+" "+entry.resources[j].resoTitle+"<button type='button' class='btn btn-outline btn-warning' value='"+chapId+"' onclick='showGetMark();'>"+"点击评价该章节内容"+"</button>";
						resoDescribe = "<p><small>"+entry.resources[j].resoDescribe+"</small></p>";
						$("#resoDescribe").empty().append(resoDescribe);
					}
					resoliststr = resoliststr +"<h4><li"+activestr+"><a href=\"learning_video.html?"+colIds+"&rid="+res+"&courId="+courId+"&chapId="+chapId+"\"><span class=\"badge badge-info\">"+entry.chapOrdinal+"."+resocount+"</span> "+entry.resources[j].resoTitle+"</a></li></h4>";
					resocount ++;
				}
			}
		}
	});
	$records.append(resoliststr);
	$("#resoTitle").append(currvediostr);
	var getcheck = $(".checkid");
	getcheck.click(function(){
		 $.cookie('colVideo','0');
	});
}
/**
 * 拿章节id-章节号
 */
function getchapterID(){
	var orgID;
	var ch = new Array();
	ch = $("li[name='videochecked']");
	for (var i=0;i<ch.length;i++){
		//alert($(ch[i]).attr("orgid"));
		if($(ch[i]).attr("rid")==rid){
			orgID = $(ch[i]).attr("orgid");
			//alert(orgID);
			return orgID;
		}
	}

} 

function doc(documents){
	showdetail("#docCourse",documents);
}

function link(links){
	showdetail("#linkCourse",links);
}


function showdetail(showId,courseContent){
	if(showId=="#docCourse"){
		var show="";
		for(var i=0;i<courseContent.length;i++){
			show = show + "<li class=\"liststyle-doc\"><a target='_blank' href=\"../../handler/resource/downloadDocumentResource.do?rtype=2&rid="+courseContent[i].resoId+"\">"+courseContent[i].resoTitle+"</a></li>";
		}
		$(showId).append(show);
	}
	else if(showId=="#linkCourse"){
		var show="";
		for(var i=0;i<courseContent.length;i++){
			show = show +"<li class=\"liststyle-doc\"><a target='_blank' href='"+courseContent[i].resoLocation+"'>"+courseContent[i].resoTitle+"</a></li>";
		}
		$(showId).append(show);
	}
}
/**
 * 课程评分显示弹出层
 * 接受评分
 */
function showGetMark(){
	$("#getMark").modal('show');
	$("#getChapScore").val("1");
	$('#sendMark').click(function(e){
		var chapterID = chapId ;
		var score = $("#getChapScore").val();
		$.post("../../handler/chapter/assessChapter",  {
			"chapterId" : chapterID,
			"score":score
		},function(data) {
			if(data.errmsg.length>0){
				alert("评分错误，请联系系统管理员");
				$("#getMark").modal('hide');
			}
			else{
				$("#getMark").modal('hide');
			}
		}, "json");

	});
	
}
//暂时  面包条（最终合并在util.js中）
function ShowSubColumn1() {
	var orgid=getchapterID();	
	var columnHtml = "<ol class='breadcrumb'><li><a href='home.html'>主页</a></li><li><a href='Learning.html?firstCol=2&secondCol=16'>学习中心</a></li>"
		+ "<li><a href='Learning_chooseCourse.html?courId="+courId+"&"+colIds+"&firstCol=2&secondCol=16"+"'>课程学习 </a></li><li class='active'><strong>"+coursename+"</strong></li></ol>";
	$("#subcolumn").html(columnHtml);
	var getcheck = $(".checkid");
	getcheck.click(function(){
		 $.cookie('colVideo','0');
	});
}
