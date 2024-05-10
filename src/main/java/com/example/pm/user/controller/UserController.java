package com.example.pm.user.controller;

//tested

import com.example.pm.user.UserDTO.UpdateUser;
import com.example.pm.user.exception.UserException;
import com.example.pm.user.model.User;
import com.example.pm.user.repository.UserRepository;
import com.example.pm.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//Admin API
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserProfileHandler(
            @RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.findUserProfileByJwt(jwt);
        user.setPassword(null);//to dont display password to FE

        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        User user = userRepository.findByEmail(email);
        user.setPassword(null);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/users/{userId}")
    public ResponseEntity<User> findUserById(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.findUserById(userId);
        user.setPassword(null);

        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userRepository.deleteById(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody UpdateUser updateUser) {
        Optional<User> existingUserOptional = userRepository.findById(userId);
        if (existingUserOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User existingUser = existingUserOptional.get();
        existingUser.setFullName(updateUser.getFullName());
        existingUser.setProjectSize(updateUser.getProjectNumber());

        User updatedUser = userRepository.save(existingUser);

        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/{userId}/project-size")
    public ResponseEntity<User> updateUserProjectSize(@PathVariable Long userId, @RequestParam int number) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User user = userOptional.get();
            user.setProjectSize(user.getProjectSize() + number);

            if (user.getProjectSize() == -1) {
                return ResponseEntity.ok(user);
            }

            User updatedUser = userRepository.save(user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
