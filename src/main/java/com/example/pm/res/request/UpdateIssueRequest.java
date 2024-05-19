package com.example.pm.res.request;


import com.example.pm.issue.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateIssueRequest {

    private String description;
    private String title;

}
