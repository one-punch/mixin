$(document).ready(function() {
	
	$(".slideBox").slide({
		mainCell: ".bd ul",
		effect: "left",
		autoPlay: true
	});

	$(".btnItem").click(function() {
		$(".btnItem").removeClass("btn-action");
		$(this).addClass("btn-action");
	});

	$(".nav2-th3-item>a").click(function() {
		$(".nav2-th3-item>a").removeClass("weui_bar_item_on");
		$(this).addClass("weui_bar_item_on");
	});

});