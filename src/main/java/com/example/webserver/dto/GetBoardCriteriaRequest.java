package com.example.webserver.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@RequiredArgsConstructor
public class GetBoardCriteriaRequest {
    @NonNull
    private final String writer;
    @NonNull
    private final String notContainWriter;
    @NonNull
    private LocalDateTime startDate;
    @NonNull
    private LocalDateTime endDate;
    @NonNull
    private Integer minReads;
    @NonNull
    private Integer maxReads;
}
