package com.example.chatroom.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.example.chatroom.entity.Message;
import com.example.chatroom.service.MessageService;
@Controller
public class WebSocketController {

    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat") 
    @SendTo("/topic/messages")
    public Message handleMessage(Message message) {
        message.setDateEnvoi(LocalDateTime.now());
        return messageService.createMessage(message); 
    }

}
