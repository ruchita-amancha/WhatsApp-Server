package com.whatsapp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String content;

	private LocalDateTime timeStamp;

	@ManyToOne
		private User user;

	@ManyToOne
	@JoinColumn(name="chat_id")
		private Chat chat;

	public Message() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Message(Integer id, String content, LocalDateTime timeStamp, User user, Chat chat) {
		super();
		this.id = id;
		this.content = content;
		this.timeStamp = timeStamp;
		this.user = user;
		this.chat = chat;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

}
