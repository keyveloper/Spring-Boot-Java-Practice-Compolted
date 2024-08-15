package com.example.webserver.repository;

import com.example.webserver.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long>, CommentQueryDslRepository {
    List<CommentEntity> findByBoardId(long bardId);
    List<CommentEntity> findByBoardWriterLike(String writer);
}

