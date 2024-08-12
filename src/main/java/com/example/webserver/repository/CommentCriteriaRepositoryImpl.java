package com.example.webserver.repository;

import com.example.webserver.dto.GetBoardCriteriaRequest;
import com.example.webserver.entity.BoardEntity;
import com.example.webserver.entity.CommentEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentCriteriaRepositoryImpl implements CommentCriteriaRepository {
    @Autowired
    private final EntityManager entityManager;

    @Override
    public List<CommentEntity> findByComplexCriteria(GetBoardCriteriaRequest criteria) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BoardEntity> query = builder.createQuery(BoardEntity.class);

        // 실제로 질의할 곳
        Root<BoardEntity> board = query.from(BoardEntity.class);

        // Fetch BoardEntity & CommentEntity
        Fetch<BoardEntity, CommentEntity> commentsFetch = board.fetch("comments");

        ArrayList<Predicate> predicates = new ArrayList<>();
        Predicate p1 = builder.like(board.get("writer"), "%" + criteria.getWriter() + "%");
        Predicate p2 = builder.notLike(board.get("writer"), "%" + criteria.getNotContainWriter() + "%");
        Predicate p3 = builder.between(board.get("writingTime"), criteria.getStartDate(), criteria.getEndDate());
        Predicate pCount = builder.or(
          builder.lessThan(board.get("readingCount"), criteria.getMaxReads()),
          builder.greaterThan(board.get("readingCount"), criteria.getMinReads())
        );
        predicates.add(p1);
        predicates.add(p2);
        predicates.add(p3);
        predicates.add(pCount);

        // Predicate에 왜 [0] 이들어가는가
        query.where(builder.and(predicates.toArray(new Predicate[0])));


        query.select(board).distinct(true);

        List<BoardEntity> boardEntities = entityManager.createQuery(query).getResultList();
        return boardEntities.stream()
                .flatMap(boardEntity -> boardEntity.getComments().stream())
                .toList();
    }
}
