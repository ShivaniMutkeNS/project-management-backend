package com.example.pm.message.serviceimpl;


import com.example.pm.chat.exception.ChatException;
import com.example.pm.chat.model.Chat;
import com.example.pm.chat.repository.ChatRepository;
import com.example.pm.message.model.Messages;
import com.example.pm.message.repository.MessageRepository;
import com.example.pm.message.service.MessageService;
import com.example.pm.project.exception.ProjectException;
import com.example.pm.project.service.ProjectService;
import com.example.pm.user.exception.UserException;
import com.example.pm.user.model.User;
import com.example.pm.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ProjectService projectService;

    @Autowired
    private ChatRepository chatRepository;

    @Override
    public Messages sendMessage(Long senderId, Long projectId, String content) throws UserException, ChatException, ProjectException, ProjectException {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new UserException("User not found with id: " + senderId));

        Chat chat = projectService.getProjectById(projectId).getChat();

        Messages message = new Messages();
        message.setContent(content);
        message.setSender(sender);
        message.setCreatedAt(LocalDateTime.now());
        message.setChat(chat);
        Messages savedMessage=messageRepository.save(message);

        chat.getMessages().add(savedMessage);
        chatRepository.save(chat);
        return savedMessage;
    }

    @Override
    public List<Messages> getMessagesByProjectId(Long projectId) throws ProjectException, ChatException, ChatException {
        Chat chat = projectService.getChatByProjectId(projectId);
        List<Messages> findByChatIdOrderByCreatedAtAsc = messageRepository.findByChatIdOrderByCreatedAtAsc(chat.getId());
        return findByChatIdOrderByCreatedAtAsc;
    }
}


