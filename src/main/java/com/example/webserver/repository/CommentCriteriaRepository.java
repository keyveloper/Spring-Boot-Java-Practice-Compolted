package com.example.webserver.repository;

import com.example.webserver.dto.GetBoardCriteriaRequest;
import com.example.webserver.entity.CommentEntity;

import java.util.List;

public interface CommentCriteriaRepository {
    List<CommentEntity> findByComplexCriteria(GetBoardCriteriaRequest criteria);
}
