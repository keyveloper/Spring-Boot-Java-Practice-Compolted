package com.example.webserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class PostBoardResponseDto {
    private final String message;
    private final long id;
    private final String writer;
    private final LocalDateTime writingDateTime;
}
