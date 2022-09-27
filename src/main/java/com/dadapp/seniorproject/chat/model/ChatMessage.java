package com.dadapp.seniorproject.chat.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "chat_message")
public class ChatMessage {

	private Long id;
	private LocalDate date;
	private String sender;
	private String recipient;
	private String channel;
	private String content;

	public ChatMessage() {
		this.date = LocalDate.now();
	}

	public ChatMessage(String content) {
		this.content = content;
		this.date = LocalDate.now();
	}

	public ChatMessage(String content, String channel) {
		this.content = content;
		this.date = LocalDate.now();
		this.channel = channel;
	}

	public ChatMessage(String sender, String recipient, String content, String channel) {
		this.sender = sender;
		this.recipient = recipient;
		this.content = content;
		this.channel = channel;
	}

	public ChatMessage(LocalDate date, String sender, String content) {
		this.date = date;
		this.sender = sender;
		this.content = content;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getChannel() {
		return channel;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
}
