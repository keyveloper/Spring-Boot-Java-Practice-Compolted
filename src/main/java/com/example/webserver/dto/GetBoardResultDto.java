package com.example.webserver.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor

public class GetBoardResultDto {
    private long id;

    private String title;

    private String writer;

    private LocalDateTime writingTime;

    private Integer readingCount;

    private String textContent;

    @Setter
    private List<GetBoardCommentDto> comments;
}
