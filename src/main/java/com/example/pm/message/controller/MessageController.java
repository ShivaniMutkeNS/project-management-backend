package com.example.pm.message.controller;

import com.example.pm.chat.exception.ChatException;
import com.example.pm.chat.model.Chat;
import com.example.pm.message.model.Messages;
import com.example.pm.message.service.MessageService;
import com.example.pm.project.exception.ProjectException;
import com.example.pm.project.service.ProjectService;
import com.example.pm.res.request.CreateMessageRequest;
import com.example.pm.user.exception.UserException;
import com.example.pm.user.model.User;
import com.example.pm.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;




    @PostMapping("/send")
    public ResponseEntity<Messages> sendMessage(@RequestBody CreateMessageRequest request)
            throws UserException, ChatException, ProjectException {

        User user = userService.findUserById(request.getSenderId());
        if(user==null) throw new UserException("user Not found with id "+request.getSenderId());

        Chat chats = projectService.getProjectById(request.getProjectId()).getChat();  // This method should throw ChatException if the chat is not found
        if(chats==null) throw new ChatException("Chats not found");

        Messages sentMessage = messageService.sendMessage(request.getSenderId(), request.getProjectId(), request.getContent());
        return ResponseEntity.ok(sentMessage);
    }

    @GetMapping("/chat/{projectId}")
    public ResponseEntity<List<Messages>> getMessagesByChatId(@PathVariable Long projectId)
            throws ProjectException, ChatException {
        List<Messages> messages = messageService.getMessagesByProjectId(projectId);
        return ResponseEntity.ok(messages);
    }
}
