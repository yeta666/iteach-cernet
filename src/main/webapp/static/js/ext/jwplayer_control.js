var address="";
var watchTime = 0;
var lastWatchTime=0;
var chapId = getRequest("chapId");
var courId = getRequest("courId");
var isCountStart=1;		//	视频是否第一次播放
var buffereTime = 0;	//	缓冲时间计时
var locations = null;	//	视频播放地址
var locationIndex = 0;
var data = null;
var userT;//用户种类
var totalTime = 0;//总时间
var countOfVideo = 1;//视频地址切换计时
var videoInterval=0;
var judge=0;
//学习防作弊功能
var cheatingControl="否";
var CheatingInterval=10;

$(document).ready(function() {
	//获取视频计时间隔参数
	$.post("../../handler/sypaController/attainValueByEnName",{
		"enName":"videoInterval"
	},function(mydata){
		if(mydata.ret){			
			videoInterval=mydata.data.value;
		}
	},"json");
	//获取视频信息
	userT = userType;
	address ="oflaDemo" ;
	var rid = getRequest("rid");
	$.post("../../handler/resource/viewVideoAddress.do",{
		"rid":rid,
		"courId":courId
	},function(mydata){
		locations = mydata.data.servers;
		data = mydata;
		totalTime = mydata.data.time; 
		var firstLocations = locations[0];
		createPlayer("rtmp://"+firstLocations+"/oflaDemo/");
	},"json");
	loadCheatingControl();
});
var myplayer;
//视频计时函数
var doTimes = null;
var keepLink=null;
var recordTime=null;

function createPlayer(videoAddr) {
	$("#mediaspace").html("Loading...");
	myplayer = jwplayer("mediaspace").setup({
		flashplayer:"../video/jwplayer.flash.swf",
		file: (videoAddr + data.data.file),
		width: "100%",
		height: 500,
		controlbar:"bottom",
		image: data.data.image,
		autostart:false,
		allowfullscreen:true,
		repeat:"none",
		shuffle:true,
		volume:80,
		bufferlength:10,
		primary: "flash",
		streamer:videoAddr,
		provider:"rtmp",
	});

	myplayer.onPlay(function(){
		$.cookie('colVideo', '1'); //控制视频 唯一开启
		buffereTime = 0;
		if(userT==1&&cheatingControl=="是"){
				window.setTimeout("getRandomCode();",CheatingInterval*60*1000);
		}
		if(doTimes != null)
			window.clearInterval(doTimes);
		doTimes = setInterval(function(){
			watchTime++;
		},'1000');		
		
		if(isCountStart==1&&userT==1){
			keepLink=window.setInterval("sendKeepWatching();",20*60*1000);//定时发送请求，防止视频关闭
			
			$.getJSON("../../handler/record/addLearnProcessRecord.do",{
				"chapId":chapId,
				"resoId":rid,
			},function(data){
				judge = data.data.status;
				if(judge>0){
					isCountStart++;
					if(videoInterval>0){
						recordTime=window.setInterval("recordProcess();",videoInterval*60*1000);
					}
				}
				if(judge ==-1){
					return ;
				}
			});
		}
	});
	myplayer.onPause(function clearTimeCounter() {
		if(doTimes != null)
			window.clearInterval(doTimes);
	});
	myplayer.onBuffer(function clearTimeCounter() {
		//window.setTimeout("changeIP();",20*1000);//计时15s，运行IP切换
		countingBufferingTime();
		if(doTimes != null)
			window.clearInterval(doTimes);
	});
	myplayer.onComplete(function clearTimeCounter() {
		if(doTimes != null)
			window.clearInterval(doTimes);
		if(keepLink!=null){
			window.clearInterval(keepLink);
		}
		if(recordTime!=null){
			window.clearInterval(recordTime);
			recordProcess();
		}
		isCountStart=1;
		lastWatchTime=0;
		watchTime=0;
		$.cookie('colVideo','0');
	});
	myplayer.onError(function clearTimeCounter() {
		buffereTime = 100;
		countingBufferingTime();
		if(doTimes != null)
			window.clearInterval(doTimes);
		if(keepLink!=null){
			window.clearInterval(keepLink);
		}
		if(recordTime!=null){
			window.clearInterval(recordTime);
			recordProcess();
		}
		isCountStart=1;
		lastWatchTime=0;
		watchTime=0;
		$.cookie('colVideo','0');
	});

	/*var test = setInterval(function(){
		$("#countTime").html("player time:"+watchTime);
	},'1000');*/
}

/**
 * 定时发送请求记录时间
 */
function recordProcess(){
	var watchT = get2min(watchTime-lastWatchTime);
	$.getJSON("../../handler/record/recordLearnProcess.do",{
		"lpreId":judge,
		"chapId":chapId,
		"studyTime":watchT,
		"resoId":rid,
		//"studyTime":totalTime,
	},function(data){
		//	alert("记录成功");
		lastWatchTime=watchTime;
	});
}





/**
 * 计时
 */
function countingBufferingTime() {
	if(buffereTime>=15) {
		buffereTime = 0;
		//window.setTimeout("changeIP();",20*1000);
		changeIP();
		//createPlayer(locations[locationIndex]);
		//locationIndex++;
	} else {
		buffereTime++;
	}
}
/**
 * 以暂停判断学生是否在学习中
 */
function getRandomCode(){
	reloadcode1();
	$('#RandomVal').val("");
	myplayer.pause(true);
	myplayer.onPause(function clearTimeCounter() {
		if(doTimes != null)
			window.clearInterval(doTimes);
	});
	myplayer.onBuffer(function clearTimeCounter() {
		countingBufferingTime();
		if(doTimes != null)
			window.clearInterval(doTimes);
	});
	$('#getRandom').modal('show');
	$('#sendRandom').click(function(){
		var Random = $('#RandomVal').val();
		if(Random!=''){
			$.post("../../handler/resource/checkVideoCode.do",
					{
				'inputCode':Random
					},function(data){
						if(data.data.status == 1) {
							$('#getRandom').modal('hide');
							//alert("验证成功！");
							myplayer.pause(false);
							myplayer.onBuffer(function clearTimeCounter() {
								countingBufferingTime();
								if(doTimes != null)
									window.clearInterval(doTimes);
							});
							window.setTimeout("getRandomCode();",10*60*1000);
						}
						else{
							$('#getRandom').modal('hide');
							//alert("验证失败！");
							reloadcode1();
							getRandomCode();
						}
					},"json");
		}
	});

}
/**
 * 定时切换IP
 */
function changeIP(){
	var allLocations = ""; 
	var lenLocations = locations.length;
	if(countOfVideo<lenLocations){
		allLocations = locations[countOfVideo];
		createPlayer("rtmp://"+allLocations+"/oflaDemo/");
		countOfVideo++;
	}
	else if(countOfVideo==lenLocations){
		allLocations = locations[countOfVideo];
		createPlayer("rtmp://"+allLocations+"/oflaDemo/");
		countOfVideo = 0;
	}
}

function uploadStudyTime() {
	if(watchTime != 0) {
		$.post("../../handler/record/recordLearnProcess.do",{studyTime:watchTime});
		var hour = parseInt(watchTime/3600);
		var minute = parseInt(watchTime/60) - hour * 60;
		var second = watchTime - parseInt(watchTime/60) * 60;
		//	alert("本次视频学习总时长："+hour+":"+minute+" "+second);
	}
}

//重新加载验证码1
function reloadcode1() {
	$("#randomcode").attr("src","../../handler/resource/getVideoConfirmCode.do?"+ new Date());
}

//加载系统参数中学习防作弊的设置
function loadCheatingControl(){
	$.post("../../handler/sypaController/attainValueByEnName", {"enName":"cheatingControl"},
			function(data) {
		if (data.ret) {
			cheatingControl=data.data.value;
		} else {
			cheatingControl=data.errmsg;
		};
		if(cheatingControl=="是")
			$.post("../../handler/sypaController/attainValueByEnName", {"enName":"CheatingInterval"},
					function(data) {
				if (data.ret) {
					CheatingInterval=data.data.value;
				}
			});
	});
}


//不断发动请求，防止观看视频时跳出系统
function sendKeepWatching(){
	$.getJSON("../../handler/user/keepAliveWhenWatchingVideo.do",{
		
	},function(data){});
	window.setTimeout("sendKeepWatching();",20*60*1000);//定时发送请求，防止视频关闭
	//window.setTimeout("sendKeepWatching();",3000);//定时发送请求，防止视频关闭
	//alert("keep going");
}