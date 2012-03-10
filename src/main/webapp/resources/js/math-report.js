function hilite(tableRowElem) {
	if (tableRowElem.className != 'zebradata-hl') {
		tableRowElem.classNameEx = tableRowElem.className;
	}
	tableRowElem.className = 'zebradata-hl';
	return false;	
}

function unhilite(tableRowElem) {
	if (tableRowElem.className == 'zebradata-hl') {
		tableRowElem.className = tableRowElem.classNameEx;
	}
	return false;
}

function installTableHighlightRow(tableRowElem) {
	tableRowElem.onmouseover = function () { return hilite(this); };
	tableRowElem.onmouseout = function () { return unhilite(this); };
	tableRowElem.onmousedown = function () { return unhilite(this); };
}

function installTableClickRow(tableRowElem) {
	tableRowElem.onclick = function() {
		var form = document.getElementsByName('tableform');
		var inputs = tableRowElem.getElementsByTagName('input');
		for ( var i = 0; i < inputs.length; i++) {
			input=inputs[i];
			if (input.className == 'rowSubmit'/* && input!=this*/) {
				input.click();
				form.subit();
				break;
			}
		}
		return false;
	};
}

function installTableRowScripts() {
	if (document.getElementById && document.createTextNode) {
		var tables = document.getElementsByTagName('table');
		for ( var i = 0; i < tables.length; i++) {
			if (tables[i].className == 'zebradata') {
				var trs = tables[i].getElementsByTagName('tr');
				for ( var j = 0; j < trs.length; j++) {
					tableRowElem=trs[j];
					if (tableRowElem.parentNode.nodeName == 'TBODY') {
						installTableHighlightRow(tableRowElem);
						installTableClickRow(tableRowElem);
					}
				}
			}
		}
	}
}

if (typeof(window.addEventListener) != "undefined") { 
	window.addEventListener("load", installTableRowScripts, false); 
}
else {
	window.attachEvent("onload", installTableRowScripts); 
}
