package com.example.pm.chat.repository;

import com.example.pm.chat.model.Chat;
import com.example.pm.project.model.Project;
import com.example.pm.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {


    Chat findByProject(Project projectById);
    Chat findByParticipants(User userId);

	List<Chat> findByProjectNameContainingIgnoreCase(String projectName);
}

