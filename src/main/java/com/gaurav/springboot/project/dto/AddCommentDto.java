package com.gaurav.springboot.project.dto;

public class AddCommentDto {
    String commentFrom;
    String commentTo;
    String message;

    public String getCommentFrom() {
        return commentFrom;
    }
    public String getCommentTo(){
        return commentTo;
    }
    public String getMessage(){
        return message;
    }
}
