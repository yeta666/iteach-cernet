var subject;//科目
var countcourse;//可选课程总数
var img;//图片路径
var courseId;//课程id
var describe;//课程说明
var data;
var reselectID;//退课id
var resoID;//资源视频ID
var selType;//选择类型
var colIds = "";
var credit = '';//所选学分
var teachID ='';
var discussNum=0;//评论个数
/**
 * @author anny
 * 主函数
 */

$(document).ready(function(){
	// back To Top
	colIds = urlColHtml();
	//alert("colIDs:"+colIds);
	ShowColumn();
	backToTop();
	var courId = getRequest("courId");
	selType = getRequest("selectedType");
	$.getJSON("../../handler/chapter/viewChapterListByCourse.do",{
		"courId":courId,
	},function(data){
		if(data.data!=null){
			var blockDivData = data.data.chapterList;
			courseId = data.data.course.courId;
			subject = data.data.course.courName;
			img = data.data.course.courImg+data.data.course.fileName;
			reselectID=data.data.course.courId;
			describe = data.data.course.courDescribe;
			selType = data.data.course.courState;
			credit =  data.data.course.courCredit;//已选学分
			//resoID = data.data.chapterList
			teachID = data.data.course.courTeacherIds;
			
			lastPost(courseId);//课程讨论信息
			showCourseDes(data.data.course);
			chooseChapter(blockDivData,selType);
			
			var userType = $.cookie("userType");
			if(userType != null && userType != "" && userType != undefined){
				if(userType == "活跃型"){
					$($("#panels_ul > li")[2]).addClass("active");
					$("#coursePost").addClass("active");
				}else if(userType == "沉思型"){
					$($("#panels_ul > li")[1]).addClass("active");
					$("#courseChapter").addClass("active");
				}else{
					$($("#panels_ul > li")[0]).addClass("active");
					$("#courseintro").addClass("active");
				}
			}
			$("#dropcourse-btn").click(function(){
				dropCoruse(reselectID);
			});
			$("#selectcourse-btn").click(function(){
				selectCourse(userId);
			});
			//主讲教师名称点击，具体内容显示 start
			$(".pop").popover().click(function(){
				var nowID = this.id;
				var courseTitle = "";
				var courseDes = "";
				var teacherCourse = "";
				var courseTeacherDepart ="";
				if(nowID=="uin"){
						$("#teacherTitle").html("暂无主讲教师");
						$("#homeLearningTeacherDes").html("本课程暂无相关主讲教师");
						$("#homeLearningTeacherDepart").html("本课程教师暂无所属单位信息");
						$("#homeLearningTeacherCourse").html("本课程暂无相关主讲教师课程介绍");
						$("#DesteachModal").modal({				
							'backdrop':true
						});
				}else{
					$.ajax({
						type:"post",
						contentType:"application/x-www-form-urlencoded;charset=UTF-8",
						url:'../../handler/user/viewMajorTeachers',
						data:{
							"teacherId":nowID
						},
						dataType:'json',
						success:function(result){
							var homeTeacherData = new Array();
							homeTeacherData = result.data.users;
							if(	homeTeacherData.length>0 ){
								courseTitle = homeTeacherData[0].teacherName;
								courseDes = homeTeacherData[0].teacherRemark;
								courseTeacherDepart = homeTeacherData[0].schoolName ;
								teacherCourse = homeTeacherData[0].courseNames;
								$("#teacherTitle").html(courseTitle);
								$("#homeLearningTeacherDes").html(courseDes);
								$("#homeLearningTeacherDepart").html(courseTeacherDepart);
								$("#homeLearningTeacherCourse").html(teacherCourse);
								$("#DesteachModal").modal({				
									'backdrop':true
								});
							}else{
								/*$("#pop").popover().click(function(){*/
									var courseTitle = subject;
									$("#teacherTitle").html(courseTitle);
									$("#homeLearningTeacherDes").html("本课程暂无相关主讲教师");
									$("#homeLearningTeacherCourse").html("本课程暂无相关主讲教师课程介绍");
									$("#DesteachModal").modal({				
										'backdrop':true
									});
								/*});*/
							}
						}
					});
				}
			
			});
		}
		$("#DesClose").click(function(){
			$("#DesteachModal").modal('hide');
		});
	});
	//主讲教师名称点击，具体内容显示 end
});
/**
 * 判定考核方式分类显示
 * @param jMString
 */
function judgeMethods(jMString){
	var allJM ="";
	var JmArray = new Array();
	JmArray = jMString.split(",");
	if(JmArray[0]!=0){
		allJM += "学习次数占"+JmArray[0]+"%"+"~~";
	}
	if(JmArray[1]!=0){
		allJM += "视频学习占"+JmArray[1]+"%"+"~~";
	}
	if(JmArray[2]!=0){
		allJM += "交互次数占"+JmArray[2]+"%"+"~~";
	}
	if(JmArray[3]!=0){
		allJM += "考试分数占"+JmArray[3]+"%"+"~~";
	}
	/*if(JmArray[4]!=0){
    	allJM += "集中学习占"+JmArray[4]+"%";
    }*/
	return allJM;
}
/*
 * 显示课程信息
 * @author:杨春明
 */
function showCourseDes(courData){
	if(courData.opentTime=="#"){
		courData.opentTime="无期限";
	}
	if(courData.closeTime=="#"){
		courData.closeTime="无期限";
	}
	var getMethods = judgeMethods(courData.courTepaPattern);//统计考核方式问题
	var teachers = new Array();
	var teacherIDs = new Array();
	teachers = courData.courTeachers.split("，");
	teacherIDs = courData.courTeacherIds.split(",");
	var courseImg = "<img class='img-polaroid' src='../../"+courData.courImg+courData.fileName+"'/ style=\"width: 100%;\">";
	$("#corseimg").empty().append(courseImg);
	var coutitle = "<i class=\"fa fa-camera-retro\"></i>&nbsp;&nbsp;"+courData.courName;                           
	$("#coursetitle").empty().append(coutitle);
	var couuseDes="";
	if(teachers.length>0&&teachers.length==1){
		if(courData.courTeachers=="#"&&teacherIDs[1]==undefined)
		{
			teacherIDs[1] = "uin";
			courData.courTeachers = "暂无主讲教师";
		}
		couuseDes += "<p><strong>主讲：</strong>"+"<a id='"+teacherIDs[1]+"' class='pop' style='cursor:pointer;' herf='DesteachModal' data-togggle='popover'>"+courData.courTeachers+"</a>"+"<br />";
	}else{
		couuseDes +="<p><strong>主讲：</strong>";
		couuseDes +="<a id='"+teacherIDs[1]+"' class='pop' style='cursor:pointer;' herf='DesteachModal' data-togggle='popover'>"+teachers[0]+"</a>";
		for(var i=1;i<teachers.length;i++){
			couuseDes += "，"+"<a id='"+teacherIDs[i+1]+"' class='pop' style='cursor:pointer;' herf='DesteachModal' data-togggle='popover'>"+teachers[i]+"</a>";
		}
		couuseDes +="<br/>";
	}
	if(courData.courType==""||courData.courType==null){
		courData.courType="暂无课程类型";
	}
	couuseDes += "领域："+"<font color=\"#00BFFF\">"+courData.courCateIds+"</font><br>";
	couuseDes += "课程类型："+"<font color=\"#00BFFF\">"+courData.courType+"</font><br>";
	couuseDes += "学分："+"<font color=\"#00BFFF\">"+courData.courCredit+"分</font><br>";
	couuseDes += "已选："+"<font color=\"#00BFFF\">"+courData.courseStuCount+"人</font><br>";
	couuseDes += "选课时间："+"<font color=\"#00BFFF\">"+courData.opentTime+"~"+courData.closeTime+"</font><br>";
	couuseDes += "课程学习要求："+"<font color=\"#00BFFF\">"+getMethods+"</font><br><br>";
	if(courData.courState == 0)
		couuseDes += "<button id='selectcourse-btn' class='btn btn-primary btn-block btn-outline'><i class=\"fa fa-sign-out\"></i>选修该课程</button>";
	else if(courData.courState == 1)
		couuseDes += "<button id='dropcourse-btn' class='btn btn-primary btn-block btn-outline'><i class=\" icon-remove icon-white\"></i>退出该课程的学习</button>";

	couuseDes += "<br/></p>";
	$("#coursedes").empty().append(couuseDes);
	
	var courseintro = "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+courData.courDescribe+"</p>";
	$("#courseintro").empty().append(courseintro);
}

//课程对应标题动态展示
/*function Subject(courseId,subject,img,describe,selType){
	var HTML = "<img align='left' class='chooseImg' src='../../"+img+"'/><p><span class='coursename'>"+subject+"</span>";
	if(selType==0){
		HTML+="<button id='selectcourse-btn' class='btn btn-primary'>选择课程</button>";
	}else if(selType==1){
		HTML+="<button id='dropcourse-btn' class='btn btn-danger'>退课</button>";
	}                                
	HTML+="<br/>"
		+""+describe+"</p>";
	$("#chapname").append(HTML);
}*/

//选择章节动态展示
function chooseChapter(data,selType) {
	var $records=$('#choosechapter').children('tbody');
	$records.empty();
	if(data!=null){
		var t=0;
		var count = 0;
		var colVideo = $.cookie('colVideo');
		if(colVideo!=''&&colVideo=='0'){
			for(var q=0;q<data.length;q++){
				//start
				t=1000;
				for(var p=0;p<data.length;p++){
					if(data[p].chapOrdinal<t){
						t=data[p].chapOrdinal;
						count=p;
					}
				}
				//end
				var rowhtml="<tr><td style='padding-left: 15px;'>"+"第"+data[count].chapOrdinal+"章："
				+data[count].chapName+"</td><td>";
				if(((data[count].resources).length)!=0){
					var icount =1;
					for(var j=0;j<data[count].resources.length;j++){
						if(data[count].resources[j].resoType==3){
							res = data[count].resources[j].resoId;
							resTitle = data[count].resources[j].resoTitle;
							resTiem = data[count].resources[j].resoVediotime;
							var classstr = "<span class=\"badge badge-info\">"+data[count].chapOrdinal+"."+icount+"</span>";
							if(data[count].resources[j].resoStatus == 1)
								classstr = "<span class=\"badge badge-success\">"+data[count].chapOrdinal+"."+icount+"</span>";
							if(selType== 0)
								rowhtml= rowhtml +classstr+"<a href=\"#\" class=\"courpop\" data-toggle=\"popover\" data-content=\"请先选择该课程后在学习中心开始学习！\" data-original-title=\"课程选择\" data-trigger=\"click\">&nbsp;&nbsp;&nbsp;&nbsp;<i class=\"fa fa-film\"></i> "+sec2min(resTiem)+"</a>&nbsp;";
							else if(selType == 1){
								rowhtml= rowhtml +classstr+"<a href=\"learning_video.html?rid="+res+"&chapId="+data[count].chapId+"&"+colIds+"\"  title=\""+resTitle+"\">&nbsp;&nbsp;&nbsp;&nbsp;<i class=\"fa fa-film\"></i> "+sec2min(resTiem)+"</a>&nbsp;";
								/*rowhtml= rowhtml +classstr+"<a class='ifsel' href=\"#\"  title=\""+resTitle+"\"><i class=\"icon-film\"></i> "+sec2min(resTiem)+"</a>&nbsp;";*/
							}
							icount ++;
						}

					}
				}
				rowhtml+="</td></tr>";
				$records.append(rowhtml);
				data[count].chapOrdinal=1000;
			}
		}
		else{
			for(var q=0;q<data.length;q++){
				//start
				t=1000;
				for(var p=0;p<data.length;p++){
					if(data[p]<t){
						t=data[p];
						count=p;
					}
				}
				//end
				var rowhtml="<tr><td>"+"第"+data[count].chapOrdinal+"章："
				+data[count].chapName+"</td><td>";
				if(data[count].resources.length!=0){
					var icount =1;
					for(var j=0;j<data[count].resources.length;j++){
						if(data[count].resources[j].resoType==3){
							res = data[count].resources[j].resoId;
							resTitle = data[count].resources[j].resoTitle;
							resTiem = data[count].resources[j].resoVediotime;
							var classstr = "<span class=\"badge badge-info\">"+data[count].chapOrdinal+"."+icount+"</span>";
							if(data[count].resources[j].resoStatus == 1)
								classstr = "<span class=\"badge badge-success\">"+data[count].chapOrdinal+"."+icount+"</span>";
							if(selType== 0)
								rowhtml= rowhtml +classstr+"<a href=\"#\" class=\"courpop\" data-toggle=\"popover\" data-content=\"请先选择该课程后在学习中心开始学习！\" data-original-title=\"课程选择\" data-trigger=\"click\"><i class=\"icon-film\"></i> "+sec2min(resTiem)+"</a>&nbsp;";
							else if(selType == 1){
								rowhtml= rowhtml +classstr+"<a class='ifsel' href=\"#\"  title=\""+resTitle+"\"><i class=\"icon-film\"></i> "+sec2min(resTiem)+"</a>&nbsp;";
							}
							icount ++;
						}
					}
				}
				rowhtml+="</td></tr>";
				$records.append(rowhtml);
				data[count]=1000;
			}

			var getcheck = $(".ifsel");
			getcheck.click(function(){
				alert("您已经开启了视频学习");
			});
		}
		$(".courpop").popover();
	}
}
/**
 * 退课
 * @author ZhangXin
 */
function dropCoruse(reselectId)
{
	$.ajax({
		type:"post",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		url:'../../handler/sc/deleteSelCou',
		data:{
			courseId:reselectId,
			userId:userId
		},
		dataType:'json',
		success:function(result){
			var data = result.data.dropCourseInfo;
			var info = data.deleteSelCourInfo;
			var status = data.status;
			alert(info);
			switch(status)
			{
			case 1:break;
			case 2:break;
			case 3:window.location="Learning.html?firstCol=2&secondCol=16";break;/*+colIds*/
			default :break;
			}
		}
	});
}


/**
 * 选课
 * 
 * @author ZhangXin
 * @param userId
 */
function selectCourse(userId)
{
	$.ajax({
		type:"post",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		url:'../../handler/sc/selectCourse',
		async:false,
		data:{
			courseId:courseId,
			userId:userId,
			courseCredit:credit,
			/*	departId:departId*/
		},
		dataType:'json',
		success:function(result){
			var value='';
			var judge = result.data.status;
			if(judge==5){
				value = result.data.insertSelCourInfo;
				alert(JSON.stringify(value).toString());
			}			
			else if(judge==4){
				value = result.data.insertSelCourInfo;
				alert(JSON.stringify(value).toString());
				window.location="../html/Learning.html?"+colIds;
			}
			else if(judge==3){
				value = result.data.insertSelCourInfo;
				alert(JSON.stringify(value).toString());
			}
			else if(judge==2){
				value = result.data.insertSelCourInfo;
				alert(JSON.stringify(value).toString());
			}
			else if(judge==1){
				value = result.data.insertSelCourInfo;
				alert(JSON.stringify(value).toString());
			}

		}
	});
}
var firstReplayResult = '<p style="color:red;">暂无回复</p>';
function getFirstReplay(postId){
	firstReplayResult = '<p style="color:red;">暂无回复</p>';
	var viewaction = "../../handler/bbsReply/viewReplyList";
	var params = {
			"bbsPostId": postId,
			"pageArray": new Array(),
			"recordPerPage": 10
	};	
	$.post(viewaction, params, function(data) {
		//console.log(data);
		if(data.ret){
			if(data.data.totalCount > 0){
				firstReplayResult = data.data.pageData[0].data[data.data.totalCount - 1].replyContent;
			}
		}else{
			firstReplayResult = data.errmsg;
		}
	});
}


//课程论坛最新信息
function lastPost(courseId){
	$.ajax({
		type:"post",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		url:'../../handler/bbsPost/viewBbsPostList',
		data:{
			courseId:courseId,
			userId:userId,
			departId:-1,
			pageArray:1,
			queryType:1,
			recordPerPage:10
		},
		dataType:'json',
		success:function(result){
			var pdata = result.data.pageData;
			var postlist = '';
			if(pdata != "" && pdata != null){
				if(pdata.length>0){
					var postdata = pdata[0].data;
					if(postdata.length >0 ){
						discussNum=postdata.length;
						$.each(postdata, function(itemIndex, item) {
							getFirstReplay(item.bbpoId);
							//console.log(firstReplayResult);
							postlist += '<li style="list-style:none">'+
							'<div class="col-sm-1" style="padding:0;"><i class="fa fa-file-text" style="width:100%; height:100%; text-align:center; font-size:20px;"></i></div>'+
							'<div class="col-sm-11"  style="margin-bottom:20px;">'+
							'<p>'+item.userName+'</p>'+
							'<a href="javascript:onclick=visitPostDetail('+item.bbpoId+');" style="font-size:18px; font-weight:bold; color:black; display:block; margin: 10px 0;">'+
							titleFormat(item.bbpoTitle,18)+'</a>'+
							'<p class="pull-left">[最新的回复]&nbsp;&nbsp;</p><p>'+firstReplayResult+
							'</p><p><span class="pull-left">时间：  '+item.realUpdatetime+'</span>'+
							'<span class="pull-right">'+item.bbpoReplynum+'&nbsp;&nbsp;回复&nbsp;&nbsp;&nbsp;&nbsp;'+item.bbpoVisitnum+'&nbsp;&nbsp;浏览</span></p></div><hr style="clear:both;" /></li>';
						});
					}
				}
			}else{
				postlist += '<li style="list-style:none">暂无讨论</li>';
			}
			
			$("#coursePost").empty().append(postlist);
		}
	});
}

function visitPostDetail(id) {
	$.ajaxSettings.async = false;
	$.ajax({
		type : 'post',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',
		url : "../../handler/bbsPost/addVisitNum",
		dataType : 'json',
		data : {
			bbsPostId : id
		},
		success : function(data) {
			if (data.ret) {
				window.location.href = "bbs_viewPost.html?id="+ id+"&page=1"+"&firstCol=3&secondCol=17";
			} else {
				alert("data.errmsg");
			}
		}
	});
	return false;
}


