package com.example.pm.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProjectUserKey implements Serializable {

    @Column(name = "user_id")
    Long userId;

    @Column(name = "project_id")
    Long projectId;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectUserKey that)) return false;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getProjectId(), that.getProjectId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getProjectId());
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public ProjectUserKey(Long userId, Long projectId) {
        this.userId = userId;
        this.projectId =projectId;
    }

    public Long getUserId() {
        return userId;
    }public Long getProjectId() {
        return projectId;
    }

    public ProjectUserKey() {

    }

}


