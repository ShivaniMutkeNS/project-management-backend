package com.example.pm.project.repository;

import com.example.pm.project.model.ProjectUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {
    @Transactional
    void deleteByProjectId(Long projectId);

}
