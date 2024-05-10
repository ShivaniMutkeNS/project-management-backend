package com.example.pm.comments.controller;


import com.example.pm.comments.model.Comments;
import com.example.pm.comments.service.CommentService;
import com.example.pm.issue.exception.IssueException;
import com.example.pm.project.exception.ProjectException;
import com.example.pm.res.request.CreateCommentRequest;
import com.example.pm.res.response.MessageResponse;
import com.example.pm.user.exception.UserException;
import com.example.pm.user.model.User;
import com.example.pm.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private CommentService commentService;
    private UserService userService;

    @Autowired
    public CommentController(CommentService commentService,UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<Comments> createComment(

            @RequestBody CreateCommentRequest req,
            @RequestHeader("Authorization") String jwt) throws UserException, IssueException, ProjectException {
        User user = userService.findUserProfileByJwt(jwt);
        Comments createdComment = commentService.createComment(req.getIssueId(), user.getId(), req.getContent());
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable Long commentId,
                                                         @RequestHeader("Authorization") String jwt) throws UserException, IssueException, ProjectException {
        User user = userService.findUserProfileByJwt(jwt);
        commentService.deleteComment(commentId, user.getId());
        MessageResponse res=new MessageResponse();
        res.setMessage("comment deleted successfully");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<List<Comments>>  getCommentsByIssueId(@PathVariable Long issueId) {
        List<Comments> comments = commentService.findCommentByIssueId(issueId);
        return new ResponseEntity<>(comments,HttpStatus.OK);
    }
}

