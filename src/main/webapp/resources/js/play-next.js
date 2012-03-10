google.load('visualization', '1', {
	packages : [ 'gauge' ]
});
google.setOnLoadCallback(drawChart);
function drawChart() {
	var data = new google.visualization.DataTable();
	data.addColumn('string', 'Label');
	data.addColumn('number', 'Value');
	data.addRows(3);
	data.setValue(0, 0, 'Test');
	data.setValue(0, 1, 80);
	data.setValue(1, 0, 'Total');
	data.setValue(1, 1, 55);

	var chart = new google.visualization.Gauge(document
			.getElementById('chart_div'));
	var options = {
		width : 400,
		height : 120,
		redFrom : 90,
		redTo : 100,
		yellowFrom : 75,
		yellowTo : 90,
		minorTicks : 5
	};
	chart.draw(data, options);
}
