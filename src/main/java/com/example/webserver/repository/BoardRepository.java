package com.example.webserver.repository;

import com.example.webserver.entity.BoardEntity;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    List<BoardEntity> findAll();
    Optional<BoardEntity> findById(long id);
    void removeById(long id);
    void save(BoardEntity board);
    List<BoardEntity> findByWriterLike(String writer);
    List<BoardEntity> findByWriterAndNotContainLike(String writer, String notContain);
    List<BoardEntity> findByWriterAndReadingCount(String writer, String notContain,
                                                  Integer minReads, Integer maxReads);
}
