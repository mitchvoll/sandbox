var express = require('express');
var app = express();
var http = require('http');
var port = 3000;
var io = require('socket.io').listen(app.listen(port));
var fs = require('fs');
// mongoDB
var mongoose = require('mongoose');
mongoose.connect('mongodb://localhost/stocks');
var db = mongoose.connection;

// global variables
var stock_symbols = []

db.on('error', console.error.bind(console, 'connection error: '));

db.once('open', function (callback){

	console.log('Connected to db');
	// schema
	var stock_schema = mongoose.Schema({
		symbol: String,
		name: String,
		data: []
	})
	// model
	var Stock = mongoose.model('Stock', stock_schema);

	var download_stocks = function (){
		console.log("downloading stock csv files");
		var all_stocks = read_csv('sp500.csv').slice(1);
		for (var i = 0; i < all_stocks.length; i++){
			var symbol = all_stocks[i][0];
			get_stock(symbol);
		}

	}

	// get company name and ticker symbols for sp500 and add to database
	var seed_db = function (){
		console.log("seeding database");
		var all_stocks = read_csv('sp500.csv');
		for (var i = 0; i < all_stocks.length; i++){
			var current_symbol = all_stocks[i][0]; var current_name = all_stocks[i][1];
			var stock_data =  read_csv(current_symbol+".csv");

			var stock = new Stock({
				symbol: current_symbol,
				name: current_name,
				data: prep_data(stock_data)
			});

			stock.save(function (err, stock){
				if (err) return console.error(err);
				console.log("added " + stock.name);
			});
		}
	}

	// initialize the application by checking to see if the data exists in 
	// the database and the stock csv files have been downloaded to the folder
	// stock_data. Setup application if this data does not exist
	var initialize = function (){
		console.log("Initializing application...");
		Stock.count({}, function (err, count){
			// seed the databse if there is not a significant amount of stocks
			console.log(count + " stocks in database");
			var files_in_folder = fs.readdirSync('stock_data').length;
			console.log(files_in_folder + " files in folder \'stock_data/\'");
			if (count < 100){
				// download stock csv files if they do not exist in /stock_data
				if (files_in_folder < 100){
					download_stocks();
				}
				seed_db();
			}

		});
	}
	initialize();

	Stock.find(function (err, stocks){
		if (err) return console.error(err);
		for (var i = 0; i < stocks.length; i++){
			stock_symbols.push(stocks[i].symbol);
		}
		
	});

// serve static files
app.use('/public', express.static('public'));

// load home
app.get('/', function(req, res){
	res.sendFile(__dirname + '/public/index.html');
});

// esstablish connection with client
io.on('connection', function (socket){
	// send stock_data on connection load
	socket.emit('stock_symbols', stock_symbols);


	socket.on('stock_request', function (data){
		console.log("Client requested symbol: " + data);
		Stock.findOne({ symbol: data }, function (err, results){
			if (err) return console.error(err);
			socket.emit('stock_data', results.data);
		});
	});

});


function read_csv(file_name){
	data = fs.readFileSync('stock_data/'+file_name, 'utf8')
	return parse_csv(data);
}

// download a stock symbol as /stock_data/symbol.csv
function get_stock(symbol){
	var file = fs.createWriteStream("stock_data/" + symbol + ".csv");
	var request = http.get("http://real-chart.finance.yahoo.com/table.csv?s=" + symbol + "&a=07&b=1&c=2014&d=07&e=1&f=2015&g=d&ignore=.csv", function(response) {
		response.pipe(file);
	});
}


// helper function: parses csv file to 2d array
function parse_csv(data){
	var csv = [];
	var lines = data.split('\n');

	for (var i = 0; i < lines.length; i++){
		csv[i] = lines[i].split(',');
	}
	return csv;
}

// helper function: returns as 2d array of relevent columns
function prep_data(data){
	var prepped_data = [];

	for (var i = 0; i < data.length - 1; i++){
		// push the date(data[i][0]) and close price(data[i][4])
		prepped_data.push([data[i][0], data[i][4]]);
	}
	return prepped_data;
}

});
