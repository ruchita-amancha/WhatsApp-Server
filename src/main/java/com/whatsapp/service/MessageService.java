package com.whatsapp.service;

import com.whatsapp.entity.Message;
import com.whatsapp.entity.User;
import com.whatsapp.exception.ChatException;
import com.whatsapp.exception.MessageException;
import com.whatsapp.exception.UserException;
import com.whatsapp.request.SendmessageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageService {

    public Message sendMessage(SendmessageRequest req) throws UserException, ChatException;

    public List<Message> getChatsMessages(Integer chatId, User reqUser) throws ChatException, UserException;

    public Message findMessageById(Integer messageId) throws MessageException;

    public void deleteMessage(Integer messageId,User userId) throws MessageException, UserException;
}
