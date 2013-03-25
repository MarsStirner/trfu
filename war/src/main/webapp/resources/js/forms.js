jQuery(document).ready(function() {
	update_content();
});
var resizeTimer = null;
jQuery(window).bind('resize', function() {
	if (resizeTimer) clearTimeout(resizeTimer);
	resizeTimer = setTimeout(update_content, 100);
});
function update_content() {
    var height = document.body.clientHeight - jQuery("#form_header").height() - jQuery("#header_content").height() -25 - 17;
    jQuery(".main_content").height(height);
};