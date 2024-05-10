package com.example.pm.message.service;

import com.example.pm.chat.exception.ChatException;
import com.example.pm.message.model.Messages;
import com.example.pm.project.exception.ProjectException;
import com.example.pm.user.exception.UserException;

import java.util.List;

public interface MessageService {

    Messages sendMessage(Long senderId, Long chatId, String content) throws UserException, ChatException, ProjectException;

    List<Messages> getMessagesByProjectId(Long projectId) throws ProjectException, ChatException;
}