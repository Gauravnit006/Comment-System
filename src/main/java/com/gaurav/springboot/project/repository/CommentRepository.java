package com.gaurav.springboot.project.repository;


import com.gaurav.springboot.project.entity.Comment;
import com.gaurav.springboot.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByCommentToUserId(Long commentToUserId);
    List<Comment> findByPostedByUserAndMessage(User user, String message);
    List<Comment> findByPostedByUser(User user);
    List<Comment> findByPostedByUserAndCommentToUserId(User userFrom, Long commentToUserId);
    long countByPostedByUserAndCommentToUserId(User userFrom, Long commentToUserId);
    Optional<Comment> findById(Long commentId);
    void deleteByPostedByUser(User user);
}


