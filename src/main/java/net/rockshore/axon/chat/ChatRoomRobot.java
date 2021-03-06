package net.rockshore.axon.chat;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.commandhandling.callbacks.LoggingCallback;
import org.jgroups.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomRobot implements SmartLifecycle {
	
	@Resource(name="commandBus")
	CommandBus commandBus;

	private boolean isRunning;
	private final ScheduledExecutorService executor = Executors
			.newScheduledThreadPool(1);
	private String robotName = "robot";

	@Override
	public boolean isRunning() {
		return isRunning;
	}

	@Override
	public void start() {
		isRunning = true;
		for (ChatRoomsType type : ChatRoomsType.values()) {;
		CommandMessage message = GenericCommandMessage
			.asCommandMessage(new CreateChatRoomCommand(type.name()));
			commandBus.dispatch(message, new LoggingCallback(message));
			message = GenericCommandMessage
					.asCommandMessage(new JoinChatRoomCommand(type.name(),
							robotName));
			commandBus.dispatch(message, new LoggingCallback(message));
		}
		executor.execute(new Runnable() {

			@Override
			public void run() {

				if (isRunning) {
					
					for (ChatRoomsType type : ChatRoomsType.values()) {
						CommandMessage message = GenericCommandMessage
						.asCommandMessage(new SendMessageCommand(type
								.name(), robotName, "RANDOM MESSAGE: "
								+ UUID.randomUUID().toString()));
						commandBus.dispatch(message/*,new LoggingCallback(message)*/);
					}
					executor.schedule(this, 5000, TimeUnit.MILLISECONDS);
				}

			}
		});
	}

	@Override
	public void stop() {
		isRunning = false;
		executor.shutdown();
		try {
			executor.awaitTermination(2000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getPhase() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		stop();
		callback.run();
		
	}

}
