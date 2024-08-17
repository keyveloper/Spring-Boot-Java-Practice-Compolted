package com.example.webserver.repository;

import com.example.webserver.entity.BoardEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.persistence.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepository {
    private final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("onlyJPAUnit");
    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    @Override
    public List<BoardEntity> findAll() {
        return entityManager.createQuery("SELECT b FROM BoardEntity b", BoardEntity.class)
                .getResultList();
    }
    @Override
    public Optional<BoardEntity> findById(long id) {
        return Optional.of(entityManager.find(BoardEntity.class, id));
    }

    @Override
    public List<BoardEntity> findByWriterLike(String writer) {
        return entityManager.createQuery("SELECT b FROM BoardEntity b WHERE b.writer like :writer"
        ,BoardEntity.class)
                .setParameter("writer", writer)
                .getResultList();
    }

    @Override
    public List<BoardEntity> findByWriterAndNotContainLike(String writer, String notContain) {
        return entityManager.createQuery("SELECT b FROM BoardEntity b " +
                "WHERE b.writer LIKE :writer AND b.writer NOT LIKE :notContain", BoardEntity.class)
                .setParameter("writer", writer)
                .setParameter("notContain", notContain)
                .getResultList();
    }

    @Override
    public List<BoardEntity> findByWriterAndReadingCount(String writer, String notContain,
                                                  Integer minReads, Integer maxReads) {
        return entityManager.createQuery("SELECT b FROM BoardEntity b " +
                "WHERE b.writer LIKE :writer AND b.writer NOT LIKE :notContain " +
                "AND (b.readingCount > :minReads or b.readingCount <= :maxReads)"
                ,BoardEntity.class)
                .setParameter("writer", writer)
                .setParameter("notContain", notContain)
                .setParameter("minReads", minReads)
                .setParameter("maxReads", maxReads)
                .getResultList();
    }

    @Override
    public void removeById(long id) {
        entityManager.remove(id);
    };
    @Override
    public void save(BoardEntity board){
        entityManager.persist(board);
    };
}
