package com.whatsapp.controller;

import com.whatsapp.entity.Message;
import com.whatsapp.entity.User;
import com.whatsapp.exception.ChatException;
import com.whatsapp.exception.MessageException;
import com.whatsapp.exception.UserException;
import com.whatsapp.repository.MessageRepository;
import com.whatsapp.request.SendmessageRequest;
import com.whatsapp.response.ApiResponse;
import com.whatsapp.service.MessageService;
import com.whatsapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageRepository messageRepository;

    @PostMapping("/sendMsg")
    public ResponseEntity<Message> sendMessageHandler(@RequestBody SendmessageRequest req, @RequestHeader("Authorization") String jwt) throws ChatException, UserException {
        User user=userService.findUserProfile(jwt);
        req.setUserId(user.getId());
        Message message=messageService.sendMessage(req);
        messageRepository.save(message); 
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> getChatsMessagesHandler(@PathVariable Integer chatId,@RequestHeader("Authorization") String jwt) throws ChatException, UserException {
        User user=userService.findUserProfile(jwt);
        List<Message> message=messageService.getChatsMessages(chatId,user);

        return new ResponseEntity<List<Message>>(message, HttpStatus.OK);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse> deleteMessageHandler(@PathVariable Integer messageId, @RequestHeader("Authorization") String jwt) throws UserException, MessageException {
        User user=userService.findUserProfile(jwt);
       messageService.deleteMessage(messageId,user);
       ApiResponse apiResponse=new ApiResponse("Message deleted succesfully",false);

        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    }

}
