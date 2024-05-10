package com.example.pm.projectresettoken.service;

import com.example.pm.projectresettoken.model.PasswordResetToken;

import java.net.InterfaceAddress;

public interface PasswordResetTokenService {
    public PasswordResetToken findByToken(String token);

    public void delete(PasswordResetToken resetToken);
}
