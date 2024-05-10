package com.example.pm.chat.service;


import com.example.pm.chat.exception.ChatException;
import com.example.pm.chat.model.Chat;
import com.example.pm.project.exception.ProjectException;

import java.util.List;

public interface ChatService {

    Chat saveChat(Chat chat);

    Chat getChatsByProjectId(Long projectId) throws ChatException, ProjectException;
  //  Chat getChatsByUserId(Long userId) throws ChatException, ProjectException;

	Chat addUsersToChat(Long chatId, List<Long> userIds) throws ChatException;

    Chat getChatsByUserId(Long userId) throws ChatException, ProjectException;
	 List<Chat> searchChatsByName(String name) throws ChatException;

    // Other methods as needed
}
