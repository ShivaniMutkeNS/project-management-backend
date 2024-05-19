package com.example.pm.user.model;

import com.example.pm.chat.model.ChatUser;
import com.example.pm.issue.model.Issue;
import com.example.pm.project.model.Project;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String email;

    private String password;

    private int projectSize = 0;

    @JsonIgnore
    @OneToMany(mappedBy = "assignee")
    @ToString.Exclude
    private List<Issue> assignedIssues = new ArrayList<>();

}
