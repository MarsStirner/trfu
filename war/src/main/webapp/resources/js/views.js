jQuery(document).ready(function() {
	update_content();
});
var resizeTimer = null;
jQuery(window).bind('resize', function() {
	if (resizeTimer) clearTimeout(resizeTimer);
	resizeTimer = setTimeout(update_content, 100);
});
function update_content() {
	var height = document.body.clientHeight - jQuery("#view_header").height() - jQuery("#footer").height() - jQuery("#searchbar").height() - jQuery("#table_paging").height()-26;
	jQuery("#table_wrap").height(height);
	jQuery("#table_inner").height(height);
	jQuery("#split").height(document.body.clientHeight);
    	jQuery(".e5ui-menu").height(document.body.clientHeight-30);
};