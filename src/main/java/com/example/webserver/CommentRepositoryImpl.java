package com.example.webserver;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.stream.events.Comment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
public class CommentRepositoryImpl implements CommentRepository {
    private final EntityManager entityManager;

    @Override
    @Transactional
    public Optional<List<CommentEntity>> getAllComment() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CommentEntity> criteriaQuery = criteriaBuilder.createQuery(CommentEntity.class);
        Root<CommentEntity> root = criteriaQuery.from(CommentEntity.class);
        criteriaQuery.select(root);

        TypedQuery<CommentEntity> query = entityManager.createQuery(criteriaQuery);
        List<CommentEntity> comments = query.getResultList();

        return comments.isEmpty() ? Optional.empty() : Optional.of(comments);
    }

    @Override
    @Transactional
    public Optional<String> write(String writer, String content) {
        try {
            CommentEntity comment = new CommentEntity();
            comment.setCommentWriter(writer);
            comment.setCommentContent(content);
            comment.setCommentDate(LocalDateTime.now());

            entityManager.persist(comment);
            return Optional.of("Comment Created and Saved Successfully" + comment.getCommentId());
        } catch (Exception e) {
            log.error("Comment Repository write err" + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<String> delete(long id) {
        try {
            CommentEntity comment = entityManager.find(CommentEntity.class, id);
            if (comment == null) {
                log.info("[in comment delete method]\nno Exist board");
                return Optional.empty();
            }
            entityManager.remove(comment);
            return Optional.of("Comment deleted Successfully " + comment.getCommentId());
        } catch (Exception e) {
            log.error("Comment Repository delete err" + e.getMessage());
            return Optional.empty();
        }
    }
}
