package com.gaurav.springboot.project.service;

import com.gaurav.springboot.project.entity.File;
import com.gaurav.springboot.project.entity.User;
import com.gaurav.springboot.project.exceptions.InvalidRequestException;
import com.gaurav.springboot.project.repository.FileRepository;
import com.gaurav.springboot.project.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public String addFile(String commentFrom, String commentTo, MultipartFile file) throws IOException {
        validateInputs(commentFrom, commentTo);

        User userFrom = userRepository.findByName(commentFrom).orElseThrow(() -> new InvalidRequestException("Please register the user " + commentFrom + " first"));
        User userTo = userRepository.findByName(commentTo).orElseThrow(() -> new InvalidRequestException("Please register the user " + commentTo + " first"));

        if (!userFrom.getIsActive()) {
            throw new InvalidRequestException("User " + commentFrom + " is inactive");
        }

        if (!userTo.getIsActive()) {
            throw new InvalidRequestException("User " + commentTo + " is inactive");
        }

        File newFile = new File();
        newFile.setCommentFrom(userFrom);
        newFile.setCommentTo(userTo);
        newFile.setFileName(file.getOriginalFilename());
        newFile.setFileType(file.getContentType());
        newFile.setFileData(file.getBytes());

        File savedFile = fileRepository.save(newFile);
        return "File is saved";
    }

    public File getFile(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new InvalidRequestException("No file found with fileId: " + fileId));
    }

    @Transactional
    public List<FileDetail> getFilesBetweenUsersWithDetails(String user1, String user2) {
        User user1Entity = userRepository.findByName(user1)
                .orElseThrow(() -> new InvalidRequestException("User1 not found"));
        User user2Entity = userRepository.findByName(user2)
                .orElseThrow(() -> new InvalidRequestException("User2 not found"));

        List<File> files = fileRepository.findByCommentFromAndCommentTo(user1Entity, user2Entity);
        files.addAll(fileRepository.findByCommentFromAndCommentTo(user2Entity, user1Entity));

        return files.stream()
                .map(file -> new FileDetail(file.getFileId(), file.getCommentFrom().getId(), file.getCommentFrom().getName(), file.getCommentTo().getId(), file.getCommentTo().getName()))
                .collect(Collectors.toList());
    }

    public List<FileDetail> getAllFilesWithDetails() {
        List<File> files = fileRepository.findAll();

        return files.stream()
                .map(file -> new FileDetail(file.getFileId(), file.getCommentFrom().getId(), file.getCommentFrom().getName(), file.getCommentTo().getId(), file.getCommentTo().getName()))
                .collect(Collectors.toList());
    }

    @Data
    public static class FileDetail {
        private Long fileId;
        private Long senderId;
        private String senderName;
        private Long receiverId;
        private String receiverName;

        public FileDetail() {
            // Default constructor
        }

        public FileDetail(Long fileId, Long senderId, String senderName, Long receiverId, String receiverName) {
            this.fileId = fileId;
            this.senderId = senderId;
            this.senderName = senderName;
            this.receiverId = receiverId;
            this.receiverName = receiverName;
        }
    }

    @Transactional
    private void validateInputs(String... inputs) {
        for (String input : inputs) {
            if (input == null || input.trim().isEmpty() || !input.matches("[a-zA-Z]+")) {
                throw new InvalidRequestException("Invalid input: " + input);
            }
        }
    }
}
