package com.example.pm.invitation.repository;

import com.example.pm.invitation.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    void deleteByToken(String token);

    Invitation findByToken(String token);

    Invitation findByEmail(String userEmail);
}
