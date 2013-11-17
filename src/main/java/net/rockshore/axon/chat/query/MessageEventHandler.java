package net.rockshore.axon.chat.query;

import net.rockshore.axon.chat.MessageSentEvent;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class MessageEventHandler {
	
	@Autowired
	private MessageDao dao;
	
	private final MessageSendingOperations<String> messagingTemplate;
	
	@Autowired
	public MessageEventHandler(MessageSendingOperations<String> messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}
	
	@EventHandler
	public void handle(MessageSentEvent event) {
		MessageView view = new MessageView();
		view.setMessage(event.getMessage());
		view.setUsername(event.getUsername());
		dao.add(view);
		this.messagingTemplate.convertAndSend("/topic/message." + event.getUsername(), event);
	}
}
