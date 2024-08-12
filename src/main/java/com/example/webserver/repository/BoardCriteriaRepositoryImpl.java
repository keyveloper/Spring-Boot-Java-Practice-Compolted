package com.example.webserver.repository;

import com.example.webserver.dto.GetBoardCriteriaRequest;
import com.example.webserver.entity.BoardEntity;
import com.example.webserver.entity.CommentEntity;
import io.micrometer.observation.annotation.Observed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class BoardCriteriaRepositoryImpl implements BoardCriteriaRepository{
    @PersistenceContext
    EntityManager entityManager;
    // find by containing board writer name
    @Override
    public List<BoardEntity> findByContainingWriter(String writer) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BoardEntity> query = builder.createQuery(BoardEntity.class);
        Root<BoardEntity> board = query.from(BoardEntity.class);

        Predicate predicate = builder.like(board.get("writer"), "%" + writer + "%");
        query.where(predicate);
        return entityManager.createQuery(query).getResultList();
    }
    // find by containing board textContent
    @Override
    public List<BoardEntity> findByContainingTextContent(String textContent) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BoardEntity> query = builder.createQuery(BoardEntity.class);
        Root<BoardEntity> board = query.from(BoardEntity.class);

        Predicate predicate = builder.like(board.get("textContent"), "%" + textContent + "%");
        query.where(predicate);
        return entityManager.createQuery(query).getResultList();
    }

    // find by containing both (writer, textContent)
    @Override
    public List<BoardEntity> findByContainingWriterAndText(String writer, String textContent) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BoardEntity> query = builder.createQuery(BoardEntity.class);
        Root<BoardEntity> board = query.from(BoardEntity.class);

        Predicate predicateWriter = builder.like(board.get("writer"), "%" + writer + "%");
        Predicate predicateText = builder.like(board.get("textContent"), "%" + textContent + "%");
        query.where(predicateWriter, predicateText);
        return entityManager.createQuery(query).getResultList();
    }

    // find by comment writer name
    @Override
    public List<BoardEntity> findByContainingCommentWriter(String writer) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BoardEntity> query = builder.createQuery(BoardEntity.class);

        // Root 정의
        Root<CommentEntity> comment = query.from(CommentEntity.class);

        //Join: [Entity=CommentEntity, JoinedEntity=BoardEntity, JoinColumn=board]
        Join<CommentEntity, BoardEntity> boardJoin = comment.join("board");

        // WHERE
        Predicate predicate = builder.like(comment.get("writer"), "%" + writer + "%");
        query.select(boardJoin).distinct(true).where(predicate);

        return entityManager.createQuery(query).getResultList();
    }
    // find by comment writer content
    @Override
    public List<BoardEntity> findByContainingCommentContent(String textContent) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BoardEntity> query = builder.createQuery(BoardEntity.class);
        Root<CommentEntity> comment = query.from(CommentEntity.class);

        Join<CommentEntity, BoardEntity> boardJoin = comment.join("board");

        Predicate predicate = builder.like(comment.get("textContent"), "%" + textContent + "%");
        query.select(boardJoin).distinct(true).where(predicate);

        return entityManager.createQuery(query).getResultList();
    }
    // find by containing comment both
    @Override
    public List<BoardEntity> findByContainingCommentWriterAndText(String writer, String textContent){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BoardEntity> query = builder.createQuery(BoardEntity.class);
        Root<CommentEntity> comment = query.from(CommentEntity.class);

        Join<CommentEntity, BoardEntity> boardJoin = comment.join("board");
        Predicate predicateWriter = builder.like(comment.get("writer"), "%" + writer + "%");
        Predicate predicateText = builder.like(comment.get("textContent"), "%" + textContent + "%");
        query.select(boardJoin).distinct(true).where(predicateWriter, predicateText);

        return entityManager.createQuery(query).getResultList();
    };
}
