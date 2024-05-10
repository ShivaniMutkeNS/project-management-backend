package com.example.pm.comments.service;

import com.example.pm.comments.model.Comments;
import com.example.pm.issue.exception.IssueException;
import com.example.pm.user.exception.UserException;

import java.util.List;

public interface CommentService {
    Comments createComment(Long issueId, Long userId, String comment) throws UserException, IssueException;

    void  deleteComment(Long commentId, Long userId) throws UserException, IssueException;

    List<Comments> findCommentByIssueId(Long issueId);
}
