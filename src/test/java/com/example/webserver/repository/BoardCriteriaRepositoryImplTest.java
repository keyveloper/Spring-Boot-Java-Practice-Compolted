package com.example.webserver.repository;

import com.example.webserver.entity.BoardEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class BoardCriteriaRepositoryImplTest {
    @Autowired
    private BoardCriteriaRepositoryImpl boardCriteriaRepositoryImpl;
    @Test
    void containingCommentWriterTest() {
        // Given
        String writer = "admin";

        // When
        List<BoardEntity> results = boardCriteriaRepositoryImpl
                .findByContainingCommentWriter(writer);
        for (BoardEntity boardEntity : results) {
            System.out.println(boardEntity.toString());
        }

        // Then
        assertNotNull(results);
    }

    @Test
    void containingCommentContentTest() {
        // Given
        String content = "S";

        // When
        List<BoardEntity> results = boardCriteriaRepositoryImpl
                .findByContainingCommentContent(content);
        for (BoardEntity boardEntity : results) {
            System.out.println(boardEntity.toString());
        }

        // Then
        assertNotNull(results);
    }
}