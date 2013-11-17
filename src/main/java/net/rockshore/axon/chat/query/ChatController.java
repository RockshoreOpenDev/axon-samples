/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.rockshore.axon.chat.query;

import java.util.List;

import net.rockshore.axon.chat.SendMessageCommand;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;


@Controller
public class ChatController {

	private static final Log logger = LogFactory.getLog(ChatController.class);

	@SubscribeMapping("/messages")
	public List<MessageView> getMessages() throws Exception {
		logger.debug("Positions for ");
		return null;
		
	}

	@MessageMapping("/comunicate")
	public void executeTrade(SendMessageCommand command) {
		logger.debug("SendMessageCommand: " + command);
	}

	@MessageExceptionHandler
	@SendToUser("/queue/errors")
	public String handleException(Throwable exception) {
		return exception.getMessage();
	}

}
