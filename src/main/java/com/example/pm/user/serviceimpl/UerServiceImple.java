package com.example.pm.user.serviceimpl;

import com.example.pm.projectresettoken.model.PasswordResetToken;
import com.example.pm.projectresettoken.repository.PasswordResetTokenRepository;
import com.example.pm.user.config.JwtProvider;
import com.example.pm.user.exception.UserException;
import com.example.pm.user.model.User;
import com.example.pm.user.repository.UserRepository;
import com.example.pm.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UerServiceImple implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email = JwtProvider.getEmailFromJwtToken(jwt);

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserException("user not exist with email " + email);
        }

        return user;
    }

    @Override
    public User findUserByEmail(String email) throws UserException {
        User user = userRepository.findByEmail(email);

        if (user != null) {

            return user;
        }

        throw new UserException("user not exist with username " + email);

    }

    @Override
    public User findUserById(Long userId) throws UserException {
        Optional<User> opt = userRepository.findById(userId);

        if (opt.isEmpty()) {
            throw new UserException("user not found with id " + userId);
        }
        return opt.get();
    }

    @Override
    public User updateUsersProjectSize(User user, int number) throws UserException {
        try {
            user.setProjectSize(user.getProjectSize() + number);

            if (user.getProjectSize() == -1) {
                return user;
            }

            return userRepository.save(user);
        } catch (Exception e) {

            throw new UserException("An error occurred while updating user's project size: " + e.getMessage());
        }
    }


    @Override
    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void sendPasswordResetEmail(User user) {

        // Generate a random token (you might want to use a library for this)
        String resetToken = generateRandomToken();

        // Calculate expiry date
        Date expiryDate = calculateExpiryDate();

        // Save the token in the database
        PasswordResetToken passwordResetToken = new PasswordResetToken(resetToken, user, expiryDate);
        passwordResetTokenRepository.save(passwordResetToken);
        String message = "Click the following link to reset your password: http://localhost:5173/reset-password?token=" + resetToken
                + "\n for vercel app" + "https://project-management-react-plum.vercel.app/reset-password?token=" + resetToken
                + "\n 2nd vercel app link " + "https://pm-git-master-shivanimutkens-projects.vercel.app/reset-password?token=" + resetToken
                + "\n 3rd vercel app link " + "http://localhost:5173/reset-password?token=" + resetToken;
        // Send an email containing the reset link

        sendEmail(user.getEmail(), "Password Reset",
                message);
    }

    private void sendEmail(String to, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        javaMailSender.send(mailMessage);
    }

    private String generateRandomToken() {
        return UUID.randomUUID().toString();
    }

    private Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, 10);
        return cal.getTime();
    }

}
