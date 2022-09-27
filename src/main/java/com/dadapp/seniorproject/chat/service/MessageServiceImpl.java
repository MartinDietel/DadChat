package com.dadapp.seniorproject.chat.service;

import com.dadapp.seniorproject.chat.model.ChatMessage;
import com.dadapp.seniorproject.chat.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepo messageRepo;

    @Override
    public void save(ChatMessage chatMessage) {
        messageRepo.save(chatMessage);
    }

    @Override
    public List<ChatMessage> findAll() {
        return messageRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public Page<ChatMessage> findAll(int page) {

        PageRequest pageReq = PageRequest.of(page, 30, Sort.Direction.DESC, "id");

        return messageRepo.findAll(pageReq);
    }

    @Override
    public List<ChatMessage> findAllByUsers(String sender, String recipient) {
        return messageRepo.findAllByUsers(sender, recipient);
    }

    @Override
    public List<ChatMessage> findAllByUserAndSessionId(String user, String sessionId) {
        return messageRepo.findAllBySenderAndAndChannel(user, sessionId);
    }
}
