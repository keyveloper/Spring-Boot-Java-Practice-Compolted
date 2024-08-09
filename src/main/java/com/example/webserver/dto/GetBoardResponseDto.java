package com.example.webserver.dto;

import com.example.webserver.entity.CommentEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor
public class GetBoardResponseDto {
    private final long id;
    private final String writer;
    private final String title;
    private final LocalDateTime writingTime;
    private final Integer readingCount;
    private final String textContent;
    private final List<CommentEntity> comments;
}
