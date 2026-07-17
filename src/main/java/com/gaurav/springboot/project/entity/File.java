package com.gaurav.springboot.project.entity;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "files")
@Data
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_from_id")
    private User commentFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_to_id")
    private User commentTo;

    @Lob
    @Access(AccessType.FIELD)
    private byte[] fileData;

    private String fileName;

    private String fileType;

    // Getters and setters



}

