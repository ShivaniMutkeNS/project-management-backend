package com.example.pm.chat.service;

import com.example.pm.chat.exception.ChatException;
import com.example.pm.chat.model.Chat;
import com.example.pm.chat.model.ChatUser;
import com.example.pm.chat.model.ChatUserKey;
import com.example.pm.chat.repository.ChatRepository;
import com.example.pm.chat.repository.ChatUserRepository;
import com.example.pm.project.exception.ProjectException;
import com.example.pm.project.model.Project;
import com.example.pm.project.repository.ProjectRepository;
import com.example.pm.user.model.User;
import com.example.pm.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatUserRepository chatUserRepository;


    @Override
    public Chat saveChat(Chat chat,User user) {
        Chat savedChat = chatRepository.save(chat);

        ChatUser chatUser = new ChatUser();

        ChatUserKey chatUserKey = new ChatUserKey(savedChat.getId(), user.getId());

        chatUser.setId(chatUserKey);
        chatUser.setUser(user);
        chatUser.setChat(savedChat);

        chatUserRepository.save(chatUser);

        return savedChat;
    }

    @Override
    public Chat getChatsByProjectId(Long projectId) throws ChatException, ProjectException {
        Project project = projectRepository.getById(projectId);

        return chatRepository.findByProject(project);
    }


    @Override
    public Chat getChatsByUserId(Long userId) throws ChatException, ProjectException {
        User user = userRepository.getById(userId);

        return chatRepository.findByParticipants(user);
    }

    @Override
    public Chat addUsersToChat(Long chatId, List<Long> userIds) throws ChatException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatException("Chat not found with id: " + chatId));

        // Find the users by their IDs
        List<User> usersToAdd = userRepository.findAllById(userIds);

        // Create ChatUser instances and associate them with the chat
        List<com.example.pm.chat.model.ChatUser> chatUsers = new ArrayList<>();
        for (User user : usersToAdd) {
            com.example.pm.chat.model.ChatUser chatUser = new com.example.pm.chat.model.ChatUser(chat, user);
            chatUsers.add(chatUser);
        }

        // Add the ChatUser instances to the chat's participants
        chat.getParticipants().addAll(chatUsers);

        return chatRepository.save(chat);
    }


    @Override
    public List<Chat> searchChatsByName(String name) throws ChatException {
        List<Chat> searchedChats = chatRepository.findByProjectNameContainingIgnoreCase(name);
        if (searchedChats != null) return searchedChats;
        throw new ChatException("Chats not available");
    }

    // Other methods as needed
}

