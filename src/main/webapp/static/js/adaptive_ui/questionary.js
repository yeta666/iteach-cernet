$(function() {
	
	/*// 伪造数据
	var forge_answers = "";
	for(var i = 0; i < 22; i++) {
		var random_num = parseInt(Math.random() * 2) + 1;
		if(random_num == 1) {
			if(i == 21){
				forge_answers += "a"
			}else{
				forge_answers += "a,"
			}
		} else {
			if(i == 21){
				forge_answers += "b"
			}else{
				forge_answers += "b,"
			}
		}
	}
	console.log(forge_answers);

	console.log($.cookie("userId"));*/

	// 初始化数据
	var answers_index = ["a", "b"];

	// 初始化按钮信息提示
	$("#submit_btn, #remove_btn").popover({
		html: true
	});

	// 给确定提交和取消提交按钮绑定事件
	$("#submit_btn").click(function() {
		var $_this = $(this);

		// 给取消提交按钮绑定事件
		$("#submit_btn_remove").click(function() {
			$_this.popover('hide');
		});

		// 给确定提交按钮绑定事件
		$("#submit_btn_ok").click(function() {
			$_this.popover('hide');

			// 封装数据
			var answers = "";
			var radios = $("input[type=radio]");
			for(var i = 0; i < radios.length; i++) {
				var radio = radios[i];
				if(radio.checked) {
					// 把用户选择的答案存入字符串，下标即为题目序号
					answers = answers + $(radio).val() + ","
				}
			}
			var new_answers = answers.substring(0, answers.length - 1);
			
			// 要求完成所有题目
			if(forge_answers.split(",").length != 22){
				alert("请完成所有题目！");
			}else{
				$.bootstrapLoading.start({
					loadingTips: "正在上传数据，请稍候..."
				});
				// 提交数据
				$.ajax({
					url: "../../handler/userType/getUserType",
					type: "post",
					data: {
						"userId": $.cookie("userId"),
						//"answers": forge_answers
						"answers": new_answers
					},
					dataType: "json",
					success: function(result) {
						console.log(result);
						if(result.success){
							var data = JSON.parse(result.message);
							if(data.status) {
								//console.log(data.data);
								$.cookie("userType", data.data);
								window.location.href = "userCenter.html?firstCol=1&secondCol=14";
							} else {
								$.bootstrapLoading.end();
								$("#submitBtn").popover('hide');
								// 后台计算不出用户类型，计算不出的原因data.message
								alert(data.message);
							}
						}
					},
					error: function(XHR) {
						alert(XHR.status);
					}
				});
			}
		});
	});

	$("#remove_btn").click(function() {

		var $_this = $(this);
		$("#remove_btn_remove").click(function() {
			$_this.popover('hide');
		});

		$("#remove_btn_ok").click(function() {
			if($.cookie("questionary") == "login") {
				// 默认定制
				$.cookie("userType", "default");
				window.location.href = "userCenter.html?firstCol=1&secondCol=14";
			} else {
				// 返回上一个操作
				window.history.back();
			}
		});
	});

	// 获取调查表数据
	$.ajax({
		url: "../../handler/question/getAll",
		type: "get",
		data: {},
		dataType: "json",
		success: function(result) {
			//console.log(result);
			if(result.ret){
				var result1 = result.data.result;
				if(result1.success){
					var data = JSON.parse(result1.message);
					if(data.status) {
						// 初始化总题目数量
						$("#total_question_num").val(data.data.length);

						// 初始化题目导航的高度
						var question_navbar_height = $(window).height() * 0.9 / data.data.length - 2;

						for(var i = 1; i <= data.data.length; i++) {
							// 初始化题目导航

							var $a = $('<a href="#question' + i + '" style="display: block; width: ' + question_navbar_height + 'px; height: ' + question_navbar_height + 'px; border: 1px solid rgb(95, 202, 255); border-radius: ' + question_navbar_height + 'px; text-align: center; line-height: ' + question_navbar_height + 'px; font-size: 12px; margin: 2px 0; background-color: rgb(192, 229, 248); color: black;">' + i + '</a>');
							$a.appendTo($(".question_navbar"));

							// 初始化调查表数据
							var $formGroup = $('<div class="form-group"></div>');
							$('<label for="question' + i + '" id="question' + i + '" style="font-size: 16px;">' + i + ". " + data.data[i - 1].content + '</label>').appendTo($formGroup);
							var answers = [];
							answers.push(data.data[i - 1].answers.answer1);
							answers.push(data.data[i - 1].answers.answer2);
							for(var j = 0; j < answers.length; j++) {
								var $div = $('<div class="radio"></div>');
								var $label = $('<label></label>');
								var $a = $('<a href="javascript:" style="display: inline-block; width: 18px; height: 16px; background: transparent url(./../img/radio.gif) no-repeat; vertical-align: middle; "></a>').click(function(e) {
									// 点击任何一个回答选项，与之对应的题目导航背景变绿
									$(".question_navbar > a[href=#" + $($(this).children()).attr("name") + "]").css("background-color", "rgb(174, 197, 45)");
									// 改变背景图片
									$(this).css("background", "transparent url(./../img/radio.gif) no-repeat 0 -16px");
									// 取消相同name的radio的选中状态，并改变背景图片
									if($(this).children().attr("name") == $(this).parent().parent().prev().find("input").attr("name")) {
										$(this).parent().parent().prev().find("input")[0].checked = false;
										$(this).parent().parent().prev().find("a").css("background", "transparent url(./../img/radio.gif) no-repeat");
									} else if($(this).children().attr("name") == $(this).parent().parent().next().find("input").attr("name")) {
										$(this).parent().parent().next().find("input")[0].checked = false;
										$(this).parent().parent().next().find("a").css("background", "transparent url(./../img/radio.gif) no-repeat");
									}
									// radio选中
									$(this).children()[0].checked = true;
								});
								$('<input type="radio" name="question' + i + '" value="' + answers_index[j] + '" style="display:none;" />').appendTo($a);
								$a.appendTo($label);
								$('<span style="margin-left: 5px;">' + answers[j] + '</span>').appendTo($label);
								$label.appendTo($div);
								$div.appendTo($formGroup);
							}
							$formGroup.appendTo($("#questionary_form"));
							$("<hr/>").appendTo($("#questionary_form"));
						}
						// 设置题目导航的margin
						$(".question_navbar").css("margin-top", ($(window).height() - $(".question_navbar").height()) / 2 + "px");
					} else {
						alert(data.message);
					}
				}
			}
		},
		error: function(XHR) {
			alert("获取题目失败！ " + XHR.status);
		}
	});
})