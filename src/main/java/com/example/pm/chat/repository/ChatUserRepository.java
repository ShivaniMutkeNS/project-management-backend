package com.example.pm.chat.repository;

import com.example.pm.chat.model.Chat;
import com.example.pm.chat.model.ChatUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    @Transactional
    void deleteByChatId(Long id);

}
