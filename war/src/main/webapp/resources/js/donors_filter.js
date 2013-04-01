function getNumberParameter() {
	return document.getElementById("main_content_form:donors_filter_document_number_parameter");
};

function getSeriesParameter() {
	return document.getElementById("main_content_form:donors_filter_document_series_parameter");
};

function disableDocumentParameters() {
	changeDisablePropertyForDocumentParameters(true);
};

function enableDocumentParameters() {
	changeDisablePropertyForDocumentParameters(false);
};

function changeDisablePropertyForDocumentParameters(disableValue) {
	getNumberParameter().disabled = disableValue;
	getSeriesParameter().disabled = disableValue;
};

function clearDocumentParameters() {
	getNumberParameter().value = "";
	getSeriesParameter().value = "";
};

function changeDocumentTypeParameter(documentTypeComboBox) {
	if (documentTypeComboBox.options[documentTypeComboBox.selectedIndex].value == 0) {
		disableDocumentParameters();
		clearDocumentParameters();
	} else {
		enableDocumentParameters();
	}
};