package com.example.pm.chat.model;

import com.example.pm.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ChatUser {
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof ChatUser chatUser)) return false;
//        return Objects.equals(getId(), chatUser.getId()) && Objects.equals(getUser(), chatUser.getUser()) && Objects.equals(getChat(), chatUser.getChat());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(getId(), getUser(), getChat());
//    }

    @EmbeddedId
    ChatUserKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;


    @ManyToOne
    @MapsId("chatId")
    @JoinColumn(name = "chat_id")
    Chat chat;

    public ChatUser(Chat chat, User user) {
        this.chat = chat;
        this.user = user;
    }

}

