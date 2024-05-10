package com.example.pm.res.request;

import com.example.pm.issue.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueRequest {

	private String title;
	private String description;
	private Status status;
	private Long projectId;
	private String priority;
	private LocalDate dueDate;
	private Long userId;

}
