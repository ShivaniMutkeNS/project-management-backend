package com.example.pm.comments.serviceimpl;


import com.example.pm.comments.model.Comments;
import com.example.pm.comments.repository.CommentRepository;
import com.example.pm.comments.service.CommentService;
import com.example.pm.issue.exception.IssueException;
import com.example.pm.issue.model.Issue;
import com.example.pm.issue.repository.IssueRepository;
import com.example.pm.user.exception.UserException;
import com.example.pm.user.model.User;
import com.example.pm.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private IssueRepository issueRepository;
    private UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, IssueRepository issueRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Comments createComment(Long issueId, Long userId, String content) throws UserException, IssueException {
        Optional<Issue> issueOptional = issueRepository.findById(issueId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (issueOptional.isEmpty()) {
            throw new IssueException("issue not found with id " + issueId);
        }
        if (userOptional.isEmpty()) {
            throw new UserException("user not found with id " + userId);
        }
        Issue issue = issueOptional.get();
        User user = userOptional.get();

        Comments comment = new Comments();

        comment.setIssue(issue);
        comment.setUser(user);
        comment.setCreatedDateTime(LocalDateTime.now());
        comment.setContent(content);

        Comments savedComments = commentRepository.save(comment);

        issue.getComments().add(savedComments);

        return savedComments;
    }


    @Override
    public void deleteComment(Long commentId, Long userId) throws UserException, IssueException {
        Optional<Comments> commentOptional = commentRepository.findById(commentId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (commentOptional.isEmpty()) {
            throw new IssueException("comment not found with id " + commentId);
        }
        if (userOptional.isEmpty()) {
            throw new UserException("user not found with id " + userId);
        }

        Comments comment = commentOptional.get();
        User user = userOptional.get();

        if (comment.getUser().equals(user)) {
            commentRepository.delete(comment);
        } else {
            throw new UserException("User does not have permission to delete this comment!");
        }

    }

    @Override
    public List<Comments> findCommentByIssueId(Long issueId) {
        return commentRepository.findByIssueId(issueId);
    }
}

