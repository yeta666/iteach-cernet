$(function(){
	
	console.log($.cookie("userId"));
	
	//初始化数据
	var finish_question_num = 0;
	var answer_index = ["a", "b", "c", "d"];
	
	//初始化按钮信息提示
	$("#submit_btn, #remove_btn").popover({
		html: true
	});
	
	//给确定提交和取消提交按钮绑定事件
	$("#submit_btn").click(function(){
		var $_this = $(this);
		
		//给取消提交按钮绑定事件
		$("#submit_btn_remove").click(function(){
			$_this.popover('hide');
		});
		
		//给确定提交按钮绑定事件
		$("#submit_btn_ok").click(function(){
			$_this.popover('hide');
			
			//要求完成所有题目
			/*if(finish_question_num != parseInt($("#total_question_num").val())){
				alert("请完成所有题目后提交！");
				return ;
			}*/
			
			$.bootstrapLoading.start({ loadingTips: "正在上传数据，请稍候..." });
			
			//封装数据
			var answers = [];
			var radios = $("input[type=radio]");
			for(var i = 0; i < radios.length; i++){
				var radio = radios[i];
				if(radio.checked){
					//把用户选择的答案存入数组，数组下标-1即为题目序号
					answers.push($(radio).val());
				}
			}
			
			console.log(answers);
			
			//伪造数据
			var forge_answers = [];
			for(var i = 0; i < 22; i++){
				var random_num = parseInt(Math.random() * 2) + 1;
				if(random_num == 1){
					forge_answers.push("a");
				}else{
					forge_answers.push("b");
				}
			}
			
			//获取userId
			$.ajax({
				type: 'GET',
				contentType: 'application/x-www-form-urlencoded;charset=UTF-8', // 发送信息至服务器时内容编码类型
				url: '../../handler/home/navibar',
				async: false, // 需要同步请求数据
				dataType: 'json',
				success: function(data, status) {
					var resultData = data.data;
					$.cookie("userId", resultData.userId);
					console.log($.cookie("userId"));
					debugger;
					//提交数据
					$.ajax({
						url: "http://127.0.0.1:8081/adaptive_ui/getUserTypeByQuestionary",
						type: "post",
						data: {
							"userId": resultData.userId,
							"answers": JSON.stringify(forge_answers)
							//"answers": JSON.stringify(answers)
						},
						dataType: "json",
						success: function(data){
							if(data.status){
								console.log(data.data);
								$.cookie("userType", data.data);
								window.location.href = "userCenter.html?firstCol=1&secondCol=14";
							}else{
								$.bootstrapLoading.end();
								$("#submitBtn").popover('hide');
								//后台计算不出用户类型，计算不出的原因data.message
								alert(data.message);
							}
						},
						error: function(XHR){
							alert(XHR.status);
						}
					});
				},
				error : function(XHR) {
					alert("出现错误，请稍后重试！错误码： " + XHR.status);
				}
			});
		});
	});
	
	$("#remove_btn").click(function(){
		
		var $_this = $(this);
		$("#remove_btn_remove").click(function(){
			$_this.popover('hide');
		});
		
		$("#remove_btn_ok").click(function(){
			if($.cookie("questionary") == "login"){
				//默认定制
				$.cookie("userType", "default");
				window.location.href = "userCenter.html?firstCol=1&secondCol=14";
			}else{
				//返回上一个操作
				window.history.back();
			}
		});
	});
	
	//获取调查表数据
	$.ajax({
		url: "http://127.0.0.1:8081/adaptive_ui/getQuestionary",
		type: "get",
		data: {},
		dataType: "json",
		success: function(data){
			if(data.status){
				//初始化总题目数量
				$("#total_question_num").val(data.data.length);
				
				//初始化题目导航的高度
				var question_navbar_height = $(window).height() * 0.9 / data.data.length - 2;
				
				for(var i = 1; i <= data.data.length; i++){
					//初始化题目导航
					
					var $a = $('<a href="#question' + i + '" style="display: block; width: '+question_navbar_height+'px; height: '+question_navbar_height+'px; border: 1px solid rgb(95, 202, 255); border-radius: '+question_navbar_height+'px; text-align: center; line-height: '+question_navbar_height+'px; font-size: 12px; margin: 2px 0; background-color: rgb(192, 229, 248); color: black;">'+i+'</a>');
					$a.appendTo($(".question_navbar"));
					
					//初始化调查表数据
					var $formGroup = $('<div class="form-group"></div>');
					$('<label for="question' + i + '" id="question' + i + '" style="font-size: 16px;">'+ i + ". " + data.data[i -1].question +'</label>').appendTo($formGroup);
					for(var j = 0; j < data.data[i - 1].answer.length; j++){
						var $div = $('<div class="radio"></div>');
						var $label = $('<label></label>');
						var $a = $('<a href="javascript:" style="display: inline-block; width: 18px; height: 16px; background: transparent url(./../img/radio.gif) no-repeat; vertical-align: middle; "></a>').click(function(e){
							//点击任何一个回答选项，与之对应的题目导航背景变绿
							$(".question_navbar > a[href=#" + $($(this).children()).attr("name") + "]").css("background-color", "rgb(174, 197, 45)");
							//完成题目数量+1
							finish_question_num++;
							//改变背景图片
							$(this).css("background", "transparent url(./../img/radio.gif) no-repeat 0 -16px");
							//取消相同name的radio的选中状态，并改变背景图片
							if($(this).children().attr("name") == $(this).parent().parent().prev().find("input").attr("name")){
								$(this).parent().parent().prev().find("input")[0].checked = false;
								$(this).parent().parent().prev().find("a").css("background", "transparent url(./../img/radio.gif) no-repeat");
							}else if($(this).children().attr("name") == $(this).parent().parent().next().find("input").attr("name")){
								$(this).parent().parent().next().find("input")[0].checked = false;
								$(this).parent().parent().next().find("a").css("background", "transparent url(./../img/radio.gif) no-repeat");
							}
							//radio选中
							$(this).children()[0].checked = true;
						});
						$('<input type="radio" name="question' + i + '" value="' + answer_index[j] + '" style="display:none;" />').appendTo($a);
						$a.appendTo($label);
						$('<span style="margin-left: 5px;">' + data.data[i -1].answer[j] + '</span>').appendTo($label);
						$label.appendTo($div);
						$div.appendTo($formGroup);
					}
					$formGroup.appendTo($("#questionary_form"));
					$("<hr/>").appendTo($("#questionary_form"));
				}
				//设置题目导航的margin
				$(".question_navbar").css("margin-top", ($(window).height() - $(".question_navbar").height()) / 2 + "px");
			}else{
				alert(data.message);
			}
		},
		error: function(XHR){
			alert(XHR.status);
		}
	});
})