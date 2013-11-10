package net.rockshore.axon.chat.query;

import net.rockshore.axon.chat.MessageSentEvent;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageEventHandler {
	@Autowired
	private MessageDao dao;
	
	@EventHandler
	public void handle(MessageSentEvent event) {
		System.out.println("MessageEventHandler.handle()");
		MessageView view = new MessageView();
		view.setMessage(event.getMessage());
		view.setUsername(event.getUsername());
		dao.add(view);
	}
}
