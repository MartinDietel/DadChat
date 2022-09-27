package com.dadapp.seniorproject.chat.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WsConfig implements WebSocketMessageBrokerConfigurer {

	@Autowired
	HttpHandshakeInterceptor handshakeInterceptor;

	@Autowired
	DefaultHandshakeHandler defaultHandshakeHandler;

	ServerHttpRequest request;
	ServerHttpResponse response;
	WebSocketHandler wsHandler;
	Map<String, Object> attributes = new HashMap<String, Object>();

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {

		((HandshakeHandler) registry.addEndpoint("/chat").addInterceptors(handshakeInterceptor)
				.setHandshakeHandler(new DefaultHandshakeHandler()))
				.doHandshake(request, response, wsHandler, attributes);

		ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
		HttpSession session = servletRequest.getServletRequest().getSession();
		attributes.put("sessionId", session.getId());

	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		/* queue prefix for SUBSCRIPTION (FROM server to CLIENT) */
		registry.enableSimpleBroker("/topic", "/queue");
		/* queue prefix for SENDING messages (FROM client TO server) */
		registry.setApplicationDestinationPrefixes("/app");
		/* queue prefix for SENDING messages (FROM client TO server TO user) */
		registry.setUserDestinationPrefix("/user");
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(new ChannelInterceptor() {

			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				System.out.println("configureClientInboundChannel");
				StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
				accessor.getSessionAttributes().get("user");
				return ChannelInterceptor.super.preSend(message, channel);
			}
		});
	}
}
