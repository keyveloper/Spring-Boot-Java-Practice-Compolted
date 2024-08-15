package com.example.webserver.repository;

import com.example.webserver.dto.CustomCriteria;
import com.example.webserver.entity.CommentEntity;

import java.util.List;

public interface CommentQueryDslRepository {
    List<CommentEntity> findByCustomCriteria(CustomCriteria customCriteria);
}
