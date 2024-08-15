package com.example.webserver.repository;

import com.example.webserver.dto.CustomCriteria;
import com.example.webserver.entity.CommentEntity;
import com.example.webserver.entity.QBoardEntity;
import com.example.webserver.entity.QCommentEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentQueryDslRepositoryImpl implements CommentQueryDslRepository {
    private final JPAQueryFactory queryFactory;
    private final QBoardEntity board = QBoardEntity.boardEntity;
    private final QCommentEntity comment = QCommentEntity.commentEntity;

    @Override
    public List<CommentEntity> findByCustomCriteria(CustomCriteria customCriteria) {
        return queryFactory
                .select(comment)
                .from(board)
                .leftJoin(board.comments, comment)
                .where(board.writer.like(customCriteria.getWriter())
                        .and(board.writer.notLike(customCriteria.getNotContainWriter()))
                        .and(board.writingTime.between(customCriteria.getStartDate(), customCriteria.getEndDate()))
                        .and(board.readingCount.gt(customCriteria.getMinReads())
                                .or(board.readingCount.lt(customCriteria.getMaxReads()))))
                .fetch();
    }
}