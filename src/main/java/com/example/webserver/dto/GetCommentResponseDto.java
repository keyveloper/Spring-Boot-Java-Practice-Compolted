package com.example.webserver.dto;

import com.example.webserver.entity.CommentEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@ToString
@RequiredArgsConstructor
public class GetCommentResponseDto {
    private final long boardId;
    private final long id;
    private final String writer;
    private final LocalDateTime writingTime;
    private final String textContent;
}
