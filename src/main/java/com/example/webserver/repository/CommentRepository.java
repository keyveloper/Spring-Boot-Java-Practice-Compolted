package com.example.webserver.repository;

import com.example.webserver.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByBoardBoardId(long bardId);

    // find Comments By username
    @Query("SELECT c FROM CommentEntity c WHERE c.board.writer = :writer")
    List<CommentEntity> findCommentsByBoardWriter(@Param("writer") String writer);
}
