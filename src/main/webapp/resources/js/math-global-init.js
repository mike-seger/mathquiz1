var sizeFactor = 0.045;
var winW = 630, winH = 460;
var screenW = 640, screenH = 480;

function recalDimensions() {
	if (document.body && document.body.offsetWidth) {
		winW = document.body.offsetWidth;
		winH = document.body.offsetHeight;
	}
	if (document.compatMode == 'CSS1Compat' && document.documentElement
			&& document.documentElement.offsetWidth) {
		winW = document.documentElement.offsetWidth;
		winH = document.documentElement.offsetHeight;
	}
	if (window.innerWidth && window.innerHeight) {
		winW = window.innerWidth;
		winH = window.innerHeight;
	}
	
	if (parseInt(navigator.appVersion) > 3) {
		screenW = screen.width;
		screenH = screen.height;
	} else if (navigator.appName == "Netscape"
			&& parseInt(navigator.appVersion) == 3 && navigator.javaEnabled()) {
		var jToolkit = java.awt.Toolkit.getDefaultToolkit();
		var jScreenSize = jToolkit.getScreenSize();
		screenW = jScreenSize.width;
		screenH = jScreenSize.height;
	}
}

function initFomtSize() {
	recalDimensions();
	var minDim = winH;
	if (winW < 0.9*winH)
		minDim = winW*1.1;
	var body = document.getElementsByTagName('body')[0];
	body.style.fontSize = (minDim * sizeFactor) + 'px';
}

window.onload = function () { initFomtSize(); };
window.onresize = function () { initFomtSize(); };
