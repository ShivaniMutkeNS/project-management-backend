package com.example.pm.project.repository;

import com.example.pm.project.model.Project;
import com.example.pm.user.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT DISTINCT p FROM Project p " +
            "LEFT JOIN FETCH p.teamMembers pm " +
            "WHERE p.owner = :user OR pm.user = :user")
    List<Project> findByOwnerOrTeamMember(User user);
    List<Project> findByNameContainingAndTeamMembers_User(String partialName, User user);

    @NotNull
    Optional<Project> findById(Long id);

    List<Project> findByOwner(User owner);



}
