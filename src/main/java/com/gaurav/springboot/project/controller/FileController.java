package com.gaurav.springboot.project.controller;

import com.gaurav.springboot.project.entity.File;
import com.gaurav.springboot.project.service.FileService;
import com.gaurav.springboot.project.service.FileService.FileDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/add-file")
    public String addFile(@RequestParam String commentFrom, @RequestParam String commentTo, @RequestParam MultipartFile file) throws IOException {
        return fileService.addFile(commentFrom, commentTo, file);
    }

    @GetMapping("/get-file/{fileId}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long fileId) {
        File file = fileService.getFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(file.getFileData());
    }

    @GetMapping("/between-file")
    public List<FileDetail> getFilesBetweenUsersWithDetails(@RequestParam String user1, @RequestParam String user2) {
        return fileService.getFilesBetweenUsersWithDetails(user1, user2);
    }

    @GetMapping("/all-file")
    public List<FileDetail> getAllFilesWithDetails() {
        return fileService.getAllFilesWithDetails();
    }
}
