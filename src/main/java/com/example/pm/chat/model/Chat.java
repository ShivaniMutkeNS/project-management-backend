package com.example.pm.chat.model;


import com.example.pm.message.model.Messages;
import com.example.pm.project.model.Project;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "chats")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "project_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Project project;//one project can have one chat

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Messages> messages;

    @ToString.Exclude
    @OneToMany(mappedBy = "chat")
    List<ChatUser> participants;

}