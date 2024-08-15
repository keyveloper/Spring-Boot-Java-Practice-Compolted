package com.example.webserver.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@Builder
@RequiredArgsConstructor
@Getter
public class GetCommentResultDto {
    private final long id;
    private final long boardId;
    private final String writer;
    private final LocalDateTime writingTime;
    private final String textContent;
}