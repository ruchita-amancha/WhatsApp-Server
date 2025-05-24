package com.whatsapp.service;

import java.util.List;

import com.whatsapp.entity.Chat;
import com.whatsapp.entity.User;
import com.whatsapp.exception.ChatException;
import com.whatsapp.exception.UserException;

public interface ChatService {

	
	public Chat createChat(User reqUser,Integer userId2) throws UserException;
	
	public Chat findChatById(Integer chatId) throws ChatException;
	
	public List<Chat> findAllChatByUserId(Integer userId) throws UserException;
	
	public Chat createGroup(GroupChatRequest req,User reqUser) throws UserException;
	
	public Chat addUserToGroup(Integer userId,Integer chatId,User reqUser) throws UserException, ChatException;
	
	public Chat renameGroup(Integer ChatId,String groupName,User reqUser) throws UserException,ChatException;
	
	public Chat removeFromGroup(Integer chatId,Integer userId,User reqUser) throws UserException,ChatException;
	
	public void deleteChat(Integer chatId,Integer userId) throws UserException,ChatException;

}
