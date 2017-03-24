$(document).ready(function() {
	ShowColumn();
	backToTop();
	$('#bbsIndex').attr("class", "active");
	//绑定编辑器
	$("#postContent").summernote({
		lang: 'zh-CN'
	});
	$("#postfile").val("");
	//显示选择课程
	selectePostCourse();
	$('#addAttachment').popover({
		content: cont,
		trigger: 'hover',
	});
});
/**
 * 提交发新帖的课程，标题，内容，附件id组，到后台
 */
function submitNewpostContent() {
	$("#infomsg").empty();
	var seletePostCourse = $("#seletePostCourse").val();
	var postTitle = $("#postTitle").val();
	var postContent = $("#postContent").code();
	//页面滚动到顶部，便于用户查看错误提示信息
	moveto("pictures");
	if(seletePostCourse == -1) {
		alert("选择错误");
		//infoNotice("error", "选择错误！", "您没有选择学习课程，还不能发表帖子，请到学习中心选择课程开始学习！",$('#data-grid'));
	} else if(postTitle == "") {
		alert("主题输入错误");
		//infoNotice("error", "输入错误！", "讨论主题不能为空！",$('#data-grid'));
		$("#postTitle").focus();
	} else if(postContent == "") {
		alert("内容输入错误");
		//infoNotice("error", "输入错误！", "讨论内容不能为空！",$('#data-grid'));
		$("#postContent").focus();
	} else {
		console.log(attachesIds);
		$.ajax({
			type: 'post',
			contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
			url: "../../handler/bbsPost/createBbsPost",
			dataType: 'json',
			data: {
				bbpoCourId: seletePostCourse,
				bbpoUserId: userId,
				bbpoTitle: postTitle,
				bbpoContent: postContent,
				attaches: attachesIds
			},
			success: function(data) {
				if(data.ret) {
					window.location.href = "bbs_index.html?firstCol=3&secondCol=17";
				} else {
					alert(data.errmsg);
				}
			}
		});
	}
}
/**
 * 显示可选课程列表
 */
function selectePostCourse() {
	$.ajax({
		type: 'post',
		contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
		url: "../../handler/course/viewCoursesByUser",
		dataType: 'json',
		data: {
			userId: userId,
			type: 1
		},
		success: function(data) {
			var bbsOption;
			if(data.ret) {
				if($.isEmptyObject(data.data.courses)) {
					bbsOption = "<option value='-1'>没有课程数据！</option>";
				} else {
					$.each(data.data.courses, function(i, val) {
						bbsOption += "<option value='" + val.courseId + "'>" + val.courseName + "</option>";
					});

				}
			} else {
				bbsOption = "<option>" + data.errmsg + "</option>";
			}
			$("#seletePostCourse").append(bbsOption);
		}
	});
}