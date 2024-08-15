package com.example.webserver.repository;

import com.example.webserver.entity.BoardEntity;
import com.example.webserver.entity.CommentEntity;

import java.util.List;

public interface BoardQueryDslRepository {
    List<BoardEntity> findByWriterLike(String writer);
}
