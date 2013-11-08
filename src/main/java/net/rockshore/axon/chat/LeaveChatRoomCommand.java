package net.rockshore.axon.chat;

import lombok.Data;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

@Data
public class LeaveChatRoomCommand {
	@TargetAggregateIdentifier
	private final String id;
	private final String userName;

}
