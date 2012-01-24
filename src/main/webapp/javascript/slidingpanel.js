$(document).ready(function() {
	$("div.panel_button").click(function(){
		$("div#panel").animate({
			width: "400px"
		})
		.animate({
			width: "300px"
		}, "fast");
		$("div.panel_button").toggle();

	});

   $("div#hide_button").click(function(){
		$("div#panel").animate({
			width: "0px"
		}, "fast");


   });

});