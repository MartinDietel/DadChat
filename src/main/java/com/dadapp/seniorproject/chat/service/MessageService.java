package com.dadapp.seniorproject.chat.service;

import com.dadapp.seniorproject.chat.model.ChatMessage;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MessageService {

	void save(ChatMessage chatMessage);

	List<ChatMessage> findAll();

	Page<ChatMessage> findAll(int page);

	List<ChatMessage> findAllByUsers(String sender, String recipient);

	Page<ChatMessage> findAllByUsers(int page, String sender, String recipient);

	List<ChatMessage> findAllByUserAndSessionId(String user, String sessionId);

}
