var siofu = require('socketio-file-upload');
var express = require('express');
var app = express().use(siofu.router);
var http = require('http').Server(app);
var io = require('socket.io')(http);
// file system utilities
var file = require('fs');

// serve static files
app.use('/public', express.static('public'));

app.get('/', function(req, res){
	res.sendFile(__dirname + '/public/index.html');
});

io.on('connection', function(socket){
	var uploader = new siofu();
	uploader.dir = './temp_folders/';
	uploader.listen(socket);
});

// io.on('connection', function(socket){
//   console.log('a user connected');

//   socket.on('name submit', function(name){
//   	console.log(name + ' is now in chat');
//   	io.emit('new name', name);
//   });
// });

http.listen(8081, function(){
	console.log('listening on port:8081');
});
