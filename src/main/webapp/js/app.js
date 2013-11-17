function ApplicationModel(stompClient) {
	var self = this;

	self.connect = function() {
		stompClient.connect('', '', function(frame) {

			console.log('Connected ' + frame + " User: " + frame.headers['user-name']);
			stompClient.subscribe("/messages", function(message) {
				console.log('Message ' + message);
			});
			
			stompClient.subscribe("/comunicate", function(message) {
				console.log('Message ' + message);
			});

			stompClient.subscribe("/topic/message.*", function(message) {
				// self.portfolio().processQuote(JSON.parse(message.body));
				$("#messages").append(
						"<p>" + JSON.parse(message.body).message + "</p>");
				console.log("Protocol: " + stompClient.ws.protocol);
				// console.log('Message ' + message);
			});
			var sendMessageCommand = {
					"id" : "bob",
					"username" : "Web user",
					"message" : "Hello world"
			};
			stompClient.send("/app/comunicate", {'user-name':"guest"}, JSON.stringify(sendMessageCommand));
		});
	}
}
