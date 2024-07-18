package com.example.webserver;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@AllArgsConstructor
public class BoardRepositoryImpl implements BoardRepository{
    private final EntityManager entityManager;
    @Override
    @Transactional
    // 이렇게 하면 Error catch를 어떻게..?
    public Optional<List<BoardEntity>> getAllBoard() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BoardEntity> criteriaQuery = criteriaBuilder.createQuery(BoardEntity.class);
        Root<BoardEntity> rootEntry = criteriaQuery.from(BoardEntity.class);
        CriteriaQuery<BoardEntity> all = criteriaQuery.select(rootEntry);

        List<BoardEntity> boards = entityManager.createQuery(all).getResultList();
        return boards.isEmpty() ? Optional.empty() : Optional.of(boards);
    }

    @Override
    @Transactional
    public Optional<String> write(String writer, String content) {
        try {
            BoardEntity board = new BoardEntity();
            board.setBoardWriter(writer);
            board.setBoardContent(content);
            board.setBoardDate(LocalDateTime.now());
            board.setBoardCount(0);

            entityManager.persist(board);
            return Optional.of("Board Created and Saved Successfully" + board.getBoardId());
        } catch (Exception e) {
            log.error("BoardRepositoryImpl write error: " + e.getMessage());
            return Optional.empty();
        }


    }

    @Override
    @Transactional
    public Optional<BoardEntity> get(long id) {
        BoardEntity board = entityManager.find(BoardEntity.class, id);
        return board != null? Optional.of(board) : Optional.empty();
    }

    @Override
    @Transactional
    public Optional<String> delete(long id) {
        try {
            BoardEntity board = entityManager.find(BoardEntity.class, id);
            if (board == null) {
                log.info("[in delete method]\nno exist board");
                return Optional.empty();
            }
            entityManager.remove(board);
            return Optional.of("Board Successfully deleted!! board id: " + board.getBoardId());
        } catch (Exception e) {
            log.error("BoardRepository delete err" + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<String> update(long id, String content) {
        try {
            BoardEntity board = entityManager.find(BoardEntity.class, id);
            if (board == null) {
                log.info("[in update method]\nNo exist Board");
                return Optional.empty();
            }
            board.setBoardContent(content);
            return Optional.of("new content updated successfully " + board.getBoardId());
        } catch (Exception e) {
            log.error("BoardRepository update err" + e.getMessage());
            return Optional.empty();
        }

    }

    @Override
    @Transactional
    public Optional<String> addCount(long id) {
        try {
            BoardEntity board = entityManager.find(BoardEntity.class, id);
            if (board == null) {
                log.info("[in plusCount method]\nNo exist board");
                return Optional.empty();
            }
            board.setBoardCount(board.getBoardCount() + 1);
            return Optional.of("count added successfully" + board.getBoardId());
        } catch (Exception e) {
            log.error("BoardRepository addCount err" + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<List<CommentEntity>> getAllComment(long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CommentEntity> criteriaQuery = criteriaBuilder.createQuery(CommentEntity.class);
        Root<CommentEntity> comment = criteriaQuery.from(CommentEntity.class);

        Predicate condition = criteriaBuilder.equal(comment.get("board").get("boardId"), id);
        criteriaQuery.where(condition);
        criteriaQuery.select(comment);

        TypedQuery<CommentEntity> query = entityManager.createQuery(criteriaQuery);
        List<CommentEntity> comments = query.getResultList();

        return comments.isEmpty() ? Optional.empty() : Optional.of(comments);
    }
}
