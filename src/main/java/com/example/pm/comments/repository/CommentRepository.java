package com.example.pm.comments.repository;

import com.example.pm.comments.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Long> {
    List<Comments> findByIssueId(Long issueId);
}
