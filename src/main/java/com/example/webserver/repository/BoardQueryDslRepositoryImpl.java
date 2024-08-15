package com.example.webserver.repository;

import com.example.webserver.entity.BoardEntity;
import com.example.webserver.entity.QBoardEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardQueryDslRepositoryImpl implements BoardQueryDslRepository{
    private final JPAQueryFactory queryFactory;
    private final QBoardEntity board = QBoardEntity.boardEntity;

    @Override
    public List<BoardEntity> findByWriterLikeDsl(String writer) {
        return queryFactory
                .selectFrom(board)
                .where(board.writer.like(writer))
                .fetch();
    }
}
