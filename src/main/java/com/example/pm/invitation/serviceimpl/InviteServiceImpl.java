package com.example.pm.invitation.serviceimpl;


import com.example.pm.invitation.exception.MailsException;
import com.example.pm.invitation.model.Invitation;
import com.example.pm.invitation.repository.InvitationRepository;
import com.example.pm.invitation.service.EmailService;
import com.example.pm.invitation.service.InvitationService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InviteServiceImpl implements InvitationService {
    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private EmailService emailService;


    public void sendInvitation(String email, Long projectId) throws MailsException, MessagingException, MailsException {
        // Generate unique invitation token
        String invitationToken = UUID.randomUUID().toString();

        // Save invitation to the database
        Invitation invitation = new Invitation();
        invitation.setEmail(email);
        invitation.setProjectId(projectId);
        invitation.setToken(invitationToken);
        invitationRepository.save(invitation);


        String invitationLink = "https://pm-git-master-shivanimutkens-projects.vercel.app/accept_invitation?token="+invitationToken;
       // String secondLink = "https://project-management-react-plum.vercel.app/accept_invitation?token="+invitationToken;
      //  String localHostLink = "http://localhost:5173/accept_invitation?token="+invitationToken;

        emailService.sendEmailWithToken(email, invitationLink, "", "");

    }

    @Override
    public Invitation acceptInvitation(String token, Long userId) throws Exception {
        Invitation invitation = invitationRepository.findByToken(token);

        if (invitation == null) {
            throw new Exception("Invalid invitation token");
        }

        return invitation;

    }

    @Override
    public void deleteToken(String token) {
        invitationRepository.deleteByToken(token);

    }

    @Override
    public String getTokenByUserMail(String userEmail) {
        Invitation token = invitationRepository.findByEmail(userEmail);
        return token.getToken();
    }

}

