package com.example.pm.projectresettoken.model;

import com.example.pm.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    private @NonNull String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private @NonNull User user;

    private @NonNull Date expiryDate;
    public boolean isExpired() {
        return expiryDate.before(new Date());
    }

}