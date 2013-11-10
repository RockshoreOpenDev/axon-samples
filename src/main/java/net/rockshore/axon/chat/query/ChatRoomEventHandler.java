package net.rockshore.axon.chat.query;

import net.rockshore.axon.chat.ChatRoomCreatedEvent;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomEventHandler {
	@Autowired
	private ChatRoomDao dao;
	
	@EventHandler
	public void handle(ChatRoomCreatedEvent event) {
		System.out.println("ChatRoomEventHandler.handle()");
		ChatRoomView view = new ChatRoomView();
		view.setChatRoomName(event.getId());
		dao.add(view);
	}
}
