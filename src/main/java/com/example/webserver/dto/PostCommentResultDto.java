package com.example.webserver.dto;

import com.example.webserver.enums.PostCommentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@RequiredArgsConstructor
public class PostCommentResultDto {
    private final PostCommentStatus postCommentStatus;
    private final long boardId;
    private final long commentId;
    private final String writer;
    private final LocalDateTime writingTime;
}
