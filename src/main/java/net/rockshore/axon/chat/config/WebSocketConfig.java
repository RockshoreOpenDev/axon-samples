package net.rockshore.axon.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.messaging.simp.config.EnableWebSocketMessageBroker;
import org.springframework.messaging.simp.config.MessageBrokerConfigurer;
import org.springframework.messaging.simp.config.StompEndpointRegistry;
import org.springframework.messaging.simp.config.WebSocketMessageBrokerConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableWebSocketMessageBroker
@EnableScheduling
@ImportResource(value="classpath:axonChatContext.xml")
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/messages").withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerConfigurer configurer) {
		configurer.enableSimpleBroker("/queue/", "/topic/");
//		configurer.enableStompBrokerRelay("/queue/", "/topic/");
		configurer.setApplicationDestinationPrefixes("/app");
	}

}
