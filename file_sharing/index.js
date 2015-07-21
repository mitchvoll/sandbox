var siofu = require('socketio-file-upload');
var app = require('express').use(siofu.router);
var http = require('http').Server(app);
var io = require('socket.io')(http);
// file system utilities
var file = require('fs');

app.get('/', function(req, res){
  res.sendFile(__dirname + '/index.html');
});

io.on('connection', function(socket){
  var uploader = new siofu();
  uploader.dir = 'temp/';
  uploader.listen(socket);
});

// io.on('connection', function(socket){
//   console.log('a user connected');

//   socket.on('name submit', function(name){
//   	console.log(name + ' is now in chat');
//   	io.emit('new name', name);
//   });
// });

http.listen(3000, function(){
  console.log('listening on port:3000');
});
