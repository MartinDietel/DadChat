package com.dadapp.seniorproject.chat.controller;

import com.dadapp.seniorproject.user.*;
import com.dadapp.seniorproject.bean.PagingInfo;
import com.dadapp.seniorproject.chat.model.ChatMessage;
import com.dadapp.seniorproject.chat.service.MessageService;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.security.Principal;
import java.util.*;

@Controller
public class ChatController implements ActiveUserChangeListener {
	
	private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

	private static final String ERROR_WHILE_INSERT = "Error while inserting value";
    private final Set<String> userList = new HashSet<>();
	
	@Autowired
	private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SimpMessagingTemplate webSocket;
    @Autowired
    private ActiveUserManager activeUserManager;

    @PostConstruct
    private void init() {
        activeUserManager.registerListener(this);
    }

    @PreDestroy
    private void destroy() {
        activeUserManager.removeListener(this);
    }
	
    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    public String chat(Model model, Principal principal) {
        String userName = principal.getName().trim();
        User userInfo = userRepo.findByUsername(userName);
        model.addAttribute("userName", userName);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("userList", userService.findAllUsers());
    	return "chat";
    }

    @MessageMapping("/message/send")
    @SendTo("/topic/messages/flow")
    public ChatMessage message(SimpMessageHeaderAccessor headerAccessor, StompHeaderAccessor accessor, @Payload ChatMessage chatMessage) throws Exception {

    	String username = StringEscapeUtils.escapeHtml4(Objects.requireNonNull(headerAccessor.getUser()).getName());
        try {
            chatMessage.setSender(username);
            chatMessage.setRecipient(getActiveUsersExceptCurrentUser(username));
            chatMessage.setChannel(StringEscapeUtils.escapeHtml4(accessor.getSessionId()));
            chatMessage.setContent(StringEscapeUtils.escapeHtml4(chatMessage.getContent()));

            messageService.save(chatMessage);

            userList.add(username);


        }
        catch(Exception e) {
            System.out.println("Something went wrong.");
        }
        return chatMessage;
    }



    @MessageMapping("/messages/askList")
    @SendTo("/topic/messages/list")
    public Page<ChatMessage> messagesList(PagingInfo page) {
    	return messageService.findAll(page.getPage());
    }

    @MessageMapping("/users/askList")
    @SendToUser("/topic/users/list")
    public List<String> usersList() {
        return new ArrayList<>(userList);
    }
    
    @MessageExceptionHandler
    @SendToUser("/topic/message/error")
    public ChatMessage error(Exception e) {
    	return new ChatMessage(ERROR_WHILE_INSERT + "." + e);
    }
    
    @EventListener
    public void onSocketConnected(SessionConnectedEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String user = Objects.requireNonNull(sha.getUser()).getName();

        String username = StringEscapeUtils.escapeHtml4(user);
        logger.info("[Connected] " + username);
        userList.add(username);
        activeUserManager.add(username, sha.getSessionId());
        
        webSocket.convertAndSend("/topic/users/list", new ArrayList<>(userList));
    }

    @EventListener
    public void onSocketDisconnected(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String user = Objects.requireNonNull(sha.getUser()).getName();
        
        String username = StringEscapeUtils.escapeHtml4(user);
        logger.info("[Disconnected] " + username);
        userList.remove(username);
        activeUserManager.remove(username);
        
        webSocket.convertAndSend("/topic/users/list", new ArrayList<>(userList));
    }

    @Override
    public void notifyActiveUserChange() {
        Set<String> activeUsers = activeUserManager.getAll();
        webSocket.convertAndSend("/topic/active", activeUsers);
    }

    public String getActiveUsersExceptCurrentUser(String userName) {
        return activeUserManager.getActiveUsersExceptCurrentUser(userName).toString().replace("[", "").replace("]", "");
    }
}