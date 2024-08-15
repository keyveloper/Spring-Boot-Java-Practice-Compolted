package com.example.webserver.repository;

import com.example.webserver.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    List<BoardEntity> findByWriterLike(String writer);
    List<BoardEntity> findByTextContentLike(String textContent);
    List<BoardEntity> findByWriterAndTextContentLike(String writer, String textContent);
}
