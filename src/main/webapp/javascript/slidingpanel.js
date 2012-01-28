$(document).ready(function() {
	$("div.panel_button").click(function(){
		$("div#toppanel").animate({
			right: "100px"
		})
		.animate({
			right: "0px"
		}, "fast");
		$("div.panel_button").toggle();

	});

   $("div#hide_button").click(function(){
		$("div#toppanel").animate({
			right: "-300px"
		}, "fast");


   });

});