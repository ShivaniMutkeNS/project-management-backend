package com.example.pm.project.model;


import com.example.pm.chat.model.Chat;
import com.example.pm.issue.model.Issue;
import com.example.pm.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.builder.HashCodeExclude;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String category;

    private List<Tags> tags = new ArrayList<>();

    @ManyToOne
    private User owner;//one user can create many projects

    @JsonIgnore
    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProjectUser> teamMembers;

    @JsonIgnore//mappedBy = "project",  jithe mapped by ahe tithe jsonIgnore wapraych
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @HashCodeExclude
    private Chat chat;//one project can have one chat

    @JsonIgnore//to stop recursive call
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Issue> issues = new ArrayList<>();//issues means task one project can have many issues


}
