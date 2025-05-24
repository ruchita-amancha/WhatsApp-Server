package com.whatsapp.controller;

import com.whatsapp.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class RealtimeChat {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    @SendTo("/group/public")
    public Message receiveMMessage(@Payload Message message){
        simpMessagingTemplate.convertAndSend("/group/"+message.getChat().getId().toString(),message);
        return message;
    }
}
