package com.gaurav.springboot.project.controller;


import com.gaurav.springboot.project.dto.*;
import com.gaurav.springboot.project.entity.Comment;
import com.gaurav.springboot.project.entity.CommentHistory;
import com.gaurav.springboot.project.entity.File;
import com.gaurav.springboot.project.entity.User;
import com.gaurav.springboot.project.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/register")
//    public String registerUser(@RequestParam String name) {
//        return commentService.registerUser(name);
//    }
    public String registerUser(@RequestBody RegisterCommentDto registerCommentDto){
        return commentService.registerUser(registerCommentDto.getName());
    }

    @PostMapping("/add")
//    public String addComment(@RequestParam String commentFrom, @RequestParam String commentTo, @RequestParam String message) {
//        return commentService.addComment(commentFrom, commentTo, message);
//    }
    public String addComment(@RequestBody AddCommentDto addCommentDto){
        return commentService.addComment(addCommentDto.getCommentFrom(), addCommentDto.getCommentTo(), addCommentDto.getMessage());
    }

    @GetMapping("/get")
//    public List<Comment> getComments(@RequestParam String commentTo) {
//        return commentService.getComments(commentTo);
//    }
    public List<Comment> getComments(@RequestBody GetCommentDto getCommentDto){
        return commentService.getComments(getCommentDto.getCommentTo());
    }

    @PutMapping("/update")
//    public String updateComment(@RequestParam Long commentId, @RequestParam String newMessage) {
//        return commentService.updateComment(commentId, newMessage);
//    }
    public String updateComment(@RequestBody UpdateCommentDto updateRequest){
        return commentService.updateComment(updateRequest.getCommentId(), updateRequest.getNewMessage());
    }

    @GetMapping("/history")
//    public List<CommentHistory> getCommentHistory(@RequestParam Long commentId) {
//        return commentService.getCommentHistory(commentId);
//    }
    public List<CommentHistory> getCommentHistory(@RequestBody HistoryCommentDto historyCommentDto){
        return commentService.getCommentHistory(historyCommentDto.getCommentId());
    }

    @GetMapping("/sent")
//    public List<Comment> getCommentsByCommentFrom(@RequestParam String commentFrom) {
//        return commentService.getCommentsByCommentFrom(commentFrom);
//    }
    public List<Comment> getCommentsByCommentFrom(@RequestBody SentCommentDto sentCommentDto){
        return commentService.getCommentsByCommentFrom(sentCommentDto.getCommentFrom());
    }

    @GetMapping("/between")
//    public List<Comment> getCommentsByCommentFromAndCommentTo(@RequestParam String commentFrom, @RequestParam String commentTo) {
//        return commentService.getCommentsByCommentFromAndCommentTo(commentFrom, commentTo);
//    }
    public List<Comment> getCommentsByCommentFromAndCommentTo(@RequestBody BetweenCommentDto betweenCommentDto){
        return commentService.getCommentsByCommentFromAndCommentTo(betweenCommentDto.getCommentFrom(), betweenCommentDto.getCommentTo());
    }

    @GetMapping("/users")
    public List<User> getUsers(){
        return commentService.getUsers();
    }

    @GetMapping("/active-users")
    public List<User> getActiveUsers() {
        return commentService.getActiveUsers();
    }

    @GetMapping("/user-count")
    public long getUserCount() {
        return commentService.getUserCount();
    }

    @GetMapping("/user-comment-count")
    public Map<String, Long> getUserCommentCount() {
        return commentService.getUserCommentCount();
    }

    @GetMapping("/exchange")
//    public List<String> getUserCommentExchange(@RequestParam String user1, @RequestParam String user2) {
//        return commentService.getUserCommentExchange(user1, user2);
//    }
    public List<String> getUserCommentExchange(@RequestBody ExchangeCommentDto exchangeCommentDto){
        return commentService.getUserCommentExchange(exchangeCommentDto.getUser1(), exchangeCommentDto.getUser2());
    }

    @GetMapping("/comments-between")
    public List<Comment> getCommentsBetweenUsers(@RequestParam String user1, @RequestParam String user2) {
        return commentService.getCommentsBetweenUsers(user1, user2);
    }

    @DeleteMapping("/delete")
//    public void deleteUser(@RequestParam String userName) {
//        commentService.deleteUser(userName);
//    }
    public void deleteUser(@RequestBody DeleteCommentDto deleteCommentDto){
        commentService.deleteUser(deleteCommentDto.getUserName());
    }
}
