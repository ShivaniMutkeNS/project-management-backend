package com.example.pm.user.service;

import com.example.pm.user.exception.UserException;
import com.example.pm.user.model.User;

import java.util.List;

public interface UserService {

    public User findUserProfileByJwt(String jwt) throws UserException;

    public User findUserByEmail(String email) throws UserException;

    public User findUserById(Long userId) throws UserException;

    public  User updateUsersProjectSize(User user, int number) throws UserException;

    void updatePassword(User user, String newPassword);

    void sendPasswordResetEmail(User user);

}