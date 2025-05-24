package com.whatsapp.service;

import com.whatsapp.entity.Chat;
import com.whatsapp.entity.Message;
import com.whatsapp.entity.User;
import com.whatsapp.exception.ChatException;
import com.whatsapp.exception.MessageException;
import com.whatsapp.exception.UserException;
import com.whatsapp.repository.MessageRepository;
import com.whatsapp.request.SendmessageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImplementation implements MessageService{

    private MessageRepository messageRepository;

    private UserService userService;

    private ChatService chatService;

    public MessageServiceImplementation(MessageRepository messageRepository,UserService userService,ChatService chatService) {
        this.messageRepository=messageRepository;
        this.userService=userService;
        this.chatService=chatService;
    }

    @Override
    public Message sendMessage(SendmessageRequest req) throws UserException, ChatException {
        User user=userService.findUserById(req.getUserId());
        Chat chat=chatService.findChatById(req.getChatId());
        Message message=new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setContent(req.getContent());
        message.setTimeStamp(LocalDateTime.now());

        return message;
    }

    @Override
    public List<Message> getChatsMessages(Integer chatId,User reqUser) throws ChatException, UserException {
        Chat chat=chatService.findChatById(chatId);

        if(!chat.getUsers().contains(reqUser)){
           throw new UserException("You are not related to these messages "+chat.getId());
        }

        List<Message> messages=messageRepository.findByChatId(chatId);

        return messages;
    }

    @Override
    public Message findMessageById(Integer messageId) throws MessageException {
        Optional<Message> opt=messageRepository.findById(messageId);

        if(opt.isPresent()){
            return opt.get();
        }
        throw new MessageException("Message not found with id "+messageId);
    }

    @Override
    public void deleteMessage(Integer messageId, User reqUser) throws MessageException, UserException {
        Message message=findMessageById(messageId);
        if(message.getUser().getId().equals(reqUser.getId())){
            messageRepository.deleteById(messageId);
        }

        throw new UserException("You can't delete another user message "+reqUser);
    }
}
