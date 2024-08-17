package com.example.webserver.repository;

import com.example.webserver.dto.CustomCriteria;
import com.example.webserver.entity.BoardEntity;
import com.example.webserver.entity.CommentEntity;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Repository
public class CommentRepositoryImpl implements CommentRepository {
    private final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("onlyJPAUnit");
    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    public List<CommentEntity> findAll() {
        return entityManager.createQuery("select c from CommentEntity c", CommentEntity.class)
                .getResultList();
    }

//    public List<CommentEntity> findByCustomCriteria(CustomCriteria customCriteria) {
//        return entityManager.createQuery(
//                "SELECT c FROM CommentEntity c " +
//                        "WHERE c.board.writer LIKE :writer " +
//                        "AND c.board.writer NOT LIKE :notContain " +
//                        "AND (c.board.readingCount > :minReads " +
//                        "OR c.board.readingCount <= :maxReads)"
//                , CommentEntity.class)
//                .setParameter("writer", customCriteria.getWriter())
//                .setParameter("notContain", customCriteria.getNotContainWriter())
//                .setParameter("minReads", customCriteria.getMinReads())
//                .setParameter("maxReads", customCriteria.getMaxReads())
//                .getResultList();
//    }

    @Override
    public List<CommentEntity> findByCustomCriteria(CustomCriteria customCriteria) {
        return entityManager.createQuery(
                        "SELECT c FROM CommentEntity c " +
                                "JOIN c.board b " +
                                "WHERE b.writer LIKE :writer " +
                                "AND b.writer NOT LIKE :notContain " +
                                "AND (b.readingCount > :minReads " +
                                "OR b.readingCount <= :maxReads)"
                        , CommentEntity.class)
                .setParameter("writer", customCriteria.getWriter())
                .setParameter("notContain", customCriteria.getNotContainWriter())
                .setParameter("minReads", customCriteria.getMinReads())
                .setParameter("maxReads", customCriteria.getMaxReads())
                .getResultList();
    }

    public void write(CommentEntity comment) {
        entityManager.persist(comment);
    }

    public void deleteById(long id) {
        entityManager.remove(id);
    }
}
