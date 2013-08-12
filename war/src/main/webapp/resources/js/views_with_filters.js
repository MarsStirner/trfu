var browserScrollWidth;
jQuery(document).ready(function() {
	browserScrollWidth = getBrowserScrollWidth();
	update_content();
});

var resizeTimer = null;
jQuery(window).bind('resize', function() {
	if (resizeTimer) clearTimeout(resizeTimer);
	resizeTimer = setTimeout(update_content, 100);
});

function update_content() {
	var height = document.body.clientHeight - jQuery("#view_header").height() - jQuery("#footer").height() - jQuery("#table_paging").height()-26;
	jQuery("#table_wrap").height(height);
	jQuery("#table_inner").height(height);
	jQuery("#filter_panel").height(height + browserScrollWidth);
	jQuery("#split").height(document.body.clientHeight);
	jQuery(".e5ui-menu").height(document.body.clientHeight-30);
	
	if (document.getElementById("filter_panel").style.display != "none") {
		resizeTableWrapDiv(); 
	}
	
	jQuery("#filter_panel").css({"overflow":"auto","overflow-x":"auto","overflow-y":"auto"});
};

function resizeTableWrapDiv() {
	var tablewidth = $("#main_table").width();
    jQuery("#table_wrap").width(tablewidth-326);
};

function hideFilter() {
	document.getElementById("hide_filter_div").style.display = "none";
	document.getElementById("show_filter_div").style.display = "";
	document.getElementById("filter_panel").style.display = "none";
	document.getElementById("table_wrap").style.width = "100%";
};

function showFilter() {
	document.getElementById("hide_filter_div").style.display = "";
	document.getElementById("show_filter_div").style.display = "none";
	document.getElementById("filter_panel").style.display = "";

	resizeTableWrapDiv(); 
	resizeTableWrapDiv(); 
};

function getBrowserScrollWidth() {
	document.body.style.overflow = "hidden"; 
	var width = document.body.clientWidth;
	document.body.style.overflow = "scroll"; 
	width -= document.body.clientWidth; 
	if(!width) {
		width = document.body.offsetWidth - document.body.clientWidth;
	}
	document.body.style.overflow = ""; 
	return width; 
}