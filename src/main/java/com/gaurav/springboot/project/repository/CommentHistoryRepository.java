package com.gaurav.springboot.project.repository;

import com.gaurav.springboot.project.entity.CommentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentHistoryRepository extends JpaRepository<CommentHistory, Long> {
    List<CommentHistory> findByCommentId(Long commentId);
}

