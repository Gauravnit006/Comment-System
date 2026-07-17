package com.gaurav.springboot.project.repository;

import com.gaurav.springboot.project.entity.File;
import com.gaurav.springboot.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByCommentFromAndCommentTo(User commentFromUser, User commentToUser);
}
