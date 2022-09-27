package com.dadapp.seniorproject.chat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WsConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {

		registry.addEndpoint("/chat").withSockJS();
	}
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		/* queue prefix for SUBSCRIPTION (FROM server to CLIENT)  */
		registry.enableSimpleBroker("/topic", "/queue");
		/* queue prefix for SENDING messages (FROM client TO server) */
		registry.setApplicationDestinationPrefixes("/app");
		/* queue prefix for SENDING messages (FROM client TO server TO user) */
		registry.setUserDestinationPrefix("/user");
	}
}
