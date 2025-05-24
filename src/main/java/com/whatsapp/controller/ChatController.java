package com.whatsapp.controller;

import com.whatsapp.entity.Chat;
import com.whatsapp.entity.User;
import com.whatsapp.exception.ChatException;
import com.whatsapp.exception.UserException;
import com.whatsapp.request.SingleChatRequest;
import com.whatsapp.response.ApiResponse;
import com.whatsapp.service.ChatService;
import com.whatsapp.service.GroupChatRequest;
import com.whatsapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @PostMapping("/single")
    public ResponseEntity<Chat> createChatHandler(@RequestBody SingleChatRequest singleChatRequest, @RequestHeader("Authorization") String jwt) throws UserException {
        User reqUser=userService.findUserProfile(jwt);

        Chat chat = chatService.createChat(reqUser,singleChatRequest.getUserId());

        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @PostMapping("/group")
    public ResponseEntity<Chat> createGroupHandler(@RequestBody GroupChatRequest req, @RequestHeader("Authorization") String jwt) throws UserException {
        User reqUser=userService.findUserProfile(jwt);

        Chat chat = chatService.createGroup(req,reqUser);

        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> findChatByIdHandler(@PathVariable Integer chatId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        Chat chat = chatService.findChatById(chatId);

        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Chat>> findAllChatByUserIdHandler(@RequestHeader("Authorization") String jwt) throws UserException {
        User reqUser=userService.findUserProfile(jwt);

        List<Chat> chats = chatService.findAllChatByUserId(reqUser.getId());

        return new ResponseEntity<List<Chat>>(chats, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<Chat> addUserToGroupHandler(Integer userId,Integer chatId,@RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User reqUser=userService.findUserProfile(jwt);

        Chat chat = chatService.addUserToGroup(userId,chatId,reqUser);

        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<Chat> removeFromGroupHandler(Integer userId,Integer chatId,@RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User reqUser=userService.findUserProfile(jwt);

        Chat chat = chatService.removeFromGroup(chatId,userId,reqUser);

        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }


    @DeleteMapping("/{chatId}")
    public ResponseEntity<ApiResponse> deleteChatHandler(Integer chatId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User reqUser=userService.findUserProfile(jwt);

        chatService.deleteChat(chatId, reqUser.getId());

        ApiResponse apiResponse=new ApiResponse("chat is deleted successfully",true);
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    }
}
