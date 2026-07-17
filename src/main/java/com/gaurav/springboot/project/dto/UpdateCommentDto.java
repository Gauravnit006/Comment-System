package com.gaurav.springboot.project.dto;

public class UpdateCommentDto {
    private Long commentId;
     private String newMessage;

    public Long getCommentId(){
        return commentId;
    }
    public String getNewMessage(){
        return newMessage;
    }
}
