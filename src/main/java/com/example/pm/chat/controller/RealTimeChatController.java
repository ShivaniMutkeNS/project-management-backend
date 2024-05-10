package com.example.pm.chat.controller;

import com.example.pm.chat.exception.ChatException;
import com.example.pm.message.model.Messages;

import com.example.pm.user.exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RealTimeChatController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @MessageMapping("/chat/{groupId}")
    public Messages sendToUser(@Payload Messages message,

                               @DestinationVariable String groupId) throws UserException, ChatException {

        simpMessagingTemplate.convertAndSendToUser(groupId, "/private",
                message);

        return message;
    }



}
