package com.example.webserver.dto;

import com.example.webserver.enums.PostBoardStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class PostBoardResultDto {
    private final PostBoardStatus postBoardStatus;
    private final long id;
    private final String writer;
    private final LocalDateTime writingTime;
}
