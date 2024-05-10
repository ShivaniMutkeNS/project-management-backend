package com.example.pm.message.model;


import com.example.pm.chat.model.Chat;
import com.example.pm.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    @ToString.Exclude
    @JsonIgnore
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;


}
