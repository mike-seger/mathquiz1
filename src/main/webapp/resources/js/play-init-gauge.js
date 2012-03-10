// Helper to execute a function after the window is loaded,
// see http://www.google.com/search?q=addLoadEvent
function addLoadEvent(func) {
	var oldonload = window.onload;
	if (typeof window.onload != 'function') {
		window.onload = func;
	} else {
		window.onload = function() {
			if (oldonload) {
				oldonload();
			}
			func();
		};
	}
}

addLoadEvent( function() {
	var options, gm;

	// Draw the gauge using custom settings (medium)
	options = {
		value: 0,
		label: 'Last',
		unitsLabel: '/10',
		min: 0,
		max: 10,
		majorTicks: 6,
		minorTicks: 1, // small ticks inside each major tick
		greenFrom: 9,
		greenTo: 10,
		greenColor: '#00FF00', // full green
		yellowFrom: 7,
		yellowTo: 9,
		yellowColor: '#FFFF00', // full yellow
		redFrom: 0,
		redTo: 7,
		redColor:  '#FF0000' // full red
	};
	gm = new Gauge( document.getElementById( 'score'), options );
	gm.setValue( document.getElementById( 'v_score').value * 10 );
	options = {
			value: 0,
			label: 'High Score',
			unitsLabel: '',
			min: 0,
			max: 1000,
			majorTicks: 11,
			minorTicks: 1, // small ticks inside each major tick
			redFrom: 0,
			redTo: 200,
			redColor:  '#FFFFFF',
			yellowFrom: 200,
			yellowTo: 600,
			yellowColor: '#FFFF00', // full yellow
			greenFrom: 600,
			greenTo: 1000,
			greenColor: '#00FF00' // full green
		};
	gm = new Gauge( document.getElementById( 'total'), options );
	gm.setValue( document.getElementById( 'v_totalscore').value * 1000);

	setInterval(function() {
		( Math.floor(Math.random()*100) );
	}, 3000);
} );

window.onkeypress = function(e) {
	if (e.which == 13) {
		var okElem=document.getElementById("playNext-ok");
		if(okElem != null) {
			okElem.click();
		}
	}
};

