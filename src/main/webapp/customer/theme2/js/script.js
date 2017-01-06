$(document).ready(function() {

	$("#nav1").click(function(e) {

		$(this).addClass("navArea");
		$("#nav2").removeClass("navArea");

		if($("#subNav1").css("display") == "none") {
			e.stopPropagation();
			var offset = $(e.target).offset();
			$("#subNav2").hide();
			$("#subNav1").show(300);

		} else {
			$("#subNav1").hide(300);
		}
	});
	$("#nav2").click(function(e) {
		$(this).addClass("navArea");
		$("#nav1").removeClass("navArea");

		if($("#subNav2").css("display") == "none") {
			e.stopPropagation();
			var offset = $(e.target).offset();
			$("#subNav1").hide();
			$("#subNav2").show(300);

		} else {
			$("#subNav2").hide(300);
		}
	});

	$(".nav2").click(function() {
		$(".nav2").removeClass("weui_bar_item_on");
		$(this).addClass("weui_bar_item_on");
	});

	$(".weui_navbar_item").click(function() {
		$(".weui_navbar_item").removeClass("navArea");
		$(this).addClass("navArea");
	});
	
	$(".slideBox").slide({
		mainCell: ".bd ul",
		effect: "left",
		autoPlay: true
	});

});