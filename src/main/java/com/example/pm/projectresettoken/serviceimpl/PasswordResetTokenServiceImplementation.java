package com.example.pm.projectresettoken.serviceimpl;

import com.example.pm.projectresettoken.model.PasswordResetToken;
import com.example.pm.projectresettoken.repository.PasswordResetTokenRepository;
import com.example.pm.projectresettoken.service.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetTokenServiceImplementation implements PasswordResetTokenService {
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public PasswordResetToken findByToken(String token) {
        PasswordResetToken passwordResetToken =passwordResetTokenRepository.findByToken(token);
        return passwordResetToken;
    }

    @Override
    public void delete(PasswordResetToken resetToken) {
        passwordResetTokenRepository.delete(resetToken);

    }

}

