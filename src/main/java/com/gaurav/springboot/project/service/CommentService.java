package com.gaurav.springboot.project.service;

import com.gaurav.springboot.project.entity.Comment;
import com.gaurav.springboot.project.entity.CommentHistory;
import com.gaurav.springboot.project.entity.File;
import com.gaurav.springboot.project.entity.User;
import com.gaurav.springboot.project.exceptions.InvalidRequestException;
import com.gaurav.springboot.project.exceptions.UserNotFoundException;
import com.gaurav.springboot.project.repository.CommentHistoryRepository;
import com.gaurav.springboot.project.repository.CommentRepository;
import com.gaurav.springboot.project.repository.FileRepository;
import com.gaurav.springboot.project.repository.UserRepository;
import com.gaurav.springboot.project.util.EncryptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentHistoryRepository commentHistoryRepository;

    @Autowired
    private FileRepository fileRepository;


    @Transactional
    public String registerUser(String name) {
        validateInputs(name);

        userRepository.findByName(name).ifPresent(user -> {
            throw new InvalidRequestException("User already registered");
        });

        User newUser = new User();
        newUser.setName(name);
        newUser.setIsActive(true);
        userRepository.save(newUser);

        return "User registered successfully";
    }


    @Transactional
    public String addComment(String commentFrom, String commentTo, String message) {

        if (commentFrom == null || commentTo == null || commentFrom.trim().isEmpty() || commentTo.trim().isEmpty() ||
            !commentFrom.matches("^[a-zA-Z]*$") || !commentTo.matches("^[a-zA-Z]*$") || message==null ||
            message.trim().isEmpty()) {
            throw new InvalidRequestException("Invalid Request: commentFrom or commentTo or message is invalid.");
        }

        User userFrom = userRepository.findByName(commentFrom).orElseThrow(() -> new InvalidRequestException("Please register the user " + commentFrom + " first"));
        User userTo = userRepository.findByName(commentTo).orElseThrow(() -> new InvalidRequestException("Please register the user " + commentTo + " first"));

        if (!userFrom.getIsActive()) {
            throw new InvalidRequestException("User " + commentFrom + " is inactive");
        }

        if (!userTo.getIsActive()) {
            throw new InvalidRequestException("User " + commentTo + " is inactive");
        }

        String encryptedMessage = EncryptionUtils.encrypt(message);

        Comment comment = new Comment();
        comment.setMessage(encryptedMessage);
        comment.setCommentDateTime(LocalDateTime.now());
        comment.setPostedByUser(userFrom);
        comment.setCommentToUserId(userTo.getId());
        comment.setIsUpdated(false);
        commentRepository.save(comment);

        // Save to comment history
        CommentHistory commentHistory = new CommentHistory();
        commentHistory.setCommentId(comment.getCommentId());
        commentHistory.setMessage(encryptedMessage);
        commentHistory.setTimestamp(LocalDateTime.now());
        commentHistoryRepository.save(commentHistory);

        return "Comment added successfully";
    }

    public List<Comment> getComments(String commentTo) {
        if (commentTo == null || commentTo.trim().isEmpty() || !commentTo.matches("^[a-zA-Z]*$")) {
            throw new InvalidRequestException("Invalid Request: commentTo is invalid.");
        }

        User userTo = userRepository.findByName(commentTo).orElseThrow(() -> new UserNotFoundException("No users found with the specified name"));

        return decryptMessages(commentRepository.findByCommentToUserId(userTo.getId()));
    }

    @Transactional
    public String updateComment(Long commentId, String newMessage) {
        if (newMessage == null || newMessage.trim().isEmpty()) {
            throw new InvalidRequestException("Message format not correct");
        }
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new InvalidRequestException("No matching comment found with the specified commentId"));
        User userFrom = comment.getPostedByUser();

        if (!userFrom.getIsActive()) {
            throw new InvalidRequestException("User is not active");
        }

        String encryptedMessage = EncryptionUtils.encrypt(newMessage);

        comment.setMessage(encryptedMessage);
        comment.setIsUpdated(true);
        commentRepository.save(comment);

        // Save to comment history
        CommentHistory commentHistory = new CommentHistory();
        commentHistory.setCommentId(comment.getCommentId());
        commentHistory.setMessage(encryptedMessage);
        commentHistory.setTimestamp(LocalDateTime.now());
        commentHistoryRepository.save(commentHistory);

        return "Comment updated successfully";
    }

    public List<CommentHistory> getCommentHistory(Long commentId) {
        List<CommentHistory> histories = commentHistoryRepository.findByCommentId(commentId);
        histories.forEach(history -> history.setMessage(EncryptionUtils.decrypt(history.getMessage())));
        return histories;
    }

    public List<Comment> getCommentsByCommentFrom(String commentFrom) {
        if (commentFrom == null || commentFrom.trim().isEmpty() || !commentFrom.matches("^[a-zA-Z]*$")) {
            throw new InvalidRequestException("Invalid Request: commentTo is invalid.");
        }

        User userFrom = userRepository.findByName(commentFrom).orElseThrow(() -> new UserNotFoundException("No matching user found with the specified commentFrom"));

        return decryptMessages(commentRepository.findByPostedByUser(userFrom));
    }

    public List<Comment> getCommentsByCommentFromAndCommentTo(String commentFrom, String commentTo) {
        if (commentFrom == null || commentTo == null || commentFrom.trim().isEmpty() || commentTo.trim().isEmpty() ||
                !commentFrom.matches("^[a-zA-Z]*$") || !commentTo.matches("^[a-zA-Z]*$")) {
            throw new InvalidRequestException("Invalid Request: commentFrom or commentTo or message is invalid.");
        }

        User userFrom = userRepository.findByName(commentFrom).orElseThrow(() -> new UserNotFoundException("No matching user found with the specified commentFrom"));
        User userTo = userRepository.findByName(commentTo).orElseThrow(() -> new UserNotFoundException("No matching user found with the specified commentTo"));

        return decryptMessages(commentRepository.findByPostedByUserAndCommentToUserId(userFrom, userTo.getId()));
    }

    public List<User> getActiveUsers() {
        return userRepository.findByIsActive(true);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public long getUserCount() {
        return userRepository.findByIsActive(true).size();
    }

    public Map<String, Long> getUserCommentCount() {
        List<Comment> allComments = commentRepository.findAll();
        return allComments.stream()
                .collect(Collectors.groupingBy(comment -> comment.getPostedByUser().getName(), Collectors.counting()));
    }

    public List<String> getUserCommentExchange(String user1, String user2) {
        if (user1 == null || user2 == null || user1.trim().isEmpty() || user2.trim().isEmpty() ||
                !user1.matches("^[a-zA-Z]*$") || !user2.matches("^[a-zA-Z]*$")) {
            throw new InvalidRequestException("Invalid Request: commentFrom or commentTo or message is invalid.");
        }

        User userFrom = userRepository.findByName(user1).orElseThrow(() -> new UserNotFoundException("No matching user found with the specified user1"));
        User userTo = userRepository.findByName(user2).orElseThrow(() -> new UserNotFoundException("No matching user found with the specified user2"));

        long user1ToUser2 = commentRepository.countByPostedByUserAndCommentToUserId(userFrom, userTo.getId());
        long user2ToUser1 = commentRepository.countByPostedByUserAndCommentToUserId(userTo, userFrom.getId());

        return List.of(
                "Comment between " + user1 +" and " + user2 + " is " +  (user1ToUser2 + user2ToUser1),
                user1 + " to " + user2 + ": " + user1ToUser2 + " comments",
                user2 + " to " + user1 + ": " + user2ToUser1 + " comments"
        );
    }

    public List<Comment> getCommentsBetweenUsers(String user1, String user2) {
        validateInputs(user1, user2);
        User userFrom = userRepository.findByName(user1).orElseThrow(() -> new UserNotFoundException("No matching user found with the specified user1"));
        User userTo = userRepository.findByName(user2).orElseThrow(() -> new UserNotFoundException("No matching user found with the specified user2"));

        List<Comment> commentsFromUser1ToUser2 = commentRepository.findByPostedByUserAndCommentToUserId(userFrom, userTo.getId());
        List<Comment> commentsFromUser2ToUser1 = commentRepository.findByPostedByUserAndCommentToUserId(userTo, userFrom.getId());

        List<Comment> allComments = new ArrayList<>();
        allComments.addAll(commentsFromUser1ToUser2);
        allComments.addAll(commentsFromUser2ToUser1);

        return decryptMessages(allComments);
    }


    @Transactional
    public void deleteUser(String userName) {
        if (userName == null || userName.trim().isEmpty() || !userName.matches("^[a-zA-Z]*$")) {
            throw new InvalidRequestException("Invalid Request: userName is invalid.");
        }
        User user = userRepository.findByName(userName).orElseThrow(() -> new UserNotFoundException("No matching user found with the specified userName"));
        user.setIsActive(false);
        userRepository.save(user);

    }

    private void validateInputs(String... inputs) {
        for (String input : inputs) {
            if (input == null || input.trim().isEmpty() || !input.matches("[a-zA-Z]+")) {
                throw new InvalidRequestException("Invalid input: " + input);
            }
        }
    }
    private List<Comment> decryptMessages(List<Comment> comments) {
        return comments.stream()
                .map(comment -> {
                    comment.setMessage(EncryptionUtils.decrypt(comment.getMessage()));
                    return comment;
                })
                .collect(Collectors.toList());
    }
}
