package com.example.pm.chat.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class ChatUserKey implements Serializable {
    //test with commented code
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatUserKey that)) return false;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getChatId(), that.getChatId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getChatId());
    }

    @Getter
    @Setter
    @Column(name = "user_id")
    Long userId;

    @Getter
    @Setter
    @Column(name = "chat_id")
    Long chatId;
}
