package com.gaurav.springboot.project.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean isActive;
}
