package com.example.pm.issue.model;

import com.example.pm.project.model.Project;
import com.example.pm.project.model.Tags;
import com.example.pm.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueDTO {

    private Long id;
    private String title;
    private String description;
    private Status status;
    private Long projectID;
    private String priority;
    private LocalDate dueDate;
    private List<Tags> tags = new ArrayList<>();
    private Project project;

    // Exclude assignee during serialization

    private User assignee;


}
