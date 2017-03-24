var courseId = getRequest("courId");
var resourceId = getRequest("resId");
var maleId = getRequest("maleId");
var courseName = getRequest("courseName");
var watchTime = 0;
var locations;
var file;
var img;
var buffereTime = 0;//	缓冲时间计时
var locationIndex = 0;
var studyTime ;
$(document).ready(function() {
	
	ShowSubColumnb();
	ShowColumn();
	AjaxJson("../../handler/resource/viewVideoAddress.do", {
		rid : resourceId,
		courId : courseId
	}, backOfResource);

	$("#study-end").click(function(){
		window.location.href = "my_Tutor_studyTogether.html?firstCol=5&secondCol=20";
	});
});

function createPlayer(videoAddr) {
	$("#mediaspace").html("Loading...");
	var myplayer = jwplayer("mediaspace").setup({
		flashplayer : "../video/jwplayer.flash.swf",
		file : (videoAddr + file),
		width : 900,
		height : 450,
		controlbar : "bottom",
		image : img,
		autostart : false,
		allowfullscreen : true,
		repeat : "none",
		shuffle : true,
		volume : 80,
		bufferlength : 10,
		primary : "flash",
		streamer : videoAddr,
		provider : "rtmp",
	});
	// 视频计时函数
	var doTimes = null;
	myplayer.onPlay(function() {
		buffereTime = 0;
		if (doTimes != null)
			window.clearInterval(doTimes);
		doTimes = setInterval(function() {
			watchTime++;
		}, '1000');
		AjaxJson("../../handler/massedLearning/startMassedLearning",{studyTime:studyTime,courseId:courseId,maleId:maleId},backOfStartOk);

	});
	myplayer.onPause(function clearTimeCounter() {
		if (doTimes != null)
			window.clearInterval(doTimes);
	});
	myplayer.onBuffer(function clearTimeCounter() {
		countingBufferingTime();
		if (doTimes != null)
			window.clearInterval(doTimes);
	});
	myplayer.onComplete(function clearTimeCounter() {
		if (doTimes != null)
			window.clearInterval(doTimes);
	});
	myplayer.onError(function clearTimeCounter() {
		buffereTime = 100;
		countingBufferingTime();
		if (doTimes != null)
			window.clearInterval(doTimes);
	});
}

function countingBufferingTime() {
	if(buffereTime>=40) {
		buffereTime = 0;
		createPlayer(locations[locationIndex]);
		locationIndex++;
	} else {
		buffereTime++;
	}
}
/**
 * 获取到资源目录
 * @param backInfo
 */
function backOfResource(backInfo) {
	locations = backInfo.data.servers;
	file = backInfo.data.file;
	img = backInfo.data.image;
	studyTime = backInfo.data.time;
	var firstLocations = locations[0];
	createPlayer("rtmp://"+firstLocations+"/oflaDemo/");
}
/**
 * 视频播放开始后台返回的数据
 * @param backInfo
 */
function backOfStartOk(backInfo)
{
//	alert(backInfo);
}
function uploadStudyTime() {
	if(watchTime != 0) {
		var watchT = get2min(watchTime);
		$.post("../../handler/record/recordLearnProcess.do",{studyTime:watchT});
		var hour = parseInt(watchTime/3600);
		var minute = parseInt(watchTime/60) - hour * 60;
		var second = watchTime - parseInt(watchTime/60) * 60;
	}
}

//暂时  面包条（最终合并在util.js中）
function ShowSubColumnb() {
	var columnHtml = "<ul class='breadcrumb bread'><li><a class='checkid' href='home.html'><i class='icon-home'></i>首页</a> </li>"+
	"<li><a class='checkid' href='my_Tutor.html?firstCol=5&secondCol=19'>我的课堂</a> </li>"+
	"<li><a class='checkid' href='my_Tutor_studyTogether.html?firstCol=5&secondCol=20'>集中学习</a></li><li class='active'>"+courseName+"</li></ul>";
	$("#subcolumn").html(columnHtml);
}