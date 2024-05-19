package com.example.pm.issue.service;

import com.example.pm.issue.exception.IssueException;
import com.example.pm.issue.model.Issue;
import com.example.pm.issue.model.Status;
import com.example.pm.project.exception.ProjectException;
import com.example.pm.res.request.IssueRequest;
import com.example.pm.res.request.UpdateIssueRequest;
import com.example.pm.user.exception.UserException;
import com.example.pm.user.model.User;

import java.util.List;
import java.util.Optional;

public interface IssueService {

    List<Issue> getAllIssues() throws IssueException;

    Optional<Issue> getIssueById(Long issueId) throws IssueException;

    List<Issue> getIssueByProjectId(Long projectId) throws ProjectException;

    Issue createIssue(IssueRequest issue, Long userid) throws UserException, IssueException, ProjectException;

    Optional<Issue> updateIssue(Long issueid, UpdateIssueRequest updatedIssue) throws IssueException, UserException, ProjectException;

    String deleteIssue(Long issueId, Long userid) throws UserException, IssueException;

    List<Issue> getIssuesByAssigneeId(Long assigneeId) throws IssueException;

    List<Issue> searchIssues(String title, String status, String priority, Long assigneeId) throws IssueException;

    User getAssigneeForIssue(Long issueId) throws IssueException;

    Issue addUserToIssue(Long issueId, Long userId) throws UserException, IssueException;

    Issue updateStatus(Long issueId, Status status) throws IssueException;


}
