package net.rockshore.axon.chat;

import java.util.HashSet;
import java.util.Set;

import lombok.NoArgsConstructor;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;

@NoArgsConstructor
public class ChatRoom extends AbstractAnnotatedAggregateRoot<String> {

	private static final long serialVersionUID = -5999295352637586208L;

	@AggregateIdentifier
	private String id;

	private final Set<String> participants = new HashSet<String>();

	@CommandHandler
	public ChatRoom(CreateChatRoomCommand command) {
		apply(new ChatRoomCreatedEvent(command.getId()));
	}

	@CommandHandler
	public void handle(JoinChatRoomCommand command) {
		if (!participants.contains(command.getUserName())) {
			apply(new ParticpantJoinedChatRoomEvent(this.id,
					command.getUserName()));
		}
	}

	@CommandHandler
	public void handle(LeaveChatRoomCommand command) {
		if (participants.contains(command.getUserName())) {
			apply(new ParticipantLeftChatRoomEvent(this.id,
					command.getUserName()));
		}
	}

	@CommandHandler
	public void handle(SendMessageCommand command) {
		if (participants.contains(command.getUsername())) {
			apply(new MessageSentEvent(this.id, command.getUsername(),
					command.getMessage()));
		}
	}

	@EventHandler
	public void handle(ChatRoomCreatedEvent event) {
		this.id = event.getId();
	}

	@EventHandler
	public void handle(ParticpantJoinedChatRoomEvent event) {
		participants.add(event.getUserName());
	}

	@EventHandler
	public void handle(ParticipantLeftChatRoomEvent event) {
		participants.remove(event.getUserName());
	}
}
