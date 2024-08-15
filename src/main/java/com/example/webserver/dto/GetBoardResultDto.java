package com.example.webserver.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor

public class GetBoardResultDto {
    private final long id;

    private final String title;

    private final String writer;

    private final LocalDateTime writingTime;

    private final Integer readingCount;

    private final String textContent;

    private final List<GetBoardCommentDto> comments;
}
