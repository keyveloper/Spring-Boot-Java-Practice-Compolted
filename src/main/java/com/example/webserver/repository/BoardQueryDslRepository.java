package com.example.webserver.repository;

import com.example.webserver.entity.BoardEntity;

import java.util.List;

public interface BoardQueryDslRepository {
    List<BoardEntity> findByWriterLikeDsl(String writer);
}
