package com.example.webserver.repository;

import com.example.webserver.dto.CustomCriteria;
import com.example.webserver.entity.CommentEntity;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    List<CommentEntity> findAll();

    List<CommentEntity> findByCustomCriteria(CustomCriteria customCriteria);

    void write(CommentEntity comment);

    void deleteById(long id);


}
