package com.example.pm.invitation.service;

import com.example.pm.invitation.exception.MailsException;
import com.example.pm.invitation.model.Invitation;
import jakarta.mail.MessagingException;

public interface InvitationService {
    public void sendInvitation(String email, Long projectId) throws MailsException, MessagingException;

    public Invitation acceptInvitation(String token, Long userId) throws Exception;
    public String getTokenByUserMail(String userEmail);
    public void deleteToken(String token);
}
