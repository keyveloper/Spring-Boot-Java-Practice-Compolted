package com.example.webserver.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Builder
@RequiredArgsConstructor
@Getter
public class CustomCriteria {
    private final String writer;
    private final String notContainWriter;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Integer maxReads;
    private final Integer minReads;
}

