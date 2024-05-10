package com.example.pm.project.model;

import com.example.pm.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ProjectUser {
    @EmbeddedId
    ProjectUserKey id;


    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    User user;


    @ManyToOne
    @MapsId("projectId")
    @ToString.Exclude
    @JoinColumn(name = "project_id")
    Project project;

    public ProjectUser(User user, Project project) {
        this.user = user;
        this.project = project;
    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof ProjectUser that)) return false;
//        return Objects.equals(getId(), that.getId()) && Objects.equals(getUser(), that.getUser()) && Objects.equals(getProject(), that.getProject());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(getId(), getUser(), getProject());
//    }
}

