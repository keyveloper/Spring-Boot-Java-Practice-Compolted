package com.example.webserver;

import ch.qos.logback.core.encoder.EchoEncoder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import javax.xml.stream.events.Comment;
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
        try {
            String jpql = "select b From BoardEntity b";
            TypedQuery<BoardEntity> query = entityManager.createQuery(jpql, BoardEntity.class);
            List<BoardEntity> boards = query.getResultList();
            return boards.isEmpty() ? Optional.empty() : Optional.of(boards);
        } catch (Exception e) {
            log.error("Error retrieving all boards: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<Long> put(String title, String writer, String content) {
        try {
            BoardEntity board = new BoardEntity();
            board.setBoardTitle(title);
            board.setBoardWriter(writer);
            board.setBoardContent(content);
            board.setBoardDate(LocalDateTime.now());
            board.setBoardCount(0);

            entityManager.persist(board);
            return Optional.of(board.getBoardId());
        } catch (Exception e) {
            log.error("BoardRepositoryImpl write error: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<BoardEntity> get(long id) {
        try {
            String jpql = "SELECT b FROM BoardEntity b WHERE b.boardId = :id";
            TypedQuery<BoardEntity> query = entityManager.createQuery(jpql, BoardEntity.class);
            query.setParameter("id", id);
            BoardEntity board = query.getSingleResult();
            return Optional.ofNullable(board);
        } catch (Exception e) {
            log.error("Error retrieving board: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<String> delete(long id) {
        try {
            String jpql = "DELETE FROM BoardEntity b WHERE b.boardId = :id";
            Query query = entityManager.createQuery(jpql);
            query.setParameter("id", id);
            int resultAffected = query.executeUpdate();
            if (resultAffected > 0) {
                return Optional.of("Board Successfully deleted!! board id: " + id);
            } else {
                log.info("Can't delete no exist id\bboardID: " + id);
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("BoardRepository delete err" + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<String> update(long id, String content) {
        try {
            String jpql = "UPDATE BoardEntity SET boardContent = :content WHERE boardId = :id";
            Query query = entityManager.createQuery(jpql);
            query.setParameter("content", content);
            query.setParameter("id", id);
            int rowAffected = query.executeUpdate();
            if (rowAffected > 0) {
                return Optional.of("new content updated successfully " + id);
            } else {
                log.info("Can't update no exist id\bboardID: " + id);
                return Optional.empty();
            }

        } catch (Exception e) {
            log.error("BoardRepository update err" + e.getMessage());
            return Optional.empty();
        }

    }

    @Override
    @Transactional
    public Optional<String> addCount(long id) {
        try {
            String jpql = "UPDATE  BoardEntity SET boardCount = boardCount + 1 WHERE boardId = :id";
            Query query = entityManager.createQuery(jpql);
            query.setParameter("id", id);
            int rowAffected = query.executeUpdate();
            if (rowAffected > 0) {
                return Optional.of("Board Successfully updated!");
            } else {
                log.info("Update Count failed");
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("BoardRepository addCount err" + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<List<CommentEntity>> getAllComment(long id) {
        String jpql = "select b From CommentEntity b";
        TypedQuery<CommentEntity> query = entityManager.createQuery(jpql, CommentEntity.class);
        List<CommentEntity> comments = query.getResultList();
        return comments.isEmpty() ? Optional.empty() : Optional.of(comments);
    }
}
