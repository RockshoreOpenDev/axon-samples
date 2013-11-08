package net.rockshore.axon.chat;

import lombok.Data;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

@Data
public class SendMessageCommand {
	@TargetAggregateIdentifier
	private final String id;
	private final String username;
	private final String message;
}
