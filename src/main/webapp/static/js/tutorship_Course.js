var announcementInfo;

$(document).ready(function() {
	ShowColumn();
	$('#myTutor').attr("class", "active");
	clickItem("#addQu");
	clickItem("a[id^='select-info']");
	clickItem("a[name='return']");
	clickItem("a[id^='look-up-detail']");
	clickItem("#select-info1");
});
/**
 * 导航栏点击事件监听
 * 
 * @author ZhangXin
 */
function clickItem(location) {
	$(location).click(function(e) {
		e.preventDefault();
		$(this).tab('show');
	});
}
function clickItemFunc(location,foo)
{
	$(location).click(function(e) {
		e.preventDefault();
		$(this).tab('show');
		foo();
	});
}
