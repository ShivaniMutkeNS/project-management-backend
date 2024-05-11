package com.example.pm.chat.controller;


import java.util.List;

import com.example.pm.chat.exception.ChatException;
import com.example.pm.chat.model.Chat;
import com.example.pm.chat.service.ChatService;
import com.example.pm.project.exception.ProjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

//    @PostMapping("/create")
//    public ResponseEntity<Chat> createChat(@RequestBody Chat chat) throws ProjectException {
//        Chat createChat = chatService.saveChat(chat);
//        return ResponseEntity.ok(createChat);
//    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Chat>> getChatsByProjectId(@PathVariable Long projectId) throws ChatException, ProjectException, ChatException {
        List<Chat> list = (List<Chat>) chatService.getChatsByProjectId(projectId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/{chatId}/addUsers")
    public ResponseEntity<Chat> addUsersToChat(@PathVariable Long chatId,
                               @RequestParam List<Long> userIds) throws ChatException {
           Chat addedUsersToChat = chatService.addUsersToChat(chatId, userIds);
           return ResponseEntity.ok(addedUsersToChat);
    }
//not required infinite recursion
    @GetMapping("/search")
    public ResponseEntity<List<Chat>> searchChatsByName(@RequestParam String name) throws ChatException {
        List<Chat> chats = chatService.searchChatsByName(name);
        return ResponseEntity.ok(chats);
    }

}

