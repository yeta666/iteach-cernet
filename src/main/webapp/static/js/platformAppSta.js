var viewaction = '';// 分页请求的action
var params;// 请求的参数
$(document).ready(function() {
	ShowColumn();
	backToTop();
	if (userId <= 0) {
		/** 加载分页 */
		$('#pagination').hide();
	} else{
		paginationPage();
	}
	$('#platformAppSta').attr("class", "active");
	var barChartData = {
			labels : ["绵阳中学","南山中学","北川中学","江油中学","九中","七中","外国语学校","科学城一中","三台中学","富乐中学","英才学校","双语学校","东辰国际学校"],
			datasets : [
				{
					fillColor : "rgba(151,187,205,0.5)",
					strokeColor : "rgba(151,187,205,1)",
					data : [65,59,50,81,56,55,40,45,65,35,85,55,62]
				}
			]
			
		}

	var myLine = new Chart(document.getElementById("canvas").getContext("2d")).Bar(barChartData);
	
});

