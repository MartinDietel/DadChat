package com.dadapp.seniorproject.bean;

import com.dadapp.seniorproject.chat.model.ChatMessage;

public class PagingInfo {

	private int page;

	public ChatMessage chatMessage;
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}

}
