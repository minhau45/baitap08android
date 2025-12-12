var express = require('express');
var app = express();
var http = require('http');
var server = http.createServer(app);
var io = require('socket.io')(server);

server.listen(3000, () => {
    console.log("Socket server running on port 3000");
});

var list_user = [];

io.on('connection', function(socket) {
    console.log('A user connected');
    socket.on('user_login', function(user_name) {
        if (list_user.includes(user_name)) {
            socket.emit("login_failed", "Username already exists");
            return;
        }

        list_user.push(user_name);
        socket.user = user_name;

        socket.emit("login_success", user_name);
        console.log("User logged in: " + user_name);
    });
    socket.on('send_message', function(message) {
        io.emit('receive_message', {
            user: socket.user,
            message: message
        });
    });
    socket.on("disconnect", function() {
        console.log("User disconnected: " + socket.user);
        list_user = list_user.filter(u => u !== socket.user);
    });
});
