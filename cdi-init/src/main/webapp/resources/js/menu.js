$(document).ready(function() {
	var url=location.pathname;
	//reception's page
	if(url=="/cdi-init/faces/template/list.jsf" || url=="/cdi-init/faces/template/details.jsf"){
		$('a[href="/cdi-init/faces/template/list.jsf"]').addClass('ui-state-active');
	}
});
