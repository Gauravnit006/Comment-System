package com.gaurav.springboot.project.entity;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;

@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "posted_by_user_id", referencedColumnName = "id", nullable = false)
    private User postedByUser;

    @Column(nullable = false)
    private Long commentToUserId;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime commentDateTime;

    @Column(nullable = false)
    private Boolean isUpdated;

//    // Getters and setters
//    public Long getCommentId() {
//        return commentId;
//    }
//
//    public void setCommentId(Long commentId) {
//        this.commentId = commentId;
//    }
//
//    public User getPostedByUser() {
//        return postedByUser;
//    }
//
//    public void setPostedByUser(User postedByUser) {
//        this.postedByUser = postedByUser;
//    }
//
//    public Long getCommentToUserId() {
//        return commentToUserId;
//    }
//
//    public void setCommentToUserId(Long commentToUserId) {
//        this.commentToUserId = commentToUserId;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public LocalDateTime getCommentDateTime() {
//        return commentDateTime;
//    }
//
//    public void setCommentDateTime(LocalDateTime commentDateTime) {
//        this.commentDateTime = commentDateTime;
//    }
//
//    public boolean getIsUpdated() {
//        return isUpdated;
//    }
//
//    public void setIsUpdated(boolean isUpdated) {
//        this.isUpdated = isUpdated;
//    }
}
