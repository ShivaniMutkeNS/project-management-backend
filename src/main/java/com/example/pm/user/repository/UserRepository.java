package com.example.pm.user.repository;


import com.example.pm.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);
    public Optional<User> findById(Long id);
}
